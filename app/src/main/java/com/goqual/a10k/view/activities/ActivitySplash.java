package com.goqual.a10k.view.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.goqual.a10k.R;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.util.Constraint;
import com.goqual.a10k.util.LogUtil;

public class ActivitySplash extends AppCompatActivity {
    private final String TAG = ActivitySplash.class.getSimpleName();

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

        LogUtil.d("CURRENT SERVER", Constraint.BASE_URL);

        if(!checkInternetConnection()) {
            finish();
            Toast.makeText(this, R.string.internet_error_content, Toast.LENGTH_SHORT).show();
        } else {
            String token = PreferenceHelper.getInstance(this)
                    .getStringValue(getString(R.string.arg_user_token), "");

            LogUtil.e(TAG, "push token new :: " + PreferenceHelper.getInstance(this)
                    .getStringValue(getString(R.string.arg_user_fcm_token), null));

            if (token.isEmpty()) {
                /**
                 * 새로운 fcm token이 생기면 preference update
                 */

                LogUtil.e(TAG, "FCM TOKEN :: " + FirebaseInstanceId.getInstance().getToken());
                startActivity(new Intent(this, ActivityPhoneAuth.class));
            } else {
                startActivity(new Intent(this, ActivityMain.class));
            }
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
