package com.goqual.a10k.model.remote.service;

import android.content.Context;

import com.goqual.a10k.model.entity.User;
import com.goqual.a10k.model.remote.ResultDTO;
import com.goqual.a10k.model.remote.RetrofitManager;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public class UserService {
    private UserApi mUserApi = null;

    public UserService(Context ctx) {
        mUserApi =
                RetrofitManager.getInstance()
                        .getRetrofitBuilder(ctx).create(UserApi.class);
    }

    public UserApi getUserApi() {
        return mUserApi;
    }

    public interface UserApi {
        @GET("connection/getUsers/{switchId}")
        Observable<ResultDTO<List<User>>> gets(
                @Path("switchId") int switchId
        );

        @FormUrlEncoded
        @HTTP(path = "connection", method = "DELETE", hasBody = true)
        Observable<ResultDTO<User>> delete(
                @Field("connectionId") int connectionId
        );

        @FormUrlEncoded
        @PUT("connection")
        Observable<ResultDTO<User>> changeAdmin(
                @Field("connectionId") int connectionId,
                @Field("targetConnectionId") int targetConnectionId
        );

        @FormUrlEncoded
        @POST("auth")
        Observable<Response<ResultDTO<User>>> join(
                @Field("num") String num,
                @Field("pushBuildToken") String pushBuildToken,
                @Field("pushType") String pushType
        );

        @FormUrlEncoded
        @PUT("auth")
        Observable<ResultDTO<User>> login(
                @Field("num") String num
        );
    }
}
