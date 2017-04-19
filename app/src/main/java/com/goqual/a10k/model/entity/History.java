package com.goqual.a10k.model.entity;

import com.goqual.a10k.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by ladmusician on 2016. 12. 28..
 */

public class History {
    public int _logid;
    public boolean operation;
    public int btn;
    public int btncount;
    public String num;
    public String created;
    public String label;

    public int get_logid() {
        return _logid;
    }

    public void set_logid(int _logid) {
        this._logid = _logid;
    }

    public boolean isOperation() {
        return operation;
    }

    public void setOperation(boolean operation) {
        this.operation = operation;
    }

    public int getBtn() {
        return btn;
    }

    public void setBtn(int btn) {
        this.btn = btn;
    }

    public CharSequence getCreated() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            SimpleDateFormat tranSimpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tranSimpleFormat.setTimeZone(TimeZone.getTimeZone("Greenwich"));
            Date data = tranSimpleFormat.parse(created.replace("Z", "").replace("T", " "));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
            stringBuilder.append(simpleDateFormat.format(data.getTime()));
            return stringBuilder;
        }
        catch (ParseException e) {
            LogUtil.e("History", e.getMessage(), e);
            return "";
        }
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getBtncount() {
        return btncount;
    }

    public void setBtncount(int btncount) {
        this.btncount = btncount;
    }

    @Override
    public String toString() {
        return String.format(Locale.KOREA, "<HISTORY> _logid: %d, operation: %b, btn: %d, created: %s, label: %s",
                _logid,
                operation,
                btn,
                created,
                label);
    }
}
