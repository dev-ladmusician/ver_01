package com.goqual.a10k.model.remote.service;

import android.content.Context;

import com.goqual.a10k.model.entity.History;
import com.goqual.a10k.model.entity.PagenationWrapper;
import com.goqual.a10k.model.remote.ResultDTO;
import com.goqual.a10k.model.remote.RetrofitManager;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public class HistoryService {
    private HistoryApi mHistoryApi = null;

    public HistoryService(Context ctx) {
        mHistoryApi =
                RetrofitManager.getInstance()
                        .getRetrofitBuilder(ctx).create(HistoryApi.class);
    }

    public HistoryApi getHistoryApi() {
        return mHistoryApi;
    }

    public interface HistoryApi {
        @GET("history/{switchId}/{year}/{month}/{day}/{page}")
        Observable<PagenationWrapper<History>> gets(
                @Path("switchId") int switchId,
                @Path("year") int year,
                @Path("month") int month,
                @Path("day") int day,
                @Path("page") int page
        );
    }
}
