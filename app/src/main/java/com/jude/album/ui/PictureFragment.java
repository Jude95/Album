package com.jude.album.ui;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.jude.album.R;
import com.jude.album.domain.entities.Picture;
import com.jude.album.model.ImageModel;
import com.jude.album.model.PictureModel;
import com.jude.album.model.server.ErrorTransform;
import com.jude.album.utils.ProgressDialogTransform;
import com.jude.beam.expansion.BeamBaseActivity;
import com.jude.beam.expansion.BeamBasePresenter;
import com.jude.tagview.TAGView;
import com.jude.utils.JTimeTransform;
import com.jude.utils.JUtils;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.flowlayout.BGAFlowLayout;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Observable;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

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
        photoview.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                mInfoViewHolder.shirk();
            }

            @Override
            public void onOutsidePhotoTap() {
                mInfoViewHolder.shirk();
            }
        });
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
        @BindView(R.id.img_avatar)
        ImageView imgAvatar;
        @BindView(R.id.tv_author_name)
        TextView tvAuthorName;
        @BindView(R.id.tv_author_picture_count)
        TextView tvAuthorPictureCount;
        @BindView(R.id.img_expand)
        ImageView imgExpand;
        @BindView(R.id.container_author)
        LinearLayout containerAuthor;
        @BindView(R.id.tv_info)
        TextView tvInfo;


        View mInfoView;
        int shirkY = 0, expandY = 0;
        ValueAnimator mExpandAnimator;
        boolean isShirk = true;
        @BindView(R.id.img_gender)
        ImageView imgGender;
        @BindView(R.id.collection)
        TAGView collection;
        @BindView(R.id.container_tag)
        BGAFlowLayout containerTag;
        @BindView(R.id.container_info)
        LinearLayout containerInfo;

        public void onCreateInfoView(FrameLayout parent) {
            mInfoView = LayoutInflater.from(getContext()).inflate(R.layout.view_picture_detail, parent, false);
            ButterKnife.bind(this, mInfoView);
            parent.addView(mInfoView);
            mInfoView.post(() -> {
                shirkY = JUtils.getScreenHeightWithStatusBar() - JUtils.dip2px(28);
                expandY = JUtils.getScreenHeightWithStatusBar() - mInfoView.getHeight();
                initAnimation();
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mInfoView.getLayoutParams();
                layoutParams.topMargin = shirkY;
                mInfoView.setLayoutParams(layoutParams);
                mInfoView.setVisibility(View.VISIBLE);
            });
            containerInfo.setOnClickListener(v -> {
                if (isShirk) {
                    expand();
                } else {
                    shirk();
                }
            });
        }

        public void expand(){
            if (!isShirk)return;
            mExpandAnimator.start();
            imgExpand.setImageResource(R.mipmap.down);
            isShirk = false;
        }

        public void shirk(){
            if (isShirk)return;
            mExpandAnimator.reverse();
            imgExpand.setImageResource(R.mipmap.up);
            isShirk = true;
        }
        public void onBindInfoView(Picture picture) {
            tvName.setText(picture.getName());
            tvWatch.setText(picture.getWatchCount() + "");
            tvCollect.setText(picture.getCollectionCount() + "");
            tvIntro.setText(picture.getIntro());
            String intro = "";
            intro += new JTimeTransform(picture.getCreateTime()).toString("yyyy年MM月dd日 hh:mm:ss    ");
            intro += picture.getWidth() + "x" + picture.getHeight() + "px";
            tvInfo.setText(intro);
            Glide.with(getContext())
                    .load(ImageModel.getSmallImage(picture.getAuthorAvatar()))
                    .bitmapTransform(new CropCircleTransformation(getContext()))
                    .into(imgAvatar);
            tvAuthorName.setText(picture.getAuthorName());
            tvAuthorPictureCount.setText(picture.getAuthorPictureCount() + "张作品");
            RxView.clicks(containerAuthor).subscribe(i -> {
                Intent intent = new Intent(getContext(), UserActivity.class);
                intent.putExtra(BeamBasePresenter.KEY_ID, picture.getAuthorId());
                getContext().startActivity(intent);
            });

            if (picture.isCollected()) {
                collection.setText("已收藏");
            } else {
                collection.setText("收藏作品");
            }
            RxView.clicks(collection)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .map(aVoid -> picture.isCollected())
                    .subscribe(isCollected -> {//网络请求
                        Observable.just(isCollected).flatMap(b -> {
                            if (!isCollected)
                                return PictureModel.getInstance().collect(picture.getId());
                            else return PictureModel.getInstance().unCollect(picture.getId());
                        })
                                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.AUTH_TOAST))
                                .compose(new ProgressDialogTransform<>((BeamBaseActivity) getActivity(), "处理中"))
                                .doOnNext(info1 -> picture.setCollected(!picture.isCollected()))
                                .subscribe(info -> {//修改UI
                                    if (picture.isCollected()) {
                                        collection.setText("已收藏");
                                    } else {
                                        collection.setText("收藏作品");
                                    }
                                });

                    });
            imgGender.setImageResource(picture.getAuthorGender() == 0 ? R.mipmap.male : R.mipmap.female);
            if (!TextUtils.isEmpty(picture.getTag())) {
                String[] tags = picture.getTag().split(",");
                for (String tag : tags) {
                    TAGView tagView = new TAGView(getContext());
                    tagView.setBackgroundColor(Color.parseColor("#44f0f0f0"));
                    tagView.setTextColor(Color.parseColor("#dddddd"));
                    tagView.setText(tag);
                    ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, JUtils.dip2px(24));
                    params.setMargins(JUtils.dip2px(4), JUtils.dip2px(4), JUtils.dip2px(4), JUtils.dip2px(4));
                    tagView.setLayoutParams(params);
                    tagView.setRadius(JUtils.dip2px(12));
                    tagView.setPadding(JUtils.dip2px(8), JUtils.dip2px(4), JUtils.dip2px(8), JUtils.dip2px(4));
                    containerTag.addView(tagView);
                }
            }
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
