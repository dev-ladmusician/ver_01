package com.goqual.a10k.model.remote.service;

import android.content.Context;

import com.goqual.a10k.model.entity.NotiWrap;
import com.goqual.a10k.model.entity.Version;
import com.goqual.a10k.model.remote.ResultDTO;
import com.goqual.a10k.model.remote.RetrofitManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import rx.Observable;

public class VersionService {
    private VersionApi mNotiApi = null;

    public VersionService(Context ctx) {
        mNotiApi =
                RetrofitManager.getInstance()
                        .getRetrofitBuilder(ctx).create(VersionApi.class);
    }

    public VersionApi getVerionApi() {
        return mNotiApi;
    }

    public interface VersionApi {
        @GET("10K")
        Observable<Version> get();
    }
}
