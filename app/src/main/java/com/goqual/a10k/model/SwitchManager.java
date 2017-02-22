package com.goqual.a10k.model;

import com.goqual.a10k.model.entity.Switch;

import java.util.ArrayList;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public class SwitchManager {

    private static SwitchManager instance;

    private ArrayList<Switch> mSwitchList = null;

    public static SwitchManager getInstance() {
        if (instance == null) {
            instance = new SwitchManager();
        }
        return instance;
    }

    private SwitchManager() {
        mSwitchList = new ArrayList<>();
    }

    public ArrayList<Switch> getList() {
        return mSwitchList;
    }

    public void addItem(Switch item) {
        if (mSwitchList != null) {
            mSwitchList.add(item);
        }
    }

    public void clear() {
        mSwitchList.clear();
    }

    public int getCount() {
        return mSwitchList.size();
    }

    public Switch getItem(int position) {
        return mSwitchList.get(position);
    }

    public int getPosition(Switch item) {
        return mSwitchList.indexOf(item);
    }

}
