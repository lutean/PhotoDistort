package com.prepod.photodistort.helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.prepod.photodistort.fragments.GalleryFragment;
import com.prepod.photodistort.fragments.TakePicFragment;

public class MainTabsAdapter extends FragmentStatePagerAdapter {

    private String[] tabTitles;

    public MainTabsAdapter(FragmentManager fm, String[] tabTitles) {
        super(fm);
        this.tabTitles = tabTitles;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return TakePicFragment.newInstance();
            case 1:
                return GalleryFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return tabTitles[0];
            case 1:
                return tabTitles[1];
            default:
                return null;
        }
    }
}
