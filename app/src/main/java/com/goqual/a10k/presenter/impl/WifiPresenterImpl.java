package com.goqual.a10k.presenter.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import com.goqual.a10k.presenter.WifiPresenter;
import com.goqual.a10k.util.Constraint;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.switchConnect.SimpleSocketClient;
import com.goqual.a10k.util.switchConnect.WifiLevelDescCompare;

import java.util.ArrayList;
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

    private int mSelectedWifiPostion;
    private String mSelectedWifiPassword;

    private ArrayList<ScanResult> mScanResultList;

    private ScanResult m10KResult;
    private WifiConfiguration mWifiConfiguration;
    private int mNetworkId;

    public WifiPresenterImpl(WifiPresenterImpl.View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void startScan() {
        mView.onScanStart();
        enableWifi();
        mContext.registerReceiver(scanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mScanResultList = new ArrayList<>();
        getWifiManager().startScan();
    }

    @Override
    public void destroy() {
        mContext.unregisterReceiver(scanReceiver);
    }

    @Override
    public void onClick(int position) {
        mSelectedWifiPostion = position;
        mView.openPassDialog(mScanResultList.get(position).SSID);
    }

    @Override
    public void connectToWifi(String pass) {
        mSelectedWifiPassword = pass;
    }

    @Override
    public void connect10K() {
        LogUtil.d(TAG, "connect10K::" + m10KResult);
        if(m10KResult != null) {
            mNetworkId = getWifiManager().addNetwork(getWifiConfiguration());
            if(mNetworkId != -1 && getWifiManager().enableNetwork(mNetworkId, true)) {
                LogUtil.d(TAG, "connect10K::SUCCESS:" + mNetworkId);
                SimpleSocketClient.getInstance(Constraint.BS_SERVER_IP, Constraint.SERVER_PORT);
            }
            else {
                LogUtil.d(TAG, "addNetwork::FAILED:" + mNetworkId);
                mView.openErrorDialog();
                endConnect10K();
            }
        }
    }

    private void endConnect10K(){
        if(m10KResult != null && mNetworkId != -1) {
            if(getWifiManager().disableNetwork(mNetworkId)) {
                LogUtil.d(TAG, "disableNetwork::SUCCESS");
            }
            else {
                LogUtil.d(TAG, "disableNetwork::FAILED");
            }
            if(getWifiManager().removeNetwork(mNetworkId)) {
                LogUtil.d(TAG, "removeNetwork::SUCCESS");
            }
            else {
                LogUtil.d(TAG, "removeNetwork::FAILED");
            }
        }
    }

    private WifiConfiguration getWifiConfiguration() {
        if(mWifiConfiguration == null) {
            mWifiConfiguration = new WifiConfiguration();
            mWifiConfiguration.SSID = m10KResult.SSID;
            mWifiConfiguration.status = WifiConfiguration.Status.DISABLED;
            mWifiConfiguration.priority = 40;
            mWifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            mWifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            mWifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            mWifiConfiguration.allowedAuthAlgorithms.clear();
            mWifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

            mWifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            mWifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            mWifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            mWifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        }
        return mWifiConfiguration;
    }

    private void enableWifi() {
        if(!getWifiManager().isWifiEnabled()) {
            getWifiManager().setWifiEnabled(true);
        }
    }

    private WifiManager getWifiManager() {
        if(mWifiManager == null) {
            mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        }
        return mWifiManager;
    }

    BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<ScanResult> scanResultList = getWifiManager().getScanResults();
            Collections.sort(scanResultList, new WifiLevelDescCompare());
            boolean isSwitchFinded = false;
            for(ScanResult result : scanResultList) {
                isSwitchFinded = isSwitchFinded || (result.SSID.equals(Constraint.AP_NAME1) || result.SSID.equals(Constraint.AP_NAME2));
                if(result.frequency < WIFI_FREQUENCY_MAX_VALUE) {
                    if(!(result.SSID.equals(Constraint.AP_NAME1) || result.SSID.equals(Constraint.AP_NAME2))) {
                        if(!result.SSID.isEmpty()) {
                            mScanResultList.add(result);
                            mView.addAP(result);
                        }
                    }
                    else {
                        m10KResult = result;
                    }
                }
            }
            if(!isSwitchFinded) {
                mView.noSwitchFound();
            }
            mView.onScanEnd();
        }
    };
}
