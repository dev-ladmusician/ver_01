package com.goqual.a10k.presenter.impl;

import android.content.Context;

import com.google.gson.Gson;
import com.goqual.a10k.R;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.model.entity.SocketData;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.presenter.SocketManager;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.SocketIoManager;
import com.goqual.a10k.util.SocketProtocols;
import com.goqual.a10k.util.event.EventSocketIO;
import com.goqual.a10k.util.event.RxBus;
import com.goqual.a10k.util.interfaces.ISocketIoConnectionListener;

import rx.functions.Action1;

/**
 * Created by HanWool on 2017. 2. 21..
 */

public class SocketManagerImpl implements SocketManager, ISocketIoConnectionListener {
    public static final String TAG = SocketManager.class.getSimpleName();
    private View mView = null;
    private Context mContext = null;
    private SocketIoManager mSocketManager;

    public SocketManagerImpl(View mView, Context ctx) {
        LogUtil.e(TAG, "SocketManagerImpl");
        this.mView = mView;
        mContext = ctx;
        mSocketManager = SocketIoManager.getInstance(ctx, this);
    }

    public void tryReconnect() {
        mSocketManager.disconnect();
        mSocketManager.connect();
    }

    @Override
    public void operationOnOff(Switch item, int btnNumber) {
        LogUtil.d(TAG, "ITEM : " + item.toString() + "\n BTN NUMBER : " + btnNumber);
        if (btnNumber > 0) {
            Gson gson = new Gson();
            String btnState;
            switch (btnNumber) {
                case 1:
                    btnState = !item.getBtn1() ? mContext.getString(R.string.btn_str_on) : mContext.getString(R.string.btn_str_off);
                    break;
                case 2:
                    btnState = !item.getBtn2() ? mContext.getString(R.string.btn_str_on) : mContext.getString(R.string.btn_str_off);
                    break;
                case 3:
                    btnState = !item.getBtn3() ? mContext.getString(R.string.btn_str_on) : mContext.getString(R.string.btn_str_off);
                    break;
                default:
                    btnState = mContext.getString(R.string.btn_str_on);
            }
            SocketData data = new SocketData(item.getMacaddr(),
                    PreferenceHelper.getInstance(mContext).getStringValue(
                            mContext.getString(R.string.arg_user_token), ""
                    ), btnNumber,
                    btnState);
            mSocketManager.emit(SocketProtocols.OPERATION_SWITCH_STATE, gson.toJson(data));
        }
    }

    @Override
    public void onConnected() {
        LogUtil.e(TAG, "SOCKET onConnected");
        mView.onConnected();
        mSocketManager.joinSwitchRoom();
    }

    @Override
    public void onConnectionTimeOut() {
        LogUtil.e(TAG, "SOCKET onConnectionTimeOut");
    }

    @Override
    public void onDisconnect() {
        LogUtil.e(TAG, "SOCKET onDisconnect");
        mSocketManager.leaveAllSwitchRooms();
    }

    @Override
    public void onError() {
        LogUtil.e(TAG, "SOCKET onError");
        mView.onConnectionError();
    }

    @Override
    public void onReceiveMessage(Object... args) {
        LogUtil.e(TAG, "SOCKET onReceiveMessage");
        for(Object obj : args) {
            LogUtil.e(TAG, "SOCKET obj :: " + obj);
        }
    }

    @Override
    public void onReconnect() {
        LogUtil.e(TAG, "SOCKET onReconnect");
    }

    @Override
    public void onReconnectionFailed() {
        LogUtil.e(TAG, "SOCKET onReconnectionFailed");
    }

    @Override
    public void refreshConnectedRoom() {
        mSocketManager.joinSwitchRoom();
    }

    @Override
    public void destroySocketConnection() {
        mSocketManager.destroy();
        mSocketManager = null;
    }

}
