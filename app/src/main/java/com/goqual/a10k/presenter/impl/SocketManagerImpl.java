package com.goqual.a10k.presenter.impl;

import android.content.Context;

import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.presenter.SocketManager;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.ServiceSocketIO;
import com.goqual.a10k.util.event.EventSocketIO;
import com.goqual.a10k.util.event.EventSwitchRefresh;
import com.goqual.a10k.util.event.RxBus;

import rx.functions.Action1;

/**
 * Created by HanWool on 2017. 2. 21..
 */

public class SocketManagerImpl implements SocketManager {
    public static final String TAG = SocketManager.class.getSimpleName();
    private View mView = null;
    private Context mContext = null;
    private ServiceSocketIO.SocketIOServiceBinder serviceBinder;

    public SocketManagerImpl(View mView, Context ctx, ServiceSocketIO.SocketIOServiceBinder binder) {
        this.mView = mView;
        mContext = ctx;
        serviceBinder = binder;
        subEvent();
    }

    @Override
    public void operationOnOff(Switch item, int btnNumber) {
        LogUtil.d(TAG, "ITEM : " + item.toString() + "\n BTN NUMBER : " + btnNumber);

    }

    private void subEvent() {
        RxBus.getInstance().toObserverable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if(event instanceof EventSocketIO) {

                        }
                    }
                });
    }


}
