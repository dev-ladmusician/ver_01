package com.goqual.a10k.model.entity;

/**
 * Created by ladmusician on 2016. 12. 13..
 */

public class SocketData {
    private String macaddr;
    private String token;
    private String operation;
    private int code;
    private int btn;

    public SocketData() {
    }

    public SocketData(String macaddr, String token) {
        this.macaddr = macaddr;
        this.token = token;
    }

    public SocketData(String macaddr, String token, int btn, String operation, int code) {
        this.macaddr = macaddr;
        this.token = token;
        this.operation = operation;
        this.btn = btn;
        this.code = code;
    }

    public String getMacaddr() {
        return macaddr;
    }

    public void setMacaddr(String macaddr) {
        this.macaddr = macaddr;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getBtn() {
        return btn;
    }

    public void setBtn(int btn) {
        this.btn = btn;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
