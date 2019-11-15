package com.galileo.cc6.helpmate.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.galileo.cc6.helpmate.projectFragments.weekDayFragment;

/**
 * Created by EEVGG on 23/10/2016.
 */

public class DayPagerAdapter extends FragmentPagerAdapter {
    private int tabCount;
    //creates tabs labels
    private String tabTitles[] = new String[]{"D","L","M","M","J","V","S"};
    private weekDayFragment wdFrag;
    //Constructor to the class
    public DayPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;

    }

    @Override
    public CharSequence getPageTitle(int position){
        return tabTitles[position];
    }
    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        // switch selects schedules based on day
        switch (position) {
            case 0:
                wdFrag=new weekDayFragment();
                Bundle args = new Bundle();
                args.putInt("pos",0);
                wdFrag.setArguments(args);
                return wdFrag;
            case 1:
                wdFrag=new weekDayFragment();
                 args = new Bundle();
                args.putInt("pos",1);
                wdFrag.setArguments(args);
                return wdFrag;
            case 2:
                wdFrag=new weekDayFragment();
                 args = new Bundle();
                args.putInt("pos",2);
                wdFrag.setArguments(args);
                return wdFrag;
            case 3:
                wdFrag=new weekDayFragment();
                args = new Bundle();
                args.putInt("pos",3);
                wdFrag.setArguments(args);
                return wdFrag;
            case 4:
                wdFrag=new weekDayFragment();
                args = new Bundle();
                args.putInt("pos",4);
                wdFrag.setArguments(args);
                return wdFrag;
            case 5:
                wdFrag=new weekDayFragment();
                args = new Bundle();
                args.putInt("pos",5);
                wdFrag.setArguments(args);
                return wdFrag;
            case 6:
                wdFrag=new weekDayFragment();
                args = new Bundle();
                args.putInt("pos",6);
                wdFrag.setArguments(args);
                return wdFrag;
        }
        return null;
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
