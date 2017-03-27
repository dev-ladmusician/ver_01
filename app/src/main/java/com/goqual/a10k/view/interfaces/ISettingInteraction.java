package com.goqual.a10k.view.interfaces;

/**
 * Created by HanWool on 2017. 2. 17..
 */

public interface ISettingInteraction extends IActivityInteraction {
    boolean checkAdmin();
    void setAdmin(boolean isAdmin);
}
