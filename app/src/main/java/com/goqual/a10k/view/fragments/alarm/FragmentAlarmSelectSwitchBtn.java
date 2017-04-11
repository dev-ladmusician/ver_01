package com.goqual.a10k.view.fragments.alarm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentAlarmSelectSwitchBtnBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Alarm;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.interfaces.IAlarmInteraction;
import com.goqual.a10k.view.interfaces.IToolbarSaveClickListener;


/**
 * Created by ladmusician on 2016. 12. 27..
 */

public class FragmentAlarmSelectSwitchBtn extends BaseFragment<FragmentAlarmSelectSwitchBtnBinding>
    implements IToolbarSaveClickListener {
    public static final String TAG = FragmentAlarmSelectSwitchBtn.class.getSimpleName();

    private static final String SWITCH_TITLE = "switch_title";

    private int mSwitchPosition;
    private Alarm mAlarm;

    public static FragmentAlarmSelectSwitchBtn newInstance(int switchPosition) {
        Bundle args = new Bundle();

        FragmentAlarmSelectSwitchBtn fragment = new FragmentAlarmSelectSwitchBtn();
        args.putInt(SWITCH_TITLE, switchPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveClickEdit() {
        ((IAlarmInteraction)getActivity()).setBtns(mAlarm.getBtn1(), mAlarm.getBtn2(), mAlarm.getBtn3());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_alarm_select_switch_btn;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public boolean hasToolbarMenus() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Switch item = null;
        mAlarm = new Alarm();

        if(getArguments() != null) {
            mSwitchPosition = getArguments().getInt(SWITCH_TITLE, -1);
            try {
                item = SwitchManager.getInstance().getItem(mSwitchPosition).clone();
                mAlarm.setBtncount(item.getBtncount());
            }
            catch (CloneNotSupportedException e) {
                LogUtil.e(TAG, e.getMessage(), e);
                item = SwitchManager.getInstance().getItem(mSwitchPosition);
            }
        }

        mAlarm.setBtncount(item.getBtncount());
        mAlarm.setBtn1(null);
        mAlarm.setBtn2(null);
        mAlarm.setBtn3(null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setFragment(this);

        mAlarm.setBtn1(null);
        mAlarm.setBtn2(null);
        mAlarm.setBtn3(null);
        mBinding.setItemAlarm(mAlarm);
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.switch_btn_1:
                Boolean btn1;
                if (mAlarm.getBtn1() != null)
                    if (mAlarm.getBtn1()) btn1 = false;
                    else btn1 = null;
                else btn1 = true;
                mAlarm.setBtn1(btn1);
                break;
            case R.id.switch_btn_2:
                Boolean btn2;
                if (mAlarm.getBtn2() != null)
                    if (mAlarm.getBtn2()) btn2 = false;
                    else btn2 = null;
                else btn2 = true;
                mAlarm.setBtn2(btn2);
                break;
            case R.id.switch_btn_3:
                Boolean btn3;
                if (mAlarm.getBtn3() != null)
                    if (mAlarm.getBtn3()) btn3 = false;
                    else btn3 = null;
                else btn3 = true;
                mAlarm.setBtn3(btn3);
                break;
        }
        mBinding.setItemAlarm(mAlarm);
    }
}
