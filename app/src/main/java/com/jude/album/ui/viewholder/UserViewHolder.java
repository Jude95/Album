package com.jude.album.ui.viewholder;

import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.jude.album.R;
import com.jude.album.domain.entities.User;
import com.jude.album.model.ImageModel;
import com.jude.album.ui.UserActivity;
import com.jude.beam.expansion.BeamBasePresenter;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import java.util.concurrent.TimeUnit;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by Mr.Jude on 2015/2/22.
 */
public class UserViewHolder extends BaseViewHolder<User> {
    private TextView mTv_name;
    private ImageView mImg_face;
    private TextView mTv_sign;


    public UserViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_user);
        mTv_name = $(R.id.person_name);
        mTv_sign = $(R.id.person_sign);
        mImg_face = $(R.id.person_face);
    }

    @Override
    public void setData(final User person){
        mTv_name.setText(person.getName());
        mTv_sign.setText(person.getIntro());
        Glide.with(getContext())
                .load(ImageModel.getSmallImage(person.getAvatar()))
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(mImg_face);
        RxView.clicks(itemView)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(i -> {
                    Intent intent = new Intent(getContext(),UserActivity.class);
                    intent.putExtra(BeamBasePresenter.KEY_ID,person.getId());
                    getContext().startActivity(intent);
                });
    }
}
