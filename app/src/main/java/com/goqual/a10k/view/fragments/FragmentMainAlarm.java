package com.goqual.a10k.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.goqual.a10k.view.activities.ActivityAlarmEdit;
import com.goqual.a10k.view.adapters.AdapterAlarm;
import com.goqual.a10k.view.base.BaseFragment;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public class FragmentMainAlarm extends BaseFragment<FragmentMainAlarmBinding>
implements AlarmPresenter.View<Alarm>{
    public static final String TAG = FragmentMainAlarm.class.getSimpleName();

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
        return getString(R.string.title_switch_each);
    }

    @Override
    public void onSuccessDelete() {

    }

    @Override
    public void onFailDelete(int position) {

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
    }

    private void initRecyclerView() {
        mBinding.setFragment(this);
        getAdapter().setOnRecyclerItemClickListener((viewId, position) -> {

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
        }
        return mAdapter;
    }

    public void onBtnClick(View view) {
        if(view.getId() == R.id.alarm_no_item_container) {
            startActivity(new Intent(getActivity(), ActivityAlarmEdit.class));
        }
    }
}
