package com.goqual.a10k.model.entity;

import android.content.Context;
import android.os.Parcelable;

import com.goqual.a10k.R;

import org.parceler.Parcel;

import java.lang.reflect.Field;

/**
 * Created by hanwool on 2017. 3. 14..
 */
@Parcel
public class Repeat{
    public boolean sun;
    public boolean mon;
    public boolean tue;
    public boolean wed;
    public boolean thu;
    public boolean fri;
    public boolean sat;

    public Repeat() {
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

    public boolean isThu() {
        return thu;
    }

    public void setThu(boolean thu) {
        this.thu = thu;
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

    public String makeString(Context ctx) {
        StringBuilder stringBuilder = new StringBuilder();
        if(sun && mon && tue && wed && thu && fri && sat) {
            return ctx.getString(R.string.alarm_repeat_everyday);
        }
        if(!sun && !mon && !tue && !wed && !thu && !fri && !sat) {
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
        if(thu) {
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
        return stringBuilder.toString();
    }
}
