package com.goqual.a10k.presenter;

import android.net.wifi.ScanResult;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

public interface WifiPresenter {
    void startScan();
    void stopScan();
    void stopNetworkReceiver();
    void onClick(int position);
    void connect10K();
    //void setName(String name);
    //void destroy();
    //void checkSwitchConnected();
    //void setWifiPasswd(String pass);

    interface View<T>{
        void set10KAP(ScanResult ap);

        void openPassDialog(String ssid);
        void addAP(ScanResult bs);
        void refresh();

        void noSwitchFound();
        void changeFragSetSwitch();
        void onErrorConnectAP();

        void loadingStart();
        void loadingStop();
    }
}
