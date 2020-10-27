package com.sgz.androidlib.others.sample;

import android.os.Bundle;
import android.widget.GridView;

import com.android.sgzcommon.activity.BaseActivity;
import com.android.sgzcommon.view.TitleBar;
import com.sgz.androidlib.R;
import com.sgz.androidlib.others.sample.adapter.MyBaseAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author sgz
 * @date 2020/10/27
 */
public class TestBaseAdapterActivity extends BaseActivity {

    @BindView(R.id.tb_title)
    TitleBar mTbTitle;
    @BindView(R.id.gv)
    GridView mGv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_base_adapter);
        ButterKnife.bind(this);
        List<String> list = new ArrayList<>();
        list.add("中兴");
        list.add("中兴");
        list.add("中兴");
        list.add("中兴");
        list.add("中兴");
        list.add("中兴");
        list.add("中兴");
        list.add("中兴");
        MyBaseAdapter adapter = new MyBaseAdapter(this,list);
        mGv.setAdapter(adapter);
    }
}
