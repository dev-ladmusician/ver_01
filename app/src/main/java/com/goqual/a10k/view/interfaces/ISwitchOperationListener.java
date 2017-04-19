package com.goqual.a10k.view.interfaces;

import com.goqual.a10k.model.entity.Switch;

import java.util.List;

/**
 * Created by HanWool on 2017. 2. 21..
 */

public interface ISwitchOperationListener {
    void onSwitchClicked(int position, int btnNumber);

    void onChangeSwitchPosition(List<Switch> list);

    void onSwitchDelete(int position);
    void onSwitchRename(int position, String title);
}
