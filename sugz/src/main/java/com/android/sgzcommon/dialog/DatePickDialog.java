package com.android.sgzcommon.dialog;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.sgzcommon.utils.DateUtils;
import com.android.sugz.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by sgz on 2017/7/7.
 */

public class DatePickDialog extends BaseDialog implements View.OnClickListener {

    private int type;
    private int mHour;
    private int mMinute;
    private TextView mConfirmTV;
    private TextView mCancleTV;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private OnDatePickListener mDateListener;
    private OnTimePickListener mTimeListener;
    public static final int TYPE_DATE = 129;
    public static final int TYPE_TIME = 508;
    public static final int TYPE_BOTH = 536;

    @Override
    protected int getContentViewId() {
        return R.layout.dialog_sgz_date_pick;
    }

    @Override
    protected int getWidth() {
        return mWindowSize.x * 9 / 10;
    }

    @Override
    protected int getHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    public DatePickDialog(Context context, int type) {
        super(context);
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * @param dateStr 格式为：yyyy-MM-dd 的日期字符串
     */
    public void setDate(String dateStr) {
        Date date = DateUtils.getDate(dateStr, "yyyy-MM-dd");
        if (date != null) {
            String yearStr = DateUtils.getDateStr(date.getTime(), "yyyy");
            String monthStr = DateUtils.getDateStr(date.getTime(), "MM");
            String dayOfMonthStr = DateUtils.getDateStr(date.getTime(), "dd");
            mDatePicker.init(Integer.parseInt(yearStr), Integer.parseInt(monthStr) - 1, Integer.parseInt(dayOfMonthStr), null);
        }
    }

    /**
     * @param year
     * @param month      0~11
     * @param dayOfMonth
     */
    public void setDate(int year, int month, int dayOfMonth) {
        mDatePicker.init(year, month, dayOfMonth, null);
    }

    public void setTime(int hour, int minute) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        }
    }

    @Override
    public void onContentChanged() {
        mDatePicker = findViewById(R.id.dp_dialog_date_pick);
        mTimePicker = findViewById(R.id.dp_dialog_time_pick);
        if (TYPE_BOTH == type) {
            mDatePicker.setVisibility(View.VISIBLE);
            mTimePicker.setVisibility(View.VISIBLE);
        } else if (TYPE_TIME == type) {
            mDatePicker.setVisibility(View.GONE);
            mTimePicker.setVisibility(View.VISIBLE);
        } else {
            mDatePicker.setVisibility(View.VISIBLE);
            mTimePicker.setVisibility(View.GONE);
        }
        mTimePicker.setIs24HourView(true);
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Log.d("DatePickDialog", "onTimeChanged: hourOfDay = " + hourOfDay + "; minute = " + minute);
                mHour = hourOfDay;
                mMinute = minute;
            }
        });
        mConfirmTV = findViewById(R.id.tv_dialog_date_pick_confirm);
        mConfirmTV.setOnClickListener(this);
        mCancleTV = findViewById(R.id.tv_dialog_date_pick_cancle);
        mCancleTV.setOnClickListener(this);
        Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        super.onContentChanged();
    }

    public void setOnDatePickListener(OnDatePickListener listener) {
        mDateListener = listener;
    }

    public void setOnTimePickListener(OnTimePickListener listener) {
        mTimeListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_dialog_date_pick_confirm) {
            if (TYPE_DATE == type) {
                if (mDateListener != null) {
                    mDateListener.onDate(mDatePicker, mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
                }
            } else if (TYPE_TIME == type) {
                if (mTimeListener != null) {
                    mTimeListener.onTime(mTimePicker, mHour, mMinute);
                }
            }
            dismiss();
        } else if (v.getId() == R.id.tv_dialog_date_pick_cancle) {
            if (TYPE_DATE == type) {
                if (mDateListener != null) {
                    mDateListener.onCancle();
                }
            } else if (TYPE_TIME == type) {
                if (mTimeListener != null) {
                    mTimeListener.onCancle();
                }
            }
            dismiss();
        }
    }

    public interface OnDatePickListener {
        void onDate(DatePicker view, int year, int monthOfYear, int dayOfMonth);

        void onCancle();
    }

    public interface OnTimePickListener {

        void onTime(TimePicker view, int hour, int minute);

        void onCancle();
    }
}
