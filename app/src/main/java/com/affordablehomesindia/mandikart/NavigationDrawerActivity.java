package com.affordablehomesindia.mandikart;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.melnykov.fab.FloatingActionButton;

public class NavigationDrawerActivity extends Fragment {

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;

    public NavigationDrawerActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        return v;
    }


    public void setUp(DrawerLayout drawerLayout, Toolbar toolbar, final FloatingActionButton fab) {
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                mDrawerLayout,
                toolbar,
                R.string.open,
                R.string.close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //fab.hide(true);
                //button.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //fab.show(true);
                //button.setVisibility(View.VISIBLE);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }
}
