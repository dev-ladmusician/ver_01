package com.goqual.a10k.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivityAlarmSwitchSelectBinding;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.util.event.EventToolbarClick;
import com.goqual.a10k.util.event.RxBus;
import com.goqual.a10k.view.base.BaseActivity;
import com.goqual.a10k.view.fragments.alarm.FragmentAlarmSelectSwitch;
import com.goqual.a10k.view.fragments.alarm.FragmentAlarmSelectSwitchBtn;
import com.goqual.a10k.view.interfaces.IActivityInteraction;
import com.goqual.a10k.view.interfaces.IAlarmInteraction;
import com.goqual.a10k.view.interfaces.IToolbarClickListener;

/**
 * Created by hanwool on 2017. 3. 13..
 */

public class ActivityAlarmSwitchSelect extends BaseActivity<ActivityAlarmSwitchSelectBinding>
        implements IAlarmInteraction, IActivityInteraction{
    public static final String TAG = ActivityAlarmSwitchSelect.class.getSimpleName();

    public static final String EXTRA_SWITCH = "extra_switch";
    public static final String EXTRA_BTN = "extra_btn";

    private int mSwitchPos;

    @Override
    public AppBarLayout getAppbar() {
        return null;
    }

    @Override
    public Toolbar getToolbar() {
        return null;
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void finishApp() {
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm_switch_select;
    }

    @Override
    public void goBtnPage(int itemPos) {
        mSwitchPos = itemPos;
        getSupportFragmentManager().beginTransaction()
                .replace(mBinding.fragmentContainer.getId(), FragmentAlarmSelectSwitchBtn.newInstance(itemPos))
        .commit();
        mBinding.timerToolbarSave.setVisibility(View.VISIBLE);
    }

    @Override
    public void setBtns(boolean btn1, boolean btn2, boolean btn3) {
        Intent result = getIntent();
        result.putExtra(EXTRA_SWITCH, mSwitchPos);
        result.putExtra(EXTRA_BTN, new boolean[]{btn1, btn2, btn3});
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding.setActivity(this);

        getSupportFragmentManager().beginTransaction()
                .add(mBinding.fragmentContainer.getId(), FragmentAlarmSelectSwitch.newInstance())
                .commit();
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.timer_toolbar_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.timer_toolbar_save:
                RxBus.getInstance().send(new EventToolbarClick(IToolbarClickListener.STATE.DONE));
                break;
        }
    }

    @Override
    public PreferenceHelper getPreferenceHelper() {
        return null;
    }
}
