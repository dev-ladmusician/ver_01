package com.goqual.a10k.view.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivityAlarmEditBinding;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.model.entity.Alarm;
import com.goqual.a10k.model.entity.Repeat;
import com.goqual.a10k.presenter.AlarmAddEditPresenter;
import com.goqual.a10k.presenter.impl.AlarmAddEditPresenterImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.base.BaseActivity;
import com.goqual.a10k.view.dialog.CustomDialog;
import com.goqual.a10k.view.interfaces.IActivityInteraction;

import org.parceler.Parcels;

/**
 * Created by hanwool on 2017. 3. 13..
 */

public class ActivityAlarmAddEdit extends BaseActivity<ActivityAlarmEditBinding>
        implements IActivityInteraction, AlarmAddEditPresenter.View {
    public static final String TAG = ActivityAlarmAddEdit.class.getSimpleName();

    public static final String EXTRA_ALARM = "extra_alarm";

    private static final int REQ_GET_SWITCH = 101;
    private static final int REQ_GET_REPEAT = 102;
    private static final int REQ_GET_SOUND = 103;

    private static final String TIME_FORMAT_STRING = "HH:mm";

    private Alarm mAlarm;
    //private Switch mSwitch;
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
        CustomDialog customDialog = new CustomDialog(this);
        DialogInterface.OnClickListener onClickListener = (dialog, which) ->  {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
            dialog.dismiss();
            finishApp();
        };
        customDialog.isEditable(false)
                .setTitleText(R.string.alarm_add_error_title)
                .setMessageText(R.string.alarm_add_error_content)
                .setPositiveButton(getString(R.string.common_retry), onClickListener)
                .setNegativeButton(false)
                .show();
    }

    @Override
    public void onBtnClick(android.view.View view) {
        switch (view.getId()) {
            case R.id.timer_toolbar_cancel:
                finishApp();
                break;
            case R.id.timer_toolbar_save:
                if (checkValidItem()) configDialog();
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
            mAlarm.setmIsSetSwitch(true);
            mAlarm.setRepeatStr(mAlarm.makeDayString(getApplicationContext()).toString());
            isEdit = true;

            // set binding
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mBinding.addAlarmTime.setHour(item.getHour());
                mBinding.addAlarmTime.setMinute(item.getMin());
            } else {
                mBinding.addAlarmTime.setCurrentHour(item.getHour());
                mBinding.addAlarmTime.setCurrentMinute(item.getMin());
            }
        } else {
            mAlarm = new Alarm(
                    getString(R.string.alarm_repeat_never),
                    getString(R.string.alarm_sound_default),
                    getString(R.string.alarm_sound_default)
            );
        }

        mBinding.setAlarmItem(mAlarm);
    }

    public static Intent getRingtoneIntent() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALL);
        return intent;
    }

    /**
     * save전 스위치 선택 유무 checking
     */
    private boolean checkValidItem() {
        if (mAlarm.get_bsid() > 0) return true;
        else {
            CustomDialog customDialog = new CustomDialog(this);
            DialogInterface.OnClickListener onClickListener = (dialog, which) ->  {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        startActivityForResult(new Intent(this, ActivityAlarmSwitchSelect.class), REQ_GET_SWITCH);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
                dialog.dismiss();
            };
            customDialog.isEditable(false)
                    .setTitleText(R.string.alarm_add_error_no_selected_switch_title)
                    .setMessageText(R.string.alarm_add_error_no_selected_switch_content)
                    .setPositiveButton(getString(R.string.common_select_switch), onClickListener)
                    .setNegativeButton(getString(R.string.common_cancel), onClickListener)
                    .show();

            return false;
        }
    }

    /**
     * save전 confirm dialog
     */
    private void configDialog() {
        CustomDialog customDialog = new CustomDialog(this);
        DialogInterface.OnClickListener onClickListener = (dialog, which) ->  {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        mAlarm.setHour(mBinding.addAlarmTime.getHour());
                        mAlarm.setMin(mBinding.addAlarmTime.getMinute());
                    } else {
                        mAlarm.setHour(mBinding.addAlarmTime.getCurrentHour());
                        mAlarm.setMin(mBinding.addAlarmTime.getCurrentMinute());
                    }

                    if (!isEdit) getPresenter().add(mAlarm);
                    else getPresenter().update(mAlarm);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
            dialog.dismiss();
        };
        customDialog.isEditable(false)
                .setTitleText(isEdit ? R.string.alarm_confirm_update_title : R.string.alarm_confirm_add_title)
                .setMessageText(R.string.alarm_confirm_add_content)
                .setPositiveButton(getString(R.string.common_save), onClickListener)
                .setNegativeButton(getString(R.string.common_cancel), onClickListener)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && data.getExtras() != null) {
            if (requestCode == REQ_GET_SWITCH) {
                Alarm item = Parcels.unwrap(data.getParcelableExtra(getString(R.string.arg_alarm)));
                if (item != null) {
                    mAlarm.setBtn1(item.getBtn1());
                    mAlarm.setBtn2(item.getBtn2());
                    mAlarm.setBtn3(item.getBtn3());
                    mAlarm.setBtncount(item.getBtncount());
                    mAlarm.set_bsid(item.get_bsid());
                    mAlarm.setTitle(item.getTitle());
                    mAlarm.setmIsSetSwitch(true);
                }
            } else if(requestCode == REQ_GET_REPEAT) {
                mRepeat = Parcels.unwrap(data.getParcelableExtra(ActivityAlarmRepeat.EXTRA_REPEAT_DAYS));
                mAlarm.setRepeats(mRepeat);
                mAlarm.setRepeatStr(mRepeat.makeString(this));
            } else if (requestCode == REQ_GET_SOUND) {
                try {
                    Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    if (uri != null) {
                        Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
                        mAlarm.setRingtone(uri.equals(Uri.EMPTY) ? getString(R.string.alarm_sound_default) : uri.toString());
                        mAlarm.setRingtone_title(ringtone.getTitle(this));
                    }
                } catch (Exception e) {
                }
            }
            mBinding.setAlarmItem(mAlarm);
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

    @Override
    public int getCurrentPage() {
        return 0;
    }
}
