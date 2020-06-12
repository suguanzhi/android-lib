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

    private int mCurrentPosition;
    private BottomNavigationView mNavigation;
    private NavigationFragment mCurrentFragment;

    protected abstract int getContentViewId();

    protected abstract int getNavigationId();

    protected abstract int getFrameLayoutId();

    protected abstract NavigationFragment newNavigationFragment(int position);

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
                Log.d("BaseMainActivity", "onNavigationItemSelected: ");
                int position = 0;
                Menu menu = mNavigation.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem menuItem = menu.getItem(i);
                    if (menuItem.getItemId() == item.getItemId()) {
                        position = i;
                        break;
                    }
                }
                Log.d("BaseMainActivity", "onNavigationItemSelected: position == " + position);
                return showFragment(position);
            }
        });
        selecteNavItem(0);
    }

    /**
     * @param fragment
     */
    protected void addFragment(NavigationFragment fragment) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            int size = fm.getFragments().size();
            String tag = fragment.getTag();
            Fragment f = fm.findFragmentByTag(tag);
            if (f == null) {
                ft.add(getFrameLayoutId(), fragment, size + "");
            }
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * @return
     */
    protected List<Fragment> getFragments() {
        return getSupportFragmentManager().getFragments();
    }

    /**
     * @param position
     */
    public void selecteNavItem(int position) {
        Menu menu = mNavigation.getMenu();
        if (position < menu.size()) {
            MenuItem menuItem = menu.getItem(position);
            mNavigation.setSelectedItemId(menuItem.getItemId());
        }
    }

    /**
     * @param position
     * @return
     */
    private boolean showFragment(int position) {
        Log.d("BaseMainActivity", "showFragment: position == " + position);
        boolean result = true;
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment fragment : fragments) {
            ft.hide(fragment);
        }
        String tag = position + "";
        NavigationFragment fragment = (NavigationFragment) fm.findFragmentByTag(tag);
        Log.d("BaseMainActivity", "showFragment: 1");
        if (fragment != null) {

            boolean isCreate = fragment.isCreate();
            if (isCreate) {
                ft.remove(fragment);
                fragment = newNavigationFragment(position);
            }
            if (fragment.isOnlyClick()) {
                fragment.onOnlyClick(this);
                result = false;
            } else {
                Fragment f = fm.findFragmentByTag(tag);
                if (f == null) {
                    ft.add(getFrameLayoutId(), fragment, tag);
                }
                mCurrentFragment = fragment;
                mCurrentPosition = position;
                if (mCurrentFragment != null) {
                    ft.show(mCurrentFragment);
                    ft.commitAllowingStateLoss();
                }
            }
        }
        return result;
    }

    /**
     *
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
        for (Fragment fragment : getFragments()) {
            if (fragment.isAdded()) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (Fragment fragment : getFragments()) {
            if (fragment.isAdded()) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}


