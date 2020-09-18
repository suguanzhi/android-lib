package com.sgz.androidlib;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.sgzcommon.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author sgz
 * @date 2020/6/10
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.btn_views)
    Button mBtnViews;
    @BindView(R.id.btn_dialogs)
    Button mBtnDialogs;
    @BindView(R.id.btn_activity)
    Button mBtnActivity;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init(Bundle savedInstanceState, View parent) {
        Log.d("NavigationFragment", "init: two init !");
    }

//    @OnClick({R.id.btn_auto_dismiss_dialog, R.id.btn_string_list_dialog, R.id.btn_web_layout, R.id.btn_web_ac, R.id.btn_qrcodet})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btn_auto_dismiss_dialog:
//                TestAutoDismissDialog dismissDialog = new TestAutoDismissDialog(mActivity, 5000);
//                dismissDialog.show();
//                break;
//            case R.id.btn_string_list_dialog:
//                TestBaseLoadListDialog dialog = new TestBaseLoadListDialog(mActivity);
//                dialog.setOnLoadListClickListener(new BaseLoadListDialog.OnLoadListClickListener<LoadListEntity>() {
//                    @Override
//                    public void onClick(int position, LoadListEntity loadListEntity) {
//
//                    }
//                });
//                dialog.setTitle("请选择");
//                dialog.show();
//                break;
//            case R.id.btn_web_layout:
//                Intent intent = new Intent(mContext, TestWebLayoutActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.btn_web_ac:
//                Intent intent1 = new Intent(mContext, TestWebActivity.class);
//                intent1.putExtra("url", "http://www.hao123.com");
//                startActivity(intent1);
//                break;
//            case R.id.btn_qrcodet:
//                Intent intent2 = new Intent(mContext, BarCodeActivity.class);
//                startActivity(intent2);
//                break;
//        }
//    }

    @OnClick({R.id.btn_views, R.id.btn_dialogs, R.id.btn_activity,R.id.btn_fragment})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_views:
                 intent = new Intent(mContext, TestViewActivity.class);
                break;
            case R.id.btn_dialogs:
                intent = new Intent(mContext, TestDialogActivity.class);
                break;
            case R.id.btn_activity:
                intent = new Intent(mContext, TestActivityActivity.class);
                break;
            case R.id.btn_fragment:
                intent = new Intent(mContext, TestFragmentActivity.class);
                break;
        }
        if (intent != null){
            startActivity(intent);
        }
    }
}
