package com.goqual.a10k.view.fragments.setting;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentSettingAbsenceBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Absence;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.presenter.AbsencePresenter;
import com.goqual.a10k.presenter.impl.AbsencePresenterImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.interfaces.IToolbarClickListener;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by hanwool on 2017. 3. 10..
 */

public class FragmentSettingAbsence extends BaseFragment<FragmentSettingAbsenceBinding>
        implements AbsencePresenter.View<Absence>, IToolbarClickListener {
    public static final String TAG = FragmentSettingAbsence.class.getSimpleName();
    private AbsencePresenter mPresenter;
    private Switch mSwitch;
    private Absence mAbsenceItem;

    public static final String TIME_FORMAT_STRING = "HH:mm a";
    public static final String EXTRA_SWITCH = "EXTRA_SWITCH";
    private boolean isItemFromServer;
    private boolean mIsChange;

    public enum BTN_STATE{ON, OFF, NONE}
    private STATE mCurrentState;

    public static FragmentSettingAbsence newInstance(int item) {
        Bundle args = new Bundle();

        FragmentSettingAbsence fragment = new FragmentSettingAbsence();
        args.putInt(EXTRA_SWITCH, item);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting_absence;
    }

    @Override
    public void onSuccessDelete() {

    }

    @Override
    public void onFailDelete(int position) {

    }

    @Override
    public void loadingStart() {

    }

    @Override
    public void loadingStop() {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e(TAG, e.getMessage(), e);
    }

    @Override
    public void addItem(Absence item) {
        LogUtil.d(TAG, ToStringBuilder.reflectionToString(item));
        if(item.get_bsid() == mSwitch.get_bsid()) {
            mAbsenceItem = item;
            mBinding.setItem(mAbsenceItem);
            isItemFromServer = true;
        }
    }

    @Override
    public void onClickEdit(STATE state) {
        LogUtil.d(TAG, "eventState?" + state);
        mCurrentState = state;
        if(mCurrentState == STATE.DONE) {
            if(isItemFromServer) {
                getPresenter().update(mAbsenceItem);
            }
            else {
                getPresenter().add(mAbsenceItem);
            }
        } else {

        }
    }

    @Override
    public void onBtnClick(View view) {
        if(mCurrentState == STATE.EDIT) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(System.currentTimeMillis()));
            switch (view.getId()) {
                case R.id.start_time:
                    new TimePickerDialog(getActivity(), (view1, hourOfDay, minute) -> {
                        LogUtil.d(TAG, String.format("start_time::hourOfDay:%d, minute:%d", hourOfDay, minute));
                        mAbsenceItem.setStart_hour(hourOfDay);
                        mAbsenceItem.setStart_min(minute);
                        mBinding.setItem(mAbsenceItem);
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                            .show();
                    break;
                case R.id.end_time:
                    new TimePickerDialog(getActivity(), (view1, hourOfDay, minute) -> {
                        LogUtil.d(TAG, String.format("start_time::hourOfDay:%d, minute:%d", hourOfDay, minute));
                        mAbsenceItem.setEnd_hour(hourOfDay);
                        mAbsenceItem.setEnd_min(minute);
                        mBinding.setItem(mAbsenceItem);
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                            .show();
                    break;
                case R.id.switch_btn_1:
                    mAbsenceItem.setBtn1(!mAbsenceItem.isBtn1());
                    break;
                case R.id.switch_btn_2:
                    mAbsenceItem.setBtn2(!mAbsenceItem.isBtn2());
                    break;
                case R.id.switch_btn_3:
                    mAbsenceItem.setBtn3(!mAbsenceItem.isBtn3());
                    break;
            }

            mBinding.setItem(mAbsenceItem);
        }

        switch (view.getId()) {
            case R.id.switch_enable:
                if (this.mSwitch.isadmin()) {
                    LogUtil.e(TAG, "check click");
                    getPresenter().update(mAbsenceItem);
                }
                break;
        }
    }

    @Override
    public String getTitle() {
        return mSwitch.getTitle();
    }

    @Override
    public boolean hasToolbarMenus() {
        return mSwitch.isadmin();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mSwitch = SwitchManager.getInstance().getItem(getArguments().getInt(EXTRA_SWITCH));
        }
        mCurrentState = STATE.DONE;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setFragment(this);
        getPresenter().loadItems(mSwitch.get_bsid());

        mAbsenceItem = new Absence(mSwitch);
        mBinding.setItem(mAbsenceItem);

        mBinding.switchEnable.setEnabled(mSwitch.isadmin());

        mBinding.switchEnable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mAbsenceItem.setState(isChecked);
            mBinding.setItem(mAbsenceItem);
        });
    }

    private AbsencePresenter getPresenter() {
        if(mPresenter == null) {
            mPresenter = new AbsencePresenterImpl(getActivity(), this, mAbsenceItem);
        }
        return mPresenter;
    }
}
