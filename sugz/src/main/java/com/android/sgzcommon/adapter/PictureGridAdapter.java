package com.android.sgzcommon.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.android.sgzcommon.recycleview.BaseRecyclerviewAdapter;
import com.android.sgzcommon.recycleview.BaseViewHolder;
import com.android.sgzcommon.utils.UnitUtil;
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
            mImageLoader.get(url,holder,300,300);
        } else {
            holder.mIvImage.setImageResource(R.drawable.img_load_fail);
        }
    }

    class ViewHolder extends BaseViewHolder implements ImageLoader.ImageListener {
        CornerImageView mIvImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mIvImage = itemView.findViewById(R.id.iv_image);
            mIvImage.setRoundCorner(UnitUtil.dp2px(mContext,10));
        }

        @Override
        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
            Bitmap bitmap = response.getBitmap();
            if (bitmap == null){
                mIvImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                mIvImage.setImageResource(R.drawable.img_loading);
            }else {
                mIvImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mIvImage.setImageBitmap(bitmap);
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            mIvImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            mIvImage.setImageResource(R.drawable.img_load_fail);
        }
    }


}
