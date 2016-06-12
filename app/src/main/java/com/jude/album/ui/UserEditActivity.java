package com.jude.album.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxRadioGroup;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jude.album.R;
import com.jude.album.domain.entities.User;
import com.jude.album.presenter.UserEditPresenter;
import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.data.BeamDataActivity;
import com.jude.utils.JUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhuchenxi on 16/6/9.
 */
@RequiresPresenter(UserEditPresenter.class)
public class UserEditActivity extends BeamDataActivity<UserEditPresenter, User> {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_avatar)
    ImageView imgAvatar;
    @BindView(R.id.male)
    RadioButton male;
    @BindView(R.id.female)
    RadioButton female;
    @BindView(R.id.rg_gender)
    RadioGroup rgGender;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_intro)
    EditText etIntro;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        ButterKnife.bind(this);
        RxView.clicks(btnSubmit)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .filter(aVoid -> {
                    if (getPresenter().getData().getName().isEmpty()){
                        JUtils.Toast("请输入昵称");
                        return false;
                    }
                    return true;
                })
                .subscribe(i -> {
                    getPresenter().updateUserDetail();
                });
    }

    @Override
    public void setData(@Nullable User data) {
        Glide.with(this)
                .load(data.getAvatar())
                .into(imgAvatar);
        etName.setText(data.getName());
        if (data.getGender() == 0) {
            male.setChecked(true);
        } else {
            female.setChecked(true);
        }
        etIntro.setText(data.getIntro());

        RxTextView.textChanges(etName).subscribe(s -> getPresenter().getData().setName(s.toString()));
        RxTextView.textChanges(etIntro).subscribe(s -> getPresenter().getData().setIntro(s.toString()));
        RxRadioGroup.checkedChanges(rgGender).subscribe(integer -> {
            if (integer == R.id.female)getPresenter().getData().setGender(1);
            else getPresenter().getData().setGender(0);
        });
    }
}
