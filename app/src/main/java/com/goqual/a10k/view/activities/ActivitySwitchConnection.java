package com.goqual.a10k.view.activities;

import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivitySwitchConnectionBinding;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.presenter.ConnectPresenter;
import com.goqual.a10k.presenter.impl.ConnectPresenterImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.base.BaseActivity;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.dialog.CustomDialog;
import com.goqual.a10k.view.fragments.connect.FragmentConnectInfo;
import com.goqual.a10k.view.fragments.connect.FragmentConnectSelectWifi;
import com.goqual.a10k.view.fragments.connect.FragmentConnectSetSwitch;
import com.goqual.a10k.view.interfaces.IActivityFragmentPageChangeListener;
import com.goqual.a10k.view.interfaces.IActivityInteraction;
import com.goqual.a10k.view.interfaces.IAuthInteraction;
import com.goqual.a10k.view.interfaces.IConnectionActivityInteraction;

public class ActivitySwitchConnection extends BaseActivity<ActivitySwitchConnectionBinding>
        implements ConnectPresenter.View, IActivityFragmentPageChangeListener,
            IActivityInteraction, IAuthInteraction, IConnectionActivityInteraction {

    public static final String TAG = ActivitySwitchConnection.class.getSimpleName();

    private ConnectPresenter mPresenter;
    private ScanResult mSelectedWifi;
    private String mSelectedWifiPasswd;
    private ScanResult m10KAP;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_switch_connection;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_connection);
        getSupportFragmentManager()
                .beginTransaction()
                .add(mBinding.activityMain.getId(), FragmentConnectInfo.newInstance())
                .commit();
        setDetectNetworkError(false);
    }

    private ConnectPresenter getPresenter() {
        if(mPresenter == null) {
            mPresenter = new ConnectPresenterImpl(this, this);
        }
        return mPresenter;
    }

    /**
     * switch connection을 4번 체킹하고도 연결이 안됬을 시 dialog 오픈
     */
    @Override
    public void onErrorCheckConnection() {
        DialogInterface.OnClickListener onClickListener = (dialog, which) -> {
            dialog.dismiss();
        };
        CustomDialog customDialog = new CustomDialog(this);
        customDialog.isEditable(false)
                .isEditable(false)
                .setTitleText(R.string.socket_error_title)
                .setMessageText(R.string.socket_error_content)
                .setPositiveButton(getString(R.string.common_ok), onClickListener)
                .setNegativeButton(false)
                .show();
    }

    /**
     * socket error시 dialog 띄우기
     */
    @Override
    public void onErrorSocketConnection() {
        DialogInterface.OnClickListener onClickListener = (dialog, which) -> {
            dialog.dismiss();
        };
        CustomDialog customDialog = new CustomDialog(this);
        customDialog.isEditable(false)
                .isEditable(false)
                .setTitleText(R.string.check_connection_error_title)
                .setMessageText(R.string.check_connection_error_content)
                .setPositiveButton(getString(R.string.common_ok), onClickListener)
                .setNegativeButton(false)
                .show();
    }

    /**
     * 스위치 등록 오류 시 dialog 띄우기
     */
    @Override
    public void onErrorRename() {
        DialogInterface.OnClickListener onClickListener = (dialog, which) -> {
            dialog.dismiss();
        };
        CustomDialog customDialog = new CustomDialog(this);
        customDialog.isEditable(false)
                .isEditable(false)
                .setTitleText(R.string.rename_title)
                .setMessageText(R.string.rename_content)
                .setPositiveButton(getString(R.string.common_ok), onClickListener)
                .setNegativeButton(false)
                .show();
    }

    @Override
    public void changePage(int page) {
        LogUtil.d(TAG, "changePage::"+page);
        BaseFragment fragment = null;
        if(page == getResources().getInteger(R.integer.frag_info)) {
            fragment = FragmentConnectInfo.newInstance();
        }
        else if(page == getResources().getInteger(R.integer.frag_select_wifi)) {
            fragment = FragmentConnectSelectWifi.newInstance();
        }
        else if(page == getResources().getInteger(R.integer.frag_set_switch)) {
            fragment = FragmentConnectSetSwitch.newInstance();
        }
        else if(page == getResources().getInteger(R.integer.frag_rename)) {

        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(mBinding.activityMain.getId(), fragment)
                .commit();
    }

    @Override
    public ScanResult getSelectedWifi() {
        return mSelectedWifi;
    }

    @Override
    public String getSelectedWifiPasswd() {
        return mSelectedWifiPasswd;
    }

    @Override
    public void setSelectedWifi(android.net.wifi.ScanResult wifi, String passwd) {
        this.mSelectedWifi = wifi;
        this.mSelectedWifiPasswd = passwd;
    }

    @Override
    public ScanResult get10KAP() {
        return this.m10KAP;
    }

    @Override
    public void set10KAP(ScanResult ap) {
        this.m10KAP = ap;
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
    public void onBtnClick(View view) {

    }

    @Override
    public PreferenceHelper getPreferenceHelper() {
        return null;
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void keyPadUp() {

    }

    @Override
    public void keyPadDown() {

    }

    @Override
    public void loadingStart() {

    }

    @Override
    public void loadingStop() {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void addItem(Object item) {

    }

    @Override
    public int getCurrentPage() {
        return 0;
    }
}
