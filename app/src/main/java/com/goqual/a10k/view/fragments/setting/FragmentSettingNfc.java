package com.goqual.a10k.view.fragments.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentSettingNfcBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Nfc;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.presenter.NfcTagPresenter;
import com.goqual.a10k.presenter.impl.NfcTagPresenterImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.ResourceUtil;
import com.goqual.a10k.view.activities.ActivityNfcDetect;
import com.goqual.a10k.view.activities.ActivityNfcSetup;
import com.goqual.a10k.view.adapters.AdapterNfc;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.dialog.CustomDialog;
import com.goqual.a10k.view.interfaces.IActivityInteraction;
import com.goqual.a10k.view.interfaces.IPaginationPage;
import com.goqual.a10k.view.interfaces.IToolbarClickListener;
import com.goqual.a10k.view.interfaces.IToolbarInteraction;

import io.realm.Realm;

/**
 * Created by hanwool on 2017. 2. 28..
 * TODO Edit모드 적용
 */

public class FragmentSettingNfc extends BaseFragment<FragmentSettingNfcBinding>
        implements NfcTagPresenter.View<Nfc>, IToolbarClickListener, IPaginationPage {
    public static final String TAG = FragmentSettingNfc.class.getSimpleName();

    public static final String EXTRA_SWITCH = "EXTRA_SWITCH";

    private static final int REQ_REGISTER_NFC = 133;

    private AdapterNfc mAdapter;
    private NfcTagPresenter mPresenter;
    private Switch mSwitch;
    private int mSwitchPosition;
    private CustomDialog mRenameDialog = null;
    private Realm mRealm;

    private int mCurrentPage = 1;
    private int mLastPage = 1;

    private STATE mCurrentToolbarState = STATE.DONE;

    public static FragmentSettingNfc newInstance(int item) {
        Bundle args = new Bundle();

        FragmentSettingNfc fragment = new FragmentSettingNfc();
        args.putInt(EXTRA_SWITCH, item);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentSettingNfc() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting_nfc;
    }

    @Override
    public String getTitle() {
        return getString(R.string.tab_title_nfc);
    }

    @Override
    public boolean hasToolbarMenus() {
        return getAdapter().getItemCount() > 0;
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
        mBinding.refresh.setRefreshing(false);
        mAdapter.refresh();

        if (((IActivityInteraction)getActivity()).getCurrentPage() ==
                getResources().getInteger(R.integer.frag_switch_setting_nfc))
            setToolbarHandler();
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e(TAG, e.getMessage(), e);
    }

    @Override
    public void addItem(Nfc item) {
        mAdapter.addItem(item);
    }

    @Override
    public void onSuccess() {
        //loadingStop();
    }

    @Override
    public void deleteItem(int position) {
        mAdapter.deleteItem(position);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mSwitchPosition = getArguments().getInt(EXTRA_SWITCH);
            mSwitch = SwitchManager.getInstance().getItem(mSwitchPosition);
            mCurrentToolbarState = STATE.DONE;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        getRealmInstance().close();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        getAdapter().clear();
        loadItems();
    }

    private void initView(){
        mBinding.setFragment(this);
        mBinding.nfcContainer.setAdapter(getAdapter());
        mBinding.nfcContainer.setLayoutManager(new LinearLayoutManager(getActivity()));

        getAdapter().setOnRecyclerItemClickListener((viewId, position) -> {
            switch (viewId) {
                case R.id.item_nfc_container:
                    LogUtil.e(TAG, "item click");
                    Intent setupReq = new Intent(getContext(), ActivityNfcSetup.class);
                    setupReq.putExtra(ActivityNfcSetup.EXTRA_NFC_TAG_ID, getAdapter().getItem(position).getTag());
                    setupReq.putExtra(ActivityNfcSetup.EXTRA_SWITCH, mSwitchPosition);
                    startActivity(setupReq);
                    break;
                case R.id.item_nfc_delete:
                    CustomDialog customDialog = new CustomDialog(getActivity());
                    DialogInterface.OnClickListener onClickListener = (dialog, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                getPresenter().delete(position);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                        dialog.dismiss();
                    };

                    customDialog.isEditable(false)
                            .setTitleText(R.string.nfc_delete_title)
                            .setMessageText(R.string.nfc_delete_content)
                            .setPositiveButton(getString(R.string.common_delete), onClickListener)
                            .setNegativeButton(getString(R.string.common_cancel), onClickListener)
                            .show();
                    break;
                case R.id.item_switch_rename:
                    getRenameDialog().isEditable(true)
                            .setTitleText(R.string.nfc_rename_update_title)
                            .setEditTextHint(getAdapter().getItem(position).getTitle())
                            .setPositiveButton(getString(R.string.common_ok), (dialog, which)-> {
                                String title = getRenameDialog().getEditTextMessage();
                                if (title.length() != 0) {
                                    getAdapter().getItem(position).setTitle(title);
                                    getPresenter().update(getAdapter().getItem(position), position);
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

        mBinding.refresh.setColorSchemeColors(ResourceUtil.getColor(getActivity(), R.color.identitiy_02));
        mBinding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAdapter().clear();
                setPage(1);
                loadItems();
            }
        });
    }

    @Override
    public void setPage(int page) {
        mCurrentPage = page;
    }

    @Override
    public void setLastPage(int lastPage) {
        mLastPage = lastPage;
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
        getPresenter().loadItems(mSwitch.get_bsid(), mCurrentPage);

        /**
         * TODO realm database 활용 :: pagination
         */
//        if (((IActivityInteraction)getActivity()).getPreferenceHelper()
//                .getBooleanValue(getString(R.string.arg_nfc_is_loaded))) {
//            getPresenter().loadItemsInRealm(mCurrentPage);
//        } else {
//            getPresenter().loadItems(mSwitch.get_bsid(), mCurrentPage);
//            ((IActivityInteraction)getActivity()).getPreferenceHelper().put(
//                    getString(R.string.arg_nfc_is_loaded), true);
//        }
    }

    /**
     * ui view click event
     * @param view
     */
    public void onBtnClick(View view) {
        if(view.getId() == R.id.nfc_add_container) {
            Intent request = new Intent(getActivity(), ActivityNfcDetect.class);
            request.setAction(ActivityNfcDetect.ACTION_REGISTER_TAG);
            request.putExtra(ActivityNfcDetect.EXTRA_SWITCH, mSwitchPosition);
            startActivity(request);
        }
    }

    /**
     * parent activity toolbar click event
     * @param state
     */
    @Override
    public void onClickEdit(STATE state) {
        LogUtil.d(TAG, "STATE::" + state);

        mCurrentToolbarState = state;
        mAdapter.setItemState(state == STATE.EDIT);
        ((IToolbarInteraction)getActivity()).setToolbarEdit(mCurrentToolbarState);
    }

    private void setToolbarHandler() {
        mAdapter.setItemState(false);
        if (getAdapter().getItemCount() == 0) {
            ((IToolbarInteraction)getActivity()).setToolbarEdit(STATE.HIDE);
        } else {
            ((IToolbarInteraction)getActivity()).setToolbarEdit(STATE.DONE);
        }
    }

    private AdapterNfc getAdapter() {
        if (mAdapter == null)
            mAdapter = new AdapterNfc(getActivity(), this);
        return mAdapter;
    }

    private Realm getRealmInstance() {
        if(mRealm == null)
            mRealm = Realm.getDefaultInstance();
        return mRealm;
    }

    private NfcTagPresenter getPresenter() {
        if(mPresenter == null) {
            mPresenter = new NfcTagPresenterImpl(getActivity(), this, getAdapter());
        }
        return mPresenter;
    }

    private CustomDialog getRenameDialog() {
        if (mRenameDialog == null) {
            mRenameDialog = new CustomDialog(getActivity());
        }
        return mRenameDialog;
    }
}
