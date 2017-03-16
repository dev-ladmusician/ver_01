package com.goqual.a10k.model;

import android.support.annotation.Nullable;

import com.goqual.a10k.model.entity.Switch;

import java.util.ArrayList;
import java.util.List;

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
        if(position>=mSwitchList.size() || position < 0) {
            return null;
        }
        return mSwitchList.get(position);
    }

    public int getPosition(Switch item) {
        return mSwitchList.indexOf(item);
    }

    public void changeTitle(int position, String title) {
        mSwitchList.get(position).setTitle(title);
    }

    public void delete(int position) {
        mSwitchList.remove(position);
    }

    @Nullable
    public Switch getSwitchByMacAddr(String addr) {
        for(Switch item : mSwitchList) {
            if(item.getMacaddr().equals(addr)) {
                return item;
            }
        }
        return null;
    }
    @Nullable
    public Switch getSwitchByBSID(int bsid) {
        for(Switch item : mSwitchList) {
            if(item.get_bsid() == bsid) {
                return item;
            }
        }
        return null;
    }


}
