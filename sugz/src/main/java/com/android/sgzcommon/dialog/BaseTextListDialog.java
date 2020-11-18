package com.android.sgzcommon.dialog;

import android.content.Context;
import android.os.Bundle;

import com.android.sgzcommon.dialog.entity.TextListItem;
import com.android.sgzcommon.recycleview.BaseRecyclerviewAdapter;
import com.android.sgzcommon.recycleview.adapter.BaseTextListAdapter;

import java.util.List;
import java.util.Map;

/**
 * @author sgz
 * @date 2020/6/30
 */
public abstract class BaseTextListDialog<V extends TextListItem> extends BaseListDialog<V> {

    protected abstract void loadList(Map<String, String> data, OnLoadListResponse response);

    @Override
    protected BaseRecyclerviewAdapter getAdapter(List<V> items) {
        return new BaseTextListAdapter(mContext, items);
    }

    public BaseTextListDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
