package com.goqual.a10k.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivityAlarmEditBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Repeat;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.base.BaseActivity;
import com.goqual.a10k.view.interfaces.IActivityInteraction;

import org.parceler.Parcels;

/**
 * Created by hanwool on 2017. 3. 13..
 */

public class ActivityAlarmEdit extends BaseActivity<ActivityAlarmEditBinding>
        implements IActivityInteraction{
    public static final String TAG = ActivityAlarmEdit.class.getSimpleName();

    private static final int REQ_GET_SWITCH = 101;
    private static final int REQ_GET_REPEAT = 102;
    private static final int REQ_GET_SOUND = 103;

    private Switch mSwitch;
    private Repeat mRepeat;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm_edit;
    }

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

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding.setActivity(this);
        mBinding.setSwitchItem(new Switch());
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.timer_toolbar_cancel:
                finish();
                break;
            case R.id.timer_toolbar_save:
                if(mSwitch == null) {
                    Snackbar.make(mBinding.getRoot(), getString(R.string.alarm_no_item), Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.alarm_menu_switch:
                startActivityForResult(new Intent(this, ActivityAlarmSwitchSelect.class), REQ_GET_SWITCH);
                break;
            case R.id.alarm_menu_repeat:
                startActivityForResult(new Intent(this, ActivityAlarmRepeat.class), REQ_GET_REPEAT);
                break;
            case R.id.alarm_menu_sound:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && data.getExtras() != null) {
            if (requestCode == REQ_GET_SWITCH) {
                try {
                    mSwitch = SwitchManager.getInstance().getItem(data.getIntExtra(ActivityAlarmSwitchSelect.EXTRA_SWITCH, -1)).clone();
                } catch (CloneNotSupportedException e) {
                    LogUtil.e(TAG, e.getMessage(), e);
                    mSwitch = SwitchManager.getInstance().getItem(data.getIntExtra(ActivityAlarmSwitchSelect.EXTRA_SWITCH, -1));
                }
                boolean[] switchState = data.getBooleanArrayExtra(ActivityAlarmSwitchSelect.EXTRA_BTN);
                if (mSwitch != null && (switchState != null && switchState.length > 0)) {
                    mSwitch.setBtn1(switchState[0]);
                    mSwitch.setBtn2(switchState[1]);
                    mSwitch.setBtn3(switchState[2]);
                    mBinding.setSwitchItem(mSwitch);
                }
            }
            else if(requestCode == REQ_GET_REPEAT) {
                mRepeat = Parcels.unwrap(data.getParcelableExtra(ActivityAlarmRepeat.EXTRA_REPEAT_DAYS));
                String rep = mRepeat.makeString(this);
                LogUtil.d(TAG, rep);
                mBinding.alarmMenuRepeatLabel.setText(rep);
            }
        }
    }
}
