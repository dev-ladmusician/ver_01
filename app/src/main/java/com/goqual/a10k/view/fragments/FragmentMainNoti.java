package com.goqual.a10k.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentMainNotiBinding;
import com.goqual.a10k.databinding.FragmentMainSwitchListBinding;
import com.goqual.a10k.model.entity.NotiWrap;
import com.goqual.a10k.model.realm.Noti;
import com.goqual.a10k.presenter.NotiPresenter;
import com.goqual.a10k.presenter.impl.NotiPresenterImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.adapters.AdapterAlarm;
import com.goqual.a10k.view.adapters.AdapterNoti;
import com.goqual.a10k.view.base.BaseFragment;

import io.realm.Realm;
import io.realm.exceptions.RealmException;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public class FragmentMainNoti extends BaseFragment<FragmentMainNotiBinding>
implements NotiPresenter.View<NotiWrap>{
    public static final String TAG = FragmentMainNoti.class.getSimpleName();
    private NotiPresenter mPresenter;
    private AdapterNoti mAdapter;

    public static FragmentMainNoti newInstance() {
        Bundle args = new Bundle();
        FragmentMainNoti fragment = new FragmentMainNoti();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_noti;
    }

    @Override
    public void loadingStart() {
        mBinding.loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadingStop() {
        mBinding.loading.setVisibility(View.GONE);
        mBinding.refresh.setRefreshing(false);
    }

    @Override
    public void refresh() {
        getAdapter().clear();
        getNotiPresenter().loadItems(1);
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e(TAG, e.getMessage(), e);
    }

    @Override
    public void addItem(NotiWrap item) {
        LogUtil.d(TAG, item.toString());
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                realm1.copyToRealm(item.getRealmObject());
            });
        }
        catch (Exception e) {
            LogUtil.e(TAG, e.getMessage(), e);
        }
        finally {
            realm.close();
        }
    }

    @Override
    public void onBtnClick(View view) {

    }

    @Override
    public String getTitle() {
        return getString(R.string.title_noti);
    }

    @Override
    public boolean hasToolbarMenus() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initiateView();
    }

    private void initiateView() {
        mBinding.setFragment(this);
        mBinding.notiContainer.setAdapter(getAdapter());
        mBinding.notiContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        // TODO: page
        getNotiPresenter().loadItems(1);
        mBinding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private NotiPresenter getNotiPresenter() {
        if(mPresenter == null) {
            mPresenter = new NotiPresenterImpl(getActivity(), this, getAdapter());
        }
        return mPresenter;
    }

    private AdapterNoti getAdapter() {
        if(mAdapter == null) {
            mAdapter = new AdapterNoti(getActivity());
            mAdapter.setOnRecyclerItemClickListener((viewId, position) -> {
                NotiWrap noti = getAdapter().getItem(position);
                noti.get_typeid();
            });
        }
        return mAdapter;
    }
}
