package com.jude.album.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.jude.album.domain.entities.Picture;
import com.jude.album.model.PictureModel;
import com.jude.album.ui.MainActivity;
import com.jude.album.ui.PictureActivity;
import com.jude.beam.expansion.list.BeamListActivityPresenter;

import java.util.ArrayList;

/**
 * Created by zhuchenxi on 16/6/1.
 */

public class MainPresenter extends BeamListActivityPresenter<MainActivity,Picture> {

    @Override
    protected void onCreate(@NonNull MainActivity view, Bundle savedState) {
        super.onCreate(view, savedState);
        onRefresh();
    }


    @Override
    public void onRefresh() {
        PictureModel.getInstance().getRecommendPicture(0)
                .unsafeSubscribe(getRefreshSubscriber());
    }

    @Override
    public void onLoadMore() {
        PictureModel.getInstance().getRecommendPicture(getCurPage())
                .unsafeSubscribe(getMoreSubscriber());
    }

    @Override
    protected void onCreateView(@NonNull MainActivity view) {
        super.onCreateView(view);
        PictureModel.getInstance().getPopularPicture().subscribe(list -> getView().refreshPopular(list));
        getAdapter().setOnItemClickListener(position -> {
            Intent i = new Intent(getView(), PictureActivity.class);
            i.putParcelableArrayListExtra(PictureActivity.KEY_PICTURES, (ArrayList<? extends Parcelable>) getAdapter().getAllData());
            i.putExtra(PictureActivity.KEY_INDEX,position);
            startActivity(i);
        });
    }
}
