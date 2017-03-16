package com.goqual.a10k.model.entity;

import com.goqual.a10k.model.realm.Nfc;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by hanwool on 2017. 3. 8..
 */

public class NfcWrap implements BaseRealmWraper<Nfc>{
    public String tag;
    public int _nfcid;
    public int _bsid;
    public boolean btn1;
    public boolean btn2;
    public boolean btn3;
    public String title;
    public String macaddr;
    public int state;
    public int btnCount;
    public boolean mIsDeletable;
    public boolean isEditing;

    public NfcWrap(Nfc nfcItem) {
        tag = nfcItem.getTag();
        _nfcid = nfcItem.get_nfcid();
        _bsid = nfcItem.get_bsid();
        btn1 = nfcItem.getBtn1();
        btn2 = nfcItem.getBtn2();
        btn3 = nfcItem.getBtn3();
        title = nfcItem.getTitle();
        macaddr = nfcItem.getMacaddr();
        state = nfcItem.getState();
        btnCount = nfcItem.getBtnCount();
        mIsDeletable = nfcItem.ismIsDeletable();
        isEditing = false;
    }

    @Override
    public Nfc getRealmObject() {
        return new Nfc(_nfcid, _bsid, btn1, btn2, btn3, title, tag, macaddr, state, btnCount, mIsDeletable);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public boolean getBtn1() {
        return btn1;
    }

    public void setBtn1(boolean btn1) {
        this.btn1 = btn1;
    }

    public boolean getBtn2() {
        return btn2;
    }

    public void setBtn2(boolean btn2) {
        this.btn2 = btn2;
    }

    public boolean getBtn3() {
        return btn3;
    }

    public void setBtn3(boolean btn3) {
        this.btn3 = btn3;
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

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
