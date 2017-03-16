package com.goqual.a10k.model.entity;

import com.goqual.a10k.model.realm.Nfc;
import com.goqual.a10k.model.realm.Noti;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by hanwool on 2017. 3. 8..
 */

public class NotiWrap implements BaseRealmWraper<Noti>{
    public static final int APP_UPDATE = 1;
    public static final int ABSENCE_MODE = 2;
    public static final int DELETE_CONNECTION = 3;
    public static final int CHANGE_ADMIN = 4;
    public static final int INVITE = 5;
    public static final int NEW_CONNECTION = 6;

    private int _notiid;
    private int _typeid;
    private int date;
    private String type;
    private String message;
    private String return_uri;

    public NotiWrap() {
    }

    public NotiWrap(Noti noti) {
        this._notiid = noti.get_notiid();
        this._typeid = noti.get_typeid();
        this.date = noti.getDate();
        this.type = noti.getType();
        this.message = noti.getMessage();
        this.return_uri = noti.getReturn_uri();
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public Noti getRealmObject() {
        return new Noti(_notiid, _typeid, date, type, message, return_uri);
    }
}
