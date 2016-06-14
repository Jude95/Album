package com.jude.album.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jude.album.domain.entities.User;
import com.jude.album.model.UserModel;
import com.jude.album.model.server.ErrorTransform;
import com.jude.album.ui.UserActivity;
import com.jude.beam.expansion.data.BeamDataActivityPresenter;

/**
 * Created by zhuchenxi on 16/6/5.
 */

public class UserPresenter extends BeamDataActivityPresenter<UserActivity,User> {
    @Override
    protected void onCreate(@NonNull UserActivity view, Bundle savedState) {
        super.onCreate(view, savedState);
        UserModel.getInstance().getUserDetail(getIdFromIntent())
                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.AUTH_TOAST))
                .subscribe(getDataSubscriber());
    }

}
