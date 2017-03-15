package com.goqual.a10k.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentMainAlarmBinding;
import com.goqual.a10k.databinding.FragmentMainSwitchListBinding;
import com.goqual.a10k.model.entity.Alarm;
import com.goqual.a10k.presenter.AlarmPresenter;
import com.goqual.a10k.presenter.impl.AlarmPresenterImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.event.EventSwitchEdit;
import com.goqual.a10k.util.event.EventToolbarClick;
import com.goqual.a10k.util.event.RxBus;
import com.goqual.a10k.view.activities.ActivityAlarmEdit;
import com.goqual.a10k.view.activities.ActivitySwitchConnection;
import com.goqual.a10k.view.adapters.AdapterAlarm;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.dialog.CustomDialog;
import com.goqual.a10k.view.interfaces.IToolbarClickListener;

import org.parceler.Parcels;

import rx.functions.Action1;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public class FragmentMainAlarm extends BaseFragment<FragmentMainAlarmBinding>
implements AlarmPresenter.View<Alarm>{
    public static final String TAG = FragmentMainAlarm.class.getSimpleName();

    private static final int REQ_NEW_ALARM = 101;

    private AlarmPresenter mPresenter;
    private AdapterAlarm mAdapter;

    public static FragmentMainAlarm newInstance() {
        Bundle args = new Bundle();
        FragmentMainAlarm fragment = new FragmentMainAlarm();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_alarm;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_alarm);
    }

    @Override
    public boolean hasToolbarMenus() {
        return true;
    }

    @Override
    public void onSuccessDelete() {
//        Snackbar.make(mBinding.getRoot(), getString(R.string.alarm_delete_error), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onFailDelete(int position) {
        Snackbar.make(mBinding.getRoot(), getString(R.string.alarm_delete_error), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void loadingStart() {
        mBinding.loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadingStop() {
        mBinding.loading.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess() {
        loadingStop();
    }

    @Override
    public void refresh() {
        loadingStop();
        getAdapter().notifyDataSetChanged();
        if(mAdapter.getSize()>0) {
            mBinding.alarmNoItemContainer.setVisibility(View.GONE);
            mBinding.listContainer.setVisibility(View.VISIBLE);
        }
        else {
            mBinding.alarmNoItemContainer.setVisibility(View.VISIBLE);
            mBinding.listContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e(TAG, e.getMessage(), e);
    }

    @Override
    public void addItem(Alarm item) {
        LogUtil.d(TAG, "ITEM::" + item.toString());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        getPresenter().loadItems();
        subEvent();
    }

    private void initRecyclerView() {
        mBinding.setFragment(this);
        getAdapter().setOnRecyclerItemClickListener((viewId, position) -> {
            if(viewId == R.id.item_alarm_delete) {
                new CustomDialog(getActivity())
                        .setTitleText(R.string.alarm_delete_title)
                        .setMessageText(R.string.alarm_delete_content)
                        .setPositiveButton(getString(R.string.common_delete), (dialog, which) -> {
                            getPresenter().delete(position);
                            dialog.dismiss();
                        })
                        .setNegativeButton(getString(R.string.common_cancel), (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
        });
        mBinding.listContainer.setAdapter(getAdapter());
        mBinding.listContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private AlarmPresenter getPresenter() {
        if(mPresenter == null) {
            mPresenter = new AlarmPresenterImpl(getActivity(), this, getAdapter());
        }
        return mPresenter;
    }

    private AdapterAlarm getAdapter() {
        if(mAdapter == null) {
            mAdapter = new AdapterAlarm(getActivity());
            mAdapter.setOnRecyclerItemClickListener((viewId, position) -> {
                switch (viewId) {
                    case R.id.item_alarm_active:
                        Alarm alarm = mAdapter.getItem(position);
                        alarm.setState(!alarm.isState());
                        getPresenter().update(alarm);
                        break;
                }
            });
        }
        return mAdapter;
    }

    public void onBtnClick(View view) {
        if(view.getId() == R.id.alarm_no_item_container) {
            startActivityForResult(new Intent(getActivity(), ActivityAlarmEdit.class), REQ_NEW_ALARM);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK && data != null) {
            if(requestCode == REQ_NEW_ALARM) {
                Alarm alarm = Parcels.unwrap(data.getParcelableExtra(ActivityAlarmEdit.EXTRA_ALARM));
                getPresenter().add(alarm);
                getAdapter().addItem(alarm);
            }
        }
    }

    private void subEvent() {
        RxBus.getInstance().toObserverable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if(event instanceof EventToolbarClick) {
                            LogUtil.e(TAG, "EVENT STATUS :: " + ((EventToolbarClick) event).getStatus());
                            if(isFragmentVisible()) {
                                switch (((EventToolbarClick) event).getStatus()) {
                                    case DONE:
                                        RxBus.getInstance().send(new EventSwitchEdit(IToolbarClickListener.STATUS.EDIT));
                                        getAdapter().setDeletable(false);
                                        break;
                                    case EDIT:
                                        RxBus.getInstance().send(new EventSwitchEdit(IToolbarClickListener.STATUS.DONE));
                                        getAdapter().setDeletable(true);
                                        break;
                                    case ADD:
                                        startActivityForResult(new Intent(getActivity(), ActivityAlarmEdit.class), REQ_NEW_ALARM);
                                        RxBus.getInstance().send(new EventSwitchEdit(IToolbarClickListener.STATUS.EDIT));
                                        break;
                                }
                            }
                        }
                    }
                });
    }
}
