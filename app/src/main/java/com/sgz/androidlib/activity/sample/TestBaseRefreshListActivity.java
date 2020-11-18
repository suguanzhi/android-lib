package com.sgz.androidlib.activity.sample;

import com.android.sgzcommon.activity.BaseRefreshListActivity;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.sgz.androidlib.R;

/**
 * @author sgz
 * @date 2020/11/18
 */
public class TestBaseRefreshListActivity extends BaseRefreshListActivity {
    @Override
    protected int getTopContainerViewId() {
        return R.layout.layout_qrcode_content;
    }

    @Override
    protected void onRefresh(RefreshLayout refreshLayout) {

    }

    @Override
    protected void onLoadMore(RefreshLayout refreshLayout) {

    }
}
