package com.goqual.a10k.util;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.goqual.a10k.R;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.model.remote.service.UserService;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hanwool on 2017. 3. 13..
 */

public class FcmInstanceIdService extends FirebaseInstanceIdService {
    public static final String TAG = FcmInstanceIdService.class.getSimpleName();

    private UserService mService;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        LogUtil.d(TAG, "onTokenRefresh");
        String refreshToken = FirebaseInstanceId.getInstance().getToken();

        String originToken = PreferenceHelper.getInstance(getApplicationContext())
                .getStringValue(getString(R.string.arg_user_fcm_token), "");
        String phoneNumber = PreferenceHelper.getInstance(getApplicationContext())
                .getStringValue(getString(R.string.arg_user_num), "");
        String userAppToken = PreferenceHelper.getInstance(getApplicationContext())
                .getStringValue(getString(R.string.arg_user_token), "");

        boolean isRefreshed = !originToken.equals(refreshToken);
        LogUtil.d(TAG, "TOKEN::" + refreshToken +"\nIS_REFRESHED? " + isRefreshed);

        if(isRefreshed) {
            PreferenceHelper.getInstance(getApplicationContext())
                    .put(getString(R.string.arg_user_fcm_token), refreshToken);
            if(!phoneNumber.isEmpty() && !userAppToken.isEmpty()) {
                getUserService().getUserApi().join(phoneNumber, refreshToken, getString(R.string.push_type))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((x) -> {
                                    String accessToken = x.headers().get("access_token");
                                    PreferenceHelper.getInstance(getApplicationContext())
                                            .put(getString(R.string.arg_user_token), accessToken);
                                    LogUtil.e(TAG, "access_token : " + accessToken);
                                },
                                (e) -> {
                                    LogUtil.e(TAG, e.getMessage(), e);
                                },
                                () -> {

                                }
                        );
            }
        }
    }


    public UserService getUserService() {
        if(mService == null) {
            mService = new UserService(getApplicationContext());
        }
        return mService;
    }
}
