package com.goqual.a10k.util.event;

import com.goqual.a10k.view.interfaces.IToolbarClickListener;

/**
 * Created by hanwool on 2017. 2. 22..
 */

public class EventToolbarClick {

    private IToolbarClickListener.STATUS status;

    public IToolbarClickListener.STATUS getStatus() {
        return status;
    }

    /**
     * 현재 스테이터스를 던져야 함!!!
     * @param status 현재 스테이터스
     */
    public EventToolbarClick(IToolbarClickListener.STATUS status) {
        this.status = status;
    }
}
