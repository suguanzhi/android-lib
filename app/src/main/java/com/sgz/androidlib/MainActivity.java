package com.sgz.androidlib;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.android.sgzcommon.activity.BaseNavigationActivity;
import com.sgz.androidlib.activity.sample.TestBaseTakePhotoActivity;
import com.sgz.androidlib.fragment.SecondFragment;
import com.sgz.androidlib.fragment.sample.TestTakePhotoFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class MainActivity extends BaseNavigationActivity {

    private static String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

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
    protected Fragment getFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new SecondFragment();
        }
        return null;
    }

    @Override
    protected Intent getStartActivityIntent(int position) {
        if (2 == position) {
            Intent intent = new Intent(this, TestBaseTakePhotoActivity.class);
            return intent;
        }
        return super.getStartActivityIntent(position);
    }

    @Override
    protected boolean getNewFragment(int position) {
        if (0 == position) {
            return true;
        }
        return super.getNewFragment(position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        showToast("123456789");
    }

    @Override
    protected void onResume() {
        super.onResume();
        int itemId = getBottomNavigationViewId();
        Log.d("MainActivity", "onResume: id == " + itemId);
        checkRequestPermissions(PERMISSIONS);
    }

    public void clear(View v) {
        resetFragments();
        selecteNavItem(0);
        Fragment fragment = getFrgment(0);
        if (fragment != null) {
            if (fragment instanceof TestTakePhotoFragment) {
                TestTakePhotoFragment testTakePhotoFragment = (TestTakePhotoFragment) fragment;
                testTakePhotoFragment.setMsg("ddddddd");
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
