package com.jude.album.ui.viewholder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.album.R;
import com.jude.album.domain.entities.Album;
import com.jude.album.model.ImageModel;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.utils.JUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhuchenxi on 16/6/6.
 */

public class AlbumViewHolder extends BaseViewHolder<Album> {
    @BindView(R.id.img_avatar)
    ImageView imgAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_picture_count)
    TextView tvPictureCount;

    public AlbumViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_album);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void setData(Album data) {
        super.setData(data);
        tvName.setText(data.getName());
        tvPictureCount.setText(data.getPictures()==null?0:data.getPictures().length);
        Glide.with(getContext())
                .load(ImageModel.getSizeImage(data.getAvatar(), JUtils.getScreenWidth()/2))
                .into(imgAvatar);
    }
}
