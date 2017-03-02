package com.goqual.a10k.model.entity;

import java.util.Locale;

/**
 * Created by ladmusician on 2016. 12. 28..
 */

public class History {
    private int _logid;
    private boolean operation;
    private int btn;
    private String created;
    private String label;

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

    public String getCreated() {
        return created;
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
