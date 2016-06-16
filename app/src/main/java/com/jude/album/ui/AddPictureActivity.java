package com.jude.album.ui;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jude.album.R;
import com.jude.album.domain.entities.Picture;
import com.jude.album.presenter.AddPicturePresenter;
import com.jude.album.utils.RoundedBackgroundSpan;
import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.data.BeamDataActivity;
import com.jude.utils.JUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhuchenxi on 16/6/6.
 */

@RequiresPresenter(AddPicturePresenter.class)
public class AddPictureActivity extends BeamDataActivity<AddPicturePresenter, Picture> {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_name)
    AppCompatEditText etName;
    @BindView(R.id.et_intro)
    AppCompatEditText etIntro;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.img_picture)
    ImageView imgPicture;
    @BindView(R.id.et_tag)
    AppCompatEditText etTag;
    @BindView(R.id.tv_tag_hint)
    TextView tvTagHint;

    private boolean mAutoChangeText = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_picture);
        ButterKnife.bind(this);
        RxView.clicks(imgPicture)
                .flatMap(aVoid -> RxPermissions.getInstance(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .subscribe(i -> {
                    if (i) getPresenter().selectPicture();
                    else JUtils.Toast("请赐臣权限!");
                });
        RxView.clicks(btnSubmit)
                .filter(aVoid -> {
                    if (etName.getText().toString().isEmpty()) {
                        JUtils.Toast("请输入作品名字");
                        return false;
                    }
                    return true;
                })
                .subscribe(v -> getPresenter().submit());
        RxTextView.textChanges(etName).subscribe(charSequence -> {
            getPresenter().getData().setName(charSequence.toString());
        });
        RxTextView.textChanges(etIntro).subscribe(charSequence -> {
            getPresenter().getData().setIntro(charSequence.toString());
        });
        RxTextView.textChanges(etTag)
                .filter(charSequence -> {
                    if (mAutoChangeText) {
                        mAutoChangeText = false;
                        return false;
                    }
                    return true;
                })
                .subscribe(charSequence -> {
                    String text = charSequence.toString();
                    text = text.replace("，", ",");
                    SpannableString msp = new SpannableString(text);
                    int start = 0;
                    int end = 0;
                    while ((end = text.indexOf(',', start)) != -1) {
                        //设置字体背景色
                        msp.setSpan(new RoundedBackgroundSpan(getResources().getColor(R.color.blue), Color.WHITE, etTag.getTextSize()), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        start = end + 1;
                    }
                    if (start != text.length() && text.length() > 0) {
                        msp.setSpan(new RoundedBackgroundSpan(getResources().getColor(R.color.blue), Color.WHITE, etTag.getTextSize()), start, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    mAutoChangeText = true;
                    etTag.setText(msp);
                    etTag.setSelection(text.length());
                    getPresenter().getData().setTag(text);
                });
    }

    @Override
    public void setData(@Nullable Picture data) {
        Glide.with(this)
                .load(data.getSrc())
                .error(R.mipmap.picture_add)
                .into(imgPicture);
        etTag.setText(data.getTag() + etTag.getText());
    }

    public void startDescribe(){
        tvTagHint.setText("正在解析图片_");
    }

    public void stopDescribe(){
        tvTagHint.setText("输入,分割标签");
    }
}
