package com.goqual.a10k.model.realm;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

public class Noti extends RealmObject{
    @PrimaryKey
    private int _notiid;
    private int _typeid;
    private int _bsid;
    private int date;
    private String message;
    private String return_uri;
    private String type;
    private boolean btn1;
    private boolean btn2;
    private boolean btn3;

    public Noti() {
    }

    public Noti(int _notiid, int _typeid, int _bsid, int date, String message, String return_uri, String type, boolean btn1, boolean btn2, boolean btn3) {
        this._notiid = _notiid;
        this._typeid = _typeid;
        this._bsid = _bsid;
        this.date = date;
        this.message = message;
        this.return_uri = return_uri;
        this.type = type;
        this.btn1 = btn1;
        this.btn2 = btn2;
        this.btn3 = btn3;
    }

    public int get_notiid() {
        return _notiid;
    }

    public void set_notiid(int _notiid) {
        this._notiid = _notiid;
    }

    public int get_typeid() {
        return _typeid;
    }

    public void set_typeid(int _typeid) {
        this._typeid = _typeid;
    }

    public int get_bsid() {
        return _bsid;
    }

    public void set_bsid(int _bsid) {
        this._bsid = _bsid;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReturn_uri() {
        return return_uri;
    }

    public void setReturn_uri(String return_uri) {
        this.return_uri = return_uri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isBtn1() {
        return btn1;
    }

    public void setBtn1(boolean btn1) {
        this.btn1 = btn1;
    }

    public boolean isBtn2() {
        return btn2;
    }

    public void setBtn2(boolean btn2) {
        this.btn2 = btn2;
    }

    public boolean isBtn3() {
        return btn3;
    }

    public void setBtn3(boolean btn3) {
        this.btn3 = btn3;
    }
}
