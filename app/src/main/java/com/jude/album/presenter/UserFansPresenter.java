package com.jude.album.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jude.album.domain.entities.User;
import com.jude.album.model.UserModel;
import com.jude.album.model.server.ErrorTransform;
import com.jude.album.ui.UserFansActivity;
import com.jude.beam.expansion.list.BeamListActivityPresenter;

/**
 * Created by Mr.Jude on 2016/6/14.
 */
public class UserFansPresenter extends BeamListActivityPresenter<UserFansActivity,User> {
    @Override
    protected void onCreate(@NonNull UserFansActivity view, Bundle savedState) {
        super.onCreate(view, savedState);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        UserModel.getInstance().getFans(getIdFromIntent())
                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.AUTH))
                .unsafeSubscribe(getRefreshSubscriber());
    }
}
