package com.jude.album.ui.viewholder;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.album.R;
import com.jude.album.domain.entities.Picture;
import com.jude.album.model.ImageModel;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.utils.JUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhuchenxi on 16/6/2.
 */

public class ImageViewHolder extends BaseViewHolder<Picture> {
    @BindView(R.id.img_picture)
    ImageView imgPicture;

    public ImageViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_picture);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void setData(Picture data) {
        int width = JUtils.getScreenWidth()/2;
        int realWidth = width;
        int realHeight = data.getHeight()*width/data.getWidth();
        ViewGroup.LayoutParams params = imgPicture.getLayoutParams();
        params.height = realHeight;
        imgPicture.setLayoutParams(params);
        Glide.with(getContext())
                .load(ImageModel.getSizeImage(data.getSrc(),width))
                .placeholder(R.color.white)
                .into(imgPicture);
    }
}
