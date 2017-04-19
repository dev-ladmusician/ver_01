package com.goqual.a10k.model.remote.service;

import android.content.Context;

import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.model.entity.User;
import com.goqual.a10k.model.remote.ResultDTO;
import com.goqual.a10k.model.remote.RetrofitManager;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public class InviteService {
    private InviteApi mInviteApi = null;

    public InviteService(Context ctx) {
        mInviteApi =
                RetrofitManager.getInstance()
                        .getRetrofitBuilder(ctx).create(InviteApi.class);
    }

    public InviteApi getInviteApi() {
        return mInviteApi;
    }

    public interface InviteApi {
        @GET("invite/{num}/{switchId}")
        Observable<ResultDTO<User>> checkUserIsJoinedAndConnected(
                @Path("num") String num,
                @Path("switchId") int switchId
        );

        @FormUrlEncoded
        @POST("invite")
        Observable<ResultDTO<Switch>> acceptInvite(
                @Field("switchId") int switchId
        );
    }
}
