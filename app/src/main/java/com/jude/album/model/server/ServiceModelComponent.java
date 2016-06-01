package com.jude.album.model.server;


import com.jude.album.app.StethoOkHttpGlideModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by zhuchenxi on 16/1/25.
 */
@Singleton
@Component(modules = {ServiceAPIModule.class})
public interface ServiceModelComponent {
//    void inject(AccountModel model);
//    void inject(DataModel model);
//    void inject(ImageModel model);
//    void inject(ManagerModel model);
    void inject(StethoOkHttpGlideModule module);
}
