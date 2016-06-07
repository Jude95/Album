package com.jude.album.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.album.R;
import com.jude.album.model.AccountModel;
import com.jude.album.model.server.ErrorTransform;
import com.jude.album.utils.ProgressDialogTransform;
import com.jude.beam.expansion.BeamBaseActivity;
import com.jude.swipbackhelper.SwipeBackHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mr.Jude on 2015/12/5.
 */
public class LoginActivity extends BeamBaseActivity {
    public static final int REQUEST_LOGIN = 12356;

    @BindView(R.id.tilNumber)
    TextInputLayout tilNumber;
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.find)
    TextView find;
    @BindView(R.id.register)
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        login.setOnClickListener(v -> AccountModel.getInstance().login(tilNumber.getEditText().getText().toString(), tilPassword.getEditText().getText().toString())
                .compose(new ProgressDialogTransform<>(this,"登录中"))
                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.AUTH_TOAST))
                .subscribe(a ->{
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    finish();
                }));
        register.setOnClickListener(v -> startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class),REQUEST_LOGIN));
        find.setOnClickListener(v -> startActivityForResult(new Intent(LoginActivity.this, FindPasswordActivity.class),REQUEST_LOGIN));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN && resultCode == RESULT_OK){
            tilNumber.getEditText().setText(data.getStringExtra("number"));
            tilPassword.getEditText().setText(data.getStringExtra("password"));
        }
    }
}
