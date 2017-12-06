package edu.usf.devices.mobile.studybuddy;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class PageAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public PageAdapter (FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new FragmentGroup();
            case 1:
                return new FragmentSearch();
            case 2:
                return new FragmentSchedule();
            case 3:
                return new FragmentProfile();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
