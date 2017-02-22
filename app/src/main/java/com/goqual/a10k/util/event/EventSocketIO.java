package com.goqual.a10k.util.event;

/**
 * Created by hanwool on 2017. 2. 22..
 */

public class EventSocketIO {
    public enum CONNECTION_STATE {
        CONNECTED,
        FAILED,
        ERROR
    }

    private CONNECTION_STATE mState;

    public EventSocketIO(CONNECTION_STATE state) {
        mState = state;
    }

    public CONNECTION_STATE getState() {
        return mState;
    }
}
