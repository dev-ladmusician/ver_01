package com.goqual.a10k.util;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.goqual.a10k.R;

/**
 * Created by hanwool on 2017. 3. 16..
 */

public class BackPressUtil {
    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressUtil(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            toast.cancel();

            activity.finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity, activity.getString(R.string.common_quit_toast), Toast.LENGTH_SHORT);
        toast.show();
    }
}
