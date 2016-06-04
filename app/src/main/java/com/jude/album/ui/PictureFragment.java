package com.jude.album.ui;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.album.R;
import com.jude.album.domain.entities.Picture;
import com.jude.album.model.ImageModel;
import com.jude.utils.JTimeTransform;
import com.jude.utils.JUtils;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by zhuchenxi on 16/6/2.
 */

public class PictureFragment extends Fragment {
    Picture mPicture;
    @BindView(R.id.wheel)
    ProgressWheel wheel;
    @BindView(R.id.photoview)
    PhotoView photoview;
    @BindView(R.id.container)
    FrameLayout container;

    InfoViewHolder mInfoViewHolder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPicture = getArguments().getParcelable("picture");
        JUtils.Log("PictureFragment onCreate " + mPicture.getSrc());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Glide.with(getContext())
                .load(mPicture.getSrc())
                .into(photoview);
        mInfoViewHolder = new InfoViewHolder();
        mInfoViewHolder.onCreateInfoView(container);
        mInfoViewHolder.onBindInfoView(mPicture);
    }


    public class InfoViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_watch)
        TextView tvWatch;
        @BindView(R.id.tv_collect)
        TextView tvCollect;
        @BindView(R.id.tv_intro)
        TextView tvIntro;
        @BindView(R.id.tv_size)
        TextView tvSize;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.img_avatar)
        ImageView imgAvatar;
        @BindView(R.id.tv_author_name)
        TextView tvAuthorName;
        @BindView(R.id.tv_author_picture_count)
        TextView tvAuthorPictureCount;
        @BindView(R.id.img_expand)
        ImageView imgExpand;

        View mInfoView;
        int shirkY = 0, expandY = 0;
        ValueAnimator mExpandAnimator;
        boolean isShirk = true;


        public void onCreateInfoView(FrameLayout parent) {
            mInfoView = LayoutInflater.from(getContext()).inflate(R.layout.view_picture_detail, parent, false);
            ButterKnife.bind(this, mInfoView);
            parent.addView(mInfoView);
            mInfoView.post(() -> {
                shirkY = JUtils.getScreenHeightWithStatusBar() - JUtils.dip2px(28);
                expandY = JUtils.getScreenHeightWithStatusBar()  - mInfoView.getHeight();
                initAnimation();
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mInfoView.getLayoutParams();
                layoutParams.topMargin = shirkY;
                mInfoView.setLayoutParams(layoutParams);
                mInfoView.setVisibility(View.VISIBLE);
            });
            imgExpand.setOnClickListener(v -> {
                if (isShirk) {
                    mExpandAnimator.start();
                    imgExpand.setImageResource(R.mipmap.down);
                } else {
                    mExpandAnimator.reverse();
                    imgExpand.setImageResource(R.mipmap.up);
                }
                isShirk = !isShirk;
            });
        }

        public void onBindInfoView(Picture picture) {
            tvName.setText(picture.getName());
            tvWatch.setText(picture.getWatchCount() + "");
            tvCollect.setText(picture.getCollectionCount() + "");
            tvIntro.setText(picture.getIntro());
            tvDate.setText(new JTimeTransform(picture.getCreateTime()).toString("yyyy年MM月dd日 hh:mm:ss"));
            tvSize.setText(picture.getWidth() + "x" + picture.getHeight());
            Glide.with(getContext())
                    .load(ImageModel.getSmallImage(picture.getAuthorAvatar()))
                    .bitmapTransform(new CropCircleTransformation(getContext()))
                    .into(imgAvatar);
            tvAuthorName.setText(picture.getAuthorName());
            tvAuthorPictureCount.setText(picture.getAuthorPictureCount() + "张作品");
        }

        public void initAnimation() {
            mExpandAnimator = ValueAnimator.ofInt(shirkY, expandY);
            mExpandAnimator.setDuration(200);
            mExpandAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mExpandAnimator.addUpdateListener(animation -> {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mInfoView.getLayoutParams();
                layoutParams.topMargin = (Integer) animation.getAnimatedValue();
                mInfoView.setLayoutParams(layoutParams);
            });
        }

    }
}
