package com.goqual.a10k.view.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivityPhoneAuthBinding;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.presenter.impl.PhoneAuthPresenterImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.base.BaseActivity;
import com.goqual.a10k.presenter.PhoneAuthPresenter;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.dialog.CustomDialog;
import com.goqual.a10k.view.fragments.auth.FragmentAuthCertification;
import com.goqual.a10k.view.fragments.auth.FragmentAuthPhone;
import com.goqual.a10k.view.interfaces.IActivityInteraction;
import com.goqual.a10k.view.interfaces.IAuthActivityInteraction;

/**
 * Created by hanwool on 2017. 2. 24..
 */

public class ActivityPhoneAuth extends BaseActivity<ActivityPhoneAuthBinding>
        implements IActivityInteraction, PhoneAuthPresenter.View, IAuthActivityInteraction{

    public enum ERROR_REASON{
        CONNECTION_ERROR,
        TIMEOUT,
        WRONG_NUMBER_FORMAT
    }
    public static final String TAG = ActivityPhoneAuth.class.getSimpleName();

    private static final int PHONE_NUMBER_PERMISSION_REQ = 112;

    private PhoneAuthPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone_auth;
    }

    @Override
    public void onBtnClick(View view) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_NUMBER_PERMISSION_REQ);
        }
        else {
            initFirstFragment();
        }
    }

    private void initFirstFragment() {
        BaseFragment baseFragment = FragmentAuthPhone.newInstance(
                getPresenter().getPhoneNumber(), getPresenter().getPhoneNumberCountryCode());
        getSupportFragmentManager().beginTransaction()
                .add(mBinding.fragmentContainer.getId(), baseFragment)
                .commit();
    }

    @Override
    public AppBarLayout getAppbar() {
        return null;
    }

    @Override
    public Toolbar getToolbar() {
        return null;
    }

    @Override
    public void setTitle(String title) {
    }

    @Override
    public void finishApp() {

    }

    @Override
    public void onSuccessAuthProcess() {
        startActivity(new Intent(this, ActivityMain.class));
        finish();
    }

    @Override
    public void onErrorAuthProcess(ERROR_REASON reson, String message) {
        CustomDialog customDialog = new CustomDialog(this);
        DialogInterface.OnClickListener onClickListener = (dialog, which) ->  {
            dialog.dismiss();
            getSupportFragmentManager().beginTransaction()
                    .replace(mBinding.fragmentContainer.getId(),
                            FragmentAuthPhone.newInstance(
                                    getPresenter().getPhoneNumber(), getPresenter().getPhoneNumberCountryCode()))
                    .commit();
        };
        customDialog.isEditable(false)
                .setTitleText(R.string.auth_certi_title)
                .setMessageText(message)
                .setPositiveButton(getString(R.string.common_retry), onClickListener)
                .setNegativeButton(getString(R.string.common_cancel), onClickListener)
                .show();

    }

    @Override
    public void loadingStart() {
        mBinding.authLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadingStop() {
        mBinding.authLoading.setVisibility(View.GONE);
    }

    @Override
    public void refresh() {

    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e(TAG, e.getMessage(), e);

        CustomDialog customDialog = new CustomDialog(this);
        DialogInterface.OnClickListener onClickListener = (dialog, which) ->  {
            dialog.dismiss();
            getSupportFragmentManager().beginTransaction()
                    .replace(mBinding.fragmentContainer.getId(),
                            FragmentAuthPhone.newInstance(
                                    getPresenter().getPhoneNumber(), getPresenter().getPhoneNumberCountryCode()))
                    .commit();
        };
        customDialog.isEditable(false)
                .setTitleText(R.string.auth_certi_title)
                .setMessageText(R.string.auth_phone_error_content)
                .setPositiveButton(getString(R.string.common_retry), onClickListener)
                .setNegativeButton(getString(R.string.common_cancel), onClickListener)
                .show();
    }

    @Override
    public void addItem(Object item) {

    }

    @Override
    public void requestSmsToken(String phoneNumber) {
        getPresenter().requestSmsToken(phoneNumber);
    }

    @Override
    public void requestAppAuthToken(String phoneNumber, String smsToken) {
        getPresenter().requestAppToken(phoneNumber, smsToken);
    }

    @Override
    public void onSuccessPhoneNumberAuth(String phoneNumber, String auth) {
        getSupportFragmentManager().beginTransaction()
                .replace(mBinding.fragmentContainer.getId(), FragmentAuthCertification.newInstance(phoneNumber, auth))
                .commit();
    }

    @Override
    public void requestStartAppAuth() {
    }

    @Override
    public void onEndAuthProcess() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PHONE_NUMBER_PERMISSION_REQ) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LogUtil.d(TAG, "onRequestPermissionsResult");
                initFirstFragment();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private PhoneAuthPresenter getPresenter() {
        if(mPresenter == null) {
            mPresenter = new PhoneAuthPresenterImpl(this, this);
        }
        return mPresenter;
    }

    @Override
    public PreferenceHelper getPreferenceHelper() {
        return null;
    }

    @Override
    public int getCurrentPage() {
        return 0;
    }
}
