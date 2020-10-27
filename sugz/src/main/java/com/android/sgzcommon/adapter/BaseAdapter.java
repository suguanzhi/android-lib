package com.android.sgzcommon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class BaseAdapter<V, H extends BaseViewHolder> extends android.widget.BaseAdapter {

    protected Context mContext;
    protected List<V> mItems;

    protected abstract int getConverViewId();

    protected abstract H initViews(View convertView);

    protected abstract void initData(H h, int position, V v);

    public BaseAdapter(Context context, List<V> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        H holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(getConverViewId(), parent, false);
            holder = initViews(convertView);
            convertView.setTag(holder);
        } else {
            holder = (H) convertView.getTag();
        }
        initData(holder, position, mItems.get(position));
        return convertView;
    }

    @Override
    public int getCount() {
        if (mItems == null) {
            return 0;
        }
        return mItems.size();
    }

    @Override
    public V getItem(int position) {
        if (mItems == null) {
            return null;
        }
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
