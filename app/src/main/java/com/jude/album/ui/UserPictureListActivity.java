package com.jude.album.ui;

import android.view.ViewGroup;

import com.jude.album.domain.entities.Picture;
import com.jude.album.presenter.UserPictureListPresenter;
import com.jude.album.ui.viewholder.ImageViewHolder;
import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.list.BeamListActivity;
import com.jude.beam.expansion.list.ListConfig;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by zhuchenxi on 16/6/6.
 */

@RequiresPresenter(UserPictureListPresenter.class)
public class UserPictureListActivity extends BeamListActivity<UserPictureListPresenter,Picture> {

    @Override
    protected BaseViewHolder<Picture> getViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(parent);
    }

    @Override
    protected ListConfig getConfig() {
        return super.getConfig().setLoadmoreAble(false);
    }
}
