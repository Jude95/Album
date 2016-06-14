package com.jude.album.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jude.album.R;
import com.jude.beam.expansion.BeamBaseActivity;
import com.jude.utils.JUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mr.Jude on 2016/4/23.
 */
public class AboutActivity extends BeamBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.version)
    TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        version.setText(getString(R.string.app_name)+" v"+ JUtils.getAppVersionName());
    }
}
