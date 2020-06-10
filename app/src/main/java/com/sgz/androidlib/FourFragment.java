package com.sgz.androidlib;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.sgzcommon.fragment.NavigationFragment;

/**
 * @author sgz
 * @date 2020/6/10
 */
public class FourFragment extends NavigationFragment {
    @Override
    public boolean isCreate() {
        return false;
    }

    @Override
    public boolean isOnlyClick() {
        return false;
    }

    @Override
    public void onOnlyClick(Activity activity) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_four;
    }

    @Override
    protected void init(Bundle savedInstanceState, View parent) {
        Log.d("NavigationFragment", "init: four init !");
    }
}
