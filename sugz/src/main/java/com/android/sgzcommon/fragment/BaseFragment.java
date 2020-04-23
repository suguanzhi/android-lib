package com.android.sgzcommon.fragment;

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
import com.android.sgzcommon.utils.SystemUtil;
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
    protected boolean isVisiable;
    protected Point mWindowSize;
    protected Context mContext;
    protected BitmapCache mCache;
    protected RequestQueue mQueue;
    protected ImageLoader mImageLoader;
    protected PopupWindow mPopupWindow;
    private DatePickDialog mDatePickDialog;
    private LoadingDialog mLoadingDialog;

    protected abstract int getLayoutId();

    protected abstract void init(Bundle savedInstanceState);

    /**
     * fragment的views已完成绘制并第一次对用户可见，特别针对viewpager中的fragment懒加载数据。
     */
    protected void userVisible(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mWindowSize = SystemUtil.getWindowSize(mContext);
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
        init(savedInstanceState);
        if (isVisiable){
            userVisible();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser){
            isVisiable = true;
            if (isInited){
                userVisible();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    protected void showLoading() {
        if (mLoadingDialog != null) {
            if (mLoadingDialog.isShowing()) {
                return;
            }
            mLoadingDialog.show();
        }
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
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}
