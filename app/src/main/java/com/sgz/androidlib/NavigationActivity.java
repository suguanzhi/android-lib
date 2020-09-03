package com.sgz.androidlib;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.android.sgzcommon.activity.BaseNavigationActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class NavigationActivity extends BaseNavigationActivity {

    private static String[] PERMISSIONS = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected int getNavigationMenuId() {
        return R.menu.navigation;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getBottomNavigationViewId() {
        return R.id.nav;
    }

    @Override
    protected int getFrameLayoutId() {
        return R.id.fl_container;
    }

    @Override
    protected Fragment getNewFragment(int position) {
        switch (position){
            case 0:
                return new TakePhotoFragment();
            case 1:
                return new FunctionTestFragment();
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
    protected Intent clickStartActivity(int position) {
        if (2 == position){
            Intent intent = new Intent(this, SampleTakePhotoActivity.class);
            return intent;
        }
        return super.clickStartActivity(position);
    }

    @Override
    protected boolean newFragment4ItemSelect(int position) {
        if (0 == position){
            return true;
        }
        return super.newFragment4ItemSelect(position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int itemId = getBottomNavigationViewId();
        Log.d("MainActivity", "onResume: id == " + itemId);
        checkRequestePermissions(PERMISSIONS);
    }

    public void clear(View v) {
        resetFragments();
        selecteNavItem(0);
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
