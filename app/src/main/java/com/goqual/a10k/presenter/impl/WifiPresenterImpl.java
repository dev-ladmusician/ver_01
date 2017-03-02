package com.goqual.a10k.presenter.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.goqual.a10k.model.remote.service.SwitchService;
import com.goqual.a10k.presenter.WifiPresenter;
import com.goqual.a10k.util.Constraint;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.SocketProtocols;
import com.goqual.a10k.util.interfaces.IRawSocketCommunicationListener;
import com.goqual.a10k.util.switchConnect.ConnectSocketData;
import com.goqual.a10k.util.switchConnect.SimpleSocketClient;
import com.goqual.a10k.util.switchConnect.WifiLevelDescCompare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hanwool on 2017. 2. 23..
 */

public class WifiPresenterImpl implements WifiPresenter, IRawSocketCommunicationListener {

    public enum CONNECTION_FAIL_TYPE {
        WIFI_ERROR,
        SOCKET_ERROR,
    }

    public static final String TAG = WifiPresenterImpl.class.getSimpleName();

    public static final int WIFI_FREQUENCY_MAX_VALUE = 3000;

    private WifiPresenterImpl.View mView;
    private Context mContext = null;
    private WifiManager mWifiManager;

    private ScanResult mSelectedWifi;
    private String mSelectedWifiPassword;

    private ArrayList<ScanResult> mScanResultList;

    private ScanResult m10KResult;
    private WifiConfiguration mWifiConfiguration;
    private int mNetworkId;

    private String mSwitchId;
    private int mSwitchBtnCount;
    private String mSwitchHwVersion;
    private String mSwitchFwVertion;
    private String mSwitchTitle;
    private boolean isConnectionSuccessed;

    private SwitchService mSwitchService;

    private SimpleSocketClient mSocketClient;

    public WifiPresenterImpl(WifiPresenterImpl.View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void startScan() {
        mView.onScanStart();
        enableWifi();
        mContext.registerReceiver(scanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mContext.registerReceiver(networkChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        mScanResultList = new ArrayList<>();
        getWifiManager().startScan();
    }

    @Override
    public void destroy() {
        mContext.unregisterReceiver(scanReceiver);
        mContext.unregisterReceiver(networkChangeReceiver);
        if(mSocketClient != null) {
            mSocketClient.disconnect();
        }
        mSocketClient = null;
    }

    @Override
    public void onClick(int position) {
        mSelectedWifi = mScanResultList.get(position);
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
            }
            else {
                LogUtil.d(TAG, "addNetwork::FAILED:" + mNetworkId);
                mView.onConnectError();
                endConnect10K();
            }
        }
    }

    @Override
    public void setName(String name) {
        mSwitchTitle = name;
        LogUtil.d(TAG, String.format(Locale.KOREA, "<SWITCH DATA> id: %s\nname: %s\nbtnCount: %d\nHW: %s\nFW: %s",
                mSwitchId,
                mSwitchTitle,
                mSwitchBtnCount,
                mSwitchHwVersion,
                mSwitchFwVertion));

        getSwitchService().getSwitchApi().add(mSwitchId, mSwitchTitle, mSwitchBtnCount)
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultDTO -> {
                            LogUtil.d(TAG, "onNext::" + resultDTO.getResult() + "\nERROR? " + resultDTO.getError());
                            mView.onRegisterSuccess();
                        },
                        (e)-> {
                            LogUtil.e(TAG, e.getMessage(), e);
                        },
                        () -> {
                            LogUtil.d(TAG, "onCompleted::");
                            mView.onRegisterSuccess();
                        });
    }

    private void endConnect10K(){
        if(m10KResult != null && mNetworkId != -1) {
            getSocketClient().disconnect();
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

    private SimpleSocketClient getSocketClient() {
        if(mSocketClient == null) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mSocketClient = SimpleSocketClient.getInstance(Constraint.BS_SERVER_IP, Constraint.SERVER_PORT, this);

                ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkRequest.Builder networkRequestBuilder = new NetworkRequest.Builder();
                networkRequestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
                connectivityManager.requestNetwork(networkRequestBuilder.build(), new ConnectivityManager.NetworkCallback(){
                    @Override
                    public void onAvailable(Network network) {
                        try {
                            connectivityManager.unregisterNetworkCallback(this);
                            mSocketClient.setNetwork(network);
                            mSocketClient.connect();
                            mSocketClient.start();
                        }
                        catch (IllegalThreadStateException e){
                            LogUtil.e(TAG, e.getMessage(), e);
                            endConnect10K();
                        }
                    }
                });
            }
            else {
                mSocketClient = SimpleSocketClient.getInstance(Constraint.BS_SERVER_IP, Constraint.SERVER_PORT, this);
            }
        }
        return mSocketClient;
    }

    private void startSetting10K(){
        LogUtil.d(TAG, "startSetting10K");
        getSocketClient();
    }

    private void sendReqGetId(){
        LogUtil.d(TAG, "sendReqGetId");
        ConnectSocketData data = new ConnectSocketData(SocketProtocols.REQ_BLUESW_DEVICE_ID, new byte[]{'0'});
        getSocketClient().sendPacket(data.makePacket());
    }

    private void sendReqSetSSID() {
        LogUtil.d(TAG, "sendReqSetSSID::" + mSelectedWifi.SSID);
        ConnectSocketData data = new ConnectSocketData(SocketProtocols.REQ_WIFI_SSID, mSelectedWifi.SSID);
        getSocketClient().sendPacket(data.makePacket());
    }

    private void sendReqSetBSSDID() {
        LogUtil.d(TAG, "sendReqSetBSSDID::" + mSelectedWifi.BSSID);
        ConnectSocketData data = new ConnectSocketData(SocketProtocols.REQ_WIFI_BSSID, mSelectedWifi.BSSID);
        getSocketClient().sendPacket(data.makePacket());
    }

    private void sendReqSetWifiPassword() {
        LogUtil.d(TAG, "sendReqSetWifiPassword::" + mSelectedWifiPassword);
        ConnectSocketData data = new ConnectSocketData(SocketProtocols.REQ_WIFI_PASS, mSelectedWifiPassword);
        getSocketClient().sendPacket(data.makePacket());
    }

    private void sendReqSetMqttServerIp() {
        LogUtil.d(TAG, "sendReqSetMqttServerIp::" + Constraint.MQTT_BROKER_IP);
        ConnectSocketData data = new ConnectSocketData(SocketProtocols.REQ_MQTT_BROKER_IP, Constraint.MQTT_BROKER_IP);
        getSocketClient().sendPacket(data.makePacket());
    }

    private void sendReqGetBtnCount() {
        LogUtil.d(TAG, "sendReqGetBtnCount");
        ConnectSocketData data = new ConnectSocketData(SocketProtocols.REQ_SWTICH_COUNT, new byte[]{'0'});
        getSocketClient().sendPacket(data.makePacket());
    }

    private void sendReqGetHwVersion() {
        LogUtil.d(TAG, "sendReqGetHwVersion");
        ConnectSocketData data = new ConnectSocketData(SocketProtocols.REQ_HW_VERSION, new byte[]{'0'});
        getSocketClient().sendPacket(data.makePacket());
    }

    private void sendReqGetFwVertion() {
        LogUtil.d(TAG, "sendReqGetFwVertion");
        ConnectSocketData data = new ConnectSocketData(SocketProtocols.REQ_FW_VERSION, new byte[]{'0'});
        getSocketClient().sendPacket(data.makePacket());
    }

    private void sendReqEndConnection() {
        LogUtil.d(TAG, "sendReqEndConnection");
        ConnectSocketData data = new ConnectSocketData(SocketProtocols.REQ_END, new byte[]{'0'});
        getSocketClient().sendPacket(data.makePacket());
    }

    private void onResId(ConnectSocketData data) {
        LogUtil.d(TAG, "onResId::" + data.toString());
        mSwitchId = data.getData();
        if(!mSwitchId.isEmpty()) {
            sendReqSetSSID();
        } else {
            LogUtil.d(TAG, "onResId::ERROR:" + data.toString());
            connectionError();
        }
    }

    private void onResSetSSID(ConnectSocketData data) {
        LogUtil.d(TAG, "onResSetSSID::" + data.toString());
        if(data.getData().length() > 0 && data.getData().equals(Integer.toString(SocketProtocols.DATA_SUCCESS))) {
            sendReqSetBSSDID();
        } else {
            LogUtil.d(TAG, "onResSetSSID::ERROR:" + data.toString());
            connectionError();
        }
    }

    private void onResSetBSSID(ConnectSocketData data) {
        LogUtil.d(TAG, "onResSetBSSID::" + data.toString());
        if(data.getData().length() > 0 && data.getData().equals(Integer.toString(SocketProtocols.DATA_SUCCESS))) {
            sendReqSetWifiPassword();
        } else {
            LogUtil.d(TAG, "onResSetBSSID::ERROR:" + data.toString());
            connectionError();
        }
    }

    private void onResSetWifiPassword(ConnectSocketData data) {
        LogUtil.d(TAG, "onResSetWifiPassword::" + data.toString());
        if(data.getData().length() > 0 && data.getData().equals(Integer.toString(SocketProtocols.DATA_SUCCESS))) {
            sendReqSetMqttServerIp();
        } else {
            LogUtil.d(TAG, "onResSetWifiPassword::ERROR:" + data.toString());
            connectionError();
        }
    }

    private void onResSetMqttServerIp(ConnectSocketData data) {
        LogUtil.d(TAG, "onResSetMqttServerIp::" + data.toString());
        if(data.getData().length() > 0 && data.getData().equals(Integer.toString(SocketProtocols.DATA_SUCCESS))) {
            sendReqGetBtnCount();
        } else {
            LogUtil.d(TAG, "onResSetMqttServerIp::ERROR:" + data.toString());
            connectionError();
        }
    }

    private void onResGetBtnCount(ConnectSocketData data) {
        LogUtil.d(TAG, "onResGetBtnCount::" + data.toString());
        mSwitchBtnCount = Integer.parseInt(data.getData());
        if(m10KResult.SSID.equals(Constraint.AP_NAME1)){
            mSwitchBtnCount = 1;
        }
        if(mSwitchBtnCount <= 3 || mSwitchBtnCount > 0) {
            sendReqGetHwVersion();
        } else {
            LogUtil.d(TAG, "onResGetBtnCount::ERROR:" + data.toString());
            connectionError();
        }
    }

    private void onResGetHwVersion(ConnectSocketData data) {
        LogUtil.d(TAG, "onResGetHwVersion::" + data.toString());
        mSwitchHwVersion = data.getData();
        if(!mSwitchHwVersion.isEmpty()) {
            sendReqGetFwVertion();
        } else {
            LogUtil.d(TAG, "onResGetHwVersion::ERROR:" + data.toString());
            connectionError();
        }
    }

    private void onResGetFwVertion(ConnectSocketData data) {
        LogUtil.d(TAG, "onResGetFwVertion::" + data.toString());
        mSwitchFwVertion = data.getData();
        if(!mSwitchFwVertion.isEmpty()) {
            sendReqEndConnection();
        } else {
            LogUtil.d(TAG, "onResGetFwVertion::ERROR:" + data.toString());
            connectionError();
        }
    }

    private void onResEndConnection(ConnectSocketData data) {
        LogUtil.d(TAG, "onResEndConnection::" + data.toString());
        if(data.getData().length() > 0 && data.getData().equals(Integer.toString(SocketProtocols.DATA_SUCCESS))) {
            endConnect10K();
            isConnectionSuccessed = true;
            mView.onConnectSuccess();
        } else {
            LogUtil.d(TAG, "onResEndConnection::ERROR:" + data.toString());
            connectionError();
            connectionError();
        }

    }

    private void connectionError(){
        mView.onConnectError();
        endConnect10K();
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

    @Override
    public void onConnected() {
        LogUtil.d(TAG, "SOCKET::onConnected");
        sendReqGetId();
    }

    @Override
    public void onDisconnected() {
        LogUtil.d(TAG, "SOCKET::onDisconnected");
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e(TAG, "SOCKET::onError", e);
        connectionError();
    }

    @Override
    public void onReceivePacket(Character[] packet) {
        LogUtil.d(TAG, "onReceivePacket::" + packet);
        ConnectSocketData data = ConnectSocketData.parsePacket(packet);
        switch (data.getCmd()) {
            case SocketProtocols.RES_BLUESW_DEVICE_ID:
                onResId(data);
                break;
            case SocketProtocols.RES_WIFI_SSID:
                onResSetSSID(data);
                break;
            case SocketProtocols.RES_WIFI_BSSID:
                onResSetBSSID(data);
                break;
            case SocketProtocols.RES_WIFI_PASS:
                onResSetWifiPassword(data);
                break;
            case SocketProtocols.RES_MQTT_BROKER_IP:
                onResSetMqttServerIp(data);
                break;
            case SocketProtocols.RES_SWTICH_COUNT:
                onResGetBtnCount(data);
                break;
            case SocketProtocols.RES_HW_VERSION:
                onResGetHwVersion(data);
                break;
            case SocketProtocols.RES_FW_VERSION:
                onResGetFwVertion(data);
                break;
            case SocketProtocols.RES_END:
                onResEndConnection(data);
                break;
        }
    }

    public SwitchService getSwitchService() {
        if (mSwitchService == null)
            mSwitchService = new SwitchService(mContext);

        return mSwitchService;
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
            if(!isSwitchFinded && !isConnectionSuccessed) {
                mView.noSwitchFound();
            }
            mView.onScanEnd();
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
                        if (!getSocketClient().isConnected()) {
                            LogUtil.d(TAG, "networkChangeReceiver :: 10K");
                            startSetting10K();
                        }
                    }
                }
            }

        }
    };
}
