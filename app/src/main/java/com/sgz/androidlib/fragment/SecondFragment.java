package com.sgz.androidlib.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.sgzcommon.fragment.BaseFragment;
import com.android.sgzcommon.service.entity.VersionDownload;
import com.sgz.androidlib.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author sgz
 * @date 2020/9/25
 */
public class SecondFragment extends BaseFragment {
    @BindView(R.id.btn_version_update)
    Button mBtnVersionUpdate;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_second;
    }

    @Override
    protected void init(Bundle savedInstanceState, View parent) {

    }

    @OnClick(R.id.btn_version_update)
    public void onViewClicked() {
        final String url ="http://192.168.0.129:80/upload/files/20200921/盛马桂v1_1_0_1600656839357.apk";
        EventBus.getDefault().post(new VersionDownload(url, 101));
    }
}
