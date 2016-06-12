package com.jude.album.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jude.album.domain.entities.User;
import com.jude.album.model.AccountModel;
import com.jude.album.model.UserModel;
import com.jude.album.model.server.ErrorTransform;
import com.jude.album.ui.UserEditActivity;
import com.jude.album.utils.ProgressDialogTransform;
import com.jude.beam.expansion.data.BeamDataActivityPresenter;
import com.jude.utils.JUtils;

/**
 * Created by zhuchenxi on 16/6/9.
 */

public class UserEditPresenter extends BeamDataActivityPresenter<UserEditActivity,User> {

    @Override
    protected void onCreate(@NonNull UserEditActivity view, Bundle savedState) {
        super.onCreate(view, savedState);
        setData(AccountModel.getInstance().getCurrentAccount());
    }

    public void updateUserDetail(){
        UserModel.getInstance()
                .updateUserDetail(getData().getAvatar(), getData().getName(), getData().getGender(), getData().getIntro())
                .doOnNext(o -> JUtils.Log(Thread.currentThread().getName()))
                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.AUTH_TOAST))
                .compose(new ProgressDialogTransform<>(getView(),"上传中"))
                .subscribe(i ->{
                    JUtils.Toast("更新成功");
                    AccountModel.getInstance().refreshAccount();
                    getView().finish();
                });
    }
}
