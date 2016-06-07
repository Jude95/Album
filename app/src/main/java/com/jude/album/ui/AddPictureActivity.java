package com.jude.album.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jude.album.R;
import com.jude.album.domain.entities.Picture;
import com.jude.album.presenter.AddPicturePresenter;
import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.data.BeamDataActivity;
import com.jude.utils.JUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Func1;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_picture);
        ButterKnife.bind(this);
        RxView.clicks(imgPicture)
                .flatMap((Func1<Void, Observable<Boolean>>) aVoid -> RxPermissions.getInstance(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .subscribe(i->{
                    if (i) getPresenter().selectPicture();
                    else JUtils.Toast("请赐臣权限!");
                });
        RxView.clicks(btnSubmit)
                .filter(aVoid -> {
                    if (etName.getText().toString().isEmpty()){
                        JUtils.Toast("请输入作品名字");
                        return false;
                    }
                    return true;
                })
                .subscribe(v->getPresenter().submit());
        RxTextView.textChanges(etName).subscribe(charSequence -> {
            getPresenter().getData().setName(charSequence.toString());
        });
    }

    @Override
    public void setData(@Nullable Picture data) {
        Glide.with(this)
                .load(data.getSrc())
                .error(R.mipmap.picture_add)
                .into(imgPicture);
    }
}
