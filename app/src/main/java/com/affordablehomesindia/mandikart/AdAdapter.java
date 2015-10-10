package com.affordablehomesindia.mandikart;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AdAdapter extends FragmentPagerAdapter {

    private Context context;
    private int num_of_item = 3;


    public AdAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                Ad1 f = new Ad1();
                return f;
            }
            case 1: {
                Ad2 f = new Ad2();
                return f;
            }
            case 2: {
                Ad3 f = new Ad3();
                return f;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return num_of_item;
    }
}
