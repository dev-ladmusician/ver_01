package com.goqual.a10k.presenter;

import com.goqual.a10k.model.entity.Switch;

import java.util.List;

/**
 * Created by ladmusician on 2017. 2. 21..
 */

public interface SwitchPresenter extends BasePresenter{
    void deleteItem(int position);
    void rename(int position, String title);
    void getByBsid(int _bsid);
    void add(String macaddr, String title, int count);
    void changeSeq(List<Switch> items);

    interface View<T> extends BasePresenterView<T> {
        void onSuccessRenameSwitch(int position, String title);
        void onSuccessDeleteSwitch(int position);

        void onSuccessChangeSeq();
        void onErrorChangeSeq(Throwable e);

        void passDeleteEvent(int switchId);
    }
}
