package com.android.sgzcommon.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.android.sgzcommon.dialog.entity.LoadListItem;
import com.android.sgzcommon.recycleview.BaseRecyclerviewAdapter;
import com.android.sgzcommon.recycleview.BaseViewHolder;
import com.android.sugz.R;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * @author sgz
 * @date 2020/6/30
 */
public class BaseLoadListAdapter extends BaseRecyclerviewAdapter<BaseLoadListAdapter.ViewHolder> {


    public BaseLoadListAdapter(Context context, List<? extends LoadListItem> list, OnItemtClickListener clickListener, OnItemtLongClickListener longClickListener) {
        super(context, list, clickListener, longClickListener);
    }

    @Override
    protected int getItemViewId(int viewType) {
        return R.layout.adapter_base_load_list;
    }

    @Override
    protected ViewHolder getViewHolder(int viewType, View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoadListItem item = (LoadListItem) mObjects.get(position);
        holder.mTvName.setText(item.getItemName());
        if (position == mObjects.size() - 1) {
            holder.mVLine.setVisibility(View.GONE);
        } else {
            holder.mVLine.setVisibility(View.VISIBLE);
        }
    }

    class ViewHolder extends BaseViewHolder {

        TextView mTvName;
        View mVLine;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mVLine = itemView.findViewById(R.id.v_line);
        }
    }
}
