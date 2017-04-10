package com.goqual.a10k.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.DialogCustomBinding;
import com.goqual.a10k.model.entity.DialogModel;
import com.goqual.a10k.util.KeyPadUtil;
import com.goqual.a10k.util.LogUtil;

/**
 * Created by hanwool on 2017. 2. 22..
 */

public class CustomDialog extends Dialog {

    private Context mContext = null;
    private DialogCustomBinding mBinding = null;
    private DialogModel mModel;
    private OnClickListener mOnPositiveClickListener;
    private OnClickListener mOnNegativeClickListener;
    private boolean mIsShowing;

    public CustomDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        mModel = new DialogModel();
    }

    public CustomDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mContext = context;
        mModel = new DialogModel();
    }

    public CustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
        mModel = new DialogModel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBinding = DialogCustomBinding.inflate(inflater);
        mBinding.setDialog(this);
        mBinding.setItem(mModel);
        setContentView(mBinding.getRoot());

        if (mModel.isPasswdType) {
            mBinding.dialogEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    @Override
    protected void onStart() {
        if (mModel.isEditable) {
            LogUtil.e("CUSTOM DIALOG", "dialog on start");
            KeyPadUtil.KeyPadUp(mContext, mBinding.dialogEdit);
            mBinding.dialogEdit.setFocusable(true);
            mBinding.dialogEdit.requestFocus();
            mBinding.dialogEdit.setEnabled(true);
            mBinding.dialogEdit.setClickable(true);
            mBinding.dialogEdit.setEnabled(true);
            mBinding.setItem(mModel);
        }

        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public CustomDialog setTitleText(String title) {
        mModel.setTitle(title);
        return this;
    }

    public CustomDialog setTitleText(@StringRes int titleId) {
        mModel.setTitle(mContext.getString(titleId));
        return this;
    }

    public CustomDialog setMessageText(String message) {
        mModel.setMessage(message);
        return this;
    }

    public CustomDialog setMessageText(@StringRes int messageId) {
        mModel.setMessage(mContext.getString(messageId));
        return this;
    }

    public CustomDialog setPositiveButton(String text, OnClickListener onClickListener) {
        mModel.setPositiveButton(true);
        mModel.setPositiveButtonText(text);
        mOnPositiveClickListener = onClickListener;
        return this;
    }

    public CustomDialog setNegativeButton(String text, OnClickListener onClickListener) {
        mModel.setNegativeButton(true);
        mModel.setNegativeButtonText(text);
        mOnNegativeClickListener = onClickListener;
        return this;
    }

    public CustomDialog setPositiveButton(boolean state) {
        mModel.setPositiveButton(state);
        return this;
    }

    public CustomDialog setNegativeButton(boolean state) {
        mModel.setNegativeButton(state);
        return this;
    }

    public CustomDialog isEditable(boolean enable) {
        mModel.setEditable(enable);
        return this;
    }

    public CustomDialog setEditTextHint(String hint) {
        mModel.setEditTextHint(hint);
        return this;
    }

    public CustomDialog setEditPasswdType(boolean type) {
        mModel.setPasswdType(type);
        return this;
    }

    public CustomDialog setEditTextHint(@StringRes int hintId) {
        mModel.setEditTextHint(mContext.getString(hintId));
        return this;
    }

    public CustomDialog setEditTextMessage(String message) {
        mModel.setEditTextMessage(message);
        return this;
    }

    public CustomDialog setEditTextMessage(@StringRes int messageId) {
        mModel.setEditTextMessage(mContext.getString(messageId));
        return this;
    }

    public EditText getEditText() {
        return mBinding.dialogEdit;
    }

    public String getEditTextMessage() {
        return mBinding.dialogEdit.getText().toString();
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
        KeyPadUtil.KeyPadDown(mContext, mBinding.dialogEdit);
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
}
