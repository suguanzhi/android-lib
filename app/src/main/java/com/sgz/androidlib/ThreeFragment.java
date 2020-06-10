package com.sgz.androidlib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.sgzcommon.fragment.NavigationFragment;

/**
 * @author sgz
 * @date 2020/6/10
 */
public class ThreeFragment extends NavigationFragment {
    @Override
    public boolean isCreate() {
        return false;
    }

    @Override
    public boolean isOnlyClick() {
        return true;
    }

    @Override
    public void onOnlyClick(Activity activity) {
        Intent intent = new Intent(activity,LoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_three;
    }

    @Override
    protected void init(Bundle savedInstanceState, View parent) {
        Log.d("NavigationFragment", "init: three init !");
    }
}
