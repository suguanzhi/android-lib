package com.sgz.androidlib;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.sgzcommon.dialog.BaseLoadListDialog;
import com.android.sgzcommon.fragment.NavigationFragment;
import com.sgz.androidlib.entity.LoadListEntity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author sgz
 * @date 2020/6/10
 */
public class FunctionTestFragment extends NavigationFragment {

    @BindView(R.id.btn_auto_dismiss_dialog)
    Button mBtnAutoDismissDialog;
    @BindView(R.id.btn_string_list_dialog)
    Button mBtnStringListDialog;

    @Override
    public boolean isOnlyClick() {
        return false;
    }

    @Override
    public void onOnlyClick(Activity activity) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_function_test;
    }

    @Override
    protected void init(Bundle savedInstanceState, View parent) {
        Log.d("NavigationFragment", "init: two init !");
    }

    @OnClick({R.id.btn_auto_dismiss_dialog, R.id.btn_string_list_dialog})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_auto_dismiss_dialog:
                TestAutoDismissDialog dismissDialog = new TestAutoDismissDialog(mActivity,5000);
                dismissDialog.show();
                break;
            case R.id.btn_string_list_dialog:
                TestBaseLoadListDialog dialog = new TestBaseLoadListDialog(mActivity);
                dialog.setOnLoadListClickListener(new BaseLoadListDialog.OnLoadListClickListener<LoadListEntity>() {
                    @Override
                    public void onClick(int position, LoadListEntity loadListEntity) {

                    }
                });
                dialog.setTitle("请选择");
                dialog.show();
                break;
        }
    }
}
