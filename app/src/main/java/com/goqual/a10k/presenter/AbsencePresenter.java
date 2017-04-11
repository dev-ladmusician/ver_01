package com.goqual.a10k.presenter;


import com.goqual.a10k.model.entity.Absence;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

public interface AbsencePresenter extends BasePresenter{
    void add(Absence item);
    void update(Absence item);
    void getItem(int switchId);

    interface View<T> extends BasePresenterView<T>{
        void onSuccessGetItem(Absence item);
        void onSuccessUpdate(Absence item);

        void setStateSwitchAvailable(boolean state);
    }
}
