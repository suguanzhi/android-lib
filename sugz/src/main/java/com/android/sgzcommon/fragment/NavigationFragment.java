package com.android.sgzcommon.fragment;

import android.app.Activity;

/**
 * @author sgz
 * @date 2020/6/10
 */
public abstract class NavigationFragment extends BaseFragment {

    /**
     * 每次显示都执行初始化fragment，重新执行生命周期
     */
    protected boolean isInitShow;

    public abstract boolean isOnlyClick();

    public abstract void onOnlyClick(Activity activity);

    public boolean isInitShow() {
        return isInitShow;
    }

    public void setInitShow(boolean initShow) {
        isInitShow = initShow;
    }
}
