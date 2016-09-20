package com.jude.album.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.jude.beam.expansion.BeamBaseActivity;
import com.jude.utils.JUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

/**
 * Created by zhuchenxi on 16/4/22.
 */
public class LaunchActivity extends BeamBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxPermissions.getInstance(this).request(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(b -> {
                    if (b){
                        startActivity(new Intent(this,MainActivity.class));
                        finish();
                    }else {
                        JUtils.Toast("请赐臣权限！");
                        finish();
                    }
                });
    }
}
