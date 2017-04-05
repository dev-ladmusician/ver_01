package com.goqual.a10k.view.fragments.switches;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentMainSwitchListBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.activities.ActivitySwitchConnection;
import com.goqual.a10k.view.adapters.AdapterSwitch;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.dialog.CustomDialog;
import com.goqual.a10k.view.interfaces.ISwitchOperationListener;
import com.goqual.a10k.view.interfaces.ISwitchRefreshListener;
import com.goqual.a10k.view.interfaces.IToolbarClickListener;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public class FragmentMainSwitchList extends BaseFragment<FragmentMainSwitchListBinding>
        implements ISwitchRefreshListener, IToolbarClickListener {
    public static final String TAG = FragmentMainSwitchList.class.getSimpleName();

    private AdapterSwitch mAdapter = null;
    private Context mContext = null;
    private CustomDialog mDeleteDialog = null;
    private CustomDialog mRenameDialog = null;
    private ISwitchOperationListener operationListener = null;

    public static FragmentMainSwitchList newInstance() {
        Bundle args = new Bundle();
        FragmentMainSwitchList fragment = new FragmentMainSwitchList();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClickEdit(STATE state) {
        getAdapter().setItemState(state == STATE.EDIT);
    }

    @Override
    public void updateSwitches() {
        getAdapter().clear();
        getAdapter().updateItems(SwitchManager.getInstance().getList());
        getAdapter().refresh();

        mBinding.setSwitchList(SwitchManager.getInstance());
    }

    @Override
    public void changeSwitchTitle(int position, String title) {
        getAdapter().updateItem(position, SwitchManager.getInstance().getItem(position));
        getAdapter().refresh();
    }

    @Override
    public void deleteSwitch(int position) {
        getAdapter().deleteItem(position);
        getAdapter().refresh();
        
        mBinding.setSwitchList(SwitchManager.getInstance());
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
    public boolean hasToolbarMenus() {
        return false;
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

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.list_empty:
                startActivity(new Intent(mContext, ActivitySwitchConnection.class));
                break;
        }
    }

    private void initView() {
        mBinding.setFragment(this);
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
                case R.id.item_switch_delete:
                    LogUtil.e(TAG, "DELETE");
                    getDeleteDialog().isEditable(false)
                            .setTitleText(R.string.switch_delete_title)
                            .setMessageText(R.string.switch_delete_content)
                            .setPositiveButton(getString(R.string.common_delete), ((dialog, i) -> {
                                ((ISwitchOperationListener) getParentFragment()).onSwitchDelete(
                                        position);
                                getDeleteDialog().dismiss();
                            }))
                            .setNegativeButton(getString(R.string.common_cancel), ((dialog, i) -> {
                                getDeleteDialog().dismiss();
                            }))
                            .show();

                    break;
                case R.id.item_switch_rename:
                    LogUtil.e(TAG, "RENAME");
                    getRenameDialog().isEditable(true)
                            .setTitleText(R.string.switch_rename_title)
                            .setEditTextHint(R.string.switch_rename_hit)
                            .setPositiveButton(getString(R.string.common_ok), (dialog, which)-> {
                                if (getRenameDialog().getEditTextMessage().length() != 0) {
                                    ((ISwitchOperationListener) getParentFragment()).onSwitchRename(
                                            position, getRenameDialog().getEditTextMessage());
                                    getRenameDialog().dismiss();
                                }

                            })
                            .setNegativeButton(getString(R.string.common_cancel), (dialog, which) -> {
                                getRenameDialog().setEditTextMessage("");
                                getRenameDialog().dismiss();
                            })
                            .show();
                    break;
            }
        });
    }

    private void onItemClick(int position) {

    }

    private CustomDialog getRenameDialog() {
        if (mRenameDialog == null) {
            mRenameDialog = new CustomDialog(getActivity());
        }
        return mRenameDialog;
    }

    private CustomDialog getDeleteDialog() {
        if (mDeleteDialog == null) {
            mDeleteDialog = new CustomDialog(getActivity());
        }
        return mDeleteDialog;
    }
}
