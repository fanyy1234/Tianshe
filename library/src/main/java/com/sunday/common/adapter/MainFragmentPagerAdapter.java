package com.sunday.common.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/21.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentsList;
    private String[] titles;

    public MainFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragmentsList = fragments;
    }

    public MainFragmentPagerAdapter(FragmentManager fm, Context context,ArrayList<Fragment> fragments, int resId) {
        super(fm);
        this.fragmentsList = fragments;
        titles = context.getResources().getStringArray(resId);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragmentsList.get(arg0);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(titles!=null){
            return titles[position];
        }
        return super.getPageTitle(position);
    }
}

