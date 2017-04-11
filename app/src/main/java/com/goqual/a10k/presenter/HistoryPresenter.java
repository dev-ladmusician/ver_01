package com.goqual.a10k.presenter;

/**
 * Created by ladmusician on 3/9/16.
 */
public interface HistoryPresenter {
   void get(int switchId, int year, int month, int day, int page);

    interface View<T> extends BasePresenterView<T>{
        void loadItems(int year, int month, int day);
        void loadItems();
        void checkLoadMore();
    }
}
