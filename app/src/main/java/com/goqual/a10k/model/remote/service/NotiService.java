package com.goqual.a10k.model.remote.service;

import android.content.Context;


import com.goqual.a10k.model.entity.NotiWrap;
import com.goqual.a10k.model.realm.Noti;
import com.goqual.a10k.model.remote.ResultDTO;
import com.goqual.a10k.model.remote.RetrofitManager;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

public class NotiService {
    private NotiApi mNotiApi = null;

    public NotiService(Context ctx) {
        mNotiApi =
                RetrofitManager.getInstance()
                        .getRetrofitBuilder(ctx).create(NotiApi.class);
    }

    public NotiApi getNotiApi() {
        return mNotiApi;
    }

    public interface NotiApi {
        @GET("noti")
        Observable<ResultDTO<List<NotiWrap>>> gets();
    }
}
