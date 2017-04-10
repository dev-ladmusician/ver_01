package com.goqual.a10k.view.interfaces;

import android.net.wifi.ScanResult;

/**
 * Created by hanwool on 2017. 2. 23..
 */

public interface IAuthInteraction {
    void setSelectedWifi(android.net.wifi.ScanResult wifi, String passwd);
    void set10KAP(android.net.wifi.ScanResult ap);

    ScanResult get10KAP();
    ScanResult getSelectedWifi();
    String getSelectedWifiPasswd();
}
