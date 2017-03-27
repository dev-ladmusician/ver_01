package com.goqual.a10k.view.base;

import android.support.annotation.StringRes;

/**
 * Created by hanwool on 2017. 3. 27..
 */

public interface BaseErrorHandler {
    void onError(Throwable e);
    void finish();
    void restartAuth();
    String getString(@StringRes int resId);
}
