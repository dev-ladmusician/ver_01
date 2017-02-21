package com.goqual.a10k.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentMainSwitchListBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.view.adapters.AdapterSwitch;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.interfaces.ISwitchOperationListener;
import com.goqual.a10k.view.interfaces.ISwitchRefreshListener;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public class FragmentMainSwitchList extends BaseFragment<FragmentMainSwitchListBinding>
 implements ISwitchRefreshListener {
    public static final String TAG = FragmentMainSwitchList.class.getSimpleName();

    private AdapterSwitch mAdapter = null;
    private Context mContext = null;

    private ISwitchOperationListener operationListener = null;

    public static FragmentMainSwitchList newInstance() {
        Bundle args = new Bundle();
        FragmentMainSwitchList fragment = new FragmentMainSwitchList();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void updateSwitches() {
        getAdapter().updateItems(SwitchManager.getInstance().getList());
        getAdapter().refresh();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_switch_list;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_switch_list);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        operationListener = (ISwitchOperationListener) getParentFragment();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private AdapterSwitch getAdapter() {
        if (mAdapter == null) {
            mAdapter = new AdapterSwitch(mContext);
        }
        return mAdapter;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        mBinding.switchList.setAdapter(getAdapter());
        mBinding.switchList.setLayoutManager(new LinearLayoutManager(mContext));


        getAdapter().setOnRecyclerItemClickListener((id, position) -> {
            switch (id) {
                case R.id.item_switch_btn_1:
                    operationListener.onSwitchClicked(position, 1);
                    break;
                case R.id.item_switch_btn_2:
                    operationListener.onSwitchClicked(position, 2);
                    break;
                case R.id.item_switch_btn_3:
                    operationListener.onSwitchClicked(position, 3);
                    break;
            }
        });
    }

    private void onItemClick(int position) {

    }
}
