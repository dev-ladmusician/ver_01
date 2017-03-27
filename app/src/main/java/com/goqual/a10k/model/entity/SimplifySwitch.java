package com.goqual.a10k.model.entity;

/**
 * Created by hanwool on 2017. 3. 22..
 */

public class SimplifySwitch {
    private int _bsid;
    private String macaddr;
    private int state;
    private boolean isavailable;

    public SimplifySwitch() {
    }

    public SimplifySwitch(int _bsid, String macaddr, int state, boolean isavailable) {
        this._bsid = _bsid;
        this.macaddr = macaddr;
        this.state = state;
        this.isavailable = isavailable;
    }

    public int get_bsid() {
        return _bsid;
    }

    public void set_bsid(int _bsid) {
        this._bsid = _bsid;
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

    public boolean isavailable() {
        return isavailable;
    }

    public void setIsavailable(boolean isavailable) {
        this.isavailable = isavailable;
    }
}
