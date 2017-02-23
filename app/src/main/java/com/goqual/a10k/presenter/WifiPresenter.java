package com.goqual.a10k.presenter;

import android.net.wifi.ScanResult;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

public interface WifiPresenter {
    void startScan();
    void onClick(int position);
    void connectToWifi(int position, String pass);
    void connect10K();

    interface View<T>{
        void onConnectError();
        void onConnectSuccess();

        void addAP(ScanResult bs);
        void noSwitchFined();
        void onScanEnd();
        void onScanStart();

        void openErrorDialog();
        void closeErrorDialog();

        void openPassDialog(int position);
        void closePassDialog();
    }
}
