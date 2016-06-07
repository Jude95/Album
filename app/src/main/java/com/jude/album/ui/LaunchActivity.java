package com.jude.album.ui;

import android.content.Intent;
import android.os.Bundle;

import com.jude.beam.expansion.BeamBaseActivity;

/**
 * Created by zhuchenxi on 16/4/22.
 */
public class LaunchActivity extends BeamBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this,MainActivity.class));
//        if (AccountModel.getInstance().hasLogin())
//            startActivity(new Intent(this,MainActivity.class));
//        else
//            startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}
