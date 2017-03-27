package com.goqual.a10k.view.fragments.alarm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentAlarmSelectSwitchBtnBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.event.EventToolbarClick;
import com.goqual.a10k.util.event.RxBus;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.interfaces.IAlarmInteraction;
import com.goqual.a10k.view.interfaces.IToolbarClickListener;

import rx.functions.Action1;


/**
 * Created by ladmusician on 2016. 12. 27..
 */

public class FragmentAlarmSelectSwitchBtn extends BaseFragment<FragmentAlarmSelectSwitchBtnBinding> {
    public static final String TAG = FragmentAlarmSelectSwitchBtn.class.getSimpleName();

    private static final String SWITCH_TITLE = "switch_title";

    private int mSwitchPosition;
    private Switch mSwitch;

    public static FragmentAlarmSelectSwitchBtn newInstance(int switchPosition) {

        Bundle args = new Bundle();

        FragmentAlarmSelectSwitchBtn fragment = new FragmentAlarmSelectSwitchBtn();
        args.putInt(SWITCH_TITLE, switchPosition);
        fragment.setArguments(args);
        return fragment;
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
        if(getArguments() != null) {
            mSwitchPosition = getArguments().getInt(SWITCH_TITLE, -1);
            try {
                mSwitch = SwitchManager.getInstance().getItem(mSwitchPosition).clone();
            }
            catch (CloneNotSupportedException e) {
                LogUtil.e(TAG, e.getMessage(), e);
                mSwitch = SwitchManager.getInstance().getItem(mSwitchPosition);
            }
        }
        subEvent();
    }

    private void subEvent() {
        RxBus.getInstance().toObserverable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if(event instanceof EventToolbarClick) {
                            if(((EventToolbarClick) event).getState() == IToolbarClickListener.STATE.DONE) {
                                ((IAlarmInteraction)getActivity()).setBtns(mSwitch.isBtn1(), mSwitch.isBtn2(), mSwitch.isBtn3());
                            }
                        }
                    }
                });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setFragment(this);
        mSwitch.setBtn1(false);
        mSwitch.setBtn2(false);
        mSwitch.setBtn3(false);
        mBinding.setItemSwitch(mSwitch);
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.switch_btn_1:
                mSwitch.setBtn1(!mSwitch.isBtn1());
                break;
            case R.id.switch_btn_2:
                mSwitch.setBtn2(!mSwitch.isBtn2());
                break;
            case R.id.switch_btn_3:
                mSwitch.setBtn3(!mSwitch.isBtn3());
                break;
        }
        mBinding.setItemSwitch(mSwitch);
    }
}
