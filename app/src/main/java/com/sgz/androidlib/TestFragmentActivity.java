package com.sgz.androidlib;

import android.os.Bundle;

import com.android.sgzcommon.activity.BaseActivity;

/**
 * @author sgz
 * @date 2020/9/18
 */
public class TestFragmentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment);
    }
}
