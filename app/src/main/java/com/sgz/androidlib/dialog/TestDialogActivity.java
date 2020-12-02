package com.sgz.androidlib.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.android.sgzcommon.activity.BaseActivity;
import com.android.sgzcommon.dialog.BaseListDialog;
import com.android.sgzcommon.dialog.DatePickDialog;
import com.android.sgzcommon.view.TitleBar;
import com.sgz.androidlib.R;
import com.sgz.androidlib.dialog.sample.TestAutoDismissDialog;
import com.sgz.androidlib.dialog.sample.TestBaseTextListDialog;
import com.sgz.androidlib.entity.TextListEntity;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author sgz
 * @date 2020/9/18
 */
public class TestDialogActivity extends BaseActivity {

    @BindView(R.id.tb_title)
    TitleBar mTbTitle;
    @BindView(R.id.btn_auto_dismiss_dialog)
    Button mBtnAutoDismissDialog;
    @BindView(R.id.btn_string_list_dialog)
    Button mBtnStringListDialog;
    @BindView(R.id.btn_date_pick)
    Button mBtnDatePick;
    @BindView(R.id.btn_time_pick)
    Button mBtnTimePick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_dialog);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_auto_dismiss_dialog, R.id.btn_string_list_dialog, R.id.btn_date_pick, R.id.btn_time_pick})
    public void onViewClicked(View view) {
        Calendar calendar = Calendar.getInstance();
        switch (view.getId()) {
            case R.id.btn_auto_dismiss_dialog:
                TestAutoDismissDialog dismissDialog = new TestAutoDismissDialog(mActivity, 5000);
                dismissDialog.show();
                break;
            case R.id.btn_string_list_dialog:
                TestBaseTextListDialog dialog = new TestBaseTextListDialog(mActivity);
                dialog.setOnListClickListener(new BaseListDialog.OnListClickListener<TextListEntity>() {
                    @Override
                    public void onClick(Dialog dialog, int position, TextListEntity loadListEntity) {

                    }
                });
                //dialog.setTitleText("标题");
                dialog.show();
                break;
            case R.id.btn_date_pick:
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                showDataPickDialog(year, month, day, new DatePickDialog.OnDatePickListener() {
                    @Override
                    public void onDate(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dateString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        showToast(dateString);
                    }

                    @Override
                    public void onCancle() {

                    }
                });
                break;
            case R.id.btn_time_pick:
                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);
                showTimePickDialog(hour, minute, new DatePickDialog.OnTimePickListener() {

                    @Override
                    public void onTime(TimePicker view, int hour, int minute) {
                        String timeString = hour + ":" + minute;
                        showToast(timeString);
                    }

                    @Override
                    public void onCancle() {

                    }
                });
                break;
        }
    }
}
