package com.goqual.a10k.util.interfaces;

/**
 * Created by hanwool on 2017. 2. 23..
 */

public interface IRawSocketCommunicationListener {
    void onConnected();
    void onDisconnected();
    void onError(Throwable e);
    void onReceivePacket(byte[] packet);
}
