package com.goqual.a10k.view.fragments.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentSettingAdminBinding;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.model.entity.User;
import com.goqual.a10k.presenter.UserPresenter;
import com.goqual.a10k.presenter.impl.UserPresenterImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.event.EventSwitchEdit;
import com.goqual.a10k.util.event.EventToolbarClick;
import com.goqual.a10k.util.event.RxBus;
import com.goqual.a10k.view.activities.ActivityInviteUser;
import com.goqual.a10k.view.adapters.AdapterUser;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.interfaces.ISettingInteraction;
import com.goqual.a10k.view.interfaces.IToolbarClickListener;

import rx.functions.Action1;

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

    public static FragmentSettingAdmin newInstance(int item) {

        Bundle args = new Bundle();

        FragmentSettingAdmin fragment = new FragmentSettingAdmin();
        args.putInt(EXTRA_SWITCH, item);
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
        LogUtil.d(TAG, "USER::" + item);
        if(!item.isadmin()) {
            getUserAdapter().addItem(item);
            getUserAdapter().notifyDataSetChanged();
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
            mSwitch = SwitchManager.getInstance().getItem(getArguments().getInt(EXTRA_SWITCH));
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
        subEvent();
    }

    private void initView(){
        mBinding.setFragment(this);
        mBinding.adminUserContainer.setAdapter(getUserAdapter());
        mBinding.adminUserContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        refresh();
    }

    private void checkIAmAdmin() {
        String myPhoneNum = PreferenceHelper.getInstance(getActivity()).getStringValue(getString(R.string.arg_user_num), "");
        if(mAdminUser != null) {
            if (mAdminUser.getNum().equals(myPhoneNum)) {
                ((ISettingInteraction)getActivity()).setAdmin(true);
                RxBus.getInstance().send(new EventToolbarClick(STATE.DONE));
            } else {
                ((ISettingInteraction)getActivity()).setAdmin(false);
                RxBus.getInstance().send(new EventToolbarClick(STATE.HIDE));
            }
        }
        else {
            ((ISettingInteraction)getActivity()).setAdmin(false);
            RxBus.getInstance().send(new EventToolbarClick(STATE.HIDE));
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

    private void subEvent() {
        RxBus.getInstance().toObserverable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if(event instanceof EventSwitchEdit) {
                            LogUtil.d(TAG, "event?"+((EventSwitchEdit) event).getSTATE());
                            if(((EventSwitchEdit) event).getSTATE() == STATE.EDIT) {

                            }
                        }
                    }
                });
    }

    public void onBtnClick(View view) {
        if(view.getId() == R.id.admin_add_user_in_exist_items) {
            startActivity(new Intent(getActivity(), ActivityInviteUser.class));
        }
    }

    @Override
    public void onClickEdit(STATE STATE) {

    }
}
