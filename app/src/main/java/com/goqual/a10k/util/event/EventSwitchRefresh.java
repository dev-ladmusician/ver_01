package com.goqual.a10k.util.event;

import com.goqual.a10k.model.entity.Switch;

/**
 * Created by hanwool on 2017. 2. 22..
 */

public class EventSwitchRefresh {
    private Switch mSwitch;

    public EventSwitchRefresh(Switch mSwitch) {
        this.mSwitch = mSwitch;
    }

    public Switch getSwitch() {
        return mSwitch;
    }
}
