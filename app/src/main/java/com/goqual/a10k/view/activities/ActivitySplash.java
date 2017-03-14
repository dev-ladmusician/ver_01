package com.goqual.a10k.view.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.goqual.a10k.R;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.dialog.CustomDialog;

public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        setContentView(R.layout.activity_splash);

        if(!checkInternetConnection()) {
            new CustomDialog(this)
                    .setTitleText(R.string.internet_error_title)
                    .setMessageText(R.string.internet_error_content)
                    .setPositiveButton(getString(R.string.common_quit), (dialog, which) -> {
                        dialog.dismiss();
                        finish();
                    })
            .show();
        }
        else {
            String token = PreferenceHelper.getInstance(this)
                    .getStringValue(getString(R.string.arg_user_token), "");
            String fcm = PreferenceHelper.getInstance(this)
                    .getStringValue(getString(R.string.arg_user_fcm_token), "");
            LogUtil.d("TOKEN", "APP::" + token + "\nFCM::" + fcm);
            if (token.isEmpty()) {
                startActivity(new Intent(this, ActivityPhoneAuth.class));
            } else {
                startActivity(new Intent(this, ActivityMain.class));
            }
            finish();
        }
    }

    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetworkInfo() != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }
}
