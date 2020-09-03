package com.sgz.androidlib;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.sgzcommon.fragment.BaseFragment;

/**
 * @author sgz
 * @date 2020/6/10
 */
public class FiveFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_five;
    }

    @Override
    protected void init(Bundle savedInstanceState, View parent) {
        Log.d("NavigationFragment", "init: five init !");
    }
}
