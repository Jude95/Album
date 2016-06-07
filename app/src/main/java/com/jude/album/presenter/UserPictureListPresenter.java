package com.jude.album.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jude.album.domain.entities.Picture;
import com.jude.album.model.AccountModel;
import com.jude.album.model.PictureModel;
import com.jude.album.ui.UserPictureListActivity;
import com.jude.beam.expansion.list.BeamListActivityPresenter;

/**
 * Created by zhuchenxi on 16/6/6.
 */

public class UserPictureListPresenter extends BeamListActivityPresenter<UserPictureListActivity,Picture> {
    @Override
    protected void onCreate(@NonNull UserPictureListActivity view, Bundle savedState) {
        super.onCreate(view, savedState);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        PictureModel.getInstance().getMyPictures(AccountModel.getInstance().getCurrentAccount().getId())
                .unsafeSubscribe(getRefreshSubscriber());
    }
}
