package com.sgz.androidlib;

import android.os.Bundle;
import android.widget.Button;

import com.android.sgzcommon.activity.BaseActivity;
import com.android.sgzcommon.view.LoadResultView;
import com.android.sgzcommon.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author sgz
 * @date 2020/9/18
 */
public class TestViewActivity extends BaseActivity {

    @BindView(R.id.tb_title)
    TitleBar mTbTitle;
    @BindView(R.id.btn_web_layout)
    Button mBtnWebLayout;
    @BindView(R.id.lrv)
    LoadResultView mLrv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view);
        ButterKnife.bind(this);
        mLrv.empty("暂无数据");
    }

    @OnClick(R.id.btn_web_layout)
    public void onViewClicked() {
    }
}
