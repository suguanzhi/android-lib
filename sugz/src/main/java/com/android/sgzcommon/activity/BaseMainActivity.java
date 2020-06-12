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
    private NavigationFragment mCurrentFragment;

    protected abstract int getContentViewId();

    protected abstract int getNavigationId();

    protected abstract int getFrameLayoutId();

    protected abstract NavigationFragment newNavigationFragment(int position);

    protected abstract List<NavigationFragment> getNavigationFragments();

    public int getLabelVisibilityMode() {
        return LabelVisibilityMode.LABEL_VISIBILITY_LABELED;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        mNavigation = findViewById(getNavigationId());
        mNavigation.setLabelVisibilityMode(getLabelVisibilityMode());
        mNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("BaseMainActivity", "onNavigationItemSelected:");
                return showFragment(item.getItemId());
            }
        });
        selecteNavItem(0);
    }

    protected void selecteNavItem(int itemId) {
        mNavigation.setSelectedItemId(itemId);
    }

    protected int getSelectedItemId() {
        return mNavigation.getSelectedItemId();
    }

    /**
     * @param itemId
     * @return
     */
    private boolean showFragment(int itemId) {
        Log.d("BaseMainActivity", "showFragment: 1");
        boolean result = true;
        int position = 0;
        Menu menu = mNavigation.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.getItemId() == itemId) {
                position = i;
                break;
            }
        }
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment fragment : fragments) {
            ft.hide(fragment);
        }
        if (getNavigationFragments() != null) {
            Log.d("BaseMainActivity", "showFragment: 2");
            String tag = position + "";
            if (position < getNavigationFragments().size()) {
                Log.d("BaseMainActivity", "showFragment: 3");
                NavigationFragment fragment = (NavigationFragment) fm.findFragmentByTag(tag);
                if (fragment == null) {
                    Log.d("BaseMainActivity", "showFragment: 4");
                    fragment = getNavigationFragments().get(position);
                } else {
                    Log.d("BaseMainActivity", "showFragment: 5");
                    boolean isCreate = fragment.isCreate();
                    if (isCreate) {
                        Log.d("BaseMainActivity", "showFragment: 6");
                        ft.remove(fragment);
                        getNavigationFragments().remove(position);
                        fragment = newNavigationFragment(position);
                        getNavigationFragments().add(position, fragment);
                    }
                }
                if (fragment.isOnlyClick()) {
                    Log.d("BaseMainActivity", "showFragment: 7");
                    fragment.onOnlyClick(this);
                    result = false;
                } else {
                    if (!fragment.isAdded()) {
                        Log.d("BaseMainActivity", "showFragment: 8");
                        ft.add(getFrameLayoutId(), fragment, tag);
                    }
                    mCurrentFragment = fragment;
                    if (mCurrentFragment != null) {
                        Log.d("BaseMainActivity", "showFragment: 9");
                        ft.show(mCurrentFragment);
                        ft.commitAllowingStateLoss();
                    }
                }
            }
        }
        return result;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getNavigationFragments() != null) {
            for (NavigationFragment fragment : getNavigationFragments()) {
                if (fragment.isAdded()) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (getNavigationFragments() != null) {
            for (NavigationFragment fragment : getNavigationFragments()) {
                if (fragment.isAdded()) {
                    fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
        }
    }
}


