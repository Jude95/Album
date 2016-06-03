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
            new Picture("0","Air","Airbnb",656,550,"http://o84n5syhk.bkt.clouddn.com/57172236_p0.jpg","","","http://i1.hdslb.com/bfs/face/67b42d78b63f36d581d3ada0f63aaa0d314c331e.jpg",10,12,"",125863246),
            new Picture("0","Air","Airbnb",550,778,"http://o84n5syhk.bkt.clouddn.com/57166531_p0.jpg","","","http://i1.hdslb.com/bfs/face/36ef7efc132d7822b2aea1ea254970b37dba892a.jpg",10,12,"",125863246),
            new Picture("0","Air","Airbnb",1142,800,"http://o84n5syhk.bkt.clouddn.com/57174070_p0.jpg","","","http://i2.hdslb.com/bfs/face/e22f9710554613d874253cce6d5b4318e5d64d88.jpg",10,12,"",125863246),
            new Picture("0","Air","Airbnb",566,800,"http://o84n5syhk.bkt.clouddn.com/57154327_p0.png","","","http://i1.hdslb.com/bfs/face/04c6389a02764b47aebbdab82d41a4b3ac6fe447.jpg",10,12,"",125863246),
            new Picture("0","Air","Airbnb",1085,755,"http://o84n5syhk.bkt.clouddn.com/57151022_p0.jpg","","","http://i0.hdslb.com/bfs/face/7651005f1f81801a4bc328ea5522c71497f58e23.jpg",10,12,"",125863246),
            new Picture("0","Air","Airbnb",1920,938,"http://o84n5syhk.bkt.clouddn.com/57174564_p0.jpg","","","http://i0.hdslb.com/bfs/face/b2be850083084539980f6006670b58c1c118af03.gif",10,12,"",125863246),
            new Picture("0","Air","Airbnb",1024,683,"http://o84n5syhk.bkt.clouddn.com/57156832_p0.jpg","","","http://i0.hdslb.com/bfs/face/2d779ca52f8613c793a1453f2604892cbf07e3a2.jpg",10,12,"",125863246),
            new Picture("0","Air","Airbnb",723,1000,"http://o84n5syhk.bkt.clouddn.com/57151474_p0.png","","","http://i1.hdslb.com/bfs/face/80f3de9289231140ea24e93da9fb651d8c4c5e58.jpg",10,12,"",125863246),
            new Picture("0","Air","Airbnb",2000,1667,"http://o84n5syhk.bkt.clouddn.com/57156623_p0.png","","","http://i1.hdslb.com/bfs/face/c97a3228f517bedc0b3a41e4f898c74777a744cf.gif",10,12,"",125863246),
            new Picture("0","Air","Airbnb",2126,1181,"http://o84n5syhk.bkt.clouddn.com/57180221_p0.jpg","","","http://i2.hdslb.com/bfs/face/471eae701e59187ac67aa3bb1cb3d56a4cce5fa7.jpg",10,12,"",125863246),
    };

    private ArrayList<Picture> createVirtualPicture(int count){
        ArrayList<Picture> arrayList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            arrayList.add(VIRTUAL_PICTURE[i%VIRTUAL_PICTURE.length]);
        }
        return arrayList;
    }
}
