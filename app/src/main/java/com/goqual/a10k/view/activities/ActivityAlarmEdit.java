package com.goqual.a10k.view.activities;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
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
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Alarm;
import com.goqual.a10k.model.entity.Repeat;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.presenter.AlarmAddEditPresenter;
import com.goqual.a10k.presenter.impl.AlarmAddEditPresenterImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.base.BaseActivity;
import com.goqual.a10k.view.interfaces.IActivityInteraction;

import org.parceler.Parcels;

/**
 * Created by hanwool on 2017. 3. 13..
 */

public class ActivityAlarmEdit extends BaseActivity<ActivityAlarmEditBinding>
        implements IActivityInteraction, AlarmAddEditPresenter.View {
    public static final String TAG = ActivityAlarmEdit.class.getSimpleName();

    public static final String EXTRA_ALARM = "extra_alarm";

    private static final int REQ_GET_SWITCH = 101;
    private static final int REQ_GET_REPEAT = 102;
    private static final int REQ_GET_SOUND = 103;

    private static final String TIME_FORMAT_STRING = "HH:mm";

    private Alarm mAlarm;
    private Switch mSwitch;
    private Repeat mRepeat;
    private String mRingtoneUri;
    private String mRingtoneTitle;

    private boolean isEdit = false;

    private AlarmAddEditPresenter mPresenter = null;

    @Override
    public void loadingStart() {
        mBinding.alarmLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadingStop() {
        mBinding.alarmLoading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSuccess() {
        finish();
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e(TAG, e.getStackTrace().toString());
    }

    @Override
    public void onBtnClick(android.view.View view) {
        switch (view.getId()) {
            case R.id.timer_toolbar_cancel:
                finishApp();
                break;
            case R.id.timer_toolbar_save:
                if(mSwitch == null) {
                    Snackbar.make(mBinding.getRoot(), getString(R.string.alarm_error_no_selected_switch), Snackbar.LENGTH_SHORT).show();
                } else {
                    Alarm alarm = new Alarm();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarm.setHour(mBinding.addAlarmTime.getHour());
                        alarm.setMin(mBinding.addAlarmTime.getMinute());
                    } else {
                        alarm.setHour(mBinding.addAlarmTime.getCurrentHour());
                        alarm.setMin(mBinding.addAlarmTime.getCurrentMinute());
                    }
                    alarm.setRepeats(mRepeat);
                    alarm.setSwitch(mSwitch);
                    alarm.setRingtone(mRingtoneUri);
                    alarm.setRingtone_title(mRingtoneTitle);

                    if (!isEdit) getPresenter().add(alarm);
                    else {
                        alarm.set_alarmid(mAlarm.get_alarmid());
                        alarm.set_bsid(mAlarm.get_bsid());
                        alarm.setState(mAlarm.isState());
                        getPresenter().update(alarm);
                    }
                }
                break;
            case R.id.alarm_menu_switch:
                startActivityForResult(new Intent(this, ActivityAlarmSwitchSelect.class), REQ_GET_SWITCH);
                break;
            case R.id.alarm_menu_repeat:
                startActivityForResult(new Intent(this, ActivityAlarmRepeat.class), REQ_GET_REPEAT);
                break;
            case R.id.alarm_menu_sound:
                startActivityForResult(getRingtoneIntent(), REQ_GET_SOUND);
                break;
        }
    }

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
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding.setActivity(this);
        //mBinding.setSwitchItem(new Switch());
        mRingtoneTitle = getString(R.string.alarm_sound_default);
        mRingtoneUri = getString(R.string.alarm_sound_default);
        mRepeat = new Repeat();

        Intent intent = getIntent();
        Alarm item = Parcels.unwrap(intent.getParcelableExtra(getString(R.string.arg_alarm)));
        if (item != null) {
            LogUtil.e(TAG, "alarm not null");
            mAlarm = item;
            isEdit = true;

            // switch
            mSwitch = new Switch(
                    item.getTitle(),
                    item.getBtncount(),
                    item.getBtn1(),
                    item.getBtn2(),
                    item.getBtn3());

            // repeat
            mRepeat = new Repeat(
                    item.sun,
                    item.mon,
                    item.tue,
                    item.wed,
                    item.thur,
                    item.fri,
                    item.sat);

            // ringtone
            mRingtoneUri = item.getRingtone();

            mRingtoneTitle = item.getRingtone_title();

            // set binding
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mBinding.addAlarmTime.setHour(item.getHour());
                mBinding.addAlarmTime.setMinute(item.getMin());
            } else {
                mBinding.addAlarmTime.setCurrentHour(item.getHour());
                mBinding.addAlarmTime.setCurrentMinute(item.getMin());
            }
            mBinding.setSwitchItem(mSwitch);
            mBinding.alarmMenuRepeatLabel.setText(mRepeat.makeString(this));
            mBinding.alarmMenuSoundLabel.setText(mRingtoneTitle);
        }
    }

    public static Intent getRingtoneIntent() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALL);
        return intent;
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
            } else if(requestCode == REQ_GET_REPEAT) {
                mRepeat = Parcels.unwrap(data.getParcelableExtra(ActivityAlarmRepeat.EXTRA_REPEAT_DAYS));
                String rep = mRepeat.makeString(this);
                mBinding.alarmMenuRepeatLabel.setText(rep);
            } else if (requestCode == REQ_GET_SOUND) {
                try {
                    Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    if (uri != null) {
                        Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
                        mRingtoneUri = uri.equals(Uri.EMPTY) ? getString(R.string.alarm_sound_default) : uri.toString();
                        mRingtoneTitle = ringtone.getTitle(this);
                        mBinding.alarmMenuSoundLabel.setText(mRingtoneTitle);
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public PreferenceHelper getPreferenceHelper() {
        return null;
    }

    private AlarmAddEditPresenter getPresenter() {
        if (mPresenter == null)
            mPresenter = new AlarmAddEditPresenterImpl(getApplicationContext(), this);
        return mPresenter;
    }
}
