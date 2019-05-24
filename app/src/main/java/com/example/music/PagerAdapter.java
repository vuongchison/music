package com.example.music;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    public static Tab1 tab1;
    public static Tab2 tab2;
    public static Tab3 tab3;
    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch(position)
        {

            case 0:
                Tab1 tab1 = new Tab1();
                this.tab1 = tab1;
                return tab1;
            case 1:
                Tab2 tab2 = new Tab2();
                this.tab2= tab2;
                return  tab2;
            case 2:
                Tab3 tab3 = new Tab3();
                this.tab3 = tab3;
                return  tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
