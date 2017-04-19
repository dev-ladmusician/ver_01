package com.goqual.a10k.view.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentMainSwitchContainerBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.presenter.SocketManager;
import com.goqual.a10k.presenter.SwitchPresenter;
import com.goqual.a10k.presenter.impl.SocketManagerImpl;
import com.goqual.a10k.presenter.impl.SwitchPresenterImpl;
import com.goqual.a10k.util.HttpResponseCode;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.activities.ActivityPhoneAuth;
import com.goqual.a10k.view.adapters.AdapterSwitchContainer;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.dialog.CustomDialog;
import com.goqual.a10k.view.interfaces.IActivityInteraction;
import com.goqual.a10k.view.interfaces.IFragmentInteraction;
import com.goqual.a10k.view.interfaces.IMainActivityInteraction;
import com.goqual.a10k.view.interfaces.ISwitchInteraction;
import com.goqual.a10k.view.interfaces.ISwitchOperationListener;
import com.goqual.a10k.view.interfaces.ISwitchRefreshListener;
import com.goqual.a10k.view.interfaces.IToolbarClickListener;
import com.goqual.a10k.view.interfaces.IToolbarInteraction;

import java.util.List;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public class FragmentMainSwitchContainer extends BaseFragment<FragmentMainSwitchContainerBinding>
        implements SwitchPresenter.View<Switch>, ISwitchInteraction, IToolbarClickListener,
        ISwitchOperationListener, SocketManager.View {
    private static final String TAG = FragmentMainSwitchContainer.class.getSimpleName();

    private String mTitle;
    private AdapterSwitchContainer mPagerAdapter;
    private SwitchPresenterImpl mPresenter;
    private SocketManagerImpl mSocketManager;
    private CustomDialog connectionFailedDialog;
    private STATE mCurrentToolbarState = STATE.DONE;
    private Handler mHandler;
    private int mCurrentPage = 0;

    public static FragmentMainSwitchContainer newInstance() {
        Bundle args = new Bundle();
        FragmentMainSwitchContainer fragment = new FragmentMainSwitchContainer();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void loadingStart() {
        mBinding.loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadingStop() {
        mBinding.loading.setVisibility(View.INVISIBLE);
    }

    /**
     * switch 순서 변경 후 호출
     */
    @Override
    public void onChangeSwitchPosition(List<Switch> switchList) {
        List<Switch> origin = SwitchManager.getInstance().getList();

        // check switchManager list and switchList same
        if(SwitchManager.getInstance().checkSwitchSequenceChange(switchList)) {
            LogUtil.e(TAG, "change switch seq");
            getPresenter().changeSeq(switchList);
        }
    }

    /**
     * 스위치 리스트에서 이름 눌렀을 시 each 페이지로 이동 할 때 호출
     * @param position
     */
    @Override
    public void changeCurrentPage(int position) {
        mCurrentPage = position + 1;
        mBinding.viewPager.setCurrentItem(mCurrentPage);
    }

    /**
     * switchList에서 pull down refresh 했을 때 호출
     */
    @Override
    public void refreshSwitchList() {
        onResume();
    }

    @Override
    public void refresh() {
        loadingStop();
        /**
         * TODO Switch가 많아지면 memory 문제가 발생함
         */
        mBinding.viewPager.setOffscreenPageLimit(SwitchManager.getInstance().getCount());
        mPagerAdapter.refresh();

        // update switch list
        ((ISwitchRefreshListener)mPagerAdapter.getItem(0)).updateSwitches();

        if (((IActivityInteraction)getActivity()).getCurrentPage() ==
                getResources().getInteger(R.integer.frag_main_switch))
            mBinding.viewPager.setCurrentItem(mCurrentPage);

        handleToolbarEdit();
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e(TAG, e.getMessage(), e);
        if(e instanceof HttpException) {
            if(((HttpException)e).code() == HttpResponseCode.ERROR_UNAUTHORIZED) {
                startActivity(new Intent(getActivity(), ActivityPhoneAuth.class));
                getActivity().finish();
            }
        }
    }

    @Override
    public void addItem(Switch item) {
        LogUtil.d(TAG, "ITEM::" + item);
        mPagerAdapter.addItem(item);
        getSocketManager().refreshConnectedRoom();
    }

    @Override
    public void onConnectionError() {
//        if(mHandler != null) {
//            mHandler.post(() -> {
//                if(connectionFailedDialog == null) {
//                    DialogInterface.OnClickListener onClickListener = (dialog, which) -> {
//                        switch (which) {
//                            case Dialog.BUTTON_POSITIVE:
//                                dialog.dismiss();
//                                mSocketManager.tryReconnect();
//                                break;
//                            case Dialog.BUTTON_NEGATIVE:
//                                // finish app;
//                                dialog.dismiss();
//                                connectionFailedDialog = null;
//                                ((IActivityInteraction)getActivity()).finishApp();
//                                break;
//                        }
//                    };
//                    connectionFailedDialog = new CustomDialog(getActivity())
//                            .isEditable(false)
//                            .setNegativeButton(getString(R.string.common_quit), onClickListener)
//                            .setPositiveButton(getString(R.string.common_retry), onClickListener)
//                            .setTitleText(R.string.socket_connection_error_title)
//                            .setMessageText(R.string.socket_connection_error_content);
//                    connectionFailedDialog.show();
//                }
//                else {
//                    if(!connectionFailedDialog.isShowing()) {
//                        connectionFailedDialog.show();
//                    }
//                }
//            });
//        }
    }

    @Override
    public void onConnected() {
        if(connectionFailedDialog != null && connectionFailedDialog.isShowing()) {
            connectionFailedDialog.cancel();
            connectionFailedDialog = null;
        }
        try {
            //Snackbar.make(mBinding.getRoot(), R.string.SOCKET_SUCCESS_CONNECT, Snackbar.LENGTH_SHORT).show();
            //ToastUtil.show(getActivity(), getString(R.string.SOCKET_SUCCESS_CONNECT));
        }
        catch (NullPointerException e) {
            LogUtil.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onServerError(int code) {

    }

    @Override
    public void refreshViews() {
        mCurrentPage = mBinding.viewPager.getCurrentItem();
        mHandler.post(() -> {
            refresh();
        });
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public boolean hasToolbarMenus() {
        return getPagerAdapter().getCount() > 1;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_switch_container;
    }

    @Override
    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.list_empty:
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(networkChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        mHandler = new Handler();
        getSocketManager();

        mPagerAdapter.clear();
        SwitchManager.getInstance().clear();

        getPresenter().loadItems(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getSocketManager().destroySocketConnection();
        getActivity().unregisterReceiver(networkChangeReceiver);
    }

    /**
     * list clear했는데도 불구하고 viewPager가 view를 다시 그려서
     * 항상 존재하는 switchListFragment로 고정
     * loadItems가 끝난 후 현제 focus되있는 frag로 다시 이동
     */
    @Override
    public void onStop() {
        super.onStop();
        mCurrentPage = mBinding.viewPager.getCurrentItem();
        mBinding.viewPager.setCurrentItem(0);
    }

    private void initView() {
        mTitle = getString(R.string.title_switch_list);
        mBinding.viewPager.setAdapter(getPagerAdapter());
        mBinding.viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    private void passToolbarClickEvent(IToolbarClickListener.STATE state) {
        ((IToolbarClickListener)mPagerAdapter.getItem(0)).onClickEdit(state);
    }

    public void setCurrentPage(int currentPage) {
        if(mPagerAdapter != null && mPagerAdapter.getCount()>0) {
            mCurrentPage = currentPage;
            mBinding.viewPager.setCurrentItem(currentPage);
        }
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        int currentPage = 0;
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mTitle = ((BaseFragment)mPagerAdapter.getItem(position)).getTitle();
            mActivityInteraction.setTitle(mTitle);

            ((IFragmentInteraction)mPagerAdapter.getItem(position)).setFragmentVisible(IFragmentInteraction.VISIBLE);
            ((IFragmentInteraction)mPagerAdapter.getItem(currentPage)).setFragmentVisible(IFragmentInteraction.INVISIBLE);
            currentPage = position;

            /**
             * Edit 모드일 때 page를 each로 변경 했을 때
             */
            if (currentPage != 0 && mCurrentToolbarState == STATE.EDIT) {
                mCurrentToolbarState = STATE.DONE;
                ((IToolbarInteraction)getActivity()).setToolbarEdit(mCurrentToolbarState);
                ((IToolbarClickListener)mPagerAdapter.getItem(0)).onClickEdit(mCurrentToolbarState);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onClickEdit(STATE state) {
        mCurrentToolbarState = state;
        ((IToolbarInteraction)getActivity()).setToolbarEdit(mCurrentToolbarState);

        // edit 상황일 시 switch list로 이동
        if (mCurrentToolbarState == STATE.EDIT) {
            mBinding.viewPager.setCurrentItem(0);
            mCurrentPage = 0;
        }

        ((IToolbarClickListener)mPagerAdapter.getItem(0)).onClickEdit(state);
    }

    /**
     * switch operation
     */
    @Override
    public void onSwitchClicked(int position, int btnNumber) {
        Switch item = SwitchManager.getInstance().getItem(position);
        getSocketManager().operationOnOff(item, btnNumber);
    }

    /**
     * switch list에서 switch delete event 받는 곳
     * @param position
     */
    @Override
    public void onSwitchDelete(int position) {
        getPresenter().deleteItem(position);
    }

    @Override
    public void onSwitchRename(int position, String title) {
        getPresenter().rename(position, title);
    }



    @Override
    public void onSuccessRenameSwitch(int position, String title) {
        ((ISwitchRefreshListener)mPagerAdapter.getItem(0)).changeSwitchTitle(position, title);
    }

    @Override
    public void onSuccessDeleteSwitch(int position) {
        // 등록된 스위치가 없으면 edit hide
        handleToolbarEdit();

        mPagerAdapter.deleteItem(position);
        ((ISwitchRefreshListener)mPagerAdapter.getItem(0)).deleteSwitch(position);
    }




    @Override
    public void onSuccessChangeSeq() {
        loadingStop();
        ((ISwitchRefreshListener)mPagerAdapter.getItem(0)).updateSwitches();
    }

    @Override
    public void onErrorChangeSeq(Throwable e) {
        CustomDialog dialog = new CustomDialog(getActivity())
                .isEditable(false)
                .setNegativeButton(false)
                .setPositiveButton(getString(R.string.common_ok), (dia, id) -> {
                    dia.dismiss();
                })
                .setTitleText(R.string.switch_change_seq_err_title)
                .setMessageText(R.string.switch_change_seq_err_content);
        dialog.show();
    }

    @Override
    public void passDeleteEvent(int switchId) {
        // delete switch event를 alarm쪽으로 넘기기
        ((IMainActivityInteraction)getActivity()).deleteSwitchEvent(switchId);
    }

    private void handleToolbarEdit() {
        mCurrentToolbarState = SwitchManager.getInstance().getCount() == 0 ?
                IToolbarClickListener.STATE.HIDE : IToolbarClickListener.STATE.DONE;

        if (((IActivityInteraction)getActivity()).getCurrentPage() ==
                getResources().getInteger(R.integer.frag_main_switch))
            ((IToolbarInteraction)getActivity()).setToolbarEdit(mCurrentToolbarState);
    }

    /**
     * 인터넷 연결이 되어 있는지 확인합니다.
     * @return 인터넷 연결 상태
     */
    private boolean isInternetConnected(){
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        /**
         * 네트워크 변경을 감시합니다.
         * 네트워크 변경이 감지되었을 때 인터넷이 연결된 상태라면 서버와 재접속을 시도합니다.
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {

            LogUtil.d(TAG, "networkChangeReceiver :: isInternetConnected :" + isInternetConnected() );
            if(isInternetConnected()) {
                refresh();
                if(!mSocketManager.isConnected()) {
                    mSocketManager.tryReconnect();
                }
            }
        }
    };







    private SwitchPresenter getPresenter() {
        if(mPresenter == null) {
            mPresenter = new SwitchPresenterImpl(getActivity(), this, mPagerAdapter);
        }
        return mPresenter;
    }

    private SocketManager getSocketManager() {
        if(mSocketManager == null) {
            mSocketManager = new SocketManagerImpl(this, getActivity());
        }
        return mSocketManager;
    }

    private AdapterSwitchContainer getPagerAdapter() {
        if (mPagerAdapter == null)
            mPagerAdapter = new AdapterSwitchContainer(getChildFragmentManager(), getActivity());
        return mPagerAdapter;
    }
}
