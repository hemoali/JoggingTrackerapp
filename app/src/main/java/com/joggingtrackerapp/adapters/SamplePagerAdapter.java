package com.joggingtrackerapp.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by ibrahimradwan on 9/3/15.
 */
public class SamplePagerAdapter extends FragmentPagerAdapter {
    private String[] tabs = new String[]{"Times", "Users"};
    private List<Fragment> fragments;

    @Override
    public CharSequence getPageTitle (int position) {
        return tabs[position];
    }

    public SamplePagerAdapter (FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem (int position) {
        return this.fragments.get(position);
    }


    @Override
    public int getCount () {
        return this.fragments.size();
    }
}
