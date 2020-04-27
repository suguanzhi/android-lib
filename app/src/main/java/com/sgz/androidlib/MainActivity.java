package com.sgz.androidlib;

import android.os.Bundle;

import com.android.sgzcommon.activity.TakePhotoActivity;

public class MainActivity extends TakePhotoActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getImagesGridLayoutId() {
        return 0;
    }

    @Override
    protected int getImagesGridEditLayoutId() {
        return R.id.rv_list;
    }

    @Override
    protected void onPictureClick(String url) {

    }

    @Override
    protected void onPictureEditClick(String path) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
