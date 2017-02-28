package com.goqual.a10k.model.realm;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by hanwool on 2017. 2. 27..
 */

public class Nfc extends RealmObject {

    @Index
    private String tagId;
    private String deviceType;
    private String macAddr;
    private boolean btnState1;
    private boolean btnState2;
    private boolean btnState3;


    public Nfc() {
    }

    public Nfc(String tagId, String deviceType, String deviceId, boolean btnState1, boolean btnState2, boolean btnState3) {
        this.tagId = tagId;
        this.deviceType = deviceType;
        this.macAddr = deviceId;
        this.btnState1 = btnState1;
        this.btnState2 = btnState2;
        this.btnState3 = btnState3;
    }

    @Override
    public String toString() {
        return String.format("TAG ID: %s, DEVICE TYPE: %s, DEVICE MAC: %s, BTN1: %b, BTN2: %b, BTN3: %b",
                tagId,
                deviceType,
                macAddr,
                btnState1,
                btnState2,
                btnState3);
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    /**
     * 설정될 동작의 반대값(켜지면 false == 현재 스테이트)
     * @return 스위치 동작
     */
    public boolean isBtnState1() {
        return btnState1;
    }

    /**
     * 하고자 하는 것의 반대값(켜고싶으면 false == 현재 스테이트)
     * @param btnState1 스위치 동작
     */
    public void setBtnState1(boolean btnState1) {
        this.btnState1 = btnState1;
    }

    /**
     * 설정될 동작의 반대값(켜지면 false == 현재 스테이트)
     * @return 스위치 동작
     */
    public boolean isBtnState2() {
        return btnState2;
    }

    /**
     * 하고자 하는 것의 반대값(켜고싶으면 false == 현재 스테이트)
     * @param btnState2 스위치 동작
     */
    public void setBtnState2(boolean btnState2) {
        this.btnState2 = btnState2;
    }

    /**
     * 설정될 동작의 반대값(켜지면 false == 현재 스테이트)
     * @return 스위치 동작
     */
    public boolean isBtnState3() {
        return btnState3;
    }

    /**
     * 하고자 하는 것의 반대값(켜고싶으면 false == 현재 스테이트)
     * @param btnState3 스위치 동작
     */
    public void setBtnState3(boolean btnState3) {
        this.btnState3 = btnState3;
    }
}
