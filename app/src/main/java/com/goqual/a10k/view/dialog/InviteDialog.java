package com.goqual.a10k.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.goqual.a10k.R;
import com.goqual.a10k.model.remote.service.SwitchService;

/**
 * Created by hanwool on 2017. 3. 16..
 */

public class InviteDialog extends CustomDialog implements DialogInterface.OnClickListener {

    private Context mContext;
    private SwitchService mSwitchService;

    public InviteDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        setTitleText(R.string.invite_dialog_title);
        setMessageText(R.string.invite_dialog_content);
        setNegativeButton(context.getString(R.string.common_deny), this);
        setPositiveButton(context.getString(R.string.common_allow), this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                dialog.dismiss();
                // TODO: macADDR???
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialog.cancel();
                break;
        }
    }



    public SwitchService getSwitchService() {
        if (mSwitchService == null)
            mSwitchService = new SwitchService(mContext);

        return mSwitchService;
    }
}
