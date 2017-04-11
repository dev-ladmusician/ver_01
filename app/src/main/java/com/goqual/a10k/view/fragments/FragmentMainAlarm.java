package com.goqual.a10k.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentMainAlarmBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Alarm;
import com.goqual.a10k.presenter.AlarmPresenter;
import com.goqual.a10k.presenter.impl.AlarmPresenterImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.ResourceUtil;
import com.goqual.a10k.view.activities.ActivityAlarmAddEdit;
import com.goqual.a10k.view.activities.ActivitySwitchConnection;
import com.goqual.a10k.view.adapters.AdapterAlarm;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.dialog.CustomDialog;
import com.goqual.a10k.view.interfaces.IPaginationPage;
import com.goqual.a10k.view.interfaces.IToolbarClickListener;
import com.goqual.a10k.view.interfaces.IToolbarInteraction;

import org.parceler.Parcels;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public class FragmentMainAlarm extends BaseFragment<FragmentMainAlarmBinding>
implements AlarmPresenter.View<Alarm>, IToolbarClickListener, IPaginationPage {
    public static final String TAG = FragmentMainAlarm.class.getSimpleName();

    private static final int REQ_NEW_ALARM = 101;

    private AlarmPresenter mPresenter;
    private AdapterAlarm mAdapter;
    private STATE mCurrentToolbarState = STATE.DONE;

    private int mCurrentPage = 1;
    private int mLastPage = 1;

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
        return getAdapter().getItemCount() > 0;
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
        mBinding.refresh.setRefreshing(false);
        getAdapter().notifyDataSetChanged();

        if(mAdapter.getSize() > 0) {
            mBinding.alarmNoItemContainer.setVisibility(View.GONE);
            mBinding.refresh.setVisibility(View.VISIBLE);
        }
        else {
            mBinding.alarmNoItemContainer.setVisibility(View.VISIBLE);
            mBinding.refresh.setVisibility(View.GONE);
        }

        setToolbarHandler();
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e(TAG, e.getMessage(), e);
    }

    @Override
    public void addItem(Alarm item) {
        getAdapter().addItem(item);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        getAdapter().clear();
        loadItems();
    }

    @Override
    public void checkLoadMore() {
        if (mCurrentPage < mLastPage) {
            mCurrentPage = mCurrentPage + 1;
            loadItems();
        }
    }

    @Override
    public void loadItems() {
        getPresenter().loadItems(mCurrentPage);

        /**
         * TODO realm database 활용 :: pagination
         */
    }

    @Override
    public void setPage(int page) {
        mCurrentPage = page;
    }

    @Override
    public void setLastPage(int lastPage) {
        mLastPage = lastPage;
    }

    private void initRecyclerView() {
        mBinding.setFragment(this);

        mBinding.refresh.setColorSchemeColors(ResourceUtil.getColor(getActivity(), R.color.identitiy_02));
        mBinding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAdapter().clear();
                setPage(1);
                loadItems();
            }
        });

        getAdapter().setOnRecyclerItemClickListener((viewId, position) -> {
            switch (viewId) {
                case R.id.item_alarm_container:
                    LogUtil.e(TAG, "item alarm container click");
                    Intent intent = new Intent(getActivity(), ActivityAlarmAddEdit.class);
                    intent.putExtra(
                            getString(R.string.arg_alarm),
                            Parcels.wrap(getAdapter().getItem(position)));
                    startActivity(intent);
                    break;
                case R.id.item_alarm_delete:
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
                    break;
                case R.id.item_alarm_active:
                    LogUtil.e(TAG, "click activate");
//                    Alarm alarm = mAdapter.getItem(position);
//                    alarm.setState(!alarm.isState());
                    getPresenter().updateState(position);
                    //getPresenter().updateState(position);
                    break;
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
            mAdapter = new AdapterAlarm(getActivity(), this);
        }
        return mAdapter;
    }

    public void onBtnClick(View view) {
        if(view.getId() == R.id.alarm_no_item_container) {
            if (checkExistSwitch())
                startActivity(new Intent(getActivity(), ActivityAlarmAddEdit.class));
        }
    }

    private boolean checkExistSwitch() {
        if (SwitchManager.getInstance().getCount() == 0) {
            new CustomDialog(getActivity())
                    .setTitleText(R.string.alarm_add_error_no_switch_title)
                    .setMessageText(R.string.alarm_add_error_no_switch_content)
                    .setPositiveButton(getString(R.string.common_new_switch), (dialog, which) -> {
                        startActivity(new Intent(getActivity(), ActivitySwitchConnection.class));
                        dialog.dismiss();
                    })
                    .setNegativeButton(getString(R.string.common_cancel), (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClickEdit(STATE state) {
        mCurrentToolbarState = state;
        ((IToolbarInteraction)getActivity()).setToolbarEdit(mCurrentToolbarState);
        getAdapter().setDeletable(state == STATE.EDIT);
    }

    private void setToolbarHandler() {
        mAdapter.setDeletable(false);
        if (getAdapter().getItemCount() == 0) {
            ((IToolbarInteraction)getActivity()).setToolbarEdit(STATE.HIDE);
        } else {
            ((IToolbarInteraction)getActivity()).setToolbarEdit(STATE.DONE);
        }
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(resultCode == Activity.RESULT_OK && data != null) {
//            if(requestCode == REQ_NEW_ALARM) {
//                Alarm alarm = Parcels.unwrap(data.getParcelableExtra(ActivityAlarmAddEdit.EXTRA_ALARM));
//                getPresenter().add(alarm);
//                getAdapter().addItem(alarm);
//            }
//        }
//    }
}
