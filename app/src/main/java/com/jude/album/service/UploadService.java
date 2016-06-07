package com.jude.album.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.jude.album.R;
import com.jude.album.domain.entities.Picture;
import com.jude.album.model.ImageModel;
import com.jude.album.model.PictureModel;
import com.jude.utils.JUtils;

import java.io.File;

/**
 * Created by zhuchenxi on 16/6/7.
 */

public class UploadService extends Service {
    public static final String KEY_PICTURE = "picture";

    private NotificationManager manger ;
    private NotificationCompat.Builder builder;

    @Override
    public void onCreate() {
        super.onCreate();
        manger = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        initBuilder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags,int startId) {
        Picture picture = intent.getParcelableExtra(KEY_PICTURE);
        if (picture!=null){
            submitToService(startId,picture.getSrc(),picture.getName(),picture.getIntro(),picture.getHeight(),picture.getWidth(),picture.getTag());
        }
        return START_NOT_STICKY;
    }


    private void submitToService(final int index, String src, String name, String intro, int height, int width, String tag){
        if (!TextUtils.isEmpty(src)){
            ImageModel.getInstance().putImageSync(new File(src), (key, percent) -> updateUpload(index, (int) (percent*100)))
                    .doOnNext(url->completeUpload(index))
                    .flatMap(url -> PictureModel.getInstance().uoloadPicture(url, name, intro, height, width, tag))
                    .subscribe(i -> JUtils.Toast(name+"上传成功"));
        }
    }

    private void initBuilder(){
        builder = new NotificationCompat.Builder(this);
        Bitmap btm = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(btm);
        //禁止用户点击删除按钮删除
        builder.setAutoCancel(false);
        //禁止滑动删除
        builder.setOngoing(true);
        //取消右上角的时间显示
        builder.setShowWhen(false);
        builder.setProgress(100,0,false);
        builder.setOngoing(true);
        builder.setShowWhen(false);
    }

    private void updateUpload(int id,int percent){
        builder.setContentTitle("上传中..."+percent+"%");
        builder.setProgress(100,0,false);
        manger.notify(id,builder.build());
    }

    private void completeUpload(int id){
        manger.cancel(id);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
