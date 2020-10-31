package com.sgz.androidlib.others.sample.adapter;

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
 * @date 2020/10/27
 */
public class MyBaseAdapter extends BaseAdapter<String, MyBaseAdapter.ViewHolder> {

    public MyBaseAdapter(Context context, List items) {
        super(context, items);

    }

    @Override
    protected int getConverViewId() {
        return R.layout.adapter_test_base_adapter;
    }

    @Override
    protected ViewHolder initViews(View convertView) {
        return new ViewHolder(convertView);
    }

    @Override
    protected void initData(ViewHolder holder, int position, String s) {
        holder.mTvText.setText(s);
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_text)
        TextView mTvText;

        public ViewHolder(View convertView) {
            super(convertView);
            ButterKnife.bind(this,convertView);
        }
    }
}
