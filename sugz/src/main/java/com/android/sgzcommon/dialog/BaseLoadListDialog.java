package com.android.sgzcommon.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.sgzcommon.dialog.entity.LoadListItem;
import com.android.sgzcommon.recycleview.BaseRecyclerviewAdapter;
import com.android.sgzcommon.recycleview.adapter.BaseLoadListAdapter;
import com.android.sgzcommon.utils.UnitUtils;
import com.android.sgzcommon.view.LoadResultView;
import com.android.sugz.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author sgz
 * @date 2020/6/30
 */
public abstract class BaseLoadListDialog<V extends LoadListItem> extends BaseDialog {

    private TextView mTvTitle;
    private ProgressBar mPbLoading;
    private RecyclerView mRvList;
    private LoadResultView mLrv;
    private BaseLoadListAdapter mAdapter;
    private OnLoadListListener mResponseListener;
    private OnLoadListClickListener listener;

    private CharSequence mTitle;
    private Map<String, String> data;
    private List<V> mItems;

    protected abstract void loadList(Map<String, String> data, OnLoadListListener listener);

    public BaseLoadListDialog(Context context) {
        super(context);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.dialog_sgz_load_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItems = new ArrayList<>();
        mTvTitle = findViewById(R.id.tv_title);
        mPbLoading = findViewById(R.id.pb_loading);
        mRvList = findViewById(R.id.rv_list);
        mLrv = findViewById(R.id.lrv);
        mResponseListener = new OnLoadListListener<V>() {
            @Override
            public void onStart() {
                mRvList.setVisibility(View.GONE);
                mPbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(List<V> items) {
                mRvList.setVisibility(View.VISIBLE);
                mPbLoading.setVisibility(View.GONE);
                mItems.clear();
                if (items != null) {
                    mItems.addAll(items);
                }
                if (mItems.size() == 0) {
                    mLrv.empty();
                } else {
                    mLrv.gone();
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed() {
                setLayoutParams(WindowManager.LayoutParams.MATCH_PARENT, UnitUtils.dp2px(300));
                mPbLoading.setVisibility(View.GONE);
                mRvList.setVisibility(View.GONE);
                mLrv.error("加载失败！");
            }
        };
        mAdapter = new BaseLoadListAdapter(mContext, mItems, new BaseRecyclerviewAdapter.OnItemtClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position < mItems.size()) {
                    if (listener != null) {
                        listener.onClick(BaseLoadListDialog.this, position, mItems.get(position));
                    }
                }
            }
        }, null);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRvList.setLayoutManager(layoutManager);
        mRvList.setAdapter(mAdapter);
        setTitle(mTitle);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLrv.gone();
        loadList(data, mResponseListener);
    }

    /**
     * 如果请求参数是变量，显示dialog需要调用该方法，否则调用show()即可。
     *
     * @param data 网络请求参数
     */
    public void show(Map<String, String> data) {
        this.data = data;
        show();
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
        if (mTvTitle != null) {
            mTvTitle.setText(title);
            if (TextUtils.isEmpty(title)) {
                mTvTitle.setVisibility(View.GONE);
            } else {
                mTvTitle.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setOnLoadListClickListener(OnLoadListClickListener<V> listener) {
        this.listener = listener;
    }

    public interface OnLoadListListener<T extends LoadListItem> {
        void onStart();

        void onSuccess(List<T> items);

        void onFailed();
    }

    public interface OnLoadListClickListener<T extends LoadListItem> {
        void onClick(Dialog dialog, int position, T t);
    }
}
