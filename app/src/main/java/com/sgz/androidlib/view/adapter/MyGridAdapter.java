package com.sgz.androidlib.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.android.sgzcommon.adapter.BaseAdapter;
import com.android.sgzcommon.adapter.BaseViewHolder;
import com.sgz.androidlib.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author sgz
 * @date 2020/11/18
 */
public class MyGridAdapter extends BaseAdapter<String, MyGridAdapter.ViewHolder> {


    public MyGridAdapter(Context context, List<String> items) {
        super(context, items);
    }

    @Override
    protected ViewHolder getViewHolder() {
        return new ViewHolder();
    }

    @Override
    protected void initData(ViewHolder holder, int position, String s) {
        holder.mTvName.setText(s);
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvName;

        @Override
        protected int getConverViewId() {
            return R.layout.adapter_my_gridview;
        }

        @Override
        protected void initView(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }
}
