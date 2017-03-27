package com.goqual.a10k.presenter;

/**
 * Created by ladmusician on 2017. 2. 21..
 */

public interface SwitchPresenter extends BasePresenter{
    void deleteItem(int position);
    void rename(int position, String title);
    void getByBsid(int _bsid);
    void add(String macaddr, String title, int count);

    interface View<T> extends BasePresenterView<T> {
        void onSuccessRenameSwitch(int position, String title);
        void onSuccessDeleteSwitch(int position);
    }
}
