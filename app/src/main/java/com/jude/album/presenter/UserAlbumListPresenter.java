package com.jude.album.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jude.album.domain.entities.Album;
import com.jude.album.model.PictureModel;
import com.jude.album.ui.UserAlbumListActivity;
import com.jude.beam.expansion.list.BeamListActivityPresenter;

/**
 * Created by zhuchenxi on 16/6/6.
 */

public class UserAlbumListPresenter extends BeamListActivityPresenter<UserAlbumListActivity,Album> {
    @Override
    protected void onCreate(@NonNull UserAlbumListActivity view, Bundle savedState) {
        super.onCreate(view, savedState);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        PictureModel.getInstance().getAlbums(getIdFromIntent())
                .unsafeSubscribe(getRefreshSubscriber());
    }
}
