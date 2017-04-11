package com.goqual.a10k.model.entity;

import com.goqual.a10k.util.LogUtil;

import org.parceler.Parcel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

@Parcel
public class Absence {
    private int _absenceid;
    private int start_hour;
    private int start_min;
    private int end_hour;
    private int end_min;
    private int _bsid;
    public boolean btn1;
    public boolean btn2;
    public boolean btn3;
    private String title;
    private boolean state;
    private int btncount; // count of btn in switch
    private boolean mIsDeletable;

    public Absence() {

        this.end_hour = 99;
        this.end_min = 99;
    }

    public Absence(Switch mSwitch) {
        this._bsid = mSwitch.get_bsid();
        this.btncount = mSwitch.getBtnCount();
        this.state = true;
    }

    public int get_absenceid() {
        return _absenceid;
    }

    public void set_absenceid(int _absenceid) {
        this._absenceid = _absenceid;
    }

    public int getStart_hour() {
        return start_hour;
    }

    public void setStart_hour(int start_hour) {
        this.start_hour = start_hour;
    }

    public int getStart_min() {
        return start_min;
    }

    public void setStart_min(int start_min) {
        this.start_min = start_min;
    }

    public int getEnd_hour() {
        return end_hour;
    }

    public void setEnd_hour(int end_hour) {
        this.end_hour = end_hour;
    }

    public int getEnd_min() {
        return end_min;
    }

    public void setEnd_min(int end_min) {
        this.end_min = end_min;
    }

    public int get_bsid() {
        return _bsid;
    }

    public void set_bsid(int _bsid) {
        this._bsid = _bsid;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getBtncount() {
        return btncount;
    }

    public void setBtncount(int btncount) {
        this.btncount = btncount;
    }

    public boolean ismIsDeletable() {
        return mIsDeletable;
    }

    public void setmIsDeletable(boolean mIsDeletable) {
        this.mIsDeletable = mIsDeletable;
    }

    public CharSequence getStartTimeString() {
        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, start_hour);
        calendar.set(Calendar.MINUTE, start_min);
        calendar.setTimeZone(TimeZone.getDefault());
        stringBuilder.append(simpleDateFormat.format(calendar.getTime()));
        return stringBuilder;
    }

    public CharSequence getEndTimeString() {
        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, end_hour);
        calendar.set(Calendar.MINUTE, end_min);
        calendar.setTimeZone(TimeZone.getDefault());
        LogUtil.d("Absence", calendar.getTime().toString());
        LogUtil.d("Absence", calendar.toString());
        stringBuilder.append(simpleDateFormat.format(calendar.getTime()));
        return stringBuilder;
    }
}
