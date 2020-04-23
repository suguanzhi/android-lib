package com.android.sgzcommon.application;

import android.app.Application;
import android.util.Log;

import com.android.sgzcommon.logutil.SLogUtil;

/**
 * Created by sgz on 2017/4/13.
 */

public class BaseApplication extends Application {

    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            String errorInfo = Log.getStackTraceString(ex);
            SLogUtil.i(getPackageName() + "/errorlog", errorInfo);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(restartHandler);
        SLogUtil.initLog(this, getPackageName());
    }
}
