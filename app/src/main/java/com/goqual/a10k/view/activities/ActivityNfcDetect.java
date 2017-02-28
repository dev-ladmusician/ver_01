package com.goqual.a10k.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivityNfcDetectBinding;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.model.entity.Nfc;
import com.goqual.a10k.presenter.SocketManager;
import com.goqual.a10k.presenter.impl.SocketManagerImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.NfcUtil;
import com.goqual.a10k.view.base.BaseActivity;

import java.math.BigInteger;

import io.realm.Realm;

/**
 * Created by hanwool on 2017. 2. 27..
 */

public class ActivityNfcDetect extends BaseActivity<ActivityNfcDetectBinding>
        implements SocketManager.View{
    public static final String TAG = ActivityNfcDetect.class.getSimpleName();

    public static final String ACTION_REGISTER_TAG = "action_register_tag";
    public static final String EXTRA_NFC_TAG_ID = "extra_nfc_tag_id";
    public static final String EXTRA_SWITCH = "extra_switch";

    private static final int REQ_SETUP_TAG = 134;

    // list of NFC technologies detected:

    private SocketManager mSocketManager;

    private boolean isRegisterMode;

    private String mReadedTagId;
    private Nfc mReadedTag;
    private Switch mSwich;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_nfc_detect;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRegisterMode = getIntent().getAction().equals(ACTION_REGISTER_TAG);
        if(!isRegisterMode) {
            getSocketManager();
            moveTaskToBack(true);
        }
        else {
            mSwich = getIntent().getParcelableExtra(EXTRA_SWITCH);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        NfcUtil.enableForegroundDispatch(this);
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        if(intent != null) {
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
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            mReadedTagId = new java.math.BigInteger(tag.getId()).toString(16);
            LogUtil.d(TAG, "onNewIntent " + action + "::" + mReadedTagId);
            if(isRegisterMode) {
                Intent setupReq = new Intent(this, ActivityNfcSetup.class);
                setupReq.putExtra(ActivityNfcSetup.EXTRA_NFC_TAG_ID, mReadedTagId);
                setupReq.putExtra(ActivityNfcSetup.EXTRA_SWITCH, mSwich);
                startActivityForResult(setupReq, REQ_SETUP_TAG);
            }
        }
    }

    @Override
    public void onConnectionError() {
        LogUtil.d(TAG, "onConnectionError");
    }

    @Override
    public void onConnected() {
        LogUtil.d(TAG, "onConnected::"+mReadedTagId);

        if(mReadedTag != null) {
            Switch item = new Switch();
//            item.setMacaddr(mReadedTag.getMacAddr());
//            item.setBtn1(mReadedTag.isBtnState1());
//            item.setBtn2(mReadedTag.isBtnState2());
//            item.setBtn3(mReadedTag.isBtnState3());
//            getSocketManager().operationOnOff(item, 1);
//            getSocketManager().operationOnOff(item, 2);
//            getSocketManager().operationOnOff(item, 3);
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_SETUP_TAG) {
            if(resultCode == Activity.RESULT_OK) {
                Intent result = new Intent();
                result.setAction(ACTION_REGISTER_TAG);
                result.putExtra(EXTRA_NFC_TAG_ID, mReadedTagId);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
