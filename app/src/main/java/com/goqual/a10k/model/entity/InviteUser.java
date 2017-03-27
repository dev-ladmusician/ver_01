package com.goqual.a10k.model.entity;

/**
 * Created by hanwool on 2017. 3. 17..
 */

public class InviteUser {
    private int _userid;
    private String num;
    private int _connectionid;

    public InviteUser() {
    }

    public InviteUser(int _userid, String num, int _connectionid) {
        this._userid = _userid;
        this.num = num;
        this._connectionid = _connectionid;
    }

    public int get_userid() {
        return _userid;
    }

    public void set_userid(int _userid) {
        this._userid = _userid;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int get_connectionid() {
        return _connectionid;
    }

    public void set_connectionid(int _connectionid) {
        this._connectionid = _connectionid;
    }
}
