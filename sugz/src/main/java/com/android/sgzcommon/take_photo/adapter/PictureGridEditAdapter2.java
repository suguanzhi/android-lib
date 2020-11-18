package com.android.sgzcommon.take_photo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.android.sgzcommon.adapter.BaseAdapter;
import com.android.sgzcommon.adapter.BaseViewHolder;
import com.android.sgzcommon.take_photo.utils.CircleTransform;
import com.android.sgzcommon.utils.UnitUtils;
import com.android.sugz.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


/**
 * Created by sgz on 2019/5/6 0006.
 */
public class PictureGridEditAdapter2 extends BaseAdapter<String, PictureGridEditAdapter2.ViewHolder> {

    public PictureGridEditAdapter2(Context context, List objects) {
        super(context, objects);
    }

    @Override
    protected ViewHolder getViewHolder() {
        return new ViewHolder();
    }

    @Override
    protected void initData(ViewHolder viewHolder, int position, String s) {
        viewHolder.mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItems.remove(position);
                notifyDataSetChanged();
            }
        });
        Picasso.with(mContext).load(new File(s)).fit().centerCrop().transform(new CircleTransform(300, 300, UnitUtils.dp2px(6))).into(viewHolder.mIvImage);
    }


    public class ViewHolder extends BaseViewHolder {
        ImageView mIvImage;
        ImageView mIvDelete;

        @Override
        protected int getConverViewId() {
            return R.layout.adapter_sgz_picture_grid_edit;
        }

        @Override
        protected void initView(View convertView) {
            mIvImage = convertView.findViewById(R.id.civ_image);
            mIvDelete = convertView.findViewById(R.id.iv_delete);
        }
    }
}
