package com.android.sgzcommon.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.android.sgzcommon.view.LoadResultView;
import com.android.sgzcommon.view.TitleBar;
import com.android.sugz.R;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author sgz
 * @date 2020/9/10
 */
public abstract class BaseRefreshListActivity extends BaseActivity {

    protected TitleBar mTitleBar;
    protected RecyclerView mRvList;
    protected LoadResultView mLrv;
    protected LinearLayout mLlTopContainer;
    protected SmartRefreshLayout mSrlRefresh;

    @LayoutRes
    protected abstract int getTopContainerViewId();

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
        mLlTopContainer = findViewById(R.id.ll_top_container);
        try {
            LayoutInflater.from(mContext).inflate(getTopContainerViewId(), mLlTopContainer, false);
        } catch (Exception e) {
            e.printStackTrace();

        }
        mRvList.setLayoutManager(createLinearLayoutManager(RecyclerView.VERTICAL));
        mSrlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                BaseRefreshListActivity.this.onRefresh(refreshLayout);
            }
        });
        mSrlRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                BaseRefreshListActivity.this.onLoadMore(refreshLayout);
            }
        });
    }

    public void setTitleText(String title) {
        mTitleBar.setTitle(title);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRvList.setAdapter(adapter);
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

    public void error(String msg) {
        mLrv.setVisibility(View.VISIBLE);
        mRvList.setVisibility(View.GONE);
        mLrv.error(msg);
    }

    public void notEmpty() {
        mRvList.setVisibility(View.VISIBLE);
        mLrv.gone();
    }
}
