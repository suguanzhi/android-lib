package com.android.sgzcommon.take_photo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;

import com.android.sgzcommon.recycleview.BaseRecyclerviewAdapter;
import com.android.sgzcommon.recycleview.BaseViewHolder;
import com.android.sgzcommon.view.imageview.CornerImageView;
import com.android.sugz.R;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by sgz on 2019/5/6 0006.
 */
public class PictureGridAdapter extends BaseRecyclerviewAdapter<PictureGridAdapter.ViewHolder> {


    public PictureGridAdapter(Context context, List list, BaseViewHolder.OnItemtClickListener clickListener, BaseViewHolder.OnItemtLongClickListener longClickListener) {
        super(context, list, clickListener, longClickListener);
    }

    @Override
    protected int getItemViewId(int viewType) {
        return R.layout.adapter_picture_grid;
    }

    @Override
    protected ViewHolder getViewHolder(int viewType, View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String url = (String) mObjects.get(position);
        if (!TextUtils.isEmpty(url)) {
            mImageLoader.get(url,holder,200,300);
        } else {
            holder.mIvImage.setImageResource(R.drawable.img_load_fail);
        }
    }

    class ViewHolder extends BaseViewHolder implements ImageLoader.ImageListener {
        CornerImageView mIvImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mIvImage = itemView.findViewById(R.id.iv_image);
        }

        @Override
        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
            Bitmap bitmap = response.getBitmap();
            if (bitmap == null){
                mIvImage.setImageResource(R.drawable.img_loading);
            }else {
                mIvImage.setImageBitmap(bitmap);
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            mIvImage.setImageResource(R.drawable.img_load_fail);
        }
    }


}
