package com.goqual.a10k.util;

import android.content.Context;

import com.google.gson.Gson;
import com.goqual.a10k.R;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.SocketData;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.util.interfaces.ISocketIoConnectionListener;

import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by hanwool on 2017. 2. 22..
 */

public class SocketIoManager{
    public static final String TAG = SocketIoManager.class.getSimpleName();

    private static final int RETRY_AFTER_TWO_SECONDS = 2000;

    private Context mContext;
    private ISocketIoConnectionListener mListener;

    private Socket mSocket;
    private boolean isConnected;

    private static SocketIoManager instance;

    public static SocketIoManager getInstance(Context ctx, ISocketIoConnectionListener listener) {
        if(instance == null) {
            instance = new SocketIoManager(ctx, listener);
        }
        return instance;
    }

    private SocketIoManager(Context ctx, ISocketIoConnectionListener listener) {
        mContext = ctx;
        mListener = listener;
        createSocket();
    }

    private void createSocket() {
        LogUtil.e(TAG, "createSocket");
        try {
            if(mSocket == null) {
                mSocket = IO.socket(Constraint.SOCKET_SERVER_IP);
            }
            if(!isConnected) {
                registerSocketCallback();
                connect();
            }
        } catch (URISyntaxException e) {
            LogUtil.e(TAG, e.getMessage(), e);
        }
    }

    public void connect() {
        mSocket.connect();
    }

    public void disconnect() {
        mSocket.disconnect();
    }

    public void destroy() {
        disconnect();
        unregisterSocketCallback();
        mSocket = null;
    }

    private void registerSocketCallback() {
        mSocket.on(Socket.EVENT_CONNECT, onConnectedListener);
        mSocket.on(Socket.EVENT_RECONNECT, onReconnectingListener);
        mSocket.on(Socket.EVENT_RECONNECT_FAILED, onReconnectFailListener);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnectedListener);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectionTimeoutListener);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectErrorListener);
    }
    
    private void unregisterSocketCallback() {
        mSocket.off(Socket.EVENT_CONNECT, onConnectedListener);
        mSocket.off(Socket.EVENT_RECONNECT, onReconnectingListener);
        mSocket.off(Socket.EVENT_RECONNECT_FAILED, onReconnectFailListener);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnectedListener);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectionTimeoutListener);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectErrorListener);
    }

    private Emitter.Listener onConnectedListener = args ->{
        LogUtil.e(TAG, "SOCKET EVENT_CONNECT");
        mListener.onConnected();
        isConnected = true;
    };

    private Emitter.Listener onReconnectingListener = args -> {
        LogUtil.e(TAG, "SOCKET EVENT_RECONNECT");
        isConnected = false;
        mListener.onReconnect();
    };

    private Emitter.Listener onReconnectFailListener = args -> {
        LogUtil.e(TAG, "SOCKET EVENT_RECONNECT_FAILED");
        isConnected = false;
        mListener.onReconnectionFailed();
        disconnect();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(!isConnected) {
                    mSocket.connect();
                }
            }
        }, RETRY_AFTER_TWO_SECONDS);
    };

    private Emitter.Listener onDisconnectedListener = args -> {
        LogUtil.e(TAG, "SOCKET EVENT_DISCONNECT");
        isConnected = false;
        mListener.onDisconnect();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(!isConnected) {
                    mSocket.connect();
                }
            }
        }, RETRY_AFTER_TWO_SECONDS);
    };

    private Emitter.Listener onConnectionTimeoutListener = args -> {
        isConnected = false;
        mListener.onConnectionTimeOut();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(!isConnected) {
                    mSocket.connect();
                }
            }
        }, RETRY_AFTER_TWO_SECONDS);
    };

    private Emitter.Listener onConnectErrorListener = args -> {
        LogUtil.e(TAG, "SOCKET EVENT_CONNECT_ERROR");
        isConnected = false;
        mListener.onError();
        disconnect();
    };

    public void emit(String event, Object... msg) {
        if(isConnected) {
            mSocket.emit(event, msg);
        }
    }

    public void emit(String event, Object[] args, Ack ack) {
        if(isConnected) {
            mSocket.emit(event, args, ack);
        }
    }

    public void send(Object... args) {
        if(isConnected) {
            mSocket.send(args);
        }
    }

    /**
     * 스위치 서버단에 그룹핑 시켜주기
     */
    public void joinSwitchRoom() {
        if(isConnected) {
            if (SwitchManager.getInstance().getCount() > 0)
                for (Switch item : SwitchManager.getInstance().getList()) {
                    leaveSwitchRoom(item);
                    Gson gson = new Gson();
                    SocketData data = new SocketData(item.getMacaddr(),
                            PreferenceHelper.getInstance(mContext).getStringValue(
                                    mContext.getString(R.string.arg_user_token), ""
                            ));
                    mSocket.emit(SocketProtocols.JOIN_REQ, gson.toJson(data));
                }
        }
        else {
            createSocket();
        }
    }

    /**
     * 스위치 룸 나가기
     * @param item 나갈 스위치
     */
    public void leaveSwitchRoom(Switch item) {
        if(isConnected) {
            Gson gson = new Gson();
            SocketData data = new SocketData(item.getMacaddr(),
                    PreferenceHelper.getInstance(mContext).getStringValue(
                            mContext.getString(R.string.arg_user_token), ""
                    ));

            mSocket.emit(SocketProtocols.LEAVE_REQ, gson.toJson(data));
        }
    }

    /**
     * 모든 스위치 룸 나가기
     */
    public void leaveAllSwitchRooms() {
        if(isConnected) {
            for (Switch item : SwitchManager.getInstance().getList()) {
                leaveSwitchRoom(item);
            }
        }
    }
}
