package com.android.sgzcommon.adapter;

import android.view.View;

public abstract class BaseViewHolder {

    private View convertView;

    protected abstract int getConverViewId();

    protected abstract void initView(View convertView);

    public BaseViewHolder() {
    }

    public View getConvertView() {
        return convertView;
    }

    public void setConvertView(View convertView) {
        this.convertView = convertView;
        initView(this.convertView);
    }
}