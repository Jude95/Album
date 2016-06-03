package com.jude.album.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.jude.album.R;
import com.jude.album.domain.entities.Picture;
import com.jude.fitsystemwindowlayout.FitSystemWindowsFrameLayout;
import com.jude.utils.JUtils;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.container)
    FitSystemWindowsFrameLayout container;

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
        RxView.clicks(btnBack).subscribe(aVoid -> getActivity().finish());
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

        View mInfoView;

        public void onCreateInfoView(FitSystemWindowsFrameLayout parent) {
            mInfoView = LayoutInflater.from(getContext()).inflate(R.layout.view_picture_detail, null);
            ButterKnife.bind(this, mInfoView);
            int top = JUtils.getScreenHeightWithStatusBar()-JUtils.dip2px(56);
            mInfoView.setPadding(0,top,0,0);
            parent.addView(mInfoView);
        }

        public void onBindInfoView(Picture picture) {
            tvName.setText(picture.getName());
            tvWatch.setText(picture.getWatchCount() + "");
            tvCollect.setText(picture.getCollectionCount() + "");
            tvIntro.setText(picture.getIntro());
            tvSize.setText(picture.getWidth() + "x" + picture.getHeight());
        }


    }
}
