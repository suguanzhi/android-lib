package com.android.sgzcommon.take_photo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.sgzcommon.adapter.MyBaseAdapter;
import com.android.sgzcommon.take_photo.utils.CircleTransform;
import com.android.sgzcommon.utils.UnitUtil;
import com.android.sugz.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by sgz on 2019/5/6 0006.
 */
public class PictureGridEditAdapter2 extends MyBaseAdapter {

    public PictureGridEditAdapter2(Context context, List<?> objects) {
        super(context, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String path = (String) mObjects.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_picture_grid_edit, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mIvImage = (ImageView) convertView.findViewById(R.id.iv_image);
            viewHolder.mIvDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //聚焦，防止自动滚动
                parent.setFocusable(true);
                parent.setFocusableInTouchMode(true);
                parent.requestFocus();

                mObjects.remove(position);
                notifyDataSetChanged();
            }
        });
        Picasso.with(mContext).load(new File(path)).fit().centerCrop().transform(new CircleTransform(300, 300, UnitUtil.dp2px(6))).into(viewHolder.mIvImage);
        return convertView;
    }

    class ViewHolder {
        ImageView mIvImage;
        ImageView mIvDelete;
    }
}
