package com.goqual.a10k.view.fragments.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentSettingNfcBinding;
import com.goqual.a10k.model.entity.Nfc;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.presenter.NfcTagPresenter;
import com.goqual.a10k.presenter.impl.NfcTagPresenterImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.activities.ActivityNfcDetect;
import com.goqual.a10k.view.activities.ActivityNfcSetup;
import com.goqual.a10k.view.adapters.AdapterNfc;
import com.goqual.a10k.view.base.BaseFragment;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by hanwool on 2017. 2. 28..
 * TODO Edit모드 적용
 */

public class FragmentSettingNfc extends BaseFragment<FragmentSettingNfcBinding>
        implements NfcTagPresenter.View<Nfc>{
    public static final String TAG = FragmentSettingNfc.class.getSimpleName();

    public static final String EXTRA_SWITCH = "EXTRA_SWITCH";

    private static final int REQ_REGISTER_NFC = 133;

    private AdapterNfc mAdapter;
    private NfcTagPresenter mPresenter;
    private Switch mSwitch;

    public static FragmentSettingNfc newInstance(Switch item) {

        Bundle args = new Bundle();

        FragmentSettingNfc fragment = new FragmentSettingNfc();
        args.putParcelable(EXTRA_SWITCH, item);
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
        mAdapter.addItem(item);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccess() {

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
            mSwitch = getArguments().getParcelable(EXTRA_SWITCH);
        }
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
        getAdapter().setOnRecyclerItemClickListener((viewId, position) -> {

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().loadItems(mSwitch.get_bsid());
    }

    private AdapterNfc getAdapter() {
        if(mAdapter == null) {
            mAdapter = new AdapterNfc(getActivity());
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
            request.putExtra(ActivityNfcDetect.EXTRA_SWITCH, mSwitch);
            startActivityForResult(request, REQ_REGISTER_NFC);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_REGISTER_NFC) {
            if(resultCode == Activity.RESULT_OK) {
                String nfcTagId = data.getExtras().getString(ActivityNfcSetup.EXTRA_NFC_TAG_ID, null);
                String nfcTagTitle = data.getExtras().getString(ActivityNfcSetup.EXTRA_NFC_TAG_TITLE, null);
                Switch item = data.getExtras().getParcelable(ActivityNfcSetup.EXTRA_SWITCH);
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
}
