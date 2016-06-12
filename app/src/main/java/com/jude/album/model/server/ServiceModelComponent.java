package com.jude.album.model.server;


import com.jude.album.app.StethoOkHttpGlideModule;
import com.jude.album.model.AccountModel;
import com.jude.album.model.ImageModel;
import com.jude.album.model.PictureModel;
import com.jude.album.model.UserModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by zhuchenxi on 16/1/25.
 */
@Singleton
@Component(modules = {ServiceAPIModule.class})
public interface ServiceModelComponent {
    void inject(AccountModel model);
    void inject(PictureModel model);
    void inject(ImageModel model);
    void inject(UserModel model);
    void inject(StethoOkHttpGlideModule module);
}
