package com.goqual.a10k.model.remote.service;

import android.content.Context;

import com.goqual.a10k.model.entity.Certifi;
import com.goqual.a10k.model.remote.ResultDTO;
import com.goqual.a10k.model.remote.RetrofitManager;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public class AuthService {
    private AuthApi mUserApi = null;

    public AuthService(Context ctx) {
        mUserApi =
                RetrofitManager.getInstance()
                        .getAuthRetrofitBuilder().create(AuthApi.class);
    }

    public AuthApi getAuthApi() {
        return mUserApi;
    }

    public interface AuthApi {
        @FormUrlEncoded
        @POST("CERTIFICATE/sms")
        Observable<ResultDTO<Certifi>> getCertification (
                @Field("num") String phone
        );
    }

}
