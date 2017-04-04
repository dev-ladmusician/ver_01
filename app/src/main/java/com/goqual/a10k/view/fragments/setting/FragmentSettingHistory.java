package com.goqual.a10k.view.fragments.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.goqual.a10k.view.adapters.AdapterHistory;
import com.goqual.a10k.view.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hanwool on 2017. 2. 28..
 */

public class FragmentSettingHistory extends BaseFragment<FragmentSettingHistoryBinding>
implements HistoryPresenter.View<History>, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    public static final String TAG = FragmentSettingHistory.class.getSimpleName();

    public static final String EXTRA_SWITCH = "EXTRA_SWITCH";
    private Switch mSwitch;
    private AdapterHistory mHistoryAdapter;

    private Calendar mViewCalendar;
    private SimpleDateFormat mSimpleDateFormat;

    private HistoryPresenter mHistoryPresenter;

    private int mCurrentPage = 1;

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
        mHistoryAdapter = new AdapterHistory(getActivity());
        mBinding.historyContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.historyContainer.setAdapter(mHistoryAdapter);

        getHistoryPresenter().get(mSwitch.get_bsid(),
                mViewCalendar.get(Calendar.YEAR),
                mViewCalendar.get(Calendar.MONTH)+1,
                mViewCalendar.get(Calendar.DAY_OF_MONTH),
                1);
    }

    private HistoryPresenter getHistoryPresenter() {
        if(mHistoryPresenter == null) {
            mHistoryPresenter = new HistoryPresenterImpl(getActivity(), this, mHistoryAdapter);
        }
        return mHistoryPresenter;
    }

    public void onBtnClick(View view) {
        if(view.getId() == R.id.history_txt_date) {

            Calendar now = Calendar.getInstance();
            com.wdullaer.materialdatetimepicker.date.DatePickerDialog datepickerDialog =
                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                            FragmentSettingHistory.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
            datepickerDialog.setMaxDate(now);
            datepickerDialog.show(getActivity().getFragmentManager(), "test");



//            new DatePickerDialog(getActivity(),
//                    (view1, year, month, dayOfMonth) -> {
//                        mViewCalendar.set(year, month, dayOfMonth);
//                        getHistoryPresenter().get(mSwitch.get_bsid(), year, month+1, dayOfMonth, 1);
//                        mBinding.historyTxtDate.setText(mSimpleDateFormat.format(mViewCalendar.getTime()));
//                    },
//                    mViewCalendar.get(Calendar.YEAR),
//                    mViewCalendar.get(Calendar.MONTH),
//                    mViewCalendar.get(Calendar.DAY_OF_MONTH))
//                    .show();
        }
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mViewCalendar.set(year, monthOfYear, dayOfMonth);
        mBinding.historyTxtDate.setText(mSimpleDateFormat.format(mViewCalendar.getTime()));
        getHistoryPresenter().get(mSwitch.get_bsid(), year, monthOfYear+1, dayOfMonth, 1);
    }
}
