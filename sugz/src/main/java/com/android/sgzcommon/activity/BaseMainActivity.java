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
    private List<NavigationFragment> mNavigationFragments;

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
        mNavigationFragments = getNavigationFragments();
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

    public void selecteNavItem(int position) {
        Menu menu = mNavigation.getMenu();
        if (position < menu.size()) {
            MenuItem menuItem = menu.getItem(position);
            mNavigation.setSelectedItemId(menuItem.getItemId());
        }
    }

    private boolean showFragment(int position) {
        boolean result = true;
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment fragment : fragments) {
            ft.hide(fragment);
        }
        if (mNavigationFragments != null) {
            if (position < mNavigationFragments.size()) {
                NavigationFragment fragment = (NavigationFragment) fm.findFragmentByTag(position + "");
                if (fragment == null) {
                    fragment = mNavigationFragments.get(position);
                } else {
                    boolean isCreate = fragment.isCreate();
                    if (isCreate) {
                        ft.remove(fragment);
                        mNavigationFragments.remove(position);
                        fragment = newNavigationFragment(position);
                        mNavigationFragments.add(position, fragment);
                    }
                }
                if (fragment.isOnlyClick()) {
                    fragment.onOnlyClick(this);
                    result = false;
                } else {
                    if (!fragment.isAdded()) {
                        ft.add(getFrameLayoutId(), fragment, position + "");
                    }
                    mCurrentFragment = fragment;
                    mCurrentPosition = position;
                    if (mCurrentFragment != null) {
                        ft.show(mCurrentFragment);
                        ft.commitAllowingStateLoss();
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mNavigationFragments != null) {
            for (NavigationFragment fragment : mNavigationFragments) {
                if (fragment.isAdded()) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mNavigationFragments != null) {
            for (NavigationFragment fragment : mNavigationFragments) {
                if (fragment.isAdded()) {
                    fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
        }
    }
}
