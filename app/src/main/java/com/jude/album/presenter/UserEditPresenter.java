package com.jude.album.presenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jude.album.domain.entities.User;
import com.jude.album.model.AccountModel;
import com.jude.album.model.ImageModel;
import com.jude.album.model.UserModel;
import com.jude.album.model.server.ErrorTransform;
import com.jude.album.ui.UserEditActivity;
import com.jude.album.utils.ProgressDialogTransform;
import com.jude.beam.expansion.data.BeamDataActivityPresenter;
import com.jude.library.imageprovider.ImageProvider;
import com.jude.library.imageprovider.OnImageSelectListener;
import com.jude.utils.JUtils;

import java.io.File;

import rx.Observable;

/**
 * Created by zhuchenxi on 16/6/9.
 */

public class UserEditPresenter extends BeamDataActivityPresenter<UserEditActivity,User> {
    private ImageProvider provider;

    @Override
    protected void onCreate(@NonNull UserEditActivity view, Bundle savedState) {
        super.onCreate(view, savedState);
        provider = new ImageProvider(getView());
        setData(AccountModel.getInstance().getCurrentAccount());
    }

    OnImageSelectListener listener = new OnImageSelectListener() {

        @Override
        public void onImageSelect() {
            getView().getExpansion().showProgressDialog("加载中");
        }

        @Override
        public void onImageLoaded(Uri uri) {
            getView().getExpansion().dismissProgressDialog();
            provider.corpImage(uri, 300, 300, new OnImageSelectListener() {
                @Override
                public void onImageSelect() {

                }

                @Override
                public void onImageLoaded(Uri uri) {
                    getView().getExpansion().dismissProgressDialog();
                    getData().setAvatar(uri.getPath());
                    refresh();
                }

                @Override
                public void onError() {
                    getView().getExpansion().dismissProgressDialog();
                    JUtils.Toast("加载错误");
                }
            });
        }

        @Override
        public void onError() {
            getView().getExpansion().dismissProgressDialog();
            JUtils.Toast("加载错误");
        }
    };

    public void selectPicture() {
        provider.getImageFromCameraOrAlbum(listener);
    }

    public void updateUserDetail(){
        Observable.just(getData().getAvatar())
                .flatMap(uri -> {
                    if (uri.startsWith("http")){
                        return Observable.just(uri);
                    }else {
                        return ImageModel.getInstance().putImageSync(new File(uri),null);
                    }
                })
                .doOnNext(avatar -> getData().setAvatar(avatar))
                .flatMap(avatar1 -> UserModel.getInstance()
                        .updateUserDetail(getData().getAvatar(), getData().getName(), getData().getGender(), getData().getIntro()))
                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.AUTH_TOAST))
                .compose(new ProgressDialogTransform<>(getView(),"上传中"))
                .subscribe(i ->{
                    JUtils.Toast("更新成功");
                    AccountModel.getInstance().refreshAccount();
                    getView().finish();
                });
    }

    @Override
    protected void onResult(int requestCode, int resultCode, Intent data) {
        super.onResult(requestCode, resultCode, data);
        provider.onActivityResult(requestCode, resultCode, data);
    }
}
