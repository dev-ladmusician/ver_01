package com.goqual.a10k.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ladmusician on 4/14/16.
 */
public class ToastUtil {
    public static void show(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
}
