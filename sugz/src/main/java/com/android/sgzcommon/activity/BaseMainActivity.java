package com.android.sgzcommon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.sgzcommon.fragment.NavigationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * @author sgz
 * @date 2020/6/10
 */
public abstract class BaseMainActivity extends BaseActivity {

    private BottomNavigationView mNavigation;
    private FragmentManager mManager;

    protected abstract int getContentViewId();

    protected abstract int getNavigationId();

    protected abstract int getFrameLayoutId();

    protected abstract NavigationFragment getNewNavigationFragment(int position);

    public int getLabelVisibilityMode() {
        return LabelVisibilityMode.LABEL_VISIBILITY_LABELED;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        mManager = getSupportFragmentManager();
        mNavigation = findViewById(getNavigationId());
        mNavigation.setLabelVisibilityMode(getLabelVisibilityMode());
        mNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("BaseMainActivity", "onNavigationItemSelected:");
                return showFragment(false, getNavigationPosition(item.getItemId()));
            }
        });
        selecteNavItem(0);
    }

    public void selecteNavItem(int position) {
        Menu menu = mNavigation.getMenu();
        if (position < menu.size()) {
            MenuItem menuItem = menu.getItem(position);
            mNavigation.setSelectedItemId(menuItem.getItemId());
        }
    }

    protected Fragment getFrgment(int position) {
        return mManager.findFragmentByTag(position + "");
    }

    /**
     * @param reset
     * @return
     */
    private boolean showFragment(boolean reset, final int position) {
        boolean result = true;
        Log.d("BaseMainActivity", "showFragment: position == " + position);
        List<Fragment> fragments = mManager.getFragments();
        Log.d("BaseMainActivity", "showFragment: fragments.size == " + fragments.size());
        FragmentTransaction ft = mManager.beginTransaction();
        for (Fragment fragment : fragments) {
            if (reset) {
                ft.remove(fragment);
            } else {
                ft.hide(fragment);
            }
        }
        Log.d("BaseMainActivity", "showFragment: 2");
        String tag = position + "";
        NavigationFragment fragment = (NavigationFragment) mManager.findFragmentByTag(tag);
        Log.d("BaseMainActivity", "showFragment: 3");
        if (fragment == null || reset) {
            Log.d("BaseMainActivity", "showFragment: 4");
            fragment = getNewNavigationFragment(position);
        } else {
            Log.d("BaseMainActivity", "showFragment: 5");
            if (fragment.isInitShow()) {
                Log.d("BaseMainActivity", "showFragment: 6");
                ft.remove(fragment);
                fragment = getNewNavigationFragment(position);
            }
        }
        if (fragment != null) {
            Log.d("BaseMainActivity", "showFragment: 7");
            if (fragment.isOnlyClick()) {
                Log.d("BaseMainActivity", "showFragment: 8");
                fragment.onOnlyClick(this);
                result = false;
            } else {
                Log.d("BaseMainActivity", "showFragment: 10");
                if (!fragment.isAdded()) {
                    Log.d("BaseMainActivity", "showFragment: 11");
                    ft.add(getFrameLayoutId(), fragment, tag);
                }
                Log.d("BaseMainActivity", "showFragment: end ");
                ft.show(fragment);
                ft.commitAllowingStateLoss();
            }
        }
        return result;
    }

    /**
     * @return
     */
    protected int getNavigationPosition() {
        return getNavigationPosition(mNavigation.getSelectedItemId());
    }

    /**
     * @return
     */
    private int getNavigationPosition(int itemId) {
        int position = 0;
        Menu menu = mNavigation.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.getItemId() == itemId) {
                position = i;
                break;
            }
        }
        return position;
    }

    /**
     * 移除已添加的Fragments
     */
    protected void clearFragments() {
        try {
            FragmentManager fm = getSupportFragmentManager();
            List<Fragment> fragments = fm.getFragments();
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment fragment : fragments) {
                ft.remove(fragment);
            }
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置Fragments
     */
    protected void resetFragments() {
        showFragment(true, getNavigationPosition());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : mManager.getFragments()) {
            if (fragment.isAdded()) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (Fragment fragment : mManager.getFragments()) {
            if (fragment.isAdded()) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}


