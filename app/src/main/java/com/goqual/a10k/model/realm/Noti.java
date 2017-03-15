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
    private int date;
    private String type;
    private String message;
    private String return_uri;

    public Noti() {
    }

    public Noti(int _notiid, int _typeid, int date, String type, String message, String return_uri) {
        this._notiid = _notiid;
        this._typeid = _typeid;
        this.date = date;
        this.type = type;
        this.message = message;
        this.return_uri = return_uri;
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
}
