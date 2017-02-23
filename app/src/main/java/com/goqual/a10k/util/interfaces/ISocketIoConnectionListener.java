package com.goqual.a10k.util.interfaces;

import com.goqual.a10k.model.entity.SocketData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hanwool on 2017. 2. 22..
 */

public interface ISocketIoConnectionListener {
    void onConnected();
    void onConnectionTimeOut();
    void onDisconnect();
    void onError();
    void onReceiveMessage(String type, SocketData args);
    void onReconnect();
    void onReconnectionFailed();
}
