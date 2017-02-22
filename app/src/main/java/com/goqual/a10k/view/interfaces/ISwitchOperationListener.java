package com.goqual.a10k.view.interfaces;

/**
 * Created by HanWool on 2017. 2. 21..
 */

public interface ISwitchOperationListener {
    void onSwitchClicked(int position, int btnNumber);

    void onSwitchDelete(int position);
    void onSwitchRename(int position, String title);
}
