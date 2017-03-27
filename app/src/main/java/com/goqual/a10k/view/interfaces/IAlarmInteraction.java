package com.goqual.a10k.view.interfaces;

import com.goqual.a10k.model.entity.Switch;

/**
 * Created by hanwool on 2017. 3. 13..
 */

public interface IAlarmInteraction {
    void setBtns(boolean btn1, boolean btn2, boolean btn3);
    void goBtnPage(int item);
}
