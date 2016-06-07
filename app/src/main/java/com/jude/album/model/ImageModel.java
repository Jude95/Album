package com.jude.album.model;

import android.content.Context;

import com.jude.album.model.server.DaggerServiceModelComponent;
import com.jude.album.model.server.SchedulerTransform;
import com.jude.album.model.server.ServiceAPI;
import com.jude.beam.model.AbsModel;
import com.jude.utils.JUtils;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import java.io.File;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by zhuchenxi on 15/7/21.
 */
public class ImageModel extends AbsModel {
    public static String UID = "";

    public static final int IMAGE_SIZE_SMALL = 200;
    public static final int IMAGE_SIZE_MIDDLE = 640;
    public static final int IMAGE_SIZE_LARGE = 1280;

    @Inject
    ServiceAPI mServiceAPI;
    @Inject
    OkHttpClient mOkHttpClient;

    public static ImageModel getInstance() {
        return getInstance(ImageModel.class);
    }
    public static final String ADDRESS = "http://7xn814.com1.z0.glb.clouddn.com/";
    public static final String QINIU = "qiniucdn.com";
    private UploadManager mUploadManager;


    public static boolean isQiniuAddress(String address){
        return address.contains(QINIU)||address.contains("clouddn");
    }

    @Override
    protected void onAppCreate(Context ctx) {
        super.onAppCreate(ctx);
        DaggerServiceModelComponent.builder().build().inject(this);
        mUploadManager = new UploadManager();
    }

    public static String getSmallImage(String image){
        if (image==null)return null;
        if (isQiniuAddress(image)) image+="?imageView2/0/w/"+IMAGE_SIZE_SMALL;
        return image;
    }
    public static String getMiddleImage(String image){
        if (image==null)return null;
        if (isQiniuAddress(image)) image+="?imageView2/0/w/"+IMAGE_SIZE_MIDDLE;
        return image;
    }

    public static String getLargeImage(String image){
        if (image==null)return null;
        if (isQiniuAddress(image)) image+="?imageView2/0/w/"+IMAGE_SIZE_LARGE;
        return image;
    }

    public static String getSizeImage(String image,int width){
        if (image==null)return null;
        if (isQiniuAddress(image)) image+="?imageView2/0/w/"+width;
        return image;
    }

    private static String createName(File file){
        String realName = "u"+UID+System.currentTimeMillis()+file.hashCode()+".jpg";
        return realName;
    }


    /**
     * 同步上传
     * @param file 需上传文件
     * @return 上传文件访问地址
     */
    public Observable<String> putImageSync(final File file,UpProgressHandler handler){
        String name = createName(file);
        return mServiceAPI.getQiniuToken()
                .flatMap(token -> Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        String url = ADDRESS + name;

                        mUploadManager.put(file, name, token.getToken(), (key, info, response) -> {
                            if (!info.isOK()) {
                                subscriber.onError(new Throwable("key:" + key + "  info:" + info + "  response:" + response));
                            } else {
                                subscriber.onNext(url);
                            }
                            subscriber.onCompleted();
                      }, new UploadOptions(null, null, false,handler, null));
                    }
                }))
                .doOnNext(s -> JUtils.Log("已上传：" + s))
                .compose(new SchedulerTransform<>());
    }

}
