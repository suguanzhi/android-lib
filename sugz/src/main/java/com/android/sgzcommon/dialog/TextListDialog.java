package com.android.sgzcommon.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.android.sgzcommon.recycleview.adapter.TextListAdapter;
import com.android.sgzcommon.recycleview.BaseRecyclerviewAdapter;
import com.android.sugz.R;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sgz on 2019/12/14.
 */
public class TextListDialog extends BaseDialog {

    private List<String> mStrings;
    private RecyclerView mRvList;
    private TextListAdapter mAdapter;
    private OnClickListener listener;

    public TextListDialog(Context context) {
        super(context);
        mStrings = new ArrayList<>();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.dialog_sgz_text_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRvList = findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRvList.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(mContext,R.drawable.shape_divider));
        mRvList.addItemDecoration(divider);

        mAdapter = new TextListAdapter(mContext, mStrings, new BaseRecyclerviewAdapter.OnItemtClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (listener != null) {
                    listener.onClick(TextListDialog.this, position);
                }
            }
        }, null);
        mRvList.setAdapter(mAdapter);
    }

    public void show(List<String> strings) {
        super.show();
        mStrings.clear();
        if (strings != null) {
            mStrings.addAll(strings);
        }
        if (mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onClick(Dialog dialog, int position);
    }
}
