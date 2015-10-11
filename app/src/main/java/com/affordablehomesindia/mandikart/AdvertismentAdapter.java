package com.affordablehomesindia.mandikart;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AdvertismentAdapter extends FragmentPagerAdapter {

    int NUMBER_OF_ADS = 3;
    Context context;

    public AdvertismentAdapter(Context context,FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                AdvertismentOne one = new AdvertismentOne();
                return one;
            }
            case 1: {
                AdvertismentTwo two = new AdvertismentTwo();
                return two;
            }

            case 2: {
                AdvertismentThree three = new AdvertismentThree();
                return three;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUMBER_OF_ADS;
    }
}
