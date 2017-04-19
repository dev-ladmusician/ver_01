package com.goqual.a10k.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.DialogCustomListBinding;
import com.goqual.a10k.model.entity.DialogModel;
import com.goqual.a10k.model.entity.User;
import com.goqual.a10k.view.adapters.AdapterUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanwool on 2017. 2. 22..
 */

public class CustomListDialog extends Dialog {

    private Context mContext = null;
    private DialogCustomListBinding mBinding = null;
    private DialogModel mModel;
    private OnClickListener mOnPositiveClickListener;
    private OnClickListener mOnNegativeClickListener;
    private boolean mIsShowing;
    private AdapterUser mAdapter = null;
    private List<User> mUsers = null;
    private int mSelectedPosition = -1;

    public CustomListDialog(@NonNull Context context, List<User> items) {
        super(context);
        mContext = context;
        this.mUsers = new ArrayList<>(items);
        mModel = new DialogModel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBinding = DialogCustomListBinding.inflate(inflater);
        mBinding.setDialog(this);
        mBinding.setItem(mModel);

        // recyclerview
        mBinding.dialogListContainer.setAdapter(getAdapter());
        mBinding.dialogListContainer.setLayoutManager(new LinearLayoutManager(mContext));

        for(User each : mUsers) {
            each.setmIsDeletable(false);
            each.setmChecked(false);
        }

        getAdapter().updateItems(mUsers);
        getAdapter().refresh();

        getAdapter().setOnRecyclerItemClickListener((id, position) -> {
            mSelectedPosition = position;
            getAdapter().setNonChecked();
            getAdapter().getItem(position).setmChecked(true);
            getAdapter().refresh();
        });

        setContentView(mBinding.getRoot());
        setOnDismissListener(dialog -> {
            mIsShowing = false;
        });
        setOnCancelListener(dialog -> {
            mIsShowing = false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAdapter().setDeletable(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getAdapter().setNonChecked();
        getAdapter().refresh();
    }

    public CustomListDialog setTitleText(String title) {
        mModel.setTitle(title);
        return this;
    }

    public CustomListDialog setTitleText(@StringRes int titleId) {
        mModel.setTitle(mContext.getString(titleId));
        return this;
    }

    public CustomListDialog setPositiveButton(String text, OnClickListener onClickListener) {
        mModel.setPositiveButton(true);
        mModel.setPositiveButtonText(text);
        mOnPositiveClickListener = onClickListener;
        return this;
    }

    public CustomListDialog setNegativeButton(String text, OnClickListener onClickListener) {
        mModel.setNegativeButton(true);
        mModel.setNegativeButtonText(text);
        mOnNegativeClickListener = onClickListener;
        return this;
    }

    public void onBtnClick(View view) {
        int which = view.getId() == R.id.dialog_btn_ok ? BUTTON_POSITIVE : BUTTON_NEGATIVE;
        switch (view.getId()) {
            case R.id.dialog_btn_ok:
                mOnPositiveClickListener.onClick(this, which);
                break;
            case R.id.dialog_btn_cancel:
                mOnNegativeClickListener.onClick(this, which);
                break;
        }
    }

    @Override
    public void show() {
        mIsShowing = true;
        super.show();
    }

    @Override
    public void dismiss() {
        mIsShowing = false;
        super.dismiss();
    }

    @Override
    public void cancel() {
        mIsShowing = false;
        super.cancel();
    }

    @Override
    public boolean isShowing() {
        return mIsShowing;
    }

    private AdapterUser getAdapter() {
        if (mAdapter == null)
            mAdapter = new AdapterUser(mContext, false);
        return mAdapter;
    }

    public int getSelectedPosition() {
        return this.mSelectedPosition;
    }
}
