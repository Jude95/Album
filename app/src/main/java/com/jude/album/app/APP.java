package com.jude.album.app;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.jude.album.R;
import com.jude.album.domain.Dir;
import com.jude.beam.Beam;
import com.jude.beam.expansion.list.ListConfig;
import com.jude.utils.JFileManager;
import com.jude.utils.JUtils;

/**
 * Created by zhuchenxi on 16/1/18.
 */
public class APP extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JUtils.initialize(this);
        JUtils.setDebug(true, "Album");
        JFileManager.getInstance().init(this, Dir.values());
        Stetho.initializeWithDefaults(this);
        Beam.init(this);
        //Beam.setViewExpansionDelegateProvider(NewViewExpansion::new);
        //Beam.setActivityLifeCycleDelegateProvider(ActivityDelegate::new);
        ListConfig.setDefaultListConfig(new ListConfig()
                .setPaddingNavigationBarAble(true)
                .setRefreshAble(true)
                .setContainerLayoutRes(R.layout.activity_recyclerview));
    }
}
