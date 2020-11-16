package com.android.sgzcommon.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.sgzcommon.recycleview.BaseRecyclerviewAdapter;
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
 * @date 2020/9/25
 */
public abstract class BaseListDialog<V> extends BaseDialog {

    RecyclerView mRvList;
    ImageView mIvDelete;
    TextView mTvTitle;
    ProgressBar mPbLoading;
    LoadResultView mLrv;
    private CharSequence mTitle;
    private BaseRecyclerviewAdapter mAdapter;
    protected OnLoadListListener<V> mLoadListener;
    protected OnListClickListener<V> mClickListener;
    protected List<V> mItems;
    private Map<String, String> mData;

    protected abstract void loadList(Map<String, String> data, OnLoadListListener listener);

    public BaseListDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItems = new ArrayList<>();
        mTvTitle = findViewById(R.id.tv_title);
        mPbLoading = findViewById(R.id.pb_loading);
        mRvList = findViewById(R.id.rv_list);
        mLrv = findViewById(R.id.lrv);
        mIvDelete = findViewById(R.id.iv_delete);
        mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mLoadListener = new OnLoadListListener<V>() {
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
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed() {
                setLayoutParams(getWidth(), UnitUtils.dp2px(300));
                mPbLoading.setVisibility(View.GONE);
                mRvList.setVisibility(View.GONE);
                mLrv.error("加载失败！");
            }
        };
        setTitle(mTitle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRvList.setLayoutManager(layoutManager);
        setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLrv.gone();
        loadList(mData, mLoadListener);
    }

    /**
     * @param title
     */
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

    /**
     *
     * @return
     */
    public CharSequence getTitle() {
        return mTitle;
    }

    /**
     * @param adapter
     */
    public void setAdapter(BaseRecyclerviewAdapter adapter) {
        mAdapter = adapter;
        if (mRvList != null) {
            mRvList.setAdapter(mAdapter);
        }
    }

    /**
     * @return
     */
    public List<V> getItems() {
        return mItems;
    }

    /**
     * 如果请求参数是变量，显示dialog需要调用该方法，否则调用show()即可。
     *
     * @param data 网络请求参数
     */
    public void show(Map<String, String> data) {
        this.mData = data;
        show();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.dialog_sgz_select_list;
    }

    public void setOnListClickListener(OnListClickListener<V> listener) {
        this.mClickListener = listener;
    }

    public interface OnLoadListListener<V> {
        void onStart();

        void onSuccess(List<V> items);

        void onFailed();
    }

    public interface OnListClickListener<V> {
        void onClick(Dialog dialog, int position, V v);
    }
}
