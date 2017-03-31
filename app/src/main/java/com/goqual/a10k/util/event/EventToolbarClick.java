package com.goqual.a10k.util.event;

import com.goqual.a10k.view.interfaces.IToolbarClickListener;

/**
 * Created by hanwool on 2017. 2. 22..
 */

public class EventToolbarClick {

    private IToolbarClickListener.STATE state;

    public IToolbarClickListener.STATE getState() {
        return state;
    }

    public void setState(IToolbarClickListener.STATE state) {
        this.state = state;
    }

    /**
     * 현재 스테이터스를 던져야 함!!!
     * @param state 현재 스테이터스
     */
    public EventToolbarClick(IToolbarClickListener.STATE state) {
        this.state = state;
    }
}
