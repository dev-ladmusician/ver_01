package com.goqual.a10k.presenter.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.goqual.a10k.presenter.WifiPresenter;
import com.goqual.a10k.util.Constraint;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.WifiLevelDescCompare;

import java.util.Collections;
import java.util.List;

/**
 * Created by hanwool on 2017. 2. 23..
 */

public class WifiPresenterImpl implements WifiPresenter {

    public static final String TAG = WifiPresenterImpl.class.getSimpleName();

    public static final int WIFI_FREQUENCY_MAX_VALUE = 3000;

    private WifiPresenterImpl.View mView;
    private Context mContext = null;
    private WifiManager mWifiManager;

    private List<ScanResult> mScanResultList;

    public WifiPresenterImpl(WifiPresenterImpl.View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void startScan() {
        LogUtil.d(TAG, "startScan");
        mView.onScanStart();
        enableWifi();
        mContext.registerReceiver(scanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        getWifiManager().startScan();
    }

    @Override
    public void onClick(int position) {

    }

    @Override
    public void connectToWifi(int position, String pass) {
    }

    @Override
    public void connect10K() {

    }

    private void enableWifi() {
        LogUtil.d(TAG, "enableWifi");
        if(!getWifiManager().isWifiEnabled()) {
            getWifiManager().setWifiEnabled(true);
        }
    }

    private WifiManager getWifiManager() {
        if(mWifiManager == null) {
            mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = mWifiManager.getConnectionInfo();
            LogUtil.d(TAG, "STATUS::info:" + info.toString());
        }
        return mWifiManager;
    }

    BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.d(TAG, "onReceive");
            mScanResultList = getWifiManager().getScanResults();
            Collections.sort(mScanResultList, new WifiLevelDescCompare());
            boolean isSwitchFinded = false;
            for(ScanResult result : mScanResultList) {
                LogUtil.d(TAG, result.toString());
                isSwitchFinded = isSwitchFinded || (result.SSID.equals(Constraint.AP_NAME1) || result.SSID.equals(Constraint.AP_NAME2));
                if(result.frequency < WIFI_FREQUENCY_MAX_VALUE) {
                    mView.addAP(result);
                }
            }
            if(!isSwitchFinded) {
                mView.noSwitchFined();
            }
            mView.onScanEnd();
        }
    };
}
