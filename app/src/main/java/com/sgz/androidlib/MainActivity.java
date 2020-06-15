package com.sgz.androidlib;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.android.sgzcommon.activity.BaseMainActivity;
import com.android.sgzcommon.fragment.NavigationFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
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

    public void clear(View v) {
        Fragment fragment = getFrgment(0);
        if (fragment != null) {
            if (fragment instanceof TakePhotoFragment) {
                TakePhotoFragment takePhotoFragment = (TakePhotoFragment) fragment;
                takePhotoFragment.setMsg("ddddddd");
            }
            Log.d("MainActivity", "clear: fragment != null ");
        } else {
            Log.d("MainActivity", "clear: null !");
        }
        selecteNavItem(0);
    }

    public void home(View v) {
        resetFragments();
        //        mHandler.sendEmptyMessageDelayed(1,1000);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    selecteNavItem(0);
                    break;
            }
            super.handleMessage(msg);
        }
    };

}
