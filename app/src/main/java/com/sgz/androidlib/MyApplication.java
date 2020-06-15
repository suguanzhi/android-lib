package com.sgz.androidlib;

import com.android.sgzcommon.application.BaseApplication;

/**
 * @author sgz
 * @date 2020/6/15
 */
public class MyApplication extends BaseApplication {
    @Override
    protected boolean isRunningLog() {
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
