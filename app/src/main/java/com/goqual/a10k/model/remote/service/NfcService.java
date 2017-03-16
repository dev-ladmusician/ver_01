package com.goqual.a10k.model.remote.service;

import android.content.Context;

import com.goqual.a10k.model.entity.NfcWrap;
import com.goqual.a10k.model.entity.PagenationWrapper;
import com.goqual.a10k.model.realm.Nfc;
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

public class NfcService {
    private NfcApi mNfcApi = null;

    public NfcService(Context ctx) {
        mNfcApi =
                RetrofitManager.getInstance()
                        .getRetrofitBuilder(ctx).create(NfcApi.class);
    }

    public NfcApi getrNfcApi() {
        return mNfcApi;
    }

    public interface NfcApi {
        @GET("nfc/{switchId}/{page}")
        Observable<ResultDTO<PagenationWrapper<NfcWrap>>> gets(
                @Path("switchId") int switchId,
                @Path("page") int page
        );

        @GET("nfc/detected/{tag}")
        Observable<ResultDTO<NfcWrap>> get(
                @Path("tag") String tag
        );

        @FormUrlEncoded
        @PUT("nfc")
        Observable<ResultDTO<NfcWrap>> put(
                @Field("nfcId") int nfcId,
                @Field("switchId") int switchId,
                @Field("tag") String tag,
                @Field("btn1") Boolean btn1,
                @Field("btn2") Boolean btn2,
                @Field("btn3") Boolean btn3,
                @Field("title") String title
        );

        @FormUrlEncoded
        @POST("nfc")
        Observable<ResultDTO<NfcWrap>> add(
                @Field("switchId") int switchId,
                @Field("tag") String tag,
                @Field("btn1") Boolean btn1,
                @Field("btn2") Boolean btn2,
                @Field("btn3") Boolean btn3,
                @Field("title") String title
        );

        @FormUrlEncoded
        @HTTP(path = "nfc", method = "DELETE", hasBody = true)
        Observable<ResultDTO<NfcWrap>> delete(
                @Field("nfcId") int nfcId
        );
    }
}
