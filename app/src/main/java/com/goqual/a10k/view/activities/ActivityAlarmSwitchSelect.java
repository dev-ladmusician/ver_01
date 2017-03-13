package com.goqual.a10k.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivityAlarmSwitchSelectBinding;
import com.goqual.a10k.view.base.BaseActivity;

/**
 * Created by hanwool on 2017. 3. 13..
 */

public class ActivityAlarmSwitchSelect extends BaseActivity<ActivityAlarmSwitchSelectBinding> {
    public static final String TAG = ActivityAlarmSwitchSelect.class.getSimpleName();

    public static final String EXTRA_SWITCH = "extra_switch";
    public static final String EXTRA_BTN = "extra_btn";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm_switch_select;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onBtnClick(View view) {

    }
}
