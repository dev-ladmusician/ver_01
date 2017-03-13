package com.goqual.a10k.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivityAlarmEditBinding;
import com.goqual.a10k.view.base.BaseActivity;
import com.goqual.a10k.view.interfaces.IActivityInteraction;

/**
 * Created by hanwool on 2017. 3. 13..
 */

public class ActivityAlarmEdit extends BaseActivity<ActivityAlarmEditBinding>
implements IActivityInteraction{

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
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.timer_toolbar_cancel:
                finish();
        }
    }
}
