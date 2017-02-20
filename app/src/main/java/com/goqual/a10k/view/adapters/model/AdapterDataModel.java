package com.goqual.a10k.view.adapters.model;

import java.util.List;

/**
 * Created by ladmusician on 4/5/16.
 */
public interface AdapterDataModel<T> {
    void addItem(T item);
    void deleteItem(int position);
    int getSize();
    void clear();
    T getItem(int position);
    void updateItem(int position, T item);
    void updateItems(List<T> items);
    void refresh();
}
