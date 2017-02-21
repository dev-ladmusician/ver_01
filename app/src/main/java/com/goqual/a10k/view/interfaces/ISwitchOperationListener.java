package com.goqual.a10k.view.interfaces;

import com.goqual.a10k.model.entity.Switch;

/**
 * Created by HanWool on 2017. 2. 21..
 */

public interface ISwitchOperationListener {
    // position으로 하면 Each에서 들고있는 Switch객체가 아무 쓸데가 없어지지 않을까요?
    void onSwitchClicked(int position, int btnNumber);
}
