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
            joinInitialRoom();
        } catch (URISyntaxException e) {
            LogUtil.e(TAG, e.getMessage(), e);
        }
    }

    private void joinInitialRoom() {
        mSocket.on(Socket.EVENT_CONNECT, args -> {
            LogUtil.e(TAG, "SOCKET EVENT_CONNECT");
        });
        mSocket.on(Socket.EVENT_CONNECT_ERROR, args -> {
            LogUtil.e(TAG, "SOCKET EVENT_CONNECT_ERROR");
            RxBus.getInstance().send(new EventSocketIO(EventSocketIO.CONNECTION_STATE.ERROR));
        });
        mSocket.on(Socket.EVENT_RECONNECT_FAILED, args -> {
            LogUtil.e(TAG, "SOCKET EVENT_RECONNECT_FAILED");
            mSocket.disconnect();
        });
        mSocket.on(Socket.EVENT_DISCONNECT, args -> {
            LogUtil.e(TAG, "SOCKET EVENT_DISCONNECT");
            if(!isInternetConnected()) {
                mSocket.disconnect();
            }
        });
        mSocket.on(SocketProtocols.CONNECTION, args -> {
            LogUtil.e(TAG, "SOCKET EVENT_CONNECT_ERROR");
            for(Object obj : args) {
                LogUtil.e(TAG, "SOCKET CONNECTION :: " + obj);
            }
        });
    }

    private void subEvent() {
        RxBus.getInstance().toObserverable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if(event instanceof EventSwitchRefresh) {
                            joinRoom(((EventSwitchRefresh) event).getSwitch());
                        }
                    }
                });
    }

    private void joinRoom(Switch item) {
        LogUtil.e(TAG, "joinRoom");
    }

    private boolean isInternetConnected(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private Emitter.Listener mEmitListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

        }
    };

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            LogUtil.d(TAG, "networkChangeReceiver :: isInternetConnected :" + isInternetConnected() );
            Bundle bundle = intent.getExtras();
            for(String key : bundle.keySet()) {
                LogUtil.d(TAG, "networkChangeReceiver :: KEY : " + key + "\nVALUE : " + bundle.get(key));
            }
        }
    };

    class SocketIOServiceBinder extends Binder {

    }

}
