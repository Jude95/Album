package com.jude.album.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.jude.album.R;
import com.jude.album.domain.entities.Picture;
import com.jude.album.domain.entities.User;
import com.jude.album.model.AccountModel;
import com.jude.album.model.ImageModel;
import com.jude.album.model.UserModel;
import com.jude.album.model.server.ErrorTransform;
import com.jude.album.presenter.UserPresenter;
import com.jude.album.ui.viewholder.ImageViewHolder;
import com.jude.album.utils.ProgressDialogTransform;
import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.data.BeamDataActivity;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;
import com.jude.utils.JUtils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Observable;

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
    @BindView(R.id.img_background)
    ImageView imgBackground;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.img_gender)
    ImageView imgGender;
    @BindView(R.id.container_name)
    LinearLayout containerName;

    private ImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        recycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recycler.setAdapter(mAdapter = new ImageAdapter(this));
        mAdapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return LayoutInflater.from(UserActivity.this).inflate(R.layout.view_user_picture_hint, parent, false);
            }

            @Override
            public void onBindView(View headerView) {

            }
        });
        recycler.addItemDecoration(new SpaceDecoration(JUtils.dip2px(4)));
    }

    @Override
    public void setData(@Nullable User data) {
        Glide.with(this)
                .load(ImageModel.getSizeImage(data.getAvatar(), JUtils.getScreenWidth()))
                .bitmapTransform(new CropCircleTransformation(this))
                .into(imgAvatar);
        collapsingToolbarLayout.setTitle(data.getName());
        mAdapter.addAll(data.getPictures());
        mAdapter.setOnItemClickListener(position -> {
            Intent i = new Intent(this, PictureActivity.class);
            i.putParcelableArrayListExtra(PictureActivity.KEY_PICTURES, (ArrayList<? extends Parcelable>) data.getPictures());
            i.putExtra(PictureActivity.KEY_INDEX, position);
            startActivity(i);
        });
        tvName.setText(data.getName());
        if (AccountModel.getInstance().hasLogin() && data.getId().equals(AccountModel.getInstance().getCurrentAccount().getId())) {
            fabFollow.setImageResource(R.mipmap.edit);
            RxView.clicks(fabFollow)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(i -> getPresenter().startActivity(UserEditActivity.class));
        }
        imgGender.setImageResource(data.getGender()==0?R.mipmap.male:R.mipmap.female);
        if (data.isFollowed()){
            fabFollow.setImageResource(R.mipmap.fab_follow_focus);
            fabFollow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        }else {
            fabFollow.setImageResource(R.mipmap.fab_follow);
            fabFollow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
        }
        RxView.clicks(fabFollow)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .map(aVoid -> data.isFollowed())
                .subscribe(isFollowed -> {//网络请求
                    Observable.just(isFollowed).flatMap(b ->{
                        if (!isFollowed)return UserModel.getInstance().follow(data.getId());
                        else return UserModel.getInstance().unFollow(data.getId());
                    })
                            .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.AUTH_TOAST))
                            .compose(new ProgressDialogTransform<>(this,"处理中"))
                            .doOnNext(info1 -> data.setFollowed(!data.isFollowed()))
                            .subscribe(info -> {//修改UI
                                if (data.isFollowed()){
                                    JUtils.Toast("已关注");
                                    fabFollow.setImageResource(R.mipmap.fab_follow_focus);
                                    fabFollow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                                }else {
                                    JUtils.Toast("已取消关注");
                                    fabFollow.setImageResource(R.mipmap.fab_follow);
                                    fabFollow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
                                }
                            });
                });
    }


    class ImageAdapter extends RecyclerArrayAdapter<Picture> {

        public ImageAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
            return new ImageViewHolder(parent);
        }
    }
}
