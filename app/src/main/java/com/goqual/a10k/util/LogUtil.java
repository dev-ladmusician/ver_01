package com.goqual.a10k.util;

import android.util.Log;

import com.goqual.a10k.view.base.ApplicationBase;


/**
 * Created by ladmusician on 2016. 12. 7..
 */

public class LogUtil {
    /** Log Level Error **/
    public static final void e(String TAG, String message) {
        if (ApplicationBase.DEBUG) Log.e(TAG, buildLogMsg(message));
    }
    public static final void e(String TAG, String message, Throwable thr) {
        if (ApplicationBase.DEBUG) Log.e(TAG, buildLogMsg(message), thr);
    }
    /** Log Level Warning **/
    public static final void w(String TAG, String message) {
        if (ApplicationBase.DEBUG)Log.w(TAG, buildLogMsg(message));
    }
    public static final void w(String TAG, String message, Throwable thr) {
        if (ApplicationBase.DEBUG)Log.w(TAG, buildLogMsg(message), thr);
    }
    /** Log Level Information **/
    public static final void i(String TAG, String message) {
        if (ApplicationBase.DEBUG)Log.i(TAG, buildLogMsg(message));
    }
    /** Log Level Debug **/
    public static final void d(String TAG, String message) {
        if (ApplicationBase.DEBUG)Log.d(TAG, buildLogMsg(message));
    }
    /** Log Level Verbose **/
    public static final void v(String TAG, String message) {
        if (ApplicationBase.DEBUG)Log.v(TAG, buildLogMsg(message));
    }


    public static String buildLogMsg(String message) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        sb.append(ste.getFileName().replace(".java", ""));
        sb.append("::");
        sb.append(ste.getMethodName());
        sb.append("] ");
        sb.append(message);

        return sb.toString();
    }
}
