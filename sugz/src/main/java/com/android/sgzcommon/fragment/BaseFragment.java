package com.android.sgzcommon.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.sgzcommon.cache.BitmapCache;
import com.android.sgzcommon.dialog.DatePickDialog;
import com.android.sgzcommon.dialog.LoadingDialog;
import com.android.sgzcommon.dialog.OneButtonDialog;
import com.android.sgzcommon.dialog.TwoButtonDialog;
import com.android.sgzcommon.take_photo.listener.OnPhotoListener;
import com.android.sgzcommon.utils.SystemUtils;
import com.android.sgzcommon.volley.VolleyManager;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    protected boolean isInited;
    protected boolean isVisiableToUser;
    private String mCurrentPath;

    protected Point mWindowSize;
    protected Context mContext;
    protected Activity mActivity;
    protected BitmapCache mCache;
    protected RequestQueue mQueue;
    protected ImageLoader mImageLoader;
    protected PopupWindow mPopupWindow;
    private OnPhotoListener mPhotoListener;
    private DatePickDialog mDatePickDialog;
    private LoadingDialog mLoadingDialog;
    private OneButtonDialog mOneButtonDialog;
    private TwoButtonDialog mTwoButtonDialog;

    private static final int CAMERA_REQUEST_CODE = 436;
    private static final int REQUEST_TAKE_PHOTO_CODE = 127;

    protected abstract int getLayoutId();

    protected abstract void init(Bundle savedInstanceState, View parent);

    /**
     * fragment的views已完成绘制并第一次对用户可见，特别针对viewpager中的fragment懒加载数据。
     */
    protected void visibleToUser() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = getActivity();
        mWindowSize = SystemUtils.getWindowSize(mContext);
        mCache = VolleyManager.getInstance(mContext).getBitmapCacheInstance();
        mQueue = VolleyManager.getInstance(mContext).getRequestQueueInstance();
        mImageLoader = VolleyManager.getInstance(mContext).getImageLoaderInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        isInited = true;
        mLoadingDialog = new LoadingDialog(getActivity());
        mOneButtonDialog = new OneButtonDialog(getActivity());
        mOneButtonDialog.setButtonText("知道了");
        mTwoButtonDialog = new TwoButtonDialog(getActivity());
        init(savedInstanceState, view);
        return view;
    }

    @Override
    public void onResume() {
        if (isVisiableToUser) {
            visibleToUser();
        }
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisiableToUser = isVisibleToUser;
        if (isVisibleToUser) {
            if (isInited) {
                visibleToUser();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    protected void showLoading() {
        showLoading("正在加载");
    }

    protected void showLoading(String tip) {
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog.show(tip);
    }

    protected void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    protected LinearLayoutManager createLinearLayoutManager(@RecyclerView.Orientation int orientation) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(orientation);
        return layoutManager;
    }

    /**
     * 显示日期选择窗口
     *
     * @param month    0~11
     * @param listener
     */
    protected void showDataPickDialog(int year, int month, int day, DatePickDialog.OnDatePickListener listener) {
        if (mDatePickDialog != null && mDatePickDialog.isShowing()) {
            return;
        }
        mDatePickDialog = new DatePickDialog(getActivity(), DatePickDialog.TYPE_DATE);
        mDatePickDialog.show();
        mDatePickDialog.setDate(year, month, day);
        mDatePickDialog.setOnDatePickListener(listener);
        mDatePickDialog.setCanceledOnTouchOutside(true);
    }

    /**
     * 显示时间选择窗口
     *
     * @param
     * @param listener
     */
    protected void showTimePickDialog(int hour, int minute, DatePickDialog.OnTimePickListener listener) {
        if (mDatePickDialog != null && mDatePickDialog.isShowing()) {
            return;
        }
        mDatePickDialog = new DatePickDialog(getActivity(), DatePickDialog.TYPE_TIME);
        mDatePickDialog.show();
        mDatePickDialog.setTime(hour, minute);
        mDatePickDialog.setOnTimePickListener(listener);
        mDatePickDialog.setCanceledOnTouchOutside(true);
    }

    protected void showPopupWindow(View v, View contentView, PopupWindow.OnDismissListener listener) {
        showPopupWindow(v, contentView, v.getWidth(), WindowManager.LayoutParams.WRAP_CONTENT, listener);
    }

    protected void showPopupWindow(View v, View contentView, int width, int height, PopupWindow.OnDismissListener listener) {
        showPopupWindow(v, contentView, width, height, 0, 0, listener);
    }

    protected void showPopupWindow(View v, View contentView, int width, int height, int x, int y, PopupWindow.OnDismissListener listener) {
        mPopupWindow = new PopupWindow(width, height);
        mPopupWindow.setContentView(contentView);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(listener);
        mPopupWindow.showAsDropDown(v, x, y);
    }

    protected void dismissPopupWindow() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
        mPopupWindow = null;
    }

    protected void showToast(String text) {
        Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 显示只有一个按钮的提示框
     *
     * @param msg 提示主文本
     */
    protected void showOneButtonDialog(String msg) {
        showOneButtonDialog(msg, "", new OneButtonDialog.OnClickListener() {
            @Override
            public void onConfirm(View view, Dialog dialog) {
                if (mOneButtonDialog != null) {
                    mOneButtonDialog.dismiss();
                }
            }
        });
    }

    /**
     * 显示只有一个按钮的提示框
     *
     * @param msg       提示主文本
     * @param secondMsg 提示副文本
     */
    protected void showOneButtonDialog(String msg, String secondMsg) {
        showOneButtonDialog(msg, secondMsg, new OneButtonDialog.OnClickListener() {
            @Override
            public void onConfirm(View view, Dialog dialog) {
                if (mOneButtonDialog != null) {
                    mOneButtonDialog.dismiss();
                }
            }
        });
    }

    /**
     * 显示只有一个按钮的提示框
     *
     * @param msg        提示主文本
     * @param secondMsg  提示副文本
     * @param buttonText 按钮文本
     * @param listener   按钮点击监听
     */
    protected void showOneButtonDialog(String msg, String secondMsg, String buttonText, OneButtonDialog.OnClickListener listener) {
        showOneButtonDialog(msg, secondMsg, listener);
        mOneButtonDialog.setButtonText(buttonText);
    }

    /**
     * 显示只有一个按钮的提示框
     *
     * @param msg       提示主文本
     * @param secondMsg 提示副文本
     * @param listener  按钮点击监听
     */
    protected void showOneButtonDialog(String msg, String secondMsg, OneButtonDialog.OnClickListener listener) {
        if (mOneButtonDialog != null) {
            mOneButtonDialog.dismiss();
        }
        mOneButtonDialog.setOnclickListener(listener);
        mOneButtonDialog.setMsg(msg);
        mOneButtonDialog.setSecondMsg(secondMsg);
        mOneButtonDialog.show();
    }

    /**
     * 隐藏只有一个按钮的提示框
     */
    protected void hideOneButtonDialog() {
        if (mOneButtonDialog != null) {
            mOneButtonDialog.dismiss();
        }
    }

    /**
     * 显示有两个按钮的提示框
     *
     * @param msg 提示主文本
     */
    protected void showTwoButtonDialog(String msg) {
        showTwoButtonDialog(msg, "", new TwoButtonDialog.OnClickListener() {
            @Override
            public void onCancle(View view, Dialog dialog) {
                if (mOneButtonDialog != null) {
                    mOneButtonDialog.dismiss();
                }
            }

            @Override
            public void onConfirm(View view, Dialog dialog) {

            }
        });
    }

    /**
     * 显示有两个按钮的提示框
     *
     * @param msg       提示主文本
     * @param secondMsg 提示副文本
     */
    protected void showTwoButtonDialog(String msg, String secondMsg) {
        showTwoButtonDialog(msg, secondMsg, new TwoButtonDialog.OnClickListener() {
            @Override
            public void onCancle(View view, Dialog dialog) {
                if (mTwoButtonDialog != null) {
                    mTwoButtonDialog.dismiss();
                }
            }

            @Override
            public void onConfirm(View view, Dialog dialog) {

            }
        });
    }

    /**
     * 显示有两个按钮的提示框
     *
     * @param msg       提示主文本
     * @param secondMsg 提示副文本
     * @param leftText  左边按钮文本
     * @param rightText 右边按钮文本
     * @param listener  按钮点击监听
     */
    protected void showTwoButtonDialog(String msg, String secondMsg, String leftText, String rightText, TwoButtonDialog.OnClickListener listener) {
        showTwoButtonDialog(msg, secondMsg, listener);
        mTwoButtonDialog.setButtonLeftText(leftText);
        mTwoButtonDialog.setButtonRightText(rightText);
    }

    /**
     * 显示有两个按钮的提示框
     *
     * @param msg       提示主文本
     * @param secondMsg 提示副文本
     * @param listener  按钮点击监听
     */
    protected void showTwoButtonDialog(String msg, String secondMsg, TwoButtonDialog.OnClickListener listener) {
        if (mTwoButtonDialog != null) {
            mTwoButtonDialog.dismiss();
        }
        mTwoButtonDialog.setOnclickListener(listener);
        mTwoButtonDialog.setMsg(msg);
        mTwoButtonDialog.setSecondMsg(secondMsg);
        mTwoButtonDialog.show();
    }

    /**
     * 显示有两个按钮的提示框
     */
    protected void hideTwoButtonDialog() {
        if (mTwoButtonDialog != null) {
            mTwoButtonDialog.dismiss();
        }
    }
}
