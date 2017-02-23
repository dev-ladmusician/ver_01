package com.goqual.a10k.presenter;

import com.goqual.a10k.model.entity.Switch;

/**
 * Created by HanWool on 2017. 2. 21..
 */

public interface SocketManager {
    void operationOnOff(Switch item, int btnNumber);
    void refreshConnectedRoom();
    void destroySocketConnection();
    boolean isConnected();

    interface View {
        void onConnectionError();
        void onConnected();
    }
}
