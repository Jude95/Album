package com.jude.album.presenter;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.jude.album.domain.entities.Picture;
import com.jude.album.domain.entities.PictureDescribeResult;
import com.jude.album.model.ImageModel;
import com.jude.album.model.server.ErrorTransform;
import com.jude.album.service.UploadService;
import com.jude.album.ui.AddPictureActivity;
import com.jude.beam.expansion.data.BeamDataActivityPresenter;
import com.jude.library.imageprovider.ImageProvider;
import com.jude.library.imageprovider.OnImageSelectListener;
import com.jude.utils.JUtils;

import java.io.File;

/**
 * Created by zhuchenxi on 16/6/6.
 */

public class AddPicturePresenter extends BeamDataActivityPresenter<AddPictureActivity,Picture> {
    private ImageProvider provider;

    @Override
    protected void onCreate(@NonNull AddPictureActivity view, Bundle savedState) {
        super.onCreate(view, savedState);
        provider = new ImageProvider(getView());
        setData(new Picture());
    }

    OnImageSelectListener listener = new OnImageSelectListener() {

        @Override
        public void onImageSelect() {
            getView().getExpansion().showProgressDialog("加载中");
        }

        @Override
        public void onImageLoaded(Uri uri) {
            getView().getExpansion().dismissProgressDialog();
            getData().setSrc(uri.getPath());

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(uri.getPath(), options);
            int height = options.outHeight;
            int width = options.outWidth;

            getData().setHeight(height);
            getData().setWidth(width);

            getView().startDescribe();
            ImageModel.getInstance().getPictureDescribeResult(new File(uri.getPath()))
                    .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.NONE))
                    .subscribe(pictureDescribeResult -> {
                        String tags = "";
                        for (PictureDescribeResult.PictureTag pictureTag : pictureDescribeResult.getTags()) {
                            tags += pictureTag.getTagName()+",";
                        }
                        getData().setTag(tags);
                        getView().stopDescribe();
                        refresh();
                    });
            refresh();
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

    public void submit(){
        Intent i = new Intent(getView(), UploadService.class);
        i.putExtra(UploadService.KEY_PICTURE, (Parcelable) getData());
        getView().startService(i);
        JUtils.Toast("正在后台上传……");
        getView().finish();
    }

    @Override
    protected void onResult(int requestCode, int resultCode, Intent data) {
        super.onResult(requestCode, resultCode, data);
        provider.onActivityResult(requestCode, resultCode, data);
    }
}
