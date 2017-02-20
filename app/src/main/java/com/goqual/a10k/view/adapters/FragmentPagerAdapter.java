package com.goqual.a10k.view.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.goqual.a10k.view.base.FragmentBase;

import java.util.ArrayList;

/**
 * Created by HanWool on 2017. 2. 17..
 */

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<FragmentBase> fragmentLists;

    public FragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragmentLists = new ArrayList<>();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentLists.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return fragmentLists.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentLists.get(position);
    }

    public void addItem(FragmentBase item) {
        fragmentLists.add(item);
    }
}
