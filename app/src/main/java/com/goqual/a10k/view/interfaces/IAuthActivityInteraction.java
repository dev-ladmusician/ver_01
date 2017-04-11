package com.goqual.a10k.view.interfaces;

import android.support.v4.app.Fragment;

/**
 * Created by hanwool on 2017. 2. 24..
 */

public interface IAuthActivityInteraction {
    void requestSmsToken(String phoneNumber);
    void requestAppAuthToken(String phoneNumber, String smsToken);
    void changeCurrentPage(Fragment frag);
    void setInitPage();
}
