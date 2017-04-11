package com.goqual.a10k.presenter;


import com.goqual.a10k.model.entity.Absence;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

public interface AbsencePresenter extends BasePresenter{
    void updateState(int position);
    void delete(int position);
    void add(Absence item);
    void update(Absence item);
    void getItem(int switchId);

    interface View<T> extends BasePresenterView<T>{
        void onSuccessDelete();
        void onFailDelete(int position);
        void onSuccessGetItem(Absence item);

        void setStateSwitchAvailable(boolean state);
    }
}
