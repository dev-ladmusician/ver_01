package com.goqual.a10k.view.fragments.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentSettingHistoryBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.History;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.presenter.HistoryPresenter;
import com.goqual.a10k.presenter.impl.HistoryPresenterImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.ResourceUtil;
import com.goqual.a10k.view.adapters.AdapterHistory;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.interfaces.IPaginationPage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hanwool on 2017. 2. 28..
 */

public class FragmentSettingHistory extends BaseFragment<FragmentSettingHistoryBinding>
implements HistoryPresenter.View<History>, IPaginationPage, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    public static final String TAG = FragmentSettingHistory.class.getSimpleName();

    public static final String EXTRA_SWITCH = "EXTRA_SWITCH";
    private Switch mSwitch;
    private AdapterHistory mHistoryAdapter;

    private Calendar mViewCalendar;
    private SimpleDateFormat mSimpleDateFormat;
    private HistoryPresenter mHistoryPresenter;

    private int mCurrentPage = 1;
    private int mLastPage = 1;

    public static FragmentSettingHistory newInstance(int item) {

        Bundle args = new Bundle();

        FragmentSettingHistory fragment = new FragmentSettingHistory();
        args.putInt(EXTRA_SWITCH, item);
        fragment.setArguments(args);
        return fragment;
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
    public void refresh() {
        loadingStop();
        mHistoryAdapter.refresh();
        mBinding.refresh.setRefreshing(false);

        LogUtil.e(TAG, "item count :: " + mHistoryAdapter.getItemCount());

        if (mHistoryAdapter.getItemCount() == 0) {
            mBinding.historyContainer.setVisibility(View.GONE);
            mBinding.historyNoItem.setVisibility(View.VISIBLE);
        } else {
            mBinding.historyContainer.setVisibility(View.VISIBLE);
            mBinding.historyNoItem.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e(TAG, e.getMessage(), e);
    }

    @Override
    public void addItem(History item) {
        mHistoryAdapter.addItem(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting_history;
    }

    @Override
    public String getTitle() {
        return getString(R.string.tab_title_history);
    }

    @Override
    public boolean hasToolbarMenus() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mSwitch = SwitchManager.getInstance().getItem(getArguments().getInt(EXTRA_SWITCH));
        }
        mViewCalendar = Calendar.getInstance();
        mViewCalendar.setTime(new Date());
        mSimpleDateFormat = new SimpleDateFormat(getString(R.string.history_date_format), Locale.KOREA);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView(){
        mBinding.setFragment(this);
        mBinding.historyTxtDate.setText(mSimpleDateFormat.format(mViewCalendar.getTime()));
        mHistoryAdapter = new AdapterHistory(getActivity(), this);
        mBinding.historyContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.historyContainer.setAdapter(mHistoryAdapter);

        mBinding.refresh.setColorSchemeColors(ResourceUtil.getColor(getActivity(), R.color.identity_02));
        mBinding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHistoryAdapter.clear();
                setPage(1);
                loadItems();
            }
        });

        loadItems(mViewCalendar.get(Calendar.YEAR),
                mViewCalendar.get(Calendar.MONTH)+1,
                mViewCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private HistoryPresenter getHistoryPresenter() {
        if(mHistoryPresenter == null) {
            mHistoryPresenter = new HistoryPresenterImpl(getActivity(), this, mHistoryAdapter);
        }
        return mHistoryPresenter;
    }

    public void onBtnClick(View view) {
        if(view.getId() == R.id.history_txt_date) {

            com.wdullaer.materialdatetimepicker.date.DatePickerDialog datepickerDialog =
                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                            FragmentSettingHistory.this,
                            mViewCalendar.get(Calendar.YEAR),
                            mViewCalendar.get(Calendar.MONTH),
                            mViewCalendar.get(Calendar.DAY_OF_MONTH)
                    );
            datepickerDialog.setMaxDate(Calendar.getInstance());
            datepickerDialog.show(getActivity().getFragmentManager(), "datepickerDialogTag");
        }
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mViewCalendar.set(year, monthOfYear, dayOfMonth);
        mBinding.historyTxtDate.setText(mSimpleDateFormat.format(mViewCalendar.getTime()));
        loadItems(year, monthOfYear+1, dayOfMonth);
    }

    /**
     * datepicker에서 날짜 선택 시 호출
     * @param year
     * @param month
     * @param day
     */
    @Override
    public void loadItems(int year, int month, int day) {
        mHistoryAdapter.clear();
        getHistoryPresenter().get(mSwitch.get_bsid(), year, month, day, mCurrentPage);
    }

    /**
     * pagination 시 호출
     */
    @Override
    public void loadItems() {
        getHistoryPresenter().get(mSwitch.get_bsid(),
                mViewCalendar.get(Calendar.YEAR),
                mViewCalendar.get(Calendar.MONTH)+1,
                mViewCalendar.get(Calendar.DAY_OF_MONTH), mCurrentPage);
    }

    @Override
    public void checkLoadMore() {
        if (mCurrentPage < mLastPage) {
            mCurrentPage = mCurrentPage + 1;
            loadItems();
        }
    }

    @Override
    public void setPage(int page) {
        mCurrentPage = page;
    }

    @Override
    public void setLastPage(int lastPage) {
        mLastPage = lastPage;
    }
}
