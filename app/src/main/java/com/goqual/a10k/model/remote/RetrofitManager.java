package com.goqual.a10k.model.remote;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.goqual.a10k.R;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.util.Constraint;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ladmusician on 2017. 2. 21..
 */

public class RetrofitManager {
    public static RetrofitManager instance;
    private final String BASE_URL = Constraint.BASE_URL;
    private final String AUTH_BASE_URL = Constraint.AUTH_BASE_URL;
    private Retrofit mRetrofit = null;
    private Retrofit mAuthRetrofit = null;
    private Context mContext = null;

    public static RetrofitManager getInstance() {
        if (instance == null) instance = new RetrofitManager();
        return instance;
    }

    public Retrofit getRetrofitBuilder(Context ctx) {
        mContext = ctx;
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header(Constraint.CUSTOM_HEADER, getToken()); // <-- this is the important line

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        OkHttpClient client = httpClient.build();


        if (mRetrofit == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
                    .registerTypeAdapter(boolean.class, booleanAsIntAdapter)
                    .setLenient()
                    .create();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
        }

        return mRetrofit;
    }

    public Retrofit getAuthRetrofitBuilder() {
        if (mAuthRetrofit == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
                    .registerTypeAdapter(boolean.class, booleanAsIntAdapter)
                    .create();
            mAuthRetrofit = new Retrofit.Builder()
                    .baseUrl(AUTH_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }

        return mAuthRetrofit;
    }

    private static final TypeAdapter<Boolean> booleanAsIntAdapter = new TypeAdapter<Boolean>() {
        @Override public void write(JsonWriter out, Boolean value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value);
            }
        }
        @Override public Boolean read(JsonReader in) throws IOException {
            JsonToken peek = in.peek();
            switch (peek) {
                case BOOLEAN:
                    return in.nextBoolean();
                case NULL:
                    in.nextNull();
                    return null;
                case NUMBER:
                    return in.nextInt() != 0;
                case STRING:
                    return Boolean.parseBoolean(in.nextString());
                default:
                    throw new IllegalStateException("Expected BOOLEAN or NUMBER but was " + peek);
            }
        }
    };

    public String getToken() {
        return getPreferenceHelper().getStringValue(
                mContext.getString(R.string.arg_user_token), ""
        );
    }

    public PreferenceHelper getPreferenceHelper() {
        return new PreferenceHelper(mContext);
    }
}
