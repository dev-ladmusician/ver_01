package com.goqual.a10k.presenter.impl;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.goqual.a10k.R;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.model.remote.service.AuthService;
import com.goqual.a10k.model.remote.service.UserService;
import com.goqual.a10k.presenter.PhoneAuthPresenter;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.activities.ActivityPhoneAuth;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hanwool on 2017. 2. 24..
 */

public class PhoneAuthPresenterImpl implements PhoneAuthPresenter {
    private static final String TAG = PhoneAuthPresenter.class.getSimpleName();


    private View mView;
    private Context mContext = null;

    private AuthService mAuthService;
    private UserService mUserService;

    public PhoneAuthPresenterImpl(View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void requestSmsToken(String phoneNumber) {
        mView.loadingStart();
        getAuthService().getAuthApi().getCertification(phoneNumber)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rtv -> {
                            LogUtil.d(TAG, "CERT::" + rtv.getResult().getCertifinum());
                            mView.onSuccessPhoneNumberAuth(getPhoneNumber(), rtv.getResult().getCertifinum());
                            mView.loadingStop();
                        },
                        (e) -> {
                            mView.loadingStop();
                            if(e instanceof HttpException) {
                                mView.onErrorAuthProcess(ActivityPhoneAuth.ERROR_REASON.CONNECTION_ERROR, ((HttpException)e).message());
                            }
                            else {
                                mView.onError(e);
                            }
                        },
                        mView::requestStartAppAuth);
    }

    @Override
    public void requestAppToken(String phoneNumber, String smsToken) {
        mView.loadingStart();
        getUserService().getUserApi().join(phoneNumber, getFcmToken(), mContext.getString(R.string.push_type))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((x) -> {
                            String accessToken = x.headers().get("access_token");
                            PreferenceHelper.getInstance(mContext).put(mContext.getString(R.string.arg_user_token), accessToken);
                            PreferenceHelper.getInstance(mContext).put(mContext.getString(R.string.arg_user_num), phoneNumber);
                            mView.onSuccessAuthProcess();
                            mView.loadingStop();

                            LogUtil.e(TAG, "access_token : " + accessToken);
                        },
                        (e) -> {
                            mView.loadingStop();
                            if(e instanceof HttpException) {
                                mView.onErrorAuthProcess(ActivityPhoneAuth.ERROR_REASON.CONNECTION_ERROR, ((HttpException)e).message());
                            }
                            else {
                                mView.onError(e);
                            }
                        },
                        mView::onEndAuthProcess
                );
    }

    @Override
    public String getPhoneNumber() {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return parsePhoneNumber(telephonyManager.getLine1Number());
    }

    @Override
    public String getFcmToken() {
        return "";
    }

    public AuthService getAuthService() {
        if (mAuthService == null) {
            mAuthService = new AuthService(mContext);
        }
        return mAuthService;
    }

    public UserService getUserService() {
        if (mUserService == null)
            mUserService = new UserService(mContext);

        return mUserService;
    }

    private String parsePhoneNumber(String number){
        return number.replace("+82", "0");
    }
}
