package com.goqual.a10k.view.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;
import com.goqual.a10k.view.fragments.FragmentMainSwitchEach;
import com.goqual.a10k.view.fragments.FragmentMainSwitchList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seyriz on 2017. 2. 17..
 */

public class AdapterSwitchContainer extends AdapterPager {
    private static final String TAG = AdapterSwitchContainer.class.getSimpleName();
    List<Switch> mSwitchList = null;
    Context mContext = null;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public AdapterSwitchContainer(FragmentManager fragmentManager, Context ctx) {
        super(fragmentManager);
        this.mSwitchList = new ArrayList<>();
        getFragmentLists().add(new FragmentMainSwitchList());
        mContext = ctx;
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof FragmentMainSwitchList)
            return super.getItemPosition(object);
        else
            return POSITION_NONE;
    }

    @Override
    public void clear() {
        mSwitchList.clear();
        super.clear();
        super.addItem(FragmentMainSwitchList.newInstance());
        refresh();
    }

    public Switch getSwitch(int position) {
        return this.mSwitchList.get(position);
    }

    public void addItem(Switch newSwitch) {

        FragmentMainSwitchEach frag = FragmentMainSwitchEach.newInstance(mSwitchList.size());
        LogUtil.e(TAG, "frag :: " + frag.getId());
        mSwitchList.add(newSwitch);
        addItem(frag);
    }

    public void deleteItem(int position) {
        mSwitchList.remove(position);
        fragmentLists.remove(position+1); // {FragmentMainSwitchList, ...}
        refresh();
    }
}
