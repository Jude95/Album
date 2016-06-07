package com.jude.album.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.album.R;
import com.jude.album.domain.entities.Picture;
import com.jude.album.domain.entities.User;
import com.jude.album.model.ImageModel;
import com.jude.album.presenter.UserPresenter;
import com.jude.album.ui.viewholder.ImageViewHolder;
import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.data.BeamDataActivity;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.utils.JUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhuchenxi on 16/6/5.
 */
@RequiresPresenter(UserPresenter.class)
public class UserActivity extends BeamDataActivity<UserPresenter, User> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.recycler)
    EasyRecyclerView recycler;
    @BindView(R.id.fab_follow)
    FloatingActionButton fabFollow;
    @BindView(R.id.img_avatar)
    ImageView imgAvatar;

    private ImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recycler.setAdapter(mAdapter = new ImageAdapter(this));
        mAdapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return LayoutInflater.from(UserActivity.this).inflate(R.layout.view_user_picture_hint,parent,false);
            }

            @Override
            public void onBindView(View headerView) {

            }
        });
    }

    @Override
    public void setData(@Nullable User data) {
        Glide.with(this)
                .load(ImageModel.getSizeImage(data.getAvatar(), JUtils.getScreenWidth()))
                .into(imgAvatar);
        collapsingToolbarLayout.setTitle(data.getName());
        mAdapter.addAll(data.getPictures());
        mAdapter.setOnItemClickListener(position -> {
            Intent i = new Intent(this, PictureActivity.class);
            i.putParcelableArrayListExtra(PictureActivity.KEY_PICTURES, (ArrayList<? extends Parcelable>) data.getPictures());
            i.putExtra(PictureActivity.KEY_INDEX,position);
            startActivity(i);
        });
    }


    class ImageAdapter extends RecyclerArrayAdapter<Picture>{

        public ImageAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
            return new ImageViewHolder(parent);
        }
    }
}
