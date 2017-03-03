package com.goqual.a10k.util.event;

import com.goqual.a10k.view.interfaces.IToolbarClickListener;

/**
 * Created by ladmusician on 2017. 2. 22..
 */

public class EventSwitchEdit {

    private IToolbarClickListener.STATUS status;

    public IToolbarClickListener.STATUS getStatus() {
        return status;
    }

    /**
     * 바꿀 스테이터스를 던져야 함!!!
     * @param status 바꿀 스테이터스
     */
    public EventSwitchEdit(IToolbarClickListener.STATUS status) {
        this.status = status;
    }
}
