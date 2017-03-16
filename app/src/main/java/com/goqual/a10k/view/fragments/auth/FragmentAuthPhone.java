package com.goqual.a10k.view.fragments.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentAuthPhoneBinding;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.interfaces.IAuthActivityInteraction;

/**
 * Created by hanwool on 2017. 2. 24..
 */

public class FragmentAuthPhone extends BaseFragment<FragmentAuthPhoneBinding> {
    public static final String TAG = FragmentAuthPhone.class.getSimpleName();

    public static final String PHONE_NUMBER = "phone_number";

    private String phoneNumber;

    private IAuthActivityInteraction mInteraction;

    public static FragmentAuthPhone newInstance(String phoneNumber) {

        Bundle args = new Bundle();

        FragmentAuthPhone fragment = new FragmentAuthPhone();
        args.putString(PHONE_NUMBER, phoneNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_auth_phone;
    }

    @Override
    public String getTitle() {
        return "Auth";
    }

    @Override
    public boolean hasToolbarMenus() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e(TAG, "onCreate::");
        mInteraction = (IAuthActivityInteraction)getActivity();
        if(getArguments() != null) {
            phoneNumber = getArguments().getString(PHONE_NUMBER, "");
        }
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

    private void initView() {
        mBinding.setFragment(this);
        mBinding.authPhoneEdit.addTextChangedListener(new PhoneNumberFormattingTextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, String.format("beforeTextChanged::string:%s, start:%d, count:%d, after:%d", s, start, count, after));
                super.beforeTextChanged(s, start, count, after);
            }

            @Override
            public synchronized void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                String ss = s.toString();
                PhoneNumberUtils
                if(PhoneNumberUtils.isGlobalPhoneNumber(ss)) {
                    LogUtil.d(TAG, "afterTextChanged::isWellFormedSmsAddress:true::" + ss);
                    mBinding.authPhoneBtnNext.setEnabled(true);
                }
                else {
                    LogUtil.d(TAG, "afterTextChanged::isWellFormedSmsAddress:false" + ss);
                    mBinding.authPhoneBtnNext.setEnabled(false);
                }
            }
        });
        mBinding.authPhoneEdit.setText(phoneNumber);
    }

    public void onBtnClick(View view) {
        if(view.getId() == R.id.auth_phone_btn_next) {
            if(mBinding.authPhoneEdit.getText().toString().isEmpty()) {
                Snackbar.make(mBinding.getRoot(), R.string.auth_phone_eror_invalid_number, Snackbar.LENGTH_LONG).show();
            }
            else {
                LogUtil.d(TAG, "onBtnClick::PhoneNumber:" + mBinding.authPhoneEdit.getText().toString());
                mInteraction.requestSmsToken(mBinding.authPhoneEdit.getText().toString());
            }
        }
    }
}
