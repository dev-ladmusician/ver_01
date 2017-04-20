package com.goqual.a10k.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentMainNotiBinding;
import com.goqual.a10k.model.entity.NotiWrap;
import com.goqual.a10k.presenter.NotiPresenter;
import com.goqual.a10k.presenter.impl.NotiPresenterImpl;
import com.goqual.a10k.util.ResourceUtil;
import com.goqual.a10k.view.adapters.AdapterNoti;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.dialog.CustomDialog;
import com.goqual.a10k.view.interfaces.IMainInviteActivityInteraction;
import com.goqual.a10k.view.interfaces.IPaginationPage;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public class FragmentMainNoti extends BaseFragment<FragmentMainNotiBinding>
    implements NotiPresenter.View<NotiWrap>, IPaginationPage {

    public static final String TAG = FragmentMainNoti.class.getSimpleName();
    private NotiPresenter mPresenter;
    private AdapterNoti mAdapter;

    private int mCurrentPage = 1;
    private int mLastPage = 1;

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
        loadingStop();
        getAdapter().refresh();
    }

    @Override
    public void onError(Throwable e) {
        CustomDialog dialog = new CustomDialog(getActivity());
        dialog.isEditable(false)
                .setTitleText(R.string.noti_load_err_title)
                .setMessageText(R.string.noti_load_err_content)
                .setNegativeButton(false)
                .setPositiveButton(getString(R.string.common_ok), (dia, id) -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    @Override
    public void onErrorInvite() {
        CustomDialog dialog = new CustomDialog(getActivity());
        dialog.isEditable(false)
                .setTitleText(R.string.noti_invite_err_title)
                .setMessageText(R.string.noti_invite_err_content)
                .setNegativeButton(false)
                .setPositiveButton(getString(R.string.common_ok), (dia, id) -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    @Override
    public void addItem(NotiWrap item) {
        getAdapter().addItem(item);
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

        loadItems();

        mBinding.refresh.setColorSchemeColors(ResourceUtil.getColor(getActivity(), R.color.identity_02));
        mBinding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    public void loadItems() {
        loadingStart();
        getNotiPresenter().loadItems(mCurrentPage);
    }

    @Override
    public void onSuccessInvite() {
        ((IMainInviteActivityInteraction)getActivity()).addSwitchForInvite();
    }

    private NotiPresenter getNotiPresenter() {
        if(mPresenter == null) {
            mPresenter = new NotiPresenterImpl(getActivity(), this, getAdapter());
        }
        return mPresenter;
    }

    private AdapterNoti getAdapter() {
        if(mAdapter == null) {
            mAdapter = new AdapterNoti(getActivity(), this);
            mAdapter.setOnRecyclerItemClickListener((viewId, position) -> {
                NotiWrap noti = getAdapter().getItem(position);

                if (noti.get_typeid() == getResources().getInteger(R.integer.notitype_invite)) {
                    CustomDialog dialog = new CustomDialog(getActivity());
                    dialog.isEditable(false)
                            .setTitleText(R.string.noti_invite_title)
                            .setMessageText(R.string.noti_invite_content)
                            .setNegativeButton(getString(R.string.common_cancel), (dia, id) -> {
                                dialog.dismiss();
                            })
                            .setPositiveButton(getString(R.string.common_allow), (dia, id) -> {
                                getNotiPresenter().acceptInvite(
                                        getAdapter().getItem(position).get_bsid());
                                getAdapter().deleteItem(position);
                                getAdapter().refresh();
                                dialog.dismiss();
                            });
                    dialog.show();
                } else if (noti.get_typeid() == getResources().getInteger(R.integer.notitype_app_update)) {

                }
            });
        }
        return mAdapter;
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
}
