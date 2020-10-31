package com.android.sgzcommon.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.sgzcommon.volley.VolleyManager;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

public abstract class BaseAdapter<V, H extends BaseViewHolder> extends android.widget.BaseAdapter {

    protected Context mContext;
    protected List<V> mItems;
    protected ImageLoader mImageLoader;

    protected abstract int getConverViewId();

    protected abstract H initViews(View convertView);

    protected abstract void initData(H h, int position, V v);

    public BaseAdapter(Context context, List<V> items) {
        mContext = context;
        mItems = items;
        mImageLoader = VolleyManager.getInstance(mContext).getImageLoaderInstance();
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

    /**
     * @param url
     * @param imageView
     * @param defaultResId
     * @param failureResId
     */
    protected void loadImage(String url, ImageView imageView, int defaultResId, int failureResId) {
        if (!TextUtils.isEmpty(url)) {
            imageView.setTag(url);
            mImageLoader.get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    String tag = (String) imageView.getTag();
                    String rUrl = response.getRequestUrl();
                    Bitmap bitmap = response.getBitmap();
                    if (bitmap != null) {
                        if (tag.equals(rUrl)) {
                            imageView.setImageBitmap(bitmap);
                        }
                    } else {
                        if (defaultResId > 0) {
                            imageView.setImageResource(defaultResId);
                        } else {
                            imageView.setImageBitmap(null);
                        }
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (defaultResId > 0) {
                        imageView.setImageResource(failureResId);
                    } else {
                        imageView.setImageBitmap(null);
                    }
                }
            });
        } else {
            imageView.setImageBitmap(null);
        }
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
