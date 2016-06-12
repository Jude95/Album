package com.jude.album.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.jude.album.domain.entities.Picture;
import com.jude.album.model.AccountModel;
import com.jude.album.model.PictureModel;
import com.jude.album.ui.PictureActivity;
import com.jude.album.ui.UserPictureListActivity;
import com.jude.beam.expansion.list.BeamListActivityPresenter;

import java.util.ArrayList;

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
    protected void onCreateView(@NonNull UserPictureListActivity view) {
        super.onCreateView(view);
        getAdapter().setOnItemClickListener(position -> {
            Intent i = new Intent(getView(), PictureActivity.class);
            i.putParcelableArrayListExtra(PictureActivity.KEY_PICTURES, (ArrayList<? extends Parcelable>) getAdapter().getAllData());
            i.putExtra(PictureActivity.KEY_INDEX,position);
            startActivity(i);
        });
    }

    @Override
    public void onRefresh() {
        PictureModel.getInstance().getMyPictures(AccountModel.getInstance().getCurrentAccount().getId())
                .unsafeSubscribe(getRefreshSubscriber());
    }
}
