package com.goqual.a10k.presenter;


import com.goqual.a10k.model.entity.Alarm;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

public interface AlarmAddEditPresenter extends BasePresenter{
    void add(Alarm item);
    void update(Alarm item);

    interface View{
        void loadingStart();
        void loadingStop();
        void onSuccess();
        void onError(Throwable e);
    }
}
