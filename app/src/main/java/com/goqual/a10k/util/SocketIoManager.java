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
            initSocketCallback();
            connect();
        } catch (URISyntaxException e) {
            LogUtil.e(TAG, e.getMessage(), e);
        }
    }

    private void connect() {
        mSocket.connect();
    }

    private void disconnect() {
        mSocket.disconnect();
    }

    private void initSocketCallback() {
        mSocket.on(Socket.EVENT_CONNECT, args -> {
            LogUtil.e(TAG, "SOCKET EVENT_CONNECT");
            mListener.onConnected();
            isConnected = true;
        });
        mSocket.on(Socket.EVENT_RECONNECT, args -> {
            LogUtil.e(TAG, "SOCKET EVENT_RECONNECT");
            isConnected = false;
            mListener.onReconnect();
        });
        mSocket.on(Socket.EVENT_RECONNECT_FAILED, args -> {
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
        });
        mSocket.on(Socket.EVENT_DISCONNECT, args -> {
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
        });
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, args -> {
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
        });
        mSocket.on(Socket.EVENT_CONNECT_ERROR, args -> {
            LogUtil.e(TAG, "SOCKET EVENT_CONNECT_ERROR");
            isConnected = false;
            mListener.onError();
            disconnect();
        });
    }

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
