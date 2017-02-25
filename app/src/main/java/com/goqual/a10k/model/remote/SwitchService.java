package com.goqual.a10k.model.remote;

import android.content.Context;

import com.goqual.a10k.model.entity.Switch;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by ladmusician on 2016. 12. 9..
 */

public class SwitchService {
    private SwitchApi mSwitchApi = null;

    public SwitchService(Context ctx) {
        mSwitchApi =
                RetrofitManager.getInstance()
                        .getRetrofitBuilder(ctx).create(SwitchApi.class);
    }

    public SwitchApi getSwitchApi() {
        return mSwitchApi;
    }

    public interface SwitchApi {
        @GET("switch/{macaddr}")
        Observable<ResultDTO<Switch>> get(
                @Path("macaddr") String macaddr
        );

        @GET("connections")
        Observable<ResultDTO<List<Switch>>> gets();

        @FormUrlEncoded
        @HTTP(path = "connection", method = "DELETE", hasBody = true)
        Observable<ResultDTO<Switch>> delete(
                @Field("connectionId") int connectionId
        );

        @FormUrlEncoded
        @PUT("connection")
        Observable<ResultDTO<Switch>> rename(
                @Field("connectionId") int connectionId,
                @Field("title") String title
        );

        @FormUrlEncoded
        @POST("connection")
        Observable<ResultDTO<Switch>> add(
                @Field("switchMacAddr") String macAddr,
                @Field("title") String title,
                @Field("count") int count
        );
    }
}
