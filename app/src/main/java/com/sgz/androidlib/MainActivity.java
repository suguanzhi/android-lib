package com.sgz.androidlib;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.android.sgzcommon.activity.BaseMainActivity;
import com.android.sgzcommon.fragment.NavigationFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
    protected NavigationFragment getNewNavigationFragment(int position) {
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
