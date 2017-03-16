package com.goqual.a10k.model.remote.service;

import android.content.Context;

import com.goqual.a10k.model.entity.Alarm;
import com.goqual.a10k.model.entity.PagenationWrapper;
import com.goqual.a10k.model.remote.ResultDTO;
import com.goqual.a10k.model.remote.RetrofitManager;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public class AlarmService {
    private AlarmApi mAlarmApi = null;

    public AlarmService(Context ctx) {
        mAlarmApi =
                RetrofitManager.getInstance()
                        .getRetrofitBuilder(ctx).create(AlarmApi.class);
    }

    public AlarmApi getAlarmApi() {
        return mAlarmApi;
    }

    public interface AlarmApi {
        @GET("alarm/{page}")
        Observable<PagenationWrapper<Alarm>> gets(
                @Path("page") int page
        );

        @FormUrlEncoded
        @PUT("alarm")
        Observable<ResultDTO<Alarm>> put(
                @Field("alarmId") int alarmId,
                @Field("switchId") int switchId,
                @Field("ringtone") String ringtone,
                @Field("ringtoneTitle") String ringtoneTitle,
                @Field("hour") int hour,
                @Field("min") int min,
                @Field("sun") boolean sun,
                @Field("mon") boolean mon,
                @Field("tue") boolean tue,
                @Field("wed") boolean wed,
                @Field("thur") boolean thur,
                @Field("fri") boolean fri,
                @Field("sat") boolean sat,
                @Field("btn1") Boolean btn1,
                @Field("btn2") Boolean btn2,
                @Field("btn3") Boolean btn3,
                @Field("state") boolean state
        );

        @FormUrlEncoded
        @HTTP(path = "alarm", method = "DELETE", hasBody = true)
        Observable<ResultDTO<Alarm>> delete(
                @Field("alarmId") int alarmId
        );

        @FormUrlEncoded
        @POST("alarm")
        Observable<ResultDTO<Alarm>> add(
                @Field("switchId") int switchId,
                @Field("ringtone") String ringtone,
                @Field("ringtoneTitle") String ringtoneTitle,
                @Field("hour") int hour,
                @Field("min") int min,
                @Field("sun") boolean sun,
                @Field("mon") boolean mon,
                @Field("tue") boolean tue,
                @Field("wed") boolean wed,
                @Field("thur") boolean thur,
                @Field("fri") boolean fri,
                @Field("sat") boolean sat,
                @Field("btn1") Boolean btn1,
                @Field("btn2") Boolean btn2,
                @Field("btn3") Boolean btn3
        );
    }
}
