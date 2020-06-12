package com.sgz.androidlib;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.sgzcommon.activity.BaseMainActivity;
import com.android.sgzcommon.fragment.NavigationFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

public class MainActivity extends BaseMainActivity {

    ArrayList<NavigationFragment> fragments;

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
                return new TakePhotoFragment();
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
        fragments = new ArrayList<>();
        fragments.add(new TakePhotoFragment());
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

    @Override
    protected void onResume() {
        super.onResume();
        int itemId = getNavigationId();
        Log.d("MainActivity", "onResume: id == " + itemId);
    }

    public void clear(View v){
//        clearFragments();
        Fragment fragment = getFrgment(0);
        if (fragment != null){
            if (fragment instanceof TakePhotoFragment){
                TakePhotoFragment takePhotoFragment = (TakePhotoFragment) fragment;
                takePhotoFragment.setMsg("ddddddd");
            }
            Log.d("MainActivity", "clear: fragment != null ");
        }else {
            Log.d("MainActivity", "clear: null !");
        }
        selecteNavItem(0);
    }
    public void home(View v){
        selecteNavItem(0);
    }

}
