package com.jude.album.model.server;


import com.jude.album.domain.body.Exist;
import com.jude.album.domain.body.Info;
import com.jude.album.domain.body.Token;
import com.jude.album.domain.entities.Album;
import com.jude.album.domain.entities.Picture;
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


    @GET("users/refreshAccount.php")
    Observable<User> refreshAccount();

    @POST("users/albums.php")
    @FormUrlEncoded
    Observable<List<Album>> getAlbums(
            @Field("id") String id);

    @POST("users/pictures.php")
    @FormUrlEncoded
    Observable<List<Picture>> getPictures(
            @Field("id") String id);


    @POST("pictures/uploadPicture.php")
    @FormUrlEncoded
    Observable<Info> uploadPicture(
            @Field("src") String src,
            @Field("name") String name,
            @Field("intro") String intro,
            @Field("height") int height,
            @Field("width") int width,
            @Field("String") String tag
            );
}
