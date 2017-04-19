package com.goqual.a10k.model.entity;


import android.content.Context;
import android.support.annotation.Nullable;

import com.goqual.a10k.R;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.parceler.Parcel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

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
    public @Nullable Boolean btn1;
    public @Nullable Boolean btn2;
    public @Nullable Boolean btn3;
    public String title;
    public boolean state;
    public int btncount; // count of btn in switch
    public String repeatStr;
    public boolean mIsDeletable;
    public boolean mIsSetSwitch;

    public Alarm() {
    }

    public Alarm(String repeatStr, String ringtone, String ringtone_title) {
        this.ringtone = ringtone;
        this.ringtone_title = ringtone_title;
        this.repeatStr = repeatStr;
    }

    public Alarm(Boolean btn1, Boolean btn2, Boolean btn3, int btnCount, int bsid, String title) {
        this.btn1 = btn1;
        this.btn2 = btn2;
        this.btn3 = btn3;
        this.btncount = btnCount;
        this._bsid = bsid;
        this.title = title;
    }

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

    public @Nullable Boolean getBtn1() {
        return btn1;
    }

    public void setBtn1(@Nullable Boolean btn1) {
        this.btn1 = btn1;
    }

    public @Nullable Boolean getBtn2() {
        return btn2;
    }

    public void setBtn2(@Nullable Boolean btn2) {
        this.btn2 = btn2;
    }

    public @Nullable Boolean getBtn3() {
        return btn3;
    }

    public void setBtn3(@Nullable Boolean btn3) {
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

    public String getRepeatStr() {
        return repeatStr;
    }

    public void setRepeatStr(String repeatStr) {
        this.repeatStr = repeatStr;
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

    public boolean ismIsSetSwitch() {
        return mIsSetSwitch;
    }

    public void setmIsSetSwitch(boolean mIsSetSwitch) {
        this.mIsSetSwitch = mIsSetSwitch;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public CharSequence makeTimeString() {
        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.setTimeZone(TimeZone.getDefault());
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
