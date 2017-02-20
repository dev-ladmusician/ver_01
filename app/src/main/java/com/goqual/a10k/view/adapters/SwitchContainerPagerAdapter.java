package com.goqual.a10k.view.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.view.fragments.FragmentMainSwitchEach;
import com.goqual.a10k.view.fragments.FragmentMainSwitchList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seyriz on 2017. 2. 17..
 */

public class SwitchContainerPagerAdapter extends FragmentPagerAdapter {
    List<Switch> mSwitchList = null;
    Context mContext = null;

    public SwitchContainerPagerAdapter(FragmentManager fragmentManager, Context ctx) {
        super(fragmentManager);
        this.mSwitchList = new ArrayList<>();
        getFragmentLists().add(new FragmentMainSwitchList());
        mContext = ctx;
    }

    private void addSwitch(Switch newSwitch) {
        FragmentMainSwitchEach frag = FragmentMainSwitchEach.newInstance(mSwitchList.size());

        mSwitchList.add(newSwitch);
        addItem(frag);
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof FragmentMainSwitchList)
            return super.getItemPosition(object);
        else
            return POSITION_NONE;
    }

    public Switch getSwitch(int position) {
        return this.mSwitchList.get(position);
    }
}
