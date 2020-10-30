package com.android.sgzcommon.activity;

import android.os.Bundle;
import android.view.View;

import com.android.sgzcommon.recycleview.BaseRecyclerviewAdapter;
import com.android.sgzcommon.view.LoadResultView;
import com.android.sgzcommon.view.TitleBar;
import com.android.sugz.R;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author sgz
 * @date 2020/9/10
 */
public abstract class BaseRefreshListActivity<A extends BaseRecyclerviewAdapter> extends BaseActivity {

    protected TitleBar mTitleBar;
    protected RecyclerView mRvList;
    protected LoadResultView mLrv;
    protected SmartRefreshLayout mSrlRefresh;

    protected abstract String getTitleText();

    protected abstract A getAdapter();

    protected abstract void onRefresh(RefreshLayout refreshLayout);

    protected abstract void onLoadMore(RefreshLayout refreshLayout);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgz_refresh_list);
        mTitleBar = findViewById(R.id.tb_title);
        mLrv = findViewById(R.id.lrv);
        mRvList = findViewById(R.id.rv_list);
        mSrlRefresh = findViewById(R.id.srl_refresh);
        mTitleBar.setTitle(getTitleText());
        mRvList.setLayoutManager(createLinearLayoutManager(RecyclerView.VERTICAL));
        mRvList.setAdapter(getAdapter());
        mSrlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                onRefresh(refreshLayout);
            }
        });
        mSrlRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                onLoadMore(refreshLayout);
            }
        });
    }

    public void setEnableLoadMore(boolean enable) {
        mSrlRefresh.setEnableLoadMore(enable);
    }

    public void finishRefresh() {
        mSrlRefresh.finishRefresh();
    }

    public void finishLoadMore() {
        mSrlRefresh.finishLoadMore();
    }

    public void finishLoadMoreWithNoMoreData() {
        mSrlRefresh.finishLoadMoreWithNoMoreData();
    }

    public void empty(String msg) {
        mLrv.setVisibility(View.VISIBLE);
        mRvList.setVisibility(View.GONE);
        mLrv.empty(msg);
    }

    public void notEmpty() {
        mRvList.setVisibility(View.VISIBLE);
        mLrv.gone();
    }
}
