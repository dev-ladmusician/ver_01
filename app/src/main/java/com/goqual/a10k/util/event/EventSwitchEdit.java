package com.goqual.a10k.util.event;

/**
 * Created by ladmusician on 2017. 2. 22..
 */

public class EventSwitchEdit {
    public enum STATUS {
        EDIT,
        DONE,
        HIDE
    }

    private STATUS status;

    public STATUS getStatus() {
        return status;
    }

    public EventSwitchEdit(STATUS status) {
        this.status = status;
    }
}
