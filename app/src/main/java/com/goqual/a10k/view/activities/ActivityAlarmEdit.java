package com.goqual.a10k.view.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivityAlarmEditBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Alarm;
import com.goqual.a10k.model.entity.Repeat;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.base.BaseActivity;
import com.goqual.a10k.view.dialog.CustomDialog;
import com.goqual.a10k.view.interfaces.IActivityInteraction;

import org.parceler.Parcels;

/**
 * Created by hanwool on 2017. 3. 13..
 */

public class ActivityAlarmEdit extends BaseActivity<ActivityAlarmEditBinding>
        implements IActivityInteraction{
    public static final String TAG = ActivityAlarmEdit.class.getSimpleName();

    public static final String EXTRA_ALARM = "extra_alarm";

    private static final int REQ_GET_SWITCH = 101;
    private static final int REQ_GET_REPEAT = 102;
    private static final int REQ_GET_SOUND = 103;

    private static final String TIME_FORMAT_STRING = "HH:mm";

    private Switch mSwitch;
    private Repeat mRepeat;
    private Uri mRingtoneUri;
    private String mRingtoneName;

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
        mRingtoneName = getString(R.string.alarm_sound_default);
        mRingtoneUri = Uri.EMPTY;
        mRepeat = new Repeat();
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.timer_toolbar_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.timer_toolbar_save:
                if(mSwitch == null) {
                    Snackbar.make(mBinding.getRoot(), getString(R.string.alarm_error_no_selected_switch), Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Alarm alarm = new Alarm();
                    String alarmTime;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarm.setHour(mBinding.addAlarmTime.getHour());
                        alarm.setMin(mBinding.addAlarmTime.getMinute());
                    } else {
                        alarm.setHour(mBinding.addAlarmTime.getCurrentHour());
                        alarm.setMin(mBinding.addAlarmTime.getCurrentMinute());
                    }
                    alarm.setRepeats(mRepeat);
                    alarm.setSwitch(mSwitch);
                    alarm.setRingtone(mRingtoneUri == Uri.EMPTY ? getString(R.string.alarm_sound_default) : mRingtoneUri.toString());
                    alarm.setRingtone_title(mRingtoneName);
                    Intent result = getIntent();
                    result.putExtra(EXTRA_ALARM, Parcels.wrap(alarm));
                    setResult(RESULT_OK, result);
                    finish();
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
