package com.goqual.a10k.presenter.impl;

import android.content.Context;

import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.presenter.SocketManager;
import com.goqual.a10k.util.LogUtil;

/**
 * Created by HanWool on 2017. 2. 21..
 */

public class SocketManagerImpl implements SocketManager {
    public static final String TAG = SocketManager.class.getSimpleName();
    private View mView = null;
    private Context mContext = null;

    public SocketManagerImpl(View mView, Context ctx) {
        this.mView = mView;
        mContext = ctx;
    }

    @Override
    public void operationOnOff(Switch item, int btnNumber) {
        LogUtil.d(TAG, "ITEM : " + item.toString() + "\n BTN NUMBER : " + btnNumber);
    }


}
