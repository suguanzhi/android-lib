package com.android.sgzcommon.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.android.sgzcommon.dialog.entity.TextListItem;
import com.android.sgzcommon.recycleview.BaseRecyclerviewAdapter;
import com.android.sgzcommon.recycleview.adapter.BaseLoadListAdapter;

import java.util.Map;

/**
 * @author sgz
 * @date 2020/6/30
 */
public abstract class BaseTextListDialog<V extends TextListItem> extends BaseListDialog<V> {

    private BaseLoadListAdapter mAdapter;

    protected abstract void loadList(Map<String, String> data, OnLoadListListener listener);

    public BaseTextListDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new BaseLoadListAdapter(mContext, mItems, new BaseRecyclerviewAdapter.OnItemtClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position < getItems().size()) {
                    if (mClickListener != null) {
                        mClickListener.onClick(BaseTextListDialog.this, position, mItems.get(position));
                    }
                }
            }
        }, null);
        setAdapter(mAdapter);
    }
}
