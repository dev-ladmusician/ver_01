package com.goqual.a10k.util.event;

/**
 * Created by hanwool on 2017. 2. 22..
 */

public class EventToolbarClick {
    public enum STATUS {
        EDIT,
        DONE,
        ADD
    }

    private STATUS status;

    public STATUS getStatus() {
        return status;
    }

    /**
     * 현재 스테이터스를 던져야 함!!!
     * @param status 현재 스테이터스
     */
    public EventToolbarClick(STATUS status) {
        this.status = status;
    }
}
