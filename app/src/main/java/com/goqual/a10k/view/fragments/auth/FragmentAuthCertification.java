package com.goqual.a10k.view.fragments.auth;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentAuthCertificationBinding;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.activities.ActivityPhoneAuth;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.dialog.CustomDialog;
import com.goqual.a10k.view.interfaces.IAuthActivityInteraction;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hanwool on 2017. 2. 24..
 */

public class FragmentAuthCertification extends BaseFragment<FragmentAuthCertificationBinding>{
    
    public static final String TAG = FragmentAuthCertification.class.getSimpleName();

    public static final String AUTH_KEY = "auth_key";
    public static final String PHONE_NUMBER = "phone_number";

    private String authKey;
    private String phoneNumber;
    private int mMinueteCount = 60;
    private Timer secondeTick;
    private Handler mHandler;

    public static FragmentAuthCertification newInstance(String phoneNumber, String authKey) {
        Bundle args = new Bundle();
        FragmentAuthCertification fragment = new FragmentAuthCertification();
        args.putString(AUTH_KEY, authKey);
        args.putString(PHONE_NUMBER, phoneNumber);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_auth_certification;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public boolean hasToolbarMenus() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            authKey = getArguments().getString(AUTH_KEY, "");
            phoneNumber = getArguments().getString(PHONE_NUMBER, "");
        }
        mHandler = new Handler();
        secondeTick = new Timer();

        TimerTask secondTickTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(mMinueteCount == 0) {
                            secondeTick.cancel();
                            secondeTick.purge();
                            mBinding.authCertiEdit.setEnabled(false);
                            ((ActivityPhoneAuth)getActivity()).onErrorAuthProcess(ActivityPhoneAuth.ERROR_REASON.TIMEOUT,
                                    getString(R.string.auth_certi_dialog_certifinum_timeover));
                        }
                        mBinding.authCertiTxtCount.setText(String.format(Locale.KOREA, "%d%s", mMinueteCount--, getString(R.string.auth_certi_unit)));
                        LogUtil.d(TAG, "mMinueteCount: " + mMinueteCount);
                    }
                });
            }
        };
        secondeTick.schedule(secondTickTask, 10, 1000);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setFragment(this);

        mBinding.authCertiEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mBinding.authCertiBtnNext.setClickable(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mBinding.authCertiBtnNext.setClickable(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 4) {
                    mBinding.authCertiBtnNext.setEnabled(true);
                    mBinding.authCertiBtnNext.setClickable(true);
                    mBinding.authCertiBtnNext.setBackgroundResource(R.drawable.selector_btn_auth);
                    mBinding.authCertiBtnNextIcon.setImageResource(R.drawable.icon_arrow_right_blue);
                } else {
                    mBinding.authCertiBtnNext.setClickable(false);
                    mBinding.authCertiBtnNext.setBackgroundResource(R.drawable.selector_btn_auth_disable);
                    mBinding.authCertiBtnNextIcon.setImageResource(R.drawable.icon_arrow_right_grey);
                }
            }
        });

        mBinding.authCertiBtnNext.setClickable(false);
        mBinding.authCertiBtnNext.setEnabled(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        secondeTick.cancel();
        secondeTick.purge();
    }

    public void onBtnClick(View view) {
        if(view.getId() == R.id.auth_certi_btn_next) {
            if(authKey.equals(mBinding.authCertiEdit.getText().toString())){
                /**
                 * 새로운 fcm token이 생기면 preference update
                 */

                ((IAuthActivityInteraction)getActivity()).requestAppAuthToken(phoneNumber, authKey);
            } else {
                CustomDialog dialog = new CustomDialog(getActivity());
                dialog.isEditable(true)
                        .isEditable(false)
                        .setTitleText(R.string.auth_certi_dialog_incorrect_certifinum_title)
                        .setMessageText(R.string.auth_certi_dialog_incorrect_certifinum)
                        .setPositiveButton(getString(R.string.common_ok), (dia, which) -> {
                            dialog.dismiss();
                        })
                        .setNegativeButton(getString(R.string.common_retry), (dia, which) -> {
                            ((ActivityPhoneAuth)getActivity()).setInitPage();
                            dialog.dismiss();
                        })
                        .show();
            }
        }
    }
}
