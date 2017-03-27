package com.goqual.a10k.view.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivityInviteUserBinding;
import com.goqual.a10k.model.entity.Phone;
import com.goqual.a10k.presenter.PhoneBookPresenter;
import com.goqual.a10k.presenter.impl.PhoneBookPresenterImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.adapters.AdapterPhoneBook;
import com.goqual.a10k.view.base.BaseActivity;
import com.goqual.a10k.view.dialog.CustomDialog;
import com.goqual.a10k.view.viewholders.PhoneBookViewHolder;

/**
 * Created by hanwool on 2017. 3. 21..
 */

public class ActivityInviteUser extends BaseActivity<ActivityInviteUserBinding>
    implements PhoneBookPresenter.View<Phone>{
    public static final String TAG = ActivityInviteUser.class.getSimpleName();

    public static final String EXTRA_BSID = "extra_bsid";


    private static final int REQ_PERMISSION_READ_CONTACTS = 123;

    private int _bsid;

    private PhoneBookPresenter mPresenter;
    private AdapterPhoneBook mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invite_user;
    }

    @Override
    public void onBtnClick(View view) {
        if(view.getId() == R.id.toolbar_back) {
            finish();
        }
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding.setActivity(this);
        _bsid = getIntent().getIntExtra(EXTRA_BSID, -1);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQ_PERMISSION_READ_CONTACTS);
            }
            else {
                initView();
            }
        }
        else {
            initView();
        }
    }

    private void initView() {
        mBinding.inviteContainer.setAdapter(getAdapter());
        mBinding.inviteContainer.setLayoutManager(new LinearLayoutManager(this));

        getPresenter().loadItems(null);

        mBinding.searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                LogUtil.d(TAG, "beforeTextChanged::" + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LogUtil.d(TAG, "onTextChanged::" + s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                LogUtil.d(TAG, "afterTextChanged::" + s);
                getPresenter().loadItems(s.toString());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQ_PERMISSION_READ_CONTACTS) {
            if(grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    permissions.length > 0 &&
                    permissions[0].equals(Manifest.permission.READ_CONTACTS)) {
                initView();
            }
            else {
                Toast.makeText(this, getString(R.string.invite_read_contacts_granted_error_info), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private PhoneBookPresenter getPresenter() {
        if(mPresenter == null) {
            mPresenter = new PhoneBookPresenterImpl(this, this, getAdapter());
        }
        return mPresenter;
    }

    private AdapterPhoneBook getAdapter() {
        if(mAdapter == null) {
            mAdapter = new AdapterPhoneBook(this);
            mAdapter.setOnRecyclerItemClickListener(((viewId, position) -> {
                Phone user = getAdapter().getItem(position);
                new CustomDialog(this)
                        .setTitleText(R.string.invite_user_dialog_title)
                        .setMessageText(user.getDisplayName() + getString(R.string.invite_user_dialog_content))
                        .setPositiveButton(getString(R.string.common_ok), (dialog, which) -> {
                            if(user.get_userid() == -1){
                                sendInviteSMS(user.getNumber());
                            }
                            else {
                                if(user.get_connectionid() == -1) {

                                }
                            }
                            dialog.dismiss();
                            finish();
                        })
                        .setNegativeButton(getString(R.string.common_cancel), (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }));
        }
        return mAdapter;
    }

    public void sendInviteSMS(String num) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + num));
        intent.putExtra("sms_body", getString(R.string.invite_message));
        startActivity(intent);
    }

}
