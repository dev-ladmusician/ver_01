package com.goqual.a10k.view.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.model.realm.Nfc;
import com.goqual.a10k.presenter.SocketManager;
import com.goqual.a10k.presenter.impl.SocketManagerImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.NfcUtil;
import com.goqual.a10k.view.base.BaseActivity;

import io.realm.Realm;

/**
 * Created by hanwool on 2017. 2. 27..
 */

public class ActivityNfcDetected extends AppCompatActivity
        implements SocketManager.View{
    public static final String TAG = ActivityNfcDetected.class.getSimpleName();

    // list of NFC technologies detected:

    private SocketManager mSocketManager;
    private Realm realm;

    private String mReadedTagId;
    private Nfc mReadedTag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSocketManager();
    }

    @Override
    protected void onResume() {
        super.onResume();

        NfcUtil.enableForegroundDispatch(this);

        if(getIntent() != null) {
            String action = "null";
            switch (getIntent().getAction()) {
                case NfcAdapter.ACTION_NDEF_DISCOVERED:
                    action = "ACTION_NDEF_DISCOVERED";
                    break;
                case NfcAdapter.ACTION_TAG_DISCOVERED:
                    action = "ACTION_TAG_DISCOVERED";
                    break;
                case NfcAdapter.ACTION_TECH_DISCOVERED:
                    action = "ACTION_TECH_DISCOVERED";
                    break;
            }
            mReadedTagId = ByteArrayToHexString(getIntent().getByteArrayExtra(NfcAdapter.EXTRA_ID));
            LogUtil.d(TAG, "onResume " + action + "::" + ByteArrayToHexString(getIntent().getByteArrayExtra(NfcAdapter.EXTRA_ID)));

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        NfcUtil.disableForegroundDispatch(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSocketManager().destroySocketConnection();
    }

    @Override
    public void onConnectionError() {
        LogUtil.d(TAG, "onConnectionError");
    }

    @Override
    public void onConnected() {
        LogUtil.d(TAG, "onConnected::"+mReadedTagId);

        realm = Realm.getDefaultInstance();

        mReadedTag = realm.where(Nfc.class).equalTo("tagId", mReadedTagId).findFirst();
        if(mReadedTag != null) {
            Switch item = new Switch();
            item.setMacaddr(mReadedTag.getMacAddr());
            item.setBtn1(mReadedTag.isBtnState1());
            item.setBtn2(mReadedTag.isBtnState2());
            item.setBtn3(mReadedTag.isBtnState3());
            getSocketManager().operationOnOff(item, 1);
            getSocketManager().operationOnOff(item, 2);
            getSocketManager().operationOnOff(item, 3);
        }

        realm.close();

        finish();
    }

    @Override
    public void onServerError(int code) {
        LogUtil.d(TAG, "onServerError::"+code);
    }

    @Override
    public void refreshViews() {
        LogUtil.d(TAG, "refreshViews");
    }

    private SocketManager getSocketManager() {
        if(mSocketManager == null) {
            mSocketManager = new SocketManagerImpl(this, this);
        }
        return mSocketManager;
    }

    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }
}
