package com.goqual.a10k.view.fragments.connect;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentConnectSetSwitchBinding;
import com.goqual.a10k.presenter.SetSwitchPresenter;
import com.goqual.a10k.presenter.impl.SetSwitchPresenterImpl;
import com.goqual.a10k.util.KeyPadUtil;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.dialog.CustomDialog;
import com.goqual.a10k.view.interfaces.IActivityFragmentPageChangeListener;
import com.goqual.a10k.view.interfaces.IAuthInteraction;
import com.goqual.a10k.view.interfaces.IConnectionActivityInteraction;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hanwool on 2017. 2. 23..
 */

public class FragmentConnectSetSwitch extends BaseFragment<FragmentConnectSetSwitchBinding>
        implements SetSwitchPresenter.View {

    public static final String TAG = FragmentConnectSetSwitch.class.getSimpleName();

    private SetSwitchPresenter mPresenter;
    private Handler mHandler;

    private final int INTERVAL_CHECKING_CONNECTION = 4000;
    private static final int REQ_LOCATION_PERMMISION = 111;
    private int mSwitchConnectCheckCount;

    public static FragmentConnectSetSwitch newInstance() {
        Bundle args = new Bundle();

        FragmentConnectSetSwitch fragment = new FragmentConnectSetSwitch();
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentConnectSetSwitch() {
    }

    @Override
    public void onConnectError() {
        ((IConnectionActivityInteraction)getActivity()).onErrorSocketConnection();
        ((IActivityFragmentPageChangeListener)getActivity()).changePage(getResources().getInteger(R.integer.frag_info));
    }

    @Override
    public boolean hasToolbarMenus() {
        return false;
    }

    @Override
    public void onSwitchConnected() {
        LogUtil.d("SWITCH_CONNECT", "onSwitchConnected");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                LogUtil.d(TAG, "onConnectSuccess");
                CustomDialog customDialog = new CustomDialog(getActivity());
                customDialog
                        .isEditable(true)
                        .setTitleText(R.string.rename_title)
                        .setMessageText(R.string.rename_content)
                        .setEditTextHint(R.string.rename_edit_hint)
                        .setPositiveButton(getString(R.string.rename_btn_txt), (dialog, which) -> {
                            getPresenter().setName(customDialog.getEditTextMessage());
                            KeyPadUtil.KeyPadDown(getActivity(), customDialog.getEditText());
                            dialog.dismiss();
                        })
                        .show();
            }
        });
    }

    @Override
    public void switchNotConnected() {
        LogUtil.d("SWITCH_CONNECT", "switchNotConnected");
        if(mSwitchConnectCheckCount <= 3) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    getPresenter().checkSwitchConnected();
                }
            }, INTERVAL_CHECKING_CONNECTION);
            mSwitchConnectCheckCount += 1;
        }
        else {
            ((IConnectionActivityInteraction)getActivity()).onErrorCheckConnection();
            ((IActivityFragmentPageChangeListener)getActivity()).changePage(getResources().getInteger(R.integer.frag_info));
        }
    }

    @Override
    public void onConnectSuccess() {
        LogUtil.d("SWITCH_CONNECT", "onConnectSuccess");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getPresenter().checkSwitchConnected();
            }
        }, INTERVAL_CHECKING_CONNECTION);
    }

    @Override
    public void onRegisterSuccess() {
        getActivity().finish();
    }

    @Override
    public void onRegisterError() {
        ((IConnectionActivityInteraction)getActivity()).onErrorRename();
        ((IActivityFragmentPageChangeListener)getActivity()).changePage(getResources().getInteger(R.integer.frag_info));
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler = new Handler();
        LogUtil.d(TAG, "onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().destroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_connect_set_switch;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwitchConnectCheckCount = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startLoading();
        getPresenter().setSelectedWifi(
                ((IAuthInteraction)getActivity()).getSelectedWifi(),
                ((IAuthInteraction)getActivity()).getSelectedWifiPasswd());
        getPresenter().set10KAP(((IAuthInteraction)getActivity()).get10KAP());
        getPresenter().startSetting10K();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQ_LOCATION_PERMMISION) {
            if(grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(!Settings.System.canWrite(getActivity())) {
                        DialogInterface.OnClickListener onClickListener = (dialog, which) -> {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                                    intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    try {
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        LogUtil.e("MainActivity", "error starting permission intent", e);
                                    }
                                    break;
                            }
                            dialog.dismiss();
                        };
                        CustomDialog customDialog = new CustomDialog(getActivity());
                        customDialog.isEditable(false)
                                .isEditable(false)
                                .setTitleText(R.string.permission_dialog_title)
                                .setMessageText(R.string.permission_dialog_message)
                                .setPositiveButton(getString(R.string.common_allow), onClickListener)
                                .setNegativeButton(false)
                                .show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void startLoading() {
        Animation in = AnimationUtils.makeInAnimation(getContext(), true);
        mBinding.loadingContainer.startAnimation(in);
        mBinding.loadingContainer.setVisibility(View.VISIBLE);

        AnimationDrawable loading = (AnimationDrawable) mBinding.setLoading.getDrawable();
        if (!loading.isRunning()) {
            loading.start();
        }
    }

    @Override
    public void stopLoading() {
        Animation out = AnimationUtils.makeOutAnimation(getContext(), true);
        mBinding.loadingContainer.startAnimation(out);
        mBinding.loadingContainer.setVisibility(View.INVISIBLE);
    }

    private SetSwitchPresenter getPresenter() {
        if(mPresenter == null) {
            mPresenter = new SetSwitchPresenterImpl(this, getActivity());
        }
        return mPresenter;
    }

    @Override
    public void onBtnClick(View view) {

    }
}
