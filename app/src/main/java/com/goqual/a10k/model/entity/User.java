package com.goqual.a10k.model.entity;

/**
 * Created by ladmusician on 2016. 12. 26..
 */

public class User {
    private int _connectionid;
    private boolean isadmin;
    private String num;
    private String name;
    private String mAuthKey;
    private String mPubKey;
    private boolean mIsDeletable;
    private boolean mChecked;

    public String getmAuthKey() {
        return mAuthKey;
    }

    public void setmAuthKey(String mAuthKey) {
        this.mAuthKey = mAuthKey;
    }

    public String getmPubKey() {
        return mPubKey;
    }

    public void setmPubKey(String mPubKey) {
        this.mPubKey = mPubKey;
    }

    public int get_connectionid() {
        return _connectionid;
    }

    public void set_connectionid(int _connectionid) {
        this._connectionid = _connectionid;
    }

    public boolean isadmin() {
        return isadmin;
    }

    public void setIsadmin(boolean isadmin) {
        this.isadmin = isadmin;
    }

    public String getNum() {
        return num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public boolean ismIsDeletable() {
        return mIsDeletable;
    }

    public void setmIsDeletable(boolean mIsDeletable) {
        this.mIsDeletable = mIsDeletable;
    }

    public boolean ismChecked() {
        return mChecked;
    }

    public void setmChecked(boolean mChecked) {
        this.mChecked = mChecked;
    }
}
