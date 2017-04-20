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

    private static int mRefCount = 0;

    private static SocketIoManager instance;

    public static SocketIoManager getInstance(Context ctx, ISocketIoConnectionListener listener) {
        if(instance == null) {
            instance = new SocketIoManager(ctx, listener);
        }
        mRefCount++;
        LogUtil.d(TAG, "getInstance::REF:"+mRefCount);
        return instance;
    }

    private SocketIoManager(Context ctx, ISocketIoConnectionListener listener) {
        LogUtil.d(TAG, "CONSTRUCTOR");
        mContext = ctx;
        mListener = listener;
        createSocket();
    }

    private void createSocket() {
        LogUtil.d(TAG, "createSocket");
        try {
            if(mSocket == null) {
                mSocket = IO.socket(com.goqual.a10k.util.Constraint.SOCKET_SERVER_IP);
            }
            if(!isConnected) {
                registerSocketCallback();
                connect();
            }
        } catch (URISyntaxException e) {
            LogUtil.d(TAG, e.getMessage());
        }
    }

    public void connect() {
        if(mSocket == null) {
            createSocket();
        }
        mSocket.connect();
    }

    public void disconnect() {
        if(mSocket != null) {
            mSocket.disconnect();
        }
    }

    public void destroy() {
        if(--mRefCount == 0) {
            disconnect();
            unregisterSocketCallback();
            mSocket = null;
            instance = null;
        }
        LogUtil.d(TAG, "destroy::REF:"+mRefCount + " SOCKET: " + mSocket);
    }

    private void registerSocketCallback() {
        mSocket.on(Socket.EVENT_CONNECT, onConnectedListener);
        mSocket.on(Socket.EVENT_RECONNECT, onReconnectingListener);
        mSocket.on(Socket.EVENT_RECONNECT_FAILED, onReconnectFailListener);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnectedListener);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectionTimeoutListener);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectErrorListener);

        mSocket.on(SocketProtocols.SOCKET_PT_REQ_JOIN, joinReqListener);
        mSocket.on(SocketProtocols.SOCKET_PT_REQ_LEAVE, leaveReqListener);
        mSocket.on(SocketProtocols.SOCKET_PT_REQ_OPERATION, operaionReqListener);

        mSocket.on(SocketProtocols.SOCKET_PT_RES_JOIN, joinResListener);
        mSocket.on(SocketProtocols.SOCKET_PT_RES_LEAVE, leaveResListener);
        mSocket.on(SocketProtocols.SOCKET_PT_RES_OPERATION, operationResListener);
    }

    private void unregisterSocketCallback() {
        if(mSocket != null) {
            mSocket.off(Socket.EVENT_CONNECT, onConnectedListener);
            mSocket.off(Socket.EVENT_RECONNECT, onReconnectingListener);
            mSocket.off(Socket.EVENT_RECONNECT_FAILED, onReconnectFailListener);
            mSocket.off(Socket.EVENT_DISCONNECT, onDisconnectedListener);
            mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectionTimeoutListener);
            mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectErrorListener);
        }
    }

    private Emitter.Listener onConnectedListener = args ->{
        LogUtil.d(TAG, "SOCKET EVENT_CONNECT");
        mListener.onConnected();
        isConnected = true;
    };

    private Emitter.Listener onReconnectingListener = args -> {
        LogUtil.d(TAG, "SOCKET EVENT_RECONNECT");
        isConnected = false;
        mListener.onReconnect();
    };

    private Emitter.Listener onReconnectFailListener = args -> {
        LogUtil.d(TAG, "SOCKET EVENT_RECONNECT_FAILED");
        isConnected = false;
        mListener.onReconnectionFailed();
        disconnect();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (mSocket != null) {
                    mSocket.connect();
                }
            }
        }, RETRY_AFTER_TWO_SECONDS);
    };

    private Emitter.Listener onDisconnectedListener = args -> {
        LogUtil.d(TAG, "SOCKET EVENT_DISCONNECT");
        isConnected = false;
        mListener.onDisconnect();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isConnected) {
                    if (mSocket != null) {
                        mSocket.connect();
                    }
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
                if (mSocket != null) {
                    mSocket.connect();
                }
            }
        }, RETRY_AFTER_TWO_SECONDS);
    };

    private Emitter.Listener onConnectErrorListener = args -> {
        LogUtil.d(TAG, "SOCKET EVENT_CONNECT_ERROR");
        isConnected = false;
        mListener.onError();
        disconnect();
    };

    private Emitter.Listener joinReqListener = args -> {
        LogUtil.d(TAG, "SOCKET joinReqListener");
        mListener.onReceiveMessage(SocketProtocols.SOCKET_PT_REQ_JOIN, new Gson().fromJson(args[0].toString(), SocketData.class));
    };

    private Emitter.Listener leaveReqListener = args -> {
        LogUtil.d(TAG, "SOCKET leaveReqListener");
        mListener.onReceiveMessage(SocketProtocols.SOCKET_PT_REQ_LEAVE, new Gson().fromJson(args[0].toString(), SocketData.class));
    };

    private Emitter.Listener operaionReqListener = args -> {
        LogUtil.d(TAG, "SOCKET operaionReqListener");
        mListener.onReceiveMessage(SocketProtocols.SOCKET_PT_REQ_OPERATION, new Gson().fromJson(args[0].toString(), SocketData.class));
    };

    private Emitter.Listener joinResListener = args -> {
        LogUtil.d(TAG, "SOCKET joinResListener");
        mListener.onReceiveMessage(SocketProtocols.SOCKET_PT_RES_JOIN, new Gson().fromJson(args[0].toString(), SocketData.class));
    };

    private Emitter.Listener leaveResListener = args -> {
        LogUtil.d(TAG, "SOCKET leaveResListener");
        mListener.onReceiveMessage(SocketProtocols.SOCKET_PT_RES_LEAVE, new Gson().fromJson(args[0].toString(), SocketData.class));
    };

    private Emitter.Listener operationResListener = args -> {
        LogUtil.d(TAG, "SOCKET operationResListener");
        mListener.onReceiveMessage(SocketProtocols.SOCKET_PT_RES_OPERATION, new Gson().fromJson(args[0].toString(), SocketData.class));
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
                    mSocket.emit(SocketProtocols.SOCKET_PT_REQ_JOIN, gson.toJson(data));
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

            mSocket.emit(SocketProtocols.SOCKET_PT_REQ_LEAVE, gson.toJson(data));
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

    public boolean isConnected() {
        return isConnected;
    }
}
