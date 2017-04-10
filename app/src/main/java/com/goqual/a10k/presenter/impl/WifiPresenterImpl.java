package com.goqual.a10k.presenter.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.goqual.a10k.presenter.WifiPresenter;
import com.goqual.a10k.util.Constraint;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.switchConnect.WifiLevelDescCompare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hanwool on 2017. 2. 23..
 */

public class WifiPresenterImpl implements WifiPresenter {
    public static final String TAG = WifiPresenterImpl.class.getSimpleName();
    public enum CONNECTION_FAIL_TYPE {
        WIFI_ERROR,
        SOCKET_ERROR,
    }

    public static final int WIFI_FREQUENCY_MAX_VALUE = 3000;

    private WifiPresenterImpl.View mView;
    private Context mContext = null;
    private WifiManager mWifiManager;

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
        enableWifi();
        mView.loadingStart();
        mContext.registerReceiver(scanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mContext.registerReceiver(networkChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        mScanResultList = new ArrayList<>();
        getWifiManager().startScan();
    }

    @Override
    public void stopScan() {
        mContext.unregisterReceiver(scanReceiver);
    }

    @Override
    public void stopNetworkReceiver() {
        mContext.unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onClick(int position) {
        //mSelectedWifi = mScanResultList.get(position);
        mView.openPassDialog(mScanResultList.get(position).SSID);
    }

    @Override
    public void connect10K() {
         mView.loadingStart();

        if(m10KResult != null) {
            // check wifi enable
            mNetworkId = getWifiManager().addNetwork(getWifiConfiguration());

            // try to connect wifi
            if(mNetworkId != -1 && getWifiManager().enableNetwork(mNetworkId, true))
                LogUtil.d(TAG, "connect10K::SUCCESS:" + mNetworkId);
            else
                mView.onErrorConnectAP();
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
            for(ScanResult result : scanResultList) {
                if(result.frequency < WIFI_FREQUENCY_MAX_VALUE) {
                    if(!(result.SSID.equals(Constraint.AP_NAME1) || result.SSID.equals(Constraint.AP_NAME2))) {
                        if(!result.SSID.isEmpty()) {
                            mScanResultList.add(result);
                            mView.addAP(result);
                        }
                    } else {
                        m10KResult = result;
                    }
                }
            }

            // find 10k ap !!
            if(m10KResult != null) {
                mView.loadingStop();
                mView.set10KAP(m10KResult);
                mView.refresh();

                stopScan();
            } else {
                mView.noSwitchFound();
            }
        }
    };

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        /**
         * 네트워크 변경을 감시합니다.
         * 네트워크 변경이 감지되었을 때 인터넷이 연결된 상태라면 서버와 재접속을 시도합니다.
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            WifiInfo info = getWifiManager().getConnectionInfo();
            if(info != null) {
                String ssid = info.getSSID().replaceAll("\"", "");
                if(ssid.equals(Constraint.AP_NAME1) || ssid.equals(Constraint.AP_NAME2)) {
                    NetworkInfo networkInfo = (NetworkInfo)intent.getExtras().get("networkInfo");
                    if(networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        mView.changeFragSetSwitch();
                        mView.loadingStop();

                        stopNetworkReceiver();
                    }
                }
            }
        }
    };
}
