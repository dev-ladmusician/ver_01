package com.goqual.a10k.model.entity;


import android.content.Context;

import com.goqual.a10k.R;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.parceler.Parcel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

@Parcel
public class Alarm {
    public int _alarmid;
    public String ringtone;
    public String ringtone_title;
    public int hour;
    public int min;
    public boolean sun;
    public boolean mon;
    public boolean tue;
    public boolean wed;
    public boolean thur;
    public boolean fri;
    public boolean sat;
    public int _bsid;
    public Boolean btn1;
    public Boolean btn2;
    public Boolean btn3;
    public String title;
    public boolean state;
    public int btncount; // count of btn in switch
    public boolean mIsDeletable;

    public int get_alarmid() {
        return _alarmid;
    }

    public void set_alarmid(int _alarmid) {
        this._alarmid = _alarmid;
    }

    public String getRingtone() {
        return ringtone;
    }

    public void setRingtone(String ringtone) {
        this.ringtone = ringtone;
    }

    public String getRingtone_title() {
        return ringtone_title;
    }

    public void setRingtone_title(String ringtone_title) {
        this.ringtone_title = ringtone_title;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public boolean isSun() {
        return sun;
    }

    public void setSun(boolean sun) {
        this.sun = sun;
    }

    public boolean isMon() {
        return mon;
    }

    public void setMon(boolean mon) {
        this.mon = mon;
    }

    public boolean isTue() {
        return tue;
    }

    public void setTue(boolean tue) {
        this.tue = tue;
    }

    public boolean isWed() {
        return wed;
    }

    public void setWed(boolean wed) {
        this.wed = wed;
    }

    public boolean isThur() {
        return thur;
    }

    public void setThur(boolean thur) {
        this.thur = thur;
    }

    public boolean isFri() {
        return fri;
    }

    public void setFri(boolean fri) {
        this.fri = fri;
    }

    public boolean isSat() {
        return sat;
    }

    public void setSat(boolean sat) {
        this.sat = sat;
    }

    public int get_bsid() {
        return _bsid;
    }

    public void set_bsid(int _bsid) {
        this._bsid = _bsid;
    }

    public Boolean getBtn1() {
        return btn1;
    }

    public void setBtn1(Boolean btn1) {
        this.btn1 = btn1;
    }

    public Boolean getBtn2() {
        return btn2;
    }

    public void setBtn2(Boolean btn2) {
        this.btn2 = btn2;
    }

    public Boolean getBtn3() {
        return btn3;
    }

    public void setBtn3(Boolean btn3) {
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

    public void setRepeats(Repeat repeats) {
        setSun(repeats.isSun());
        setMon(repeats.isMon());
        setTue(repeats.isTue());
        setWed(repeats.isWed());
        setThur(repeats.isThu());
        setFri(repeats.isFri());
        setSat(repeats.isSat());
    }

    public void setSwitch(Switch item) {
        set_bsid(item.get_bsid());
        setBtn1(item.getBtn1());
        setBtn2(item.getBtn2());
        setBtn3(item.getBtn3());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public CharSequence makeTimeString() {
        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm a", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, min);
        stringBuilder.append(simpleDateFormat.format(calendar.getTime()));
        return stringBuilder;
    }

    public CharSequence makeDayString(Context ctx) {
        StringBuilder stringBuilder = new StringBuilder();
        if(sun && mon && tue && wed && thur && fri && sat) {
            return ctx.getString(R.string.alarm_repeat_everyday);
        }
        if(!sun && !mon && !tue && !wed && !thur && !fri && !sat) {
            return ctx.getString(R.string.alarm_repeat_never);
        }
        if(sun) {
            stringBuilder.append(ctx.getString(R.string.alarm_repeat_sun));
            stringBuilder.append(" ");
        }
        if(mon) {
            stringBuilder.append(ctx.getString(R.string.alarm_repeat_mon));
            stringBuilder.append(" ");
        }
        if(tue) {
            stringBuilder.append(ctx.getString(R.string.alarm_repeat_tue));
            stringBuilder.append(" ");
        }
        if(wed) {
            stringBuilder.append(ctx.getString(R.string.alarm_repeat_wed));
            stringBuilder.append(" ");
        }
        if(thur) {
            stringBuilder.append(ctx.getString(R.string.alarm_repeat_thur));
            stringBuilder.append(" ");
        }
        if(fri) {
            stringBuilder.append(ctx.getString(R.string.alarm_repeat_fri));
            stringBuilder.append(" ");
        }
        if(sat) {
            stringBuilder.append(ctx.getString(R.string.alarm_repeat_sat));
            stringBuilder.append(" ");
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(" "));
        return stringBuilder;
    }
}
