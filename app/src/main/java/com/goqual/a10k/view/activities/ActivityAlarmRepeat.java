package com.goqual.a10k.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivityAlarmRepeatBinding;
import com.goqual.a10k.model.entity.Repeat;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.base.BaseActivity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by hanwool on 2017. 3. 14..
 */

public class ActivityAlarmRepeat extends BaseActivity<ActivityAlarmRepeatBinding> {
    public static final String TAG = ActivityAlarmRepeat.class.getSimpleName();

    public static final String EXTRA_REPEAT_DAYS = "extra_repeat_days";

    public final static int SUNDAY = 0;
    public final static int MONDAY = 1;
    public final static int TUESDAY = 2;
    public final static int WEDNESDAY = 3;
    public final static int THURSDAY = 4;
    public final static int FRIDAY = 5;
    public final static int SATURDAY = 6;

    private Repeat repeats;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm_repeat;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repeats = new Repeat();
        mBinding.setRepeats(repeats);
        mBinding.setActivity(this);
    }

    @Override
    public void onBtnClick(View view) {
        LogUtil.d(TAG, "onBtnClick::"+view.getId());
        if(view.getId() == R.id.toolbar_back) {
            Intent result = getIntent();
            result.putExtra(EXTRA_REPEAT_DAYS, Parcels.wrap(repeats));
            setResult(RESULT_OK, result);
            finish();
        }
        else {
            int index = Integer.parseInt((String)view.getTag());
            LogUtil.d(TAG, "onBtnClick::index: " + index);
            switch (index) {
                case SUNDAY:
                    repeats.setSun(!repeats.isSun());
                    break;
                case MONDAY:
                    repeats.setMon(!repeats.isMon());
                    break;
                case TUESDAY:
                    repeats.setTue(!repeats.isTue());
                    break;
                case WEDNESDAY:
                    repeats.setWed(!repeats.isWed());
                    break;
                case THURSDAY:
                    repeats.setThu(!repeats.isThu());
                    break;
                case FRIDAY:
                    repeats.setFri(!repeats.isFri());
                    break;
                case SATURDAY:
                    repeats.setSat(!repeats.isSat());
                    break;
            }
            LogUtil.d(TAG, "Repeats: " + ToStringBuilder.reflectionToString(repeats));
            mBinding.setRepeats(repeats);
        }
    }
}
