package com.goqual.a10k.util;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import com.google.gson.Gson;
import com.goqual.a10k.R;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.SocketData;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.util.event.EventSocketIO;
import com.goqual.a10k.util.event.EventSwitchRefresh;
import com.goqual.a10k.util.event.RxBus;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import rx.functions.Action1;

public class ServiceSocketIO extends Service {
    public static final String TAG = "ServiceSocketIO";

    private Socket mSocket;
    private boolean isConnected;

    public ServiceSocketIO() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        LogUtil.e(TAG, "onBind");
        return new SocketIOServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.e(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e(TAG, "onStartCommand");
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        LogUtil.e(TAG, "onDestroy");
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(networkChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

        LogUtil.e(TAG, "onCreate");

        subEvent();

        createSocket();
    }

    private void createSocket() {
        try {
            mSocket = IO.socket(Constraint.SOCKET_SERVER_IP);
            mSocket.connect();
            initSocketCallback();
        } catch (URISyntaxException e) {
            LogUtil.e(TAG, e.getMessage(), e);
        }
    }

    private void initSocketCallback() {
        mSocket.on(Socket.EVENT_CONNECT, args -> {
            LogUtil.e(TAG, "SOCKET EVENT_CONNECT");
            isConnected = true;
            RxBus.getInstance().send(new EventSocketIO(EventSocketIO.CONNECTION_STATE.CONNECTED));
        });
        mSocket.on(Socket.EVENT_CONNECT_ERROR, args -> {
            LogUtil.e(TAG, "SOCKET EVENT_CONNECT_ERROR");
            isConnected = false;
            RxBus.getInstance().send(new EventSocketIO(EventSocketIO.CONNECTION_STATE.ERROR));
        });
        mSocket.on(Socket.EVENT_RECONNECT_FAILED, args -> {
            LogUtil.e(TAG, "SOCKET EVENT_RECONNECT_FAILED");
            isConnected = false;
            RxBus.getInstance().send(new EventSocketIO(EventSocketIO.CONNECTION_STATE.FAILED));
            mSocket.disconnect();
        });
        mSocket.on(Socket.EVENT_DISCONNECT, args -> {
            LogUtil.e(TAG, "SOCKET EVENT_DISCONNECT");
            isConnected = false;
            RxBus.getInstance().send(new EventSocketIO(EventSocketIO.CONNECTION_STATE.DISCONNECTED));
            leaveAllSwitchRoom();
            if(!isInternetConnected()) {
                mSocket.disconnect();
            }
        });
    }

    private void subEvent() {
        RxBus.getInstance().toObserverable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if(event instanceof EventSwitchRefresh) {
                            leaveAllSwitchRoom();
                            joinSwitchRoom();
                        }
                    }
                });
    }



    /**
     * 스위치 동작 시키기
     * @param macaddr
     * @param btn
     * @param operation
     */
    public void operationBtn(String macaddr, int btn, String operation) {
        LogUtil.e(TAG, "macaddr : " + macaddr + " btn : " + btn + " op : " + operation);
        if (btn > 0) {
            Gson gson = new Gson();
            SocketData data = new SocketData(macaddr,
                    PreferenceHelper.getInstance(getApplicationContext()).getStringValue(
                            getString(R.string.arg_user_token), ""
                    ), btn, operation);
            mSocket.emit(SocketProtocols.OPERATION_SWITCH_STATE, gson.toJson(data));
        }
    }

    /**
     * 스위치 서버단에 그룹핑 시켜주기
     */
    public void joinSwitchRoom() {
        if (SwitchManager.getInstance().getCount() > 0)
            for(Switch item : SwitchManager.getInstance().getList()) {
                Gson gson = new Gson();
                SocketData data = new SocketData(item.getMacaddr(),
                        PreferenceHelper.getInstance(getApplicationContext()).getStringValue(
                                getString(R.string.arg_user_token), ""
                        ));

                mSocket.emit(SocketProtocols.JOIN_REQ, gson.toJson(data));
            }
    }

    public void leaveSwitchRoom(int position) {
        Switch item = SwitchManager.getInstance().getItem(position);
        Gson gson = new Gson();
        SocketData data = new SocketData(item.getMacaddr(),
                PreferenceHelper.getInstance(getApplicationContext()).getStringValue(
                        getString(R.string.arg_user_token), ""
                ));

        mSocket.emit(SocketProtocols.LEAVE_REQ, gson.toJson(data));
    }

    public void leaveAllSwitchRoom() {
        if (SwitchManager.getInstance().getCount() > 0)
            for(Switch item : SwitchManager.getInstance().getList()) {
                Gson gson = new Gson();
                SocketData data = new SocketData(item.getMacaddr(),
                        PreferenceHelper.getInstance(getApplicationContext()).getStringValue(
                                getString(R.string.arg_user_token), ""
                        ));

                mSocket.emit(SocketProtocols.LEAVE_REQ, gson.toJson(data));
            }
    }

    private boolean isInternetConnected(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            LogUtil.d(TAG, "networkChangeReceiver :: isInternetConnected :" + isInternetConnected() );
            Bundle bundle = intent.getExtras();
            for(String key : bundle.keySet()) {
                LogUtil.d(TAG, "networkChangeReceiver :: KEY : " + key + "\nVALUE : " + bundle.get(key));
            }
            if(isInternetConnected()) {
                if (!isConnected) {

                }
            }
        }
    };

    public class SocketIOServiceBinder extends Binder {

    }

}
