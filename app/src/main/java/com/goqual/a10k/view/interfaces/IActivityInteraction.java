package com.goqual.a10k.view.interfaces;

import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;

/**
 * Created by HanWool on 2017. 2. 17..
 */

public interface IActivityInteraction {
    AppBarLayout getAppbar();
    Toolbar getToolbar();
    void setTitle(String title);
}
