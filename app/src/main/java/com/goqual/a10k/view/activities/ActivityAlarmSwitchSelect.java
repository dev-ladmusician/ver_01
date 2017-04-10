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
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Alarm;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.view.base.BaseActivity;
import com.goqual.a10k.view.fragments.alarm.FragmentAlarmSelectSwitch;
import com.goqual.a10k.view.fragments.alarm.FragmentAlarmSelectSwitchBtn;
import com.goqual.a10k.view.interfaces.IActivityInteraction;
import com.goqual.a10k.view.interfaces.IAlarmInteraction;
import com.goqual.a10k.view.interfaces.IToolbarSaveClickListener;

import org.parceler.Parcels;

/**
 * Created by hanwool on 2017. 3. 13..
 */

public class ActivityAlarmSwitchSelect extends BaseActivity<ActivityAlarmSwitchSelectBinding>
        implements IAlarmInteraction, IActivityInteraction{
    public static final String TAG = ActivityAlarmSwitchSelect.class.getSimpleName();

    public static final String EXTRA_SWITCH = "extra_switch";
    public static final String EXTRA_BTN = "extra_btn";

    private final String FRAG_TAG_SELECT_SWITCH = "SELECT_SWITCH";
    private final String FRAG_TAG_SELECT_BTN = "SELECT_BTN";

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

    /**
     * 스위치 선택될 시 호출
     * @param position
     */
    @Override
    public void setSwitch(int position) {
        mSwitchPos = position;
        getSupportFragmentManager().beginTransaction()
                .replace(mBinding.fragmentContainer.getId(),
                        FragmentAlarmSelectSwitchBtn.newInstance(position), FRAG_TAG_SELECT_BTN).commit();
        mBinding.timerToolbarSave.setVisibility(View.VISIBLE);
    }

    @Override
    public void setBtns(Boolean btn1, Boolean btn2, Boolean btn3) {
        Intent result = getIntent();
        Switch item = SwitchManager.getInstance().getItem(mSwitchPos);
        Alarm param = new Alarm(btn1, btn2, btn3, item.getBtnCount(), item.get_bsid(), item.getTitle());
        result.putExtra(getString(R.string.arg_alarm), Parcels.wrap(param));
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding.setActivity(this);

        getSupportFragmentManager().beginTransaction()
                .add(mBinding.fragmentContainer.getId(), FragmentAlarmSelectSwitch.newInstance(), FRAG_TAG_SELECT_SWITCH)
                .commit();
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.timer_toolbar_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.timer_toolbar_save:
                ((IToolbarSaveClickListener)getSupportFragmentManager().findFragmentByTag(FRAG_TAG_SELECT_BTN)).onSaveClickEdit();
                break;
        }
    }

    @Override
    public PreferenceHelper getPreferenceHelper() {
        return null;
    }
}
