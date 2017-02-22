package com.goqual.a10k.util.interfaces;

/**
 * Created by hanwool on 2017. 2. 22..
 */

public interface ISocketIoConnectionListener {
    void onConnected();
    void onConnectionTimeOut();
    void onDisconnect();
    void onError();
    void onReceiveMessage(Object... args);
    void onReconnect();
    void onReconnectionFailed();
}
