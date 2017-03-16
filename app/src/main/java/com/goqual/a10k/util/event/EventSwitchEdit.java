package com.goqual.a10k.util.event;

import com.goqual.a10k.view.interfaces.IToolbarClickListener;

/**
 * Created by ladmusician on 2017. 2. 22..
 */

public class EventSwitchEdit {

    private IToolbarClickListener.STATE state;

    public IToolbarClickListener.STATE getSTATE() {
        return state;
    }

    /**
     * 바꿀 스테이터스를 던져야 함!!!
     * @param state 바꿀 스테이터스
     */
    public EventSwitchEdit(IToolbarClickListener.STATE state) {
        this.state = state;
    }
}
