package com.goqual.a10k.util;

import android.content.Context;
import android.content.Intent;

import com.crashlytics.android.Crashlytics;
import com.goqual.a10k.R;
import com.goqual.a10k.view.activities.ActivityPhoneAuth;
import com.goqual.a10k.view.base.BaseErrorHandler;

import io.realm.exceptions.RealmException;
import io.realm.internal.IOException;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by hanwool on 2017. 3. 27..
 */

public class ErrorHandler {
    public static String makeErrorString(BaseErrorHandler ctx, Throwable e){
        if(e instanceof HttpException) {
            switch (((HttpException) e).code()) {
                case HttpResponseCode.ERROR_BAD_GATEWAY:
                case HttpResponseCode.ERROR_INTERNAL_SERVER:
                case HttpResponseCode.ERROR_BAD_REQUEST:
                case HttpResponseCode.ERROR_NOT_FOUND:
                    return ctx.getString(R.string.error_string_server);
                case HttpResponseCode.ERROR_UNAUTHORIZED:
                    ctx.restartAuth();
                    ctx.finish();
                    return ctx.getString(R.string.error_string_auth);
                default:
                    return ctx.getString(R.string.error_string_unknown);
            }
        }
        else if(e instanceof RealmException) {
            return ctx.getString(R.string.error_string_realm);
        }
        else if(e instanceof IOException) {
            return ctx.getString(R.string.error_string_network);
        }
        else {
            Crashlytics.logException(e);
            return ctx.getString(R.string.error_string_unknown);
        }
    }
}
