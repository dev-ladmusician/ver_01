package com.goqual.a10k.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivityNfcSetupBinding;
import com.goqual.a10k.model.entity.Nfc;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.view.base.BaseActivity;

/**
 * Created by hanwool on 2017. 2. 28..
 */

public class ActivityNfcSetup extends BaseActivity<ActivityNfcSetupBinding> {

    public static final String TAG = ActivityNfcSetup.class.getSimpleName();

    public static final String EXTRA_NFC_TAG_ID = "EXTRA_NFC_TAG_ID";
    public static final String EXTRA_SWITCH = "EXTRA_SWITCH";

    private Switch mSwitch;
    private Nfc mNfcTag;
    private String mNfcId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_nfc_setup;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwitch = getIntent().getParcelableExtra(EXTRA_SWITCH);
        mNfcId = getIntent().getStringExtra(EXTRA_NFC_TAG_ID);
        initView();
    }

    private void initView() {
        mBinding.setActivity(this);
        mBinding.setItemSwitch(mSwitch);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.switch_btn_1:
                mSwitch.setBtn1(!mSwitch.getBtn1());
                break;
            case R.id.switch_btn_2:
                mSwitch.setBtn2(!mSwitch.getBtn2());
                break;
            case R.id.switch_btn_3:
                mSwitch.setBtn3(!mSwitch.getBtn3());
                break;
        }
    }
}
