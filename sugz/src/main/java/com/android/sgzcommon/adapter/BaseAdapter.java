package com.android.sgzcommon.adapter;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.sgzcommon.cache.BitmapCache;
import com.android.sgzcommon.utils.SystemUtils;
import com.android.sgzcommon.volley.VolleyManager;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

public abstract class BaseAdapter<V, H extends BaseAdapter.BaseViewHolder> extends android.widget.BaseAdapter {

    protected Point mWindowSize;
    protected Context mContext;
    protected BitmapCache mCache;
    protected RequestQueue mQueue;
    protected ImageLoader mImageLoader;
    protected LayoutInflater mInflater;
    protected List<V> mItems;

    protected abstract int getConverViewId();

    protected abstract H initViews(View convertView);

    protected abstract void initData(H h, int position, V v);

    public BaseAdapter(Context context, List items) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mCache = VolleyManager.getInstance(mContext).getBitmapCacheInstance();
        mQueue = VolleyManager.getInstance(mContext).getRequestQueueInstance();
        mImageLoader = VolleyManager.getInstance(mContext).getImageLoaderInstance();
        mWindowSize = SystemUtils.getWindowSize(context);
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

    public class BaseViewHolder {

		private View convertView;

		public BaseViewHolder(View convertView) {
			this.convertView = convertView;
		}
	}
}
