package com.android.sgzcommon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.sugz.R;
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
public abstract class BaseNavigationActivity extends BaseActivity {

    private BottomNavigationView mNavigation;
    private FragmentManager mManager;

    protected abstract int getNavigationMenuId();

    /**
     * 获取position显示的fragment，return new fragment()；
     *
     * @param position
     * @return
     */
    protected abstract Fragment getNewFragment(int position);

    protected int getContentViewId() {
        return R.layout.activity_sgz_navigation;
    }

    protected int getBottomNavigationViewId() {
        return R.id.navigation;
    }

    protected int getFrameLayoutId() {
        return R.id.fl_nav_fragments;
    }

    /**
     * @return
     */
    protected boolean newFragment4ItemSelect(int position) {
        return false;
    }

    /**
     * @return
     */
    protected Intent clickStartActivity(int position) {
        return null;
    }

    public int getLabelVisibilityMode() {
        return LabelVisibilityMode.LABEL_VISIBILITY_LABELED;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        mManager = getSupportFragmentManager();
        mNavigation = findViewById(getBottomNavigationViewId());
        mNavigation.inflateMenu(getNavigationMenuId());
        mNavigation.setLabelVisibilityMode(getLabelVisibilityMode());
        mNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int position = getNavigationPosition(item.getItemId());
                Log.d("BaseMainActivity", "onNavigationItemSelected: position == " + position);
                Intent intent = clickStartActivity(position);
                if (intent == null) {
                    boolean newFragment = newFragment4ItemSelect(position);
                    showFragment(newFragment, position);
                    return true;
                } else {
                    startActivity(intent);
                    return false;
                }
            }
        });

        selecteNavItem(0);
    }

    public void selecteNavItem(int position) {
        Menu menu = mNavigation.getMenu();
        if (position < menu.size()) {
            MenuItem menuItem = menu.getItem(position);
            mNavigation.setSelectedItemId(menuItem.getItemId());
            menuItem.setChecked(true);
        }
    }

    protected Fragment getFrgment(int position) {
        return mManager.findFragmentByTag(position + "");
    }

    /**
     * @param remove 是否移除旧的创建新的fragment并显示
     * @return
     */
    private void showFragment(boolean remove, final int position) {
        Log.d("BaseMainActivity", "showFragment: position == " + position);
        List<Fragment> fragments = mManager.getFragments();
        Log.d("BaseMainActivity", "showFragment: fragments.size == " + fragments.size());
        FragmentTransaction ft = mManager.beginTransaction();
        for (Fragment fragment : fragments) {
            ft.hide(fragment);
        }
        Log.d("BaseMainActivity", "showFragment: 2");
        String tag = position + "";
        Fragment fragment = mManager.findFragmentByTag(tag);
        Log.d("BaseMainActivity", "showFragment: 3");
        if (fragment == null) {
            Log.d("BaseMainActivity", "showFragment: 4");
            fragment = getNewFragment(position);
        }else {
            Log.d("BaseMainActivity", "showFragment: 5");
            if (remove){
                Log.d("BaseMainActivity", "showFragment: 6");
                ft.remove(fragment);
                fragment = getNewFragment(position);
            }
        }
        if (fragment != null) {
            Log.d("BaseMainActivity", "showFragment: 7");
            Log.d("BaseMainActivity", "showFragment: 10");
            if (!fragment.isAdded()) {
                Log.d("BaseMainActivity", "showFragment: 11");
                ft.add(getFrameLayoutId(), fragment, tag);
            }
            Log.d("BaseMainActivity", "showFragment: end ");
            ft.show(fragment);
            ft.commitNow();
        }
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

