package com.goqual.a10k.view.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.goqual.a10k.R;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.view.activities.ActivityMain;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String token = PreferenceHelper.getInstance(this)
                .getStringValue(getString(R.string.arg_user_token), "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MywiaWF0IjoxNDg3NjUwOTc2LCJleHAiOjE0ODgxNzY1NzZ9.z4eJQRtAcGYJFyEWikVYYHrtmcfOl1R3c9bsFy5VNTI");
        // TODO: AUTH
//        if(token.isEmpty()) {
//            startActivity(new Intent(this, ActivityPhoneAuth.class));
//        }
//        else {
            startActivity(new Intent(this, ActivityMain.class));
//        }
        finish();
    }
}
