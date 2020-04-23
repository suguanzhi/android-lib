package com.android.sgzcommon.volley;

import android.content.Context;

import com.android.sgzcommon.cache.BitmapCache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleyManager {

	private BitmapCache mCache;
	private RequestQueue mQueue;
	private ImageLoader mImageLoader;
	private static VolleyManager mManager;
	
	private VolleyManager(Context context){
		mCache = new BitmapCache();
		mQueue = Volley.newRequestQueue(context);
		mImageLoader = new ImageLoader(mQueue, mCache);
	}
	
	public static VolleyManager getInstance(Context context){
		if (mManager == null) {
			synchronized(VolleyManager.class){
				if (mManager == null) {
					mManager = new VolleyManager(context);
				}
			}
		}
		return mManager;
	}
	
	public BitmapCache getBitmapCacheInstance() {
		return mCache;
	}
	
	public RequestQueue getRequestQueueInstance() {
		return mQueue;
	}
	
	public ImageLoader getImageLoaderInstance(){
		return mImageLoader;
	}
}
