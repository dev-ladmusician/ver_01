package com.goqual.a10k.view.fragments.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentSettingAdminBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.model.entity.User;
import com.goqual.a10k.presenter.UserPresenter;
import com.goqual.a10k.presenter.impl.UserPresenterImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.activities.ActivityInviteUser;
import com.goqual.a10k.view.adapters.AdapterUser;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.dialog.CustomDialog;
import com.goqual.a10k.view.dialog.CustomListDialog;
import com.goqual.a10k.view.interfaces.IToolbarClickListener;
import com.goqual.a10k.view.interfaces.IToolbarInteraction;

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
    private CustomListDialog mListDialog = null;

    private STATE mCurrentToolbarState = STATE.DONE;

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

    /**
     * admin일 경우 Edit 기능 사용
     * @return admin 유무 (Edit 기능 사용 유무)
     */
    @Override
    public boolean hasToolbarMenus() {
        if (getUserAdapter().getItemCount() == 0) return false;
        else return mSwitch.isadmin();
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
        loadingStop();
        getUserAdapter().refresh();

        setToolbarHideNoneConnectedUser();
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e(TAG, e.getMessage(), e);
    }

    @Override
    public void addItem(User item) {
        LogUtil.d(TAG, "USER::" + item);
        if(!item.isadmin()) {
            getUserAdapter().addItem(item);
        }
        else {
            getUserAdapter().setAdmin(item);
            mBinding.setAdminUser(item);
        }
    }

    @Override
    public void handleChangeAdmin(int position) {
        try {
            User admin = getUserAdapter().getAdmin().clone();
            admin.setIsadmin(false);

            getUserAdapter().getItem(position).setIsadmin(true);
            getUserAdapter().setAdmin(getUserAdapter().getItem(position));
            getUserAdapter().deleteItem(position);
            getUserAdapter().addItem(admin);
            getUserAdapter().refresh();
            mBinding.setAdminUser(getUserAdapter().getAdmin());
            mSwitch.setIsadmin(false);
        } catch (Exception e) {

        }

        //((IToolbarInteraction)getActivity()).setToolbarEdit(STATE.HIDE);
        onClickEdit(STATE.DONE);
        getListDialog().dismiss();
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

        getUserAdapter().clear();
        getUserPresenter().loadItems(mSwitch.get_bsid());

        getUserAdapter().setOnRecyclerItemClickListener((id, position) -> {
            switch (id) {
                case R.id.item_user_delete:
                    onClickDeleteItem(position);
                    break;
            }
        });
    }

    private void initView(){
        mBinding.setFragment(this);
        mBinding.adminUserContainer.setAdapter(getUserAdapter());
        mBinding.adminUserContainer.setLayoutManager(new LinearLayoutManager(getActivity()));

//        if (mSwitch.isadmin) ((IToolbarInteraction)getActivity()).setToolbarEdit(STATE.DONE);
//        else ((IToolbarInteraction)getActivity()).setToolbarEdit(STATE.HIDE);
    }

    private void onClickDeleteItem(int position) {
        CustomDialog dialog = new CustomDialog(getContext());
        DialogInterface.OnClickListener onClickListener = (dialog1, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //endSetting(dialog.getEditTextMessage());
                    getUserPresenter().delete(position);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
            dialog.dismiss();
        };

        dialog
            .setTitleText(R.string.switch_setting_admin_delete_user_title)
            .setMessageText(getUserAdapter().getItem(position).getNum() + getString(R.string.switch_setting_admin_delete_user_content))
            .isEditable(false)
            .setPositiveButton(getString(R.string.common_delete), onClickListener)
            .setNegativeButton(getString(R.string.common_cancel), onClickListener)
            .show();
    }

    private void onClickChangeAdmin() {
        DialogInterface.OnClickListener onClickListener = (dialog1, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    LogUtil.e(TAG, "SELECTED POSITION :: " + getListDialog().getSelectedPosition());
                    if (getListDialog().getSelectedPosition() != -1)
                        getUserPresenter().changeAdmin(getListDialog().getSelectedPosition());
                    else
                        Toast.makeText(getContext(),
                                R.string.switch_setting_admin_change_admin_not_selected, Toast.LENGTH_SHORT).show();

                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    getListDialog().dismiss();
                    break;
            }
        };

        getListDialog().setTitleText(R.string.swtich_change_admin_title)
                .setPositiveButton(getString(R.string.common_save), onClickListener)
                .setNegativeButton(getString(R.string.common_cancel), onClickListener)
                .show();
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
        if(view.getId() == R.id.admin_add_user_in_exist_items) {
            startActivity(new Intent(getActivity(), ActivityInviteUser.class));
        } else if (view.getId() == R.id.admin_change_admin) {
            onClickChangeAdmin();
        }
    }

    @Override
    public void onClickEdit(STATE state) {
        mCurrentToolbarState = state;
        getUserAdapter().setDeletable(state == STATE.EDIT);

        if(mCurrentToolbarState == STATE.DONE) {
            mBinding.adminChangeAdmin.setVisibility(View.GONE);
        } else {
            mBinding.adminChangeAdmin.setVisibility(View.VISIBLE);
        }
        ((IToolbarInteraction)getActivity()).setToolbarEdit(mCurrentToolbarState);
    }

    private CustomListDialog getListDialog() {
        if (mListDialog == null)
            mListDialog = new CustomListDialog(getContext(), getUserAdapter().getmItemList());

        return mListDialog;
    }

    private void setToolbarHideNoneConnectedUser() {
        if (getUserAdapter().getItemCount() == 0) {
            ((IToolbarInteraction)getActivity()).setToolbarEdit(STATE.HIDE);
            mBinding.adminChangeAdmin.setVisibility(View.GONE);
        } else {
            if (mSwitch.isadmin) ((IToolbarInteraction)getActivity()).setToolbarEdit(STATE.DONE);
            else ((IToolbarInteraction)getActivity()).setToolbarEdit(STATE.HIDE);
        }
    }
}
