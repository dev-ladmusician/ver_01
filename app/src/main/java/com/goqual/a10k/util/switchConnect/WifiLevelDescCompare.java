package com.goqual.a10k.util.switchConnect;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.Comparator;

/**
 * Created by ladmusician on 2016. 12. 30..
 */

public class WifiLevelDescCompare implements Comparator<ScanResult> {
    @Override
    public int compare(ScanResult arg0, ScanResult arg1) {
        return getWifiPower(arg0) > getWifiPower(arg1) ?
                -1 : getWifiPower(arg0) < getWifiPower(arg1) ?
                1:0;
    }

    public int getWifiPower(ScanResult item) {
        return WifiManager.calculateSignalLevel(item.level, 101);
    }
}
