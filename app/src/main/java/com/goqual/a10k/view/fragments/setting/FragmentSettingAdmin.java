package com.goqual.a10k.view.fragments.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentSettingAdminBinding;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.model.entity.User;
import com.goqual.a10k.presenter.UserPresenter;
import com.goqual.a10k.presenter.impl.UserPresenterImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.event.EventToolbarClick;
import com.goqual.a10k.util.event.RxBus;
import com.goqual.a10k.view.adapters.AdapterUser;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.interfaces.IToolbarClickListener;

/**
 * Created by hanwool on 2017. 2. 28..
 */

public class FragmentSettingAdmin extends BaseFragment<FragmentSettingAdminBinding>
implements UserPresenter.View<User>, IToolbarClickListener {
    public static final String TAG = FragmentSettingAdmin.class.getSimpleName();


    public static final String EXTRA_SWITCH = "EXTRA_SWITCH";
    private Switch mSwitch;

    private UserPresenter mUserPresenter;
    private AdapterUser mUserAdapter;
    private User mAdminUser;

    public static FragmentSettingAdmin newInstance(Switch item) {

        Bundle args = new Bundle();

        FragmentSettingAdmin fragment = new FragmentSettingAdmin();
        args.putParcelable(EXTRA_SWITCH, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting_admin;
    }

    @Override
    public String getTitle() {
        return getString(R.string.tab_title_admin);
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
        loadingStart();
        getUserAdapter().clear();
        getUserPresenter().loadItems(mSwitch.get_bsid());
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e(TAG, e.getMessage(), e);
    }

    @Override
    public void onLoadComplete() {
        loadingStop();
        checkIAmAdmin();
    }

    @Override
    public void addItem(User item) {
        LogUtil.d(TAG, "ITEM::" + item);
        if(!item.isadmin()) {
            getUserAdapter().addItem(item);
        }
        else {
            mAdminUser = item;
            mBinding.setAdminUser(mAdminUser);
        }
    }

    @Override
    public void handleChangeAdmin(int position) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mSwitch = getArguments().getParcelable(EXTRA_SWITCH);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView(){
        mBinding.setFragment(this);
        mBinding.adminUserContainer.setAdapter(getUserAdapter());
        mBinding.adminUserContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        refresh();
    }

    private void checkIAmAdmin() {
        String myAuthKey = PreferenceHelper.getInstance(getActivity()).getStringValue(getString(R.string.arg_user_token), "");
        if(mAdminUser.getmAuthKey().equals(myAuthKey)) {
            RxBus.getInstance().send(new EventToolbarClick(STATUS.EDIT));
        }
        else {
            RxBus.getInstance().send(new EventToolbarClick(STATUS.HIDE));
        }
    }

    private UserPresenter getUserPresenter() {
        if(mUserPresenter == null) {
            mUserPresenter = new UserPresenterImpl(getActivity(), this, getUserAdapter());
        }
        return mUserPresenter;
    }

    private AdapterUser getUserAdapter() {
        if(mUserAdapter == null) {
            mUserAdapter = new AdapterUser(getActivity());
            mUserAdapter.setOnRecyclerItemClickListener((viewId, position) -> {

            });
        }
        return mUserAdapter;
    }

    public void onBtnClick(View view) {

    }

    @Override
    public void onClickEdit(STATUS status) {

    }
}
