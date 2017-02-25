package com.goqual.a10k.util.event;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by ladmusician on 2017. 1. 5..
 */

public class RxBus {
    private static RxBus instance;
    public static RxBus getInstance() {
        if (instance == null)
            instance = new RxBus();
        return instance;
    }

    private Subject<Object, Object> mBus = null;

    public Subject<Object, Object> getBus() {
        if(mBus == null)
            mBus = new SerializedSubject<>(PublishSubject.create());
        return mBus;
    }

    public void send(Object o) {
        mBus.onNext(o);
    }

    public Observable<Object> toObserverable() {
        return getBus();
    }
}
