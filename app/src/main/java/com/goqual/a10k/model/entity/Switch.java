package com.goqual.a10k.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.annotations.Nullable;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public class Switch{
    private int _connectionid;
    private int _bsid;
    private int state;
    private int seq;
    @Nullable private Boolean btn1;
    @Nullable private Boolean btn2;
    @Nullable private Boolean btn3;
    private String hw;
    private String fw;
    public String title;
    private String macaddr;
    private boolean isavailable;
    private boolean outlet;
    public boolean mIsStateView;

    private String WIFI_SSID = "";
    private String WIFI_BSSID = "";
    private String WIFI_PASSWORD = "";
    private String WIFI_CAPABILITY = "";

    public Switch() {
    }

    public Switch(Parcel in) {
        _connectionid = in.readInt();
        _bsid = in.readInt();
        state = in.readInt();
        seq = in.readInt();
        btn1 = in.readByte()==1;
        btn2 = in.readByte()==1;
        btn3 = in.readByte()==1;
        hw = in.readString();
        fw = in.readString();
        title = in.readString();
        macaddr = in.readString();
        isavailable = in.readByte()==1;
        outlet = in.readByte()==1;
        mIsStateView = in.readByte()==1;
        WIFI_SSID = in.readString();
        WIFI_BSSID = in.readString();
        WIFI_PASSWORD = in.readString();
        WIFI_CAPABILITY = in.readString();
    }

    // BS
    private int AP_SERVER_PORT = 5050;

    public int get_connectionid() {
        return _connectionid;
    }

    public void set_connectionid(int _connectionid) {
        this._connectionid = _connectionid;
    }

    public int get_bsid() {
        return _bsid;
    }

    public void set_bsid(int _bsid) {
        this._bsid = _bsid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Nullable
    public Boolean getBtn1() {
        return btn1;
    }

    public void setBtn1(@Nullable Boolean btn1) {
        this.btn1 = btn1;
    }

    @Nullable
    public Boolean getBtn2() {
        return btn2;
    }

    public void setBtn2(@Nullable Boolean btn2) {
        this.btn2 = btn2;
    }

    @Nullable
    public Boolean getBtn3() {
        return btn3;
    }

    public void setBtn3(@Nullable Boolean btn3) {
        this.btn3 = btn3;
    }

    public String getHw() {
        return hw;
    }

    public void setHw(String hw) {
        this.hw = hw;
    }

    public String getFw() {
        return fw;
    }

    public void setFw(String fw) {
        this.fw = fw;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMacaddr() {
        return macaddr;
    }

    public void setMacaddr(String macaddr) {
        this.macaddr = macaddr;
    }

    public boolean isavailable() {
        return isavailable;
    }

    public void setIsavailable(boolean isavailable) {
        this.isavailable = isavailable;
    }

    public boolean isOutlet() {
        return outlet;
    }

    public void setOutlet(boolean outlet) {
        this.outlet = outlet;
    }

    public String getWIFI_SSID() {
        return WIFI_SSID;
    }

    public void setWIFI_SSID(String WIFI_SSID) {
        this.WIFI_SSID = WIFI_SSID;
    }

    public String getWIFI_BSSID() {
        return WIFI_BSSID;
    }

    public void setWIFI_BSSID(String WIFI_BSSID) {
        this.WIFI_BSSID = WIFI_BSSID;
    }

    public String getWIFI_PASSWORD() {
        return WIFI_PASSWORD;
    }

    public void setWIFI_PASSWORD(String WIFI_PASSWORD) {
        this.WIFI_PASSWORD = WIFI_PASSWORD;
    }

    public String getWIFI_CAPABILITY() {
        return WIFI_CAPABILITY;
    }

    public void setWIFI_CAPABILITY(String WIFI_CAPABILITY) {
        this.WIFI_CAPABILITY = WIFI_CAPABILITY;
    }

    public int getAP_SERVER_PORT() {
        return AP_SERVER_PORT;
    }

    public void setAP_SERVER_PORT(int AP_SERVER_PORT) {
        this.AP_SERVER_PORT = AP_SERVER_PORT;
    }

    public boolean ismIsStateView() {
        return mIsStateView;
    }

    public void setmIsStateView(boolean mIsStateView) {
        this.mIsStateView = mIsStateView;
    }
}
