package com.goqual.a10k.model.entity;

import java.util.Date;

/**
 * Created by hanwool on 2017. 3. 16..
 */

public class Version {
    private int _appid;
    private float version;
    private String desc;
    private Date created;
    private Date updated;
    private int isdeprecated;

    public int get_appid() {
        return _appid;
    }

    public void set_appid(int _appid) {
        this._appid = _appid;
    }

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public int getIsdeprecated() {
        return isdeprecated;
    }

    public void setIsdeprecated(int isdeprecated) {
        this.isdeprecated = isdeprecated;
    }
}
