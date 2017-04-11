package com.goqual.a10k.view.activities;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.goqual.a10k.R;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.presenter.SwitchPresenter;
import com.goqual.a10k.presenter.impl.SwitchPresenterImpl;
import com.goqual.a10k.view.dialog.CustomDialog;

/**
 * Created by hanwool on 2017. 3. 17..
 */

public class PendingActivityInvite extends AppCompatActivity
        implements SwitchPresenter.View<Switch>{
    public static final String TAG = PendingActivityInvite.class.getSimpleName();

    public static final String EXTRA_BSID = "_bsid";
    private SwitchPresenter mPresenter;
    private int bsid;

    @Override
    public void loadingStart() {

    }

    @Override
    public void loadingStop() {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void addItem(Switch item) {
        getPresenter().add(item.getMacaddr(), item.getTitle(), item.getSeq());
    }

    @Override
    public void onSuccessRenameSwitch(int position, String title) {

    }

    @Override
    public void onSuccessDeleteSwitch(int position) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bsid = getIntent().getIntExtra(EXTRA_BSID, -1);
        if(bsid>0) {
            DialogInterface.OnClickListener onClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        getPresenter().getByBsid(bsid);

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
                dialog.dismiss();
                finish();
            };
            new CustomDialog(this)
                    .setTitleText(R.string.invite_dialog_title)
                    .setMessageText(R.string.invite_dialog_content)
                    .setPositiveButton(getString(R.string.common_allow), onClickListener)
                    .setNegativeButton(getString(R.string.common_deny), onClickListener)
                    .show();
        }
    }

    private SwitchPresenter getPresenter() {
        if(mPresenter == null) {
            mPresenter = new SwitchPresenterImpl(this, this, null);
        }
        return mPresenter;
    }

    @Override
    public void passDeleteEvent(int position) {

    }
}
