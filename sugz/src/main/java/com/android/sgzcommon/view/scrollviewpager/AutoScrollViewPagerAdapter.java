package com.android.sgzcommon.view.scrollviewpager;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.viewpager.widget.PagerAdapter;

/**
 * AutoScrollViewPager的适配器
 *
 * @author sgz
 */
public abstract class AutoScrollViewPagerAdapter extends PagerAdapter {

    private ArrayList<View> mItemViews;

    /**
     * 获取所有item的view
     */
    protected abstract ArrayList<? extends View> getItemViews();

    /**
     * 获取Viewpager的最后一个item的view，该view必须是重新new的view，即用
     * {@link #getItemViews()}的最后一个item的资源重新new一个view
     */
    protected abstract View getLastNewItemView();

    /**
     * 获取Viewpager的第一个item的view，该view必须是重新new的view，即用{@link #getItemViews()}
     * 的第一个item的资源重新new一个view
     */
    protected abstract View getFirstNewItemView();

    public AutoScrollViewPagerAdapter() {
        mItemViews = new ArrayList<View>();
    }

    @Override
    public int getCount() {
        return mItemViews.size();
    }

    private void initItemViews() {
        ArrayList<? extends View> itemViews = getItemViews();
        if (itemViews != null) {
            mItemViews.clear();
            mItemViews.addAll(itemViews);
            int size = itemViews.size();
            if (size > 1) {
                View firstItemView = getFirstNewItemView();
                View lastItemView = getLastNewItemView();
                if (firstItemView != null && lastItemView != null) {
                    itemViews.get(0).setTag("first");
                    itemViews.get(size - 1).setTag("last");
                    mItemViews.add(size, firstItemView);
                    mItemViews.add(0, lastItemView);
                }
            }
        }
    }

    @Override
    public void notifyDataSetChanged() {
        initItemViews();
        super.notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = null;
        view = mItemViews.get(position);
        if (position == 1) {
            if (container.findViewWithTag("first") == null) {
                container.addView(view);
            }
        } else if (position == getCount() - 2) {
            if (container.findViewWithTag("last") == null) {
                container.addView(view);
            }
        } else {
            container.addView(view);
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position != 1 && position != getCount() - 2) {
            if (position < mItemViews.size()) {
                container.removeView(mItemViews.get(position));
            }
        }
    }

}