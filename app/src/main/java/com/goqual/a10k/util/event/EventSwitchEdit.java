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

    /**
     * 바꿀 스테이터스를 던져야 함!!!
     * @param status 바꿀 스테이터스
     */
    public EventSwitchEdit(STATUS status) {
        this.status = status;
    }
}
