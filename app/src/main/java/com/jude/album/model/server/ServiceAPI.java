package com.jude.album.model.server;


import com.jude.album.domain.body.Exist;
import com.jude.album.domain.body.Info;
import com.jude.album.domain.body.Token;
import com.jude.album.domain.entities.Album;
import com.jude.album.domain.entities.Picture;
import com.jude.album.domain.entities.UpdateInfo;
import com.jude.album.domain.entities.User;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Mr.Jude on 2015/11/18.
 */
public interface ServiceAPI {
    String SERVER_ADDRESS = "http://115.29.107.20/v1/";

    @GET("config/qiniu.php")
    Observable<Token> getQiniuToken();

    @POST("users/checkAccountExist.php")
    @FormUrlEncoded
    Observable<Exist> checkAccountExist(@Field("number") String number);

    @POST("users/register.php")
    @FormUrlEncoded
    Observable<Info> register(
            @Field("number") String number,
            @Field("name") String name,
//            @Field("avatar") String avatar,
            @Field("password") String password,
            @Field("code") String code);

    @POST("users/login.php")
    @FormUrlEncoded
    Observable<User> login(
            @Field("number") String number,
            @Field("password") String password);

    @POST("users/modifyPassword.php")
    @FormUrlEncoded
    Observable<Info> modifyPassword(
            @Field("number") String number,
            @Field("password") String password,
            @Field("code") String code);


    @GET("users/prolongToken.php")
    Observable<User> refreshAccount();


//_________________________________________________________________________________

    @POST("users/albums.php")
    @FormUrlEncoded
    Observable<List<Album>> getAlbums(
            @Field("id") String id);

    @POST("users/pictures.php")
    @FormUrlEncoded
    Observable<List<Picture>> getPictures(
            @Field("id") String id);

    @POST("pictures/updateWatchCount.php")
    @FormUrlEncoded
    Observable<Info> updateWatchCount(
            @Field("id") String id);

    @POST("users/pictureCollection.php")
    @FormUrlEncoded
    Observable<List<Picture>> getCollectionsPictures(
            @Field("id") String id);

    @POST("pictures/uploadPicture.php")
    @FormUrlEncoded
    Observable<Info> uploadPicture(
            @Field("src") String src,
            @Field("name") String name,
            @Field("intro") String intro,
            @Field("height") int height,
            @Field("width") int width,
            @Field("tag") String tag
            );

    @POST("pictures/recommendPicture.php")
    @FormUrlEncoded
    Observable<List<Picture>> getRecommendPicture(
            @Field("page") int page);

    @GET("pictures/popularPicture.php")
    Observable<List<Picture>> getPopularPicture();

    @POST("pictures/collection.php")
    @FormUrlEncoded
    Observable<Info> collection(
            @Field("id") String id);

    @POST("pictures/cancelCollection.php")
    @FormUrlEncoded
    Observable<Info> cancelCollection(
            @Field("id") String id);


    @POST("users/follow.php")
    @FormUrlEncoded
    Observable<Info> follow(
            @Field("id") String id);

    @POST("users/cancelFollow.php")
    @FormUrlEncoded
    Observable<Info> unFollow(
            @Field("id") String id);

    @POST("users/userDetail.php")
    @FormUrlEncoded
    Observable<User> getUserDetail(
            @Field("id") String id);

    @POST("users/followers.php")
    @FormUrlEncoded
    Observable<List<User>> getFans(
            @Field("id") String id);

    @POST("users/stars.php")
    @FormUrlEncoded
    Observable<List<User>> getStars(
            @Field("id") String id);

    @POST("users/updateUserDetail.php")
    @FormUrlEncoded
    Observable<Info> updateUserDetail(
            @Field("avatar") String avatar,
            @Field("name") String name,
            @Field("intro") String intro,
            @Field("gender") int gender
            );

    @GET("config/checkUpdate.php")
    Observable<UpdateInfo> getUpdateInfo();

}

