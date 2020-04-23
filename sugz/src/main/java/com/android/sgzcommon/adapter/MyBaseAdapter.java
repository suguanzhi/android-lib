package com.android.sgzcommon.adapter;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.android.sgzcommon.cache.BitmapCache;
import com.android.sgzcommon.utils.SystemUtil;
import com.android.sgzcommon.volley.VolleyManager;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

public abstract class MyBaseAdapter extends BaseAdapter {

	protected Point mWindowSize;
	protected Context mContext;
	protected BitmapCache mCache;
	protected RequestQueue mQueue;
	protected ImageLoader mImageLoader;
	protected LayoutInflater mInflater;
	protected List<Object> mObjects;
	
	public MyBaseAdapter(Context context, List<? extends Object> objects){
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mCache = VolleyManager.getInstance(mContext).getBitmapCacheInstance();
		mQueue = VolleyManager.getInstance(mContext).getRequestQueueInstance();
		mImageLoader = VolleyManager.getInstance(mContext).getImageLoaderInstance();
		mWindowSize = SystemUtil.getWindowSize(context);
		mObjects = (List<Object>) objects;
	}
	
	@Override
	public int getCount() {
		return mObjects.size();
	}
	
	@Override
	public Object getItem(int position) {
		return mObjects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
