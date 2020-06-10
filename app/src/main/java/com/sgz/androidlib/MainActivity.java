package com.sgz.androidlib;

import android.os.Bundle;

import com.android.sgzcommon.activity.BaseMainActivity;
import com.android.sgzcommon.fragment.NavigationFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseMainActivity {


    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getNavigationId() {
        return R.id.nav;
    }

    @Override
    protected int getFrameLayoutId() {
        return R.id.fl_container;
    }

    @Override
    protected NavigationFragment newNavigationFragment(int position) {
        switch (position) {
            case 0:
                return new OneFragment();
            case 1:
                return new TwoFragment();
            case 2:
                return new ThreeFragment();
            case 3:
                return new FourFragment();
            case 4:
                return new FiveFragment();
        }
        return null;
    }

    @Override
    protected List<NavigationFragment> getNavigationFragments() {
        ArrayList<NavigationFragment> fragments = new ArrayList<>();
        fragments.add(new OneFragment());
        fragments.add(new TwoFragment());
        fragments.add(new ThreeFragment());
        fragments.add(new FourFragment());
        fragments.add(new FiveFragment());
        return fragments;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
