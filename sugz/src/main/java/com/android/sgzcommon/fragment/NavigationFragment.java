package com.android.sgzcommon.fragment;

import android.app.Activity;

/**
 * @author sgz
 * @date 2020/6/10
 */
public abstract class NavigationFragment extends TakePhotoFragment {

    /**
     * 每次显示都执行初始化fragment，重新执行生命周期
     */
    protected boolean isInitEveryShow;

    public abstract boolean isOnlyClick();

    public abstract void onOnlyClick(Activity activity);

    public boolean isInitEveryShow() {
        return isInitEveryShow;
    }

    public void setInitEveryShow(boolean initEveryShow) {
        isInitEveryShow = initEveryShow;
    }
}
