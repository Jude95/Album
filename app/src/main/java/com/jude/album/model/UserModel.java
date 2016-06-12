package com.jude.album.model;

import android.content.Context;

import com.jude.album.domain.entities.User;
import com.jude.album.model.server.DaggerServiceModelComponent;
import com.jude.album.model.server.SchedulerTransform;
import com.jude.album.model.server.ServiceAPI;
import com.jude.beam.model.AbsModel;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zhuchenxi on 16/6/5.
 */

public class UserModel extends AbsModel {
    public static UserModel getInstance() {
        return getInstance(UserModel.class);
    }
    @Inject
    ServiceAPI mServiceAPI;

    @Override
    protected void onAppCreate(Context ctx) {
        super.onAppCreate(ctx);
        DaggerServiceModelComponent.builder().build().inject(this);
    }

    public Observable<User> getUserDetail(String id){
        return Observable.timer(1, TimeUnit.SECONDS)
                .flatMap(l -> Observable.just(createVirtualUser(5).get((int) (Math.random()*5))))
                .compose(new SchedulerTransform<>());
    }

//    public Observable<User> getUserDetail(String id){
//        return mServiceAPI.getUserDetail(id).compose(new SchedulerTransform<>());
//    }

    public Observable updateUserDetail(String avatar,String name,int gender,String intro){
        return mServiceAPI.updateUserDetail(avatar,name,intro,gender).compose(new SchedulerTransform<>());
    }



    public static final User[] VIRTUAL_USER = {
            new User("0","灵雨水榭","http://i1.hdslb.com/bfs/face/4fca1ce835ea7246ab279b2e92eed5cb90728b70.jpg","",1,"感觉对了就收藏吧，硬币火不火什么的都不重要，喵！", PictureModel.getInstance().createVirtualPicture((int) (Math.random()*10)),PictureModel.getInstance().createVirtualPicture((int) (Math.random()*10)),null,null),
            new User("1","何人君","http://i2.hdslb.com/bfs/face/4bc37248bf6b18cdc34c627590aab9cc6d80295d.gif","",1,"熱愛孤獨者，非神即獸。/無論如何,我一定會自由地活著！！！",PictureModel.getInstance().createVirtualPicture((int) (Math.random()*10)),PictureModel.getInstance().createVirtualPicture((int) (Math.random()*10)),null,null),
            new User("2","怕挂科的无敌曲老师","http://i1.hdslb.com/bfs/face/bbce919cdaebac39e4d4d4bdf58cffdda47c5b0c.jpg","",1,"唉呀我摔倒了 要吃蛋挞女神亲亲才能起来|(￣3￣)|",PictureModel.getInstance().createVirtualPicture((int) (Math.random()*10)),PictureModel.getInstance().createVirtualPicture((int) (Math.random()*10)),null,null),
            new User("3","哟嚯啊呜","http://i2.hdslb.com/bfs/face/eaa8ae186b4f57fefafd34c153b0751b557d87a9.jpg","",1,"电波相通了～",PictureModel.getInstance().createVirtualPicture((int) (Math.random()*10)),PictureModel.getInstance().createVirtualPicture((int) (Math.random()*10)),null,null),
            new User("4","二小姐の日常","http://i0.hdslb.com/bfs/face/5938239101170ba0d995c6143614f6890fdf8467.gif","",1,"某中二的邪王真眼",PictureModel.getInstance().createVirtualPicture((int) (Math.random()*10)),PictureModel.getInstance().createVirtualPicture((int) (Math.random()*10)),null,null),
    };
    ArrayList<User> createVirtualUser(int count){
        ArrayList<User> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(VIRTUAL_USER[i%VIRTUAL_USER.length]);
        }
        return list;
    }
}
