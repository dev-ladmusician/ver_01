package com.goqual.a10k.view.interfaces;

/**
 * Created by HanWool on 2017. 2. 17..
 */

public interface IToolbarClickListener {
    enum STATE {
        EDIT,
        DONE,
        HIDE,
        ADD
    }
    void onClickEdit(STATE STATE);
}
