package com.goqual.a10k.view.fragments.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentAuthPhoneBinding;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.dialog.CustomDialog;
import com.goqual.a10k.view.interfaces.IAuthActivityInteraction;

/**
 * Created by hanwool on 2017. 2. 24..
 */

public class FragmentAuthPhone extends BaseFragment<FragmentAuthPhoneBinding> {
    public static final String TAG = FragmentAuthPhone.class.getSimpleName();

    public static final String PHONE_NUMBER = "phone_number";

    private static String phoneNumber;
    private static String phoneCountryCode;

    private IAuthActivityInteraction mInteraction;

    public static FragmentAuthPhone newInstance(String phoneNumber, String countryCode) {

        Bundle args = new Bundle();

        FragmentAuthPhone fragment = new FragmentAuthPhone();
        args.putString(PHONE_NUMBER, phoneNumber);
        fragment.setArguments(args);

        phoneCountryCode = countryCode;

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

        // set init country code
        mBinding.authPhoneEdit.setHint(R.string.auth_phone_edit_hint);
        mBinding.authPhoneEdit.setDefaultCountry(phoneCountryCode.toUpperCase());
    }

    public void onBtnClick(View view) {
        if (mBinding.authPhoneEdit.isValid()) {
            mInteraction.requestSmsToken(mBinding.authPhoneEdit.getPhoneNumber().toString());
        } else {
            CustomDialog dialog = new CustomDialog(getActivity());
            dialog.isEditable(true)
                    .isEditable(false)
                    .setTitleText(R.string.auth_phone_eror_invalid_number_title)
                    .setMessageText(R.string.auth_phone_eror_invalid_number_content)
                    .setPositiveButton(false)
                    .setNegativeButton(getString(R.string.common_cancel), (dia, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        }
    }
}
