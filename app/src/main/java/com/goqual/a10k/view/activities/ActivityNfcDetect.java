package com.goqual.a10k.view.activities;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivityNfcDetectBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.model.realm.NfcRealm;
import com.goqual.a10k.presenter.NfcTagPresenter;
import com.goqual.a10k.presenter.SocketManager;
import com.goqual.a10k.presenter.impl.SocketManagerImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.NfcUtil;
import com.goqual.a10k.view.base.BaseActivity;

import io.realm.Realm;

/**
 * NFC 태그 리딩 액티비티. <br />
 * 액티비티의 onCreate 인텐트 ACTION이 ACTION_REGISTER_TAG이면 {@see com.goqual.a10k.view.activities.ActivityNfcSetup}를 실행해서
 * ActivityResult를 Result로 ACTION만 ACTION_REGISTER_TAG로 바꿔서 전송.
 * @author Hanwool
 * @version 1
 * @date 2017. 2. 27.
 */

public class ActivityNfcDetect extends BaseActivity<ActivityNfcDetectBinding>{
    public static final String TAG = ActivityNfcDetect.class.getSimpleName();

    public static final String ACTION_REGISTER_TAG = "action_register_tag";
    public static final String EXTRA_NFC_TAG_ID = "extra_nfc_tag_id";
    public static final String EXTRA_SWITCH = "extra_switch";

    private static final int REQ_SETUP_TAG = 134;

    // list of NFC technologies detected:

    private SocketManager mSocketManager;

    private boolean isRegisterMode;

    private String mReadedTagId;
    private NfcRealm mReadedTag;
    private Switch mSwitch;
    private int mSwitchPosition;
    private NfcTagPresenter mNfcTagPresenter;

    private boolean isSocketConnected;

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
            mBinding.appbar.setVisibility(View.GONE);
        } else {
            mSwitchPosition = getIntent().getIntExtra(EXTRA_SWITCH, -1);
            mSwitch = SwitchManager.getInstance().getItem(mSwitchPosition);
            mBinding.setActivity(this);
            mBinding.appbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        NfcUtil.enableForegroundDispatch(this);
        if(!isRegisterMode) {
            onNewIntent(getIntent());
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
                setupReq.putExtra(ActivityNfcSetup.EXTRA_SWITCH, mSwitchPosition);
                startActivity(setupReq);
                finish();
            }
            else {
                Realm realm = Realm.getDefaultInstance();
                mReadedTag = realm.where(NfcRealm.class).equalTo("tag", mReadedTagId).findFirst();
                LogUtil.e(TAG, "TAG??::" + mReadedTag);
                if(mReadedTag != null && getSocketManager().isConnected()) {
                    operateSwitch();
                }
                else {
                    finish();
                }
            }
        }
    }

    public void onBtnClick(View view) {
        if(view.getId() == R.id.toolbar_back) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private void operateSwitch(){
        Switch iSwitch = new Switch();
        iSwitch.setMacaddr(mReadedTag.getMacaddr());
        iSwitch.setBtn1(!mReadedTag.getBtn1());
        iSwitch.setBtn2(!mReadedTag.getBtn2());
        iSwitch.setBtn3(!mReadedTag.getBtn3());
        for (int i = 1; i <= mReadedTag.getBtnCount() ; i++) {
            getSocketManager().operationOnOff(iSwitch, i);
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                LogUtil.e(TAG, e.getMessage(), e);
            }
        }
        finish();
    }

    private SocketManager getSocketManager() {
        if(mSocketManager == null) {
            mSocketManager = new SocketManagerImpl(new SocketManager.View(){
                @Override
                public void onConnectionError() {
                    LogUtil.d(TAG, "SOCKET::onConnectionError");

                }

                @Override
                public void onConnected() {
                    LogUtil.d(TAG, "SOCKET::onConnected");
                    isSocketConnected = true;
                    if(mReadedTag != null) {
                        operateSwitch();
                    }
                }

                @Override
                public void onServerError(int code) {
                    LogUtil.d(TAG, "SOCKET::onServerError:"+code);

                }

                @Override
                public void refreshViews() {
                    LogUtil.d(TAG, "SOCKET::refreshViews");

                }
            }, this);
        }
        return mSocketManager;
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode == REQ_SETUP_TAG) {
//            if(resultCode == Activity.RESULT_OK) {
//                data.setAction(ACTION_REGISTER_TAG);
//                setResult(Activity.RESULT_OK, data);
//                finish();
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}
