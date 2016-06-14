package com.jude.album.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.jude.album.domain.entities.Picture;
import com.jude.album.model.PictureModel;
import com.jude.album.ui.CollectionPictureActivity;
import com.jude.album.ui.PictureActivity;
import com.jude.beam.expansion.list.BeamListActivityPresenter;

import java.util.ArrayList;

/**
 * Created by Mr.Jude on 2016/6/14.
 */
public class CollectionPicturePresenter extends BeamListActivityPresenter<CollectionPictureActivity,Picture> {
    @Override
    protected void onCreate(@NonNull CollectionPictureActivity view, Bundle savedState) {
        super.onCreate(view, savedState);
        onRefresh();
    }

    @Override
    protected void onCreateView(@NonNull CollectionPictureActivity view) {
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
        PictureModel.getInstance().getCollectionsPictures(getIdFromIntent())
                .unsafeSubscribe(getRefreshSubscriber());
    }
}
