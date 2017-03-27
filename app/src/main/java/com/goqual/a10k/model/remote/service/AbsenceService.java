package com.goqual.a10k.model.remote.service;

import android.content.Context;

import com.goqual.a10k.model.entity.Absence;
import com.goqual.a10k.model.remote.ResultDTO;
import com.goqual.a10k.model.remote.RetrofitManager;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public class AbsenceService {
    private AbsenceApi mAbsenceApi = null;

    public AbsenceService(Context ctx) {
        mAbsenceApi =
                RetrofitManager.getInstance()
                        .getRetrofitBuilder(ctx).create(AbsenceApi.class);
    }

    public AbsenceApi getAbsenceApi() {
        return mAbsenceApi;
    }

    public interface AbsenceApi {
        @GET("absence/{switchId}")
        Observable<ResultDTO<Absence>> get(
                @Path("switchId") int switchId
        );

        @FormUrlEncoded
        @PUT("absence")
        Observable<ResultDTO<Absence>> put(
                @Field("absenceId") int absenceId,
                @Field("switchId") int switchId,
                @Field("startHour") int start_hour,
                @Field("startMin") int start_min,
                @Field("endHour") int end_hour,
                @Field("endMin") int end_min,
                @Field("btn1") boolean btn1,
                @Field("btn2") boolean btn2,
                @Field("btn3") boolean btn3,
                @Field("state") boolean state
        );

        @FormUrlEncoded
        @POST("absence")
        Observable<ResultDTO<Absence>> add(
                @Field("switchId") int switchId,
                @Field("startHour") int start_hour,
                @Field("startMin") int start_min,
                @Field("endHour") int end_hour,
                @Field("endMin") int end_min,
                @Field("btn1") boolean btn1,
                @Field("btn2") boolean btn2,
                @Field("btn3") boolean btn3
        );

        @FormUrlEncoded
        @HTTP(path = "absence", method = "DELETE", hasBody = true)
        Observable<ResultDTO<Absence>> delete(
                @Field("absenceId") int absenceId
        );
    }
}
