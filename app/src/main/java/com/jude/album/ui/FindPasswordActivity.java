package com.jude.album.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.jude.album.R;
import com.jude.album.model.AccountModel;
import com.jude.album.model.server.ErrorTransform;
import com.jude.album.utils.ProgressDialogTransform;
import com.jude.beam.expansion.BeamBaseActivity;
import com.jude.smssdk_mob.SMSManager;
import com.jude.smssdk_mob.TimeListener;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.jude.utils.JUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Mr.Jude on 2015/12/5.
 */
public class FindPasswordActivity extends BeamBaseActivity implements TimeListener {
    public static final int EXTERNAL_STORAGE_REQ_CODE = 10 ;

    @BindView(R.id.til_number)
    TextInputLayout tilNumber;
    @BindView(R.id.btn_code)
    Button btnCode;
    @BindView(R.id.til_code)
    TextInputLayout tilCode;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.btn_modify)
    Button btnModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        ButterKnife.bind(this);
        SMSManager.getInstance().registerTimeListener(this);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        btnCode.setOnClickListener(v -> sendCode());
        btnModify.setOnClickListener(v -> modify());
    }

    public void sendCode() {
        if (!requestPermission()){
            return;
        }
        if (tilNumber.getEditText().getText().toString().length() != 11) {
            Toast.makeText(this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        AccountModel.getInstance().checkAccount(tilNumber.getEditText().getText().toString())
                .compose(new ProgressDialogTransform<>(this,"发送中"))
                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.NONE))
                .subscribe(a-> {
                    if (!a) JUtils.Toast("手机号还没注册");
                    else SMSManager.getInstance().sendMessage(this, "86",tilNumber.getEditText().getText().toString());
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSManager.getInstance().unRegisterTimeListener(this);
    }

    public void modify(){
        if (tilPassword.getEditText().getText().toString().length() < 6 || tilPassword.getEditText().getText().toString().length() > 12) {
            Toast.makeText(this,"请输入6-12位密码",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tilCode.getEditText().getText().toString())){
            Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
            return;
        }

        AccountModel.getInstance().modifyPassword(
                tilNumber.getEditText().getText().toString(),
                tilPassword.getEditText().getText().toString(),
                tilCode.getEditText().getText().toString())
                .compose(new ProgressDialogTransform<>(this,"修改中"))
                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.AUTH_TOAST))
                .subscribe(a -> {
                    Intent i = new Intent();
                    i.putExtra("number", tilNumber.getEditText().getText().toString());
                    i.putExtra("password", tilPassword.getEditText().getText().toString());
                    setResult(RESULT_OK, i);
                    Toast.makeText(FindPasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    @Override
    public void onLastTimeNotify(int lastSecond) {
        if (lastSecond > 0)
            btnCode.setText(lastSecond + "秒后重新发送");
        else
            btnCode.setText("发送验证码");
    }

    @Override
    public void onAbleNotify(boolean valuable) {
        btnCode.setEnabled(valuable);
    }

    public boolean requestPermission(){
        //判断当前Activity是否已经获得了该权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "please give me the permission", Toast.LENGTH_SHORT).show();
            } else {
                //进行权限请求
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.READ_PHONE_STATE},
                        EXTERNAL_STORAGE_REQ_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQ_CODE: {
                // 如果请求被拒绝，那么通常grantResults数组为空
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //申请成功，进行相应操作
                    sendCode();
                } else {
                    //申请失败，可以继续向用户解释。
                }
                return;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
