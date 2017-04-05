package com.goqual.a10k.view.fragments.setting;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentSettingNfcBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Nfc;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.model.realm.NfcRealm;
import com.goqual.a10k.presenter.NfcTagPresenter;
import com.goqual.a10k.presenter.impl.NfcTagPresenterImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.activities.ActivityNfcDetect;
import com.goqual.a10k.view.activities.ActivityNfcSetup;
import com.goqual.a10k.view.adapters.AdapterNfc;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.dialog.CustomDialog;
import com.goqual.a10k.view.interfaces.IActivityInteraction;
import com.goqual.a10k.view.interfaces.IPaginationPage;
import com.goqual.a10k.view.interfaces.IToolbarClickListener;

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
    private STATE mCurrentState;
    private int mSwitchPosition;

    private Realm mRealm;

    private int mCurrentPage = 1;
    private int mLastPage = 1;

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
        return true;
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
        mAdapter.refresh();
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e(TAG, e.getMessage(), e);
        ((IActivityInteraction)getActivity()).getPreferenceHelper().put(
                getString(R.string.arg_nfc_is_loaded), false);
    }

    @Override
    public void addItem(Nfc item) {
        //LogUtil.d(TAG, "ITEM::" + item);
        //item.setmIsDeletable(mCurrentState == STATE.EDIT);
        mAdapter.addItem(item);
    }

    @Override
    public void onSuccess() {
        loadingStop();
    }

    @Override
    public void deleteItem(int position) {
        if(mAdapter != null) {
            mAdapter.deleteItem(position);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mSwitchPosition = getArguments().getInt(EXTRA_SWITCH);
            mSwitch = SwitchManager.getInstance().getItem(mSwitchPosition);
            mCurrentState = STATE.DONE;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mRealm.close();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadItems();
    }

    private void initView(){
        mBinding.setFragment(this);
        mBinding.nfcContainer.setAdapter(getAdapter());
        mBinding.nfcContainer.setLayoutManager(new LinearLayoutManager(getActivity()));

        getAdapter().setOnRecyclerItemClickListener((viewId, position) -> {
            switch (viewId) {
                case R.id.item_nfc_delete:
                    CustomDialog customDialog = new CustomDialog(getActivity());
                    DialogInterface.OnClickListener onClickListener = (dialog, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                getPresenter().delete(getAdapter().getItem(position)._nfcid);
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

    private void loadItems() {
        if (((IActivityInteraction)getActivity()).getPreferenceHelper()
                .getBooleanValue(getString(R.string.arg_nfc_is_loaded))) {
            getPresenter().loadItemsInRealm(mCurrentPage);
        } else {
            getPresenter().loadItems(mSwitch.get_bsid(), mCurrentPage);
            ((IActivityInteraction)getActivity()).getPreferenceHelper().put(
                    getString(R.string.arg_nfc_is_loaded), true);
        }
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
            startActivityForResult(request, REQ_REGISTER_NFC);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_REGISTER_NFC) {
            if(resultCode == Activity.RESULT_OK) {
                String nfcTagId = data.getExtras().getString(ActivityNfcSetup.EXTRA_NFC_TAG_ID, null);
                String nfcTagTitle = data.getExtras().getString(ActivityNfcSetup.EXTRA_NFC_TAG_TITLE, null);
                int itemId = data.getExtras().getInt(ActivityNfcSetup.EXTRA_SWITCH);
                Switch item = SwitchManager.getInstance().getItem(itemId);
                if(nfcTagId != null) {
                    LogUtil.e(TAG, "NFC_TAG_REGISTER::tagID: " + nfcTagId);
                    NfcRealm tag = new NfcRealm();
                    tag.set_bsid(item.get_bsid());
                    tag.setTag(nfcTagId);
                    tag.setTitle(nfcTagTitle);
                    tag.setBtn1(item.isBtn1());
                    tag.setBtn2(item.isBtn2());
                    tag.setBtn3(item.isBtn3());
                    getPresenter().add(tag);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * parent activity toolbar click event
     * @param STATE
     */
    @Override
    public void onClickEdit(STATE STATE) {
        LogUtil.d(TAG, "STATE::" + STATE);
        mCurrentState = STATE;
        mAdapter.setItemState(STATE == STATE.EDIT);
    }

    private AdapterNfc getAdapter() {
        if (mAdapter == null)
            mAdapter = new AdapterNfc(getActivity());
        return mAdapter;
    }

    private Realm getRealmInstance() {
        if(mRealm == null)
            mRealm = Realm.getDefaultInstance();
        return mRealm;
    }

    private NfcTagPresenter getPresenter() {
        if(mPresenter == null) {
            mPresenter = new NfcTagPresenterImpl(getActivity(), this, getRealmInstance());
        }
        return mPresenter;
    }
}
