package com.goqual.a10k.view.interfaces;

import com.goqual.a10k.util.event.EventSwitchEdit;

/**
 * Created by HanWool on 2017. 2. 17..
 */

public interface IToolbarClickListener {
    enum STATUS {
        EDIT,
        DONE,
        HIDE,
        ADD
    }
    void onClickEdit(STATUS status);
}
