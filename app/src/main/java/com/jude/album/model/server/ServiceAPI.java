package com.jude.album.model.server;


import com.jude.album.domain.body.Token;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Mr.Jude on 2015/11/18.
 */
public interface ServiceAPI {
    String SERVER_ADDRESS = "http://123.56.230.6/2.0/";

    @GET("qiniu.php")
    Observable<Token> getQiniuToken();
}
