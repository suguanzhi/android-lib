package com.android.sgzcommon.fragment;

import android.app.Activity;

/**
 * @author sgz
 * @date 2020/6/10
 */
public abstract class NavigationFragment extends BaseFragment {
    public abstract boolean isCreate();

    public abstract boolean isOnlyClick();

    public abstract void onOnlyClick(Activity activity);
}
