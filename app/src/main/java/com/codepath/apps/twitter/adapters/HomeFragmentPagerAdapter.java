package com.codepath.apps.twitter.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.twitter.fragments.HomeFragment;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter{

    final int PAGE_COUNT = 2;

    private String tabTitles[] = new String[]{"Home", "Mentions"};

    public HomeFragmentPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return HomeFragment.newInstance(position+1);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
