package com.goqual.a10k.presenter.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.goqual.a10k.model.entity.NotiWrap;
import com.goqual.a10k.model.remote.ResultDTO;
import com.goqual.a10k.model.remote.service.NotiService;
import com.goqual.a10k.model.remote.service.VersionService;
import com.goqual.a10k.presenter.NetworkPresenter;
import com.goqual.a10k.presenter.NotiPresenter;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.adapters.model.AdapterDataModel;

import org.apache.commons.lang3.builder.ToStringBuilder;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

public class NetworkPresenterImpl implements NetworkPresenter {
    private static final String TAG = NetworkPresenterImpl.class.getSimpleName();
    private View mView;
    private NotiService mNotiService;
    private Context mContext;

    public NetworkPresenterImpl(Context ctx, View mView) {
        this.mContext = ctx;
        this.mView = mView;
    }

    @Override
    public void onResume() {
        mContext.registerReceiver(networkStateReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        mContext.registerReceiver(networkStateReceiver, new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED"));
    }

    @Override
    public void onStop() {
        mContext.unregisterReceiver(networkStateReceiver);
    }

    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if(info != null && info.isConnected()) {
                LogUtil.d(TAG, info.toString());
                LogUtil.d(TAG, "NETWORK CONNECTED");
                // TODO: ping
//                new VersionService(mContext).getNotiApi().gets()
//                        .subscribeOn(Schedulers.newThread())
//                        .filter(result -> result.getResult() != null)
//                        .map(ResultDTO::getResult)
//                        .filter(items -> items != null && !items.isEmpty())
//                        .flatMap(items -> Observable.from(items))
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe((item)->{
//                                    mView.onConnected();
//                                    LogUtil.d(TAG, "VERSION::"+ ToStringBuilder.reflectionToString(item));
//                                },
//                                e->{
//                                    mView.onDisconnected();
//                                    LogUtil.e(TAG, e.getMessage(), e);
//                                },
//                                ()->{}
//                        );
                mView.onConnected();
            }
            else {
                LogUtil.d(TAG, "NETWORK DISCONNECTED");
                mView.onDisconnected();
            }
        }
    };
}
