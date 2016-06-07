package com.jude.album.ui;

import android.view.ViewGroup;

import com.jude.album.domain.entities.Album;
import com.jude.album.presenter.UserAlbumListPresenter;
import com.jude.album.ui.viewholder.AlbumViewHolder;
import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.list.BeamListActivity;
import com.jude.beam.expansion.list.ListConfig;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by zhuchenxi on 16/6/6.
 */

@RequiresPresenter(UserAlbumListPresenter.class)
public class UserAlbumListActivity extends BeamListActivity<UserAlbumListPresenter,Album> {

    @Override
    protected BaseViewHolder<Album> getViewHolder(ViewGroup parent, int viewType) {
        return new AlbumViewHolder(parent);
    }

    @Override
    protected ListConfig getConfig() {
        return super.getConfig().setLoadmoreAble(false);
    }
}
