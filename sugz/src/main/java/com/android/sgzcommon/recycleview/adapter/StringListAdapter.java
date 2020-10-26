package com.android.sgzcommon.recycleview.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.android.sgzcommon.recycleview.BaseRecyclerviewAdapter;
import com.android.sgzcommon.recycleview.BaseViewHolder;
import com.android.sugz.R;

import java.util.List;

/**
 * Created by sgz on 2019/12/14.
 */
public class StringListAdapter extends BaseRecyclerviewAdapter<String,StringListAdapter.ViewHolder> {

    public StringListAdapter(Context context, List list, OnItemtClickListener clickListener, OnItemtLongClickListener longClickListener) {
        super(context, list, clickListener, longClickListener);
    }

    @Override
    protected int getItemViewId(int viewType) {
        return R.layout.adapter_sgz_string_list;
    }

    @Override
    protected ViewHolder getViewHolder(int viewType, View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTvString.setText(getItem(position));
        if (position == mItems.size() - 1) {
            holder.mVLine.setVisibility(View.GONE);
        } else {
            holder.mVLine.setVisibility(View.VISIBLE);
        }
    }

    class ViewHolder extends BaseViewHolder {

        TextView mTvString;
        View mVLine;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvString = itemView.findViewById(R.id.tv_string);
            mVLine = itemView.findViewById(R.id.v_line);
        }
    }
}
