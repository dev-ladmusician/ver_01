package com.goqual.a10k.model.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by ladmusician on 2016. 12. 26..
 */

public class User implements Cloneable{
    private int _connectionid;
    private int _userid;
    private boolean isadmin;
    private String num;
    private String name;
    private String mAuthKey;
    private String mPubKey;
    private String photoUriString;
    public boolean mIsDeletable;
    public boolean mChecked;

    @Override
    public User clone() throws CloneNotSupportedException {
        User item = (User)super.clone();
        item.set_connectionid(this.get_connectionid());
        item.set_userid(this.get_userid());
        item.setIsadmin(this.isadmin());
        item.setNum(this.getNum());
        item.setName(this.getName());
        item.setmAuthKey(this.getmAuthKey());
        item.setmPubKey(this.getmPubKey());
        item.setmIsDeletable(this.ismIsDeletable());
        item.setmChecked(this.ismChecked());
        return item;
    }

    public User(String name, String num, String photoString) {
        this.name = name;
        this.num = num;
        this.photoUriString = photoString;
    }

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

    public int get_userid() {
        return _userid;
    }

    public void set_userid(int _userid) {
        this._userid = _userid;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
