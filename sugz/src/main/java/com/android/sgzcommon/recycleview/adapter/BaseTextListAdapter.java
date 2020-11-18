package com.android.sgzcommon.recycleview.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.android.sgzcommon.dialog.entity.TextListItem;
import com.android.sgzcommon.recycleview.BaseRecyclerviewAdapter;
import com.android.sgzcommon.recycleview.BaseViewHolder;
import com.android.sugz.R;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * @author sgz
 * @date 2020/6/30
 */
public class BaseTextListAdapter extends BaseRecyclerviewAdapter<TextListItem, BaseTextListAdapter.ViewHolder> {


    public BaseTextListAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    protected int getItemViewId(int viewType) {
        return R.layout.adapter_sgz_text_list;
    }

    @Override
    protected ViewHolder getViewHolder(int viewType, View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTvText.setText(getItem(position).getItemName());
        if (position == mItems.size() - 1) {
            holder.mVLine.setVisibility(View.GONE);
        } else {
            holder.mVLine.setVisibility(View.VISIBLE);
        }
    }

    class ViewHolder extends BaseViewHolder {

        TextView mTvText;
        View mVLine;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvText = itemView.findViewById(R.id.tv_text);
            mVLine = itemView.findViewById(R.id.v_divider);
        }
    }
}
