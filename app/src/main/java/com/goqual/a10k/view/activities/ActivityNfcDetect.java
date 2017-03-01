package com.goqual.a10k.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivityNfcDetectBinding;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.model.realm.Nfc;
import com.goqual.a10k.presenter.NfcTagPresenter;
import com.goqual.a10k.presenter.SocketManager;
import com.goqual.a10k.presenter.SwitchPresenter;
import com.goqual.a10k.presenter.impl.NfcTagPresenterImpl;
import com.goqual.a10k.presenter.impl.SocketManagerImpl;
import com.goqual.a10k.presenter.impl.SwitchPresenterImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.NfcUtil;
import com.goqual.a10k.view.adapters.AdapterPager;
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

    private Realm realm;

    private String mReadedTagId;
    private Nfc mReadedTag;
    private Switch mSwich;
    private NfcTagPresenter mNfcTagPresenter;
    private SwitchPresenter mSwitchPresenter;

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
            mBinding.appbar.setVisibility(View.GONE);
            realm = Realm.getDefaultInstance();
        }
        else {
            mSwich = getIntent().getParcelableExtra(EXTRA_SWITCH);
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
                setupReq.putExtra(ActivityNfcSetup.EXTRA_SWITCH, mSwich);
                startActivityForResult(setupReq, REQ_SETUP_TAG);
            }
            else {
                Nfc item = realm.where(Nfc.class).equalTo("tag", mReadedTagId).findFirst();
                /**
                 * TODO REST 서버에서 NFC및 Switch 단일 아이템 가져오는 코드 필요
                 */
            }
        }
    }

    public void onBtnClick(View view) {
        if(view.getId() == R.id.toolbar_back) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private SocketManager getSocketManager() {
        if(mSocketManager == null) {
            mSocketManager = new SocketManagerImpl(new SocketManager.View(){
                @Override
                public void onConnectionError() {

                }

                @Override
                public void onConnected() {

                }

                @Override
                public void onServerError(int code) {

                }

                @Override
                public void refreshViews() {

                }
            }, this);
        }
        return mSocketManager;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_SETUP_TAG) {
            if(resultCode == Activity.RESULT_OK) {
                data.setAction(ACTION_REGISTER_TAG);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private SwitchPresenter getSwitchPresenter() {
        if(mSwitchPresenter == null) {
            mSwitchPresenter = new SwitchPresenterImpl(this, new SwitchPresenter.View() {
                @Override
                public void onSuccessRenameSwitch(int position, String title) {

                }

                @Override
                public void onSuccessDeleteSwitch(int position) {

                }

                @Override
                public void loadingStart() {

                }

                @Override
                public void loadingStop() {

                }

                @Override
                public void refresh() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void addItem(Object item) {

                }
            }, new AdapterPager(getSupportFragmentManager()){
                @Override
                public Fragment getItem(int position) {
                    return null;
                }

                @Override
                public int getCount() {
                    return 0;
                }
            });
        }
        return mSwitchPresenter;
    }
}
