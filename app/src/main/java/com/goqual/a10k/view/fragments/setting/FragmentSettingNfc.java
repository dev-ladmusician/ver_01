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
import com.goqual.a10k.model.realm.Nfc;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.presenter.NfcTagPresenter;
import com.goqual.a10k.presenter.impl.NfcTagPresenterImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.activities.ActivityNfcDetect;
import com.goqual.a10k.view.activities.ActivityNfcSetup;
import com.goqual.a10k.view.adapters.AdapterNfc;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.dialog.CustomDialog;
import com.goqual.a10k.view.interfaces.IToolbarClickListener;

import io.realm.Realm;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by hanwool on 2017. 2. 28..
 * TODO Edit모드 적용
 */

public class FragmentSettingNfc extends BaseFragment<FragmentSettingNfcBinding>
        implements NfcTagPresenter.View<Nfc>, IToolbarClickListener{
    public static final String TAG = FragmentSettingNfc.class.getSimpleName();

    public static final String EXTRA_SWITCH = "EXTRA_SWITCH";

    private static final int REQ_REGISTER_NFC = 133;

    private AdapterNfc mAdapter;
    private NfcTagPresenter mPresenter;
    private Switch mSwitch;
    private STATUS mCurrentState;
    private int mSwitchPosition;

    private Realm realm;

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
    public void loadingStart() {
        if(mAdapter != null) {
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
        }
        mBinding.loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadingStop() {
        mBinding.loading.setVisibility(View.GONE);
    }

    @Override
    public void refresh() {
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e(TAG, e.getMessage(), e);
        if(e instanceof HttpException) {
            LogUtil.e(TAG, String.format("HTTPE::STATUS: %d, MESSAGE: %s", ((HttpException) e).code(), ((HttpException) e).message()));
        }
    }

    @Override
    public void addItem(Nfc item) {
        LogUtil.d(TAG, "ITEM::" + item);
        item.setmIsDeletable(mCurrentState == STATUS.EDIT);
        mAdapter.addItem(item);
        mAdapter.notifyDataSetChanged();
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
            mCurrentState = STATUS.DONE;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(realm != null) {
            realm = Realm.getDefaultInstance();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView(){
        mBinding.setFragment(this);
        mBinding.nfcContainer.setAdapter(getAdapter());
        mBinding.nfcContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadingStop();
        getAdapter().setOnRecyclerItemClickListener((viewId, position) -> {
            LogUtil.d(TAG, "setOnRecyclerItemClickListener::viewID:"+viewId+" position:" + position);
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
                    .isPositiveButton(true, getString(R.string.common_delete), onClickListener)
                    .isNegativeButtonEnable(true, getString(R.string.common_cancel), onClickListener)
                    .show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private AdapterNfc getAdapter() {
        if(mAdapter == null) {
            if(realm == null) {
                realm = Realm.getDefaultInstance();
            }
            mAdapter = new AdapterNfc(getActivity(), realm.where(Nfc.class).findAll());
        }
        return mAdapter;
    }

    private NfcTagPresenter getPresenter() {
        if(mPresenter == null) {
            mPresenter = new NfcTagPresenterImpl(getActivity(), this);
        }
        return mPresenter;
    }

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
                    Nfc tag = new Nfc();
                    tag.set_bsid(item.get_bsid());
                    tag.setTag(nfcTagId);
                    tag.setTitle(nfcTagTitle);
                    tag.setBtn1(item.getBtn1());
                    tag.setBtn2(item.getBtn2());
                    tag.setBtn3(item.getBtn3());
                    getPresenter().add(tag);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClickEdit(STATUS status) {
        LogUtil.d(TAG, "STATE::" + status);
        mCurrentState = status;
        mAdapter.setItemState(status == STATUS.EDIT);
    }
}
