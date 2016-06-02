package com.jude.album.model;

import com.jude.album.domain.entities.Picture;
import com.jude.album.model.server.SchedulerTransform;
import com.jude.beam.model.AbsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * Created by zhuchenxi on 16/6/1.
 */

public class PictureModel extends AbsModel {
    public static PictureModel getInstance() {
        return getInstance(PictureModel.class);
    }

    public Observable<List<Picture>> getPopularPicture(){
        return Observable.timer(1, TimeUnit.SECONDS)
                .compose(new SchedulerTransform<>())
                .map(time->createVirtualPicture(10));
    }

    public Observable<List<Picture>> getRecommendPicture(int page){
        return Observable.timer(1, TimeUnit.SECONDS)
                .compose(new SchedulerTransform<>())
                .map(time->createVirtualPicture(10));
    }

    static final Picture[] VIRTUAL_PICTURE = {
            new Picture("0","1","Air","Airbnb",550,778,"http://o84n5syhk.bkt.clouddn.com/57166531_p0.jpg","","",10,12,125863246),
            new Picture("0","2","Air","Airbnb",656,550,"http://o84n5syhk.bkt.clouddn.com/57172236_p0.jpg","","",10,12,125863246),
            new Picture("0","3","Air","Airbnb",1024,683,"http://o84n5syhk.bkt.clouddn.com/57156832_p0.jpg","","",10,12,125863246),
            new Picture("0","4","Air","Airbnb",1142,800,"http://o84n5syhk.bkt.clouddn.com/57174070_p0.jpg","","",10,12,125863246),
            new Picture("0","5","Air","Airbnb",566,800,"http://o84n5syhk.bkt.clouddn.com/57154327_p0.png","","",10,12,125863246),
            new Picture("0","6","Air","Airbnb",1085,755,"http://o84n5syhk.bkt.clouddn.com/57151022_p0.jpg","","",10,12,125863246),
            new Picture("0","7","Air","Airbnb",1920,938,"http://o84n5syhk.bkt.clouddn.com/57174564_p0.jpg","","",10,12,125863246),
            new Picture("0","8","Air","Airbnb",723,1000,"http://o84n5syhk.bkt.clouddn.com/57151474_p0.png","","",10,12,125863246),
            new Picture("0","9","Air","Airbnb",2000,1667,"http://o84n5syhk.bkt.clouddn.com/57156623_p0.png","","",10,12,125863246),
            new Picture("0","10","Air","Airbnb",2126,1181,"http://o84n5syhk.bkt.clouddn.com/57180221_p0.jpg","","",10,12,125863246),
    };

    private ArrayList<Picture> createVirtualPicture(int count){
        ArrayList<Picture> arrayList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            arrayList.add(VIRTUAL_PICTURE[i%VIRTUAL_PICTURE.length]);
        }
        return arrayList;
    }
}
