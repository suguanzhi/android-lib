package com.android.sgzcommon.adapter;

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
public class StringListAdapter extends BaseRecyclerviewAdapter<StringListAdapter.ViewHolder> {

    public StringListAdapter(Context context, List list, BaseViewHolder.OnItemtClickListener clickListener, BaseViewHolder.OnItemtLongClickListener longClickListener) {
        super(context, list, clickListener, longClickListener);
    }

    @Override
    protected int getItemViewId(int viewType) {
        return R.layout.adapter_string_list;
    }

    @Override
    protected ViewHolder getViewHolder(int viewType, View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String text = (String) mObjects.get(position);
        holder.mTvString.setText(text);
    }

    class ViewHolder extends BaseViewHolder{

        TextView mTvString;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvString = itemView.findViewById(R.id.tv_string);
        }
    }
}
