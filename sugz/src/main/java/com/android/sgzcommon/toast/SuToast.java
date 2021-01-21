package com.android.sgzcommon.toast;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sugz.R;


/**
 * Created by sgz on 2016/4/28.
 */
public class SuToast extends Toast {

    private TextView mTvToast;
    private LayoutInflater mInflater;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public SuToast(Context context) {
        super(context);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.layout_sgz_toast, null);
        setView(view);
        mTvToast = view.findViewById(R.id.tv_toast);
        setGravity(Gravity.CENTER, 0, 0);
    }

    private void setToastText(CharSequence text) {
        mTvToast.setText(text);
    }

    public static void showText(Context context, CharSequence text) {
        SuToast toast = new SuToast(context);
        toast.setToastText(text);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showTextLong(Context context, CharSequence text) {
        SuToast toast = new SuToast(context);
        toast.setToastText(text);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }
}
