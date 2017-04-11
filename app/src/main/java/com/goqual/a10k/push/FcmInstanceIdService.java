package com.goqual.a10k.push;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.goqual.a10k.R;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.model.remote.service.UserService;
import com.goqual.a10k.util.LogUtil;

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

        String newFcmToken = FirebaseInstanceId.getInstance().getToken();
        String originFcmToken = PreferenceHelper.getInstance(getApplicationContext())
                .getStringValue(getString(R.string.arg_user_fcm_token), "");

        String phoneNumber = PreferenceHelper.getInstance(getApplicationContext())
                .getStringValue(getString(R.string.arg_user_num), "");
        String userAppToken = PreferenceHelper.getInstance(getApplicationContext())
                .getStringValue(getString(R.string.arg_user_token), "");

        boolean isRefreshed = !originFcmToken.equals(newFcmToken);
        LogUtil.d(TAG, "TOKEN::" + newFcmToken +"\nIS_REFRESHED? " + isRefreshed);

        if(isRefreshed) {
            LogUtil.d(TAG, "save new fcm token");
            PreferenceHelper.getInstance(getApplicationContext())
                    .put(getString(R.string.arg_user_fcm_token), newFcmToken);

            if(!phoneNumber.isEmpty() && !userAppToken.isEmpty()) {
                LogUtil.d(TAG, "set new fcm token in server");
                getUserService().getUserApi().join(phoneNumber, newFcmToken, getString(R.string.push_type))
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
