package com.goqual.a10k.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import kotlin.NotImplementedError;

/**
 * Created by hanwool on 2017. 2. 27..
 */

public class Nfc implements Parcelable{

    @Index
    private int _nfcid;
    private int _bsid;
    private Boolean btn1;
    private Boolean btn2;
    private Boolean btn3;
    private String title;
    private String tag;
    private String macaddr;
    private int state;
    private int btnCount;
    private boolean mIsDeletable;

    public Nfc() {
    }

    public Nfc(int _nfcid, int _bsid, Boolean btn1, Boolean btn2, Boolean btn3, String title, String tag, String macaddr, int state, int btnCount, boolean mIsDeletable) {
        this._nfcid = _nfcid;
        this._bsid = _bsid;
        this.btn1 = btn1;
        this.btn2 = btn2;
        this.btn3 = btn3;
        this.title = title;
        this.tag = tag;
        this.macaddr = macaddr;
        this.state = state;
        this.btnCount = btnCount;
        this.mIsDeletable = mIsDeletable;
    }

    public int get_nfcid() {
        return _nfcid;
    }

    public void set_nfcid(int _nfcid) {
        this._nfcid = _nfcid;
    }

    public int get_bsid() {
        return _bsid;
    }

    public void set_bsid(int _bsid) {
        this._bsid = _bsid;
    }

    /**
     * 설정될 동작의 반대값(켜지면 false == 현재 스테이트)
     * @return 스위치 동작
     */
    public Boolean getBtn1() {
        return btn1;
    }

    /**
     * 하고자 하는 것의 반대값(켜고싶으면 false == 현재 스테이트)
     * @param btn1 스위치 동작
     */
    public void setBtn1(Boolean btn1) {
        this.btn1 = btn1;
    }

    /**
     * 설정될 동작의 반대값(켜지면 false == 현재 스테이트)
     * @return 스위치 동작
     */
    public Boolean getBtn2() {
        return btn2;
    }

    /**
     * 하고자 하는 것의 반대값(켜고싶으면 false == 현재 스테이트)
     * @param btn2 스위치 동작
     */
    public void setBtn2(Boolean btn2) {
        this.btn2 = btn2;
    }

    /**
     * 설정될 동작의 반대값(켜지면 false == 현재 스테이트)
     * @return 스위치 동작
     */
    public Boolean getBtn3() {
        return btn3;
    }

    /**
     * 하고자 하는 것의 반대값(켜고싶으면 false == 현재 스테이트)
     * @param btn3 스위치 동작
     */
    public void setBtn3(Boolean btn3) {
        this.btn3 = btn3;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMacaddr() {
        return macaddr;
    }

    public void setMacaddr(String macaddr) {
        this.macaddr = macaddr;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getBtnCount() {
        return btnCount;
    }

    public void setBtnCount(int btnCount) {
        this.btnCount = btnCount;
    }

    public boolean ismIsDeletable() {
        return mIsDeletable;
    }

    public void setmIsDeletable(boolean mIsDeletable) {
        this.mIsDeletable = mIsDeletable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        throw new NotImplementedError("STUB!");
    }

    public static Parcelable.Creator<Nfc> CREATOR = new Parcelable.Creator<Nfc>(){
        @Override
        public Nfc createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public Nfc[] newArray(int size) {
            return new Nfc[size];
        }
    };
}
