package com.android.sgzcommon.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.sgzcommon.activity.utils.EmptyEntity;
import com.android.sgzcommon.dialog.DatePickDialog;
import com.android.sgzcommon.dialog.LoadingDialog;
import com.android.sgzcommon.dialog.OneButtonDialog;
import com.android.sgzcommon.dialog.TwoButtonDialog;
import com.android.sgzcommon.toast.SuToast;
import com.android.sgzcommon.utils.SystemUtils;
import com.android.sgzcommon.volley.VolleyManager;
import com.android.volley.toolbox.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    protected Activity mActivity;
    protected Point mWindowSize;
    protected PopupWindow mPopupWindow;
    protected ImageLoader mImageLoader;
    private DatePickDialog mDatePickDialog;
    private LoadingDialog mLoadingDialog;
    private OneButtonDialog mOneButtonDialog;
    private TwoButtonDialog mTwoButtonDialog;
    private TwoButtonDialog mPermissionDialog;
    private OnPermissionResultListener listener;

    private static final int REQUEST_TAKE_PHOTO_CODE = 510;
    protected static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    private long mBackTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mActivity = this;
        mContext = getApplicationContext();
        mWindowSize = SystemUtils.getWindowSize(this);
        mLoadingDialog = new LoadingDialog(this);
        mOneButtonDialog = new OneButtonDialog(this);
        mOneButtonDialog.setButtonText("知道了");
        mTwoButtonDialog = new TwoButtonDialog(this);
        mImageLoader = VolleyManager.getInstance(mContext).getImageLoaderInstance();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("exit", false)) {
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EmptyEntity event) {/* Do something */}


    public LinearLayoutManager createLinearLayoutManager(@RecyclerView.Orientation int orientation) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(orientation);
        return layoutManager;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void showLoading() {
        showLoading("正在加载");
    }

    public void showLoading(String tip) {
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog.show(tip);
    }

    public void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    public void showToast(String msg) {
        SuToast.showText(this, msg);
    }

    public void showToastLong(String msg) {
        SuToast.showTextLong(this, msg);
    }

    public void showSystemToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 显示日期选择窗口
     *
     * @param month    0~11
     * @param listener
     */
    public void showDataPickDialog(int year, int month, int day, DatePickDialog.OnDatePickListener listener) {
        if (mDatePickDialog != null && mDatePickDialog.isShowing()) {
            mDatePickDialog.dismiss();
        }
        mDatePickDialog = new DatePickDialog(this, DatePickDialog.TYPE_DATE);
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
    public void showTimePickDialog(int hour, int minute, DatePickDialog.OnTimePickListener listener) {
        if (mDatePickDialog != null && mDatePickDialog.isShowing()) {
            mDatePickDialog.dismiss();
        }
        mDatePickDialog = new DatePickDialog(this, DatePickDialog.TYPE_TIME);
        mDatePickDialog.show();
        mDatePickDialog.setTime(hour, minute);
        mDatePickDialog.setOnTimePickListener(listener);
        mDatePickDialog.setCanceledOnTouchOutside(true);
    }

    public void showPopupWindow(View v, View contentView, PopupWindow.OnDismissListener listener) {
        showPopupWindow(v, contentView, v.getWidth(), WindowManager.LayoutParams.WRAP_CONTENT, listener);
    }

    public void showPopupWindow(View v, View contentView, int width, int height, PopupWindow.OnDismissListener listener) {
        showPopupWindow(v, contentView, width, height, 0, 0, listener);
    }

    public void showPopupWindow(View v, View contentView, int width, int height, int x, int y, PopupWindow.OnDismissListener listener) {
        mPopupWindow = new PopupWindow(width, height);
        mPopupWindow.setContentView(contentView);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(listener);
        mPopupWindow.showAsDropDown(v, x, y);
    }

    public void hidePopupWindow() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
        mPopupWindow = null;
    }

    /**
     * 显示只有一个按钮的提示框
     *
     * @param msg 提示主文本
     */
    public void showOneButtonDialog(String msg) {
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
    public void showOneButtonDialog(String msg, String secondMsg) {
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
    public void showOneButtonDialog(String msg, String secondMsg, String buttonText, OneButtonDialog.OnClickListener listener) {
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
    public void showOneButtonDialog(String msg, String secondMsg, OneButtonDialog.OnClickListener listener) {
        if (mOneButtonDialog != null) {
            mOneButtonDialog.dismiss();
        }
        mOneButtonDialog.setOnClickListener(listener);
        mOneButtonDialog.setMsg(msg);
        mOneButtonDialog.setSecondMsg(secondMsg);
        mOneButtonDialog.show();
    }

    /**
     * 隐藏只有一个按钮的提示框
     */
    public void hideOneButtonDialog() {
        if (mOneButtonDialog != null) {
            mOneButtonDialog.dismiss();
        }
    }

    /**
     * 显示有两个按钮的提示框
     *
     * @param msg 提示主文本
     */
    public void showTwoButtonDialog(String msg) {
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
    public void showTwoButtonDialog(String msg, String secondMsg) {
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
    public void showTwoButtonDialog(String msg, String secondMsg, String leftText, String rightText, TwoButtonDialog.OnClickListener listener) {
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
    public void showTwoButtonDialog(String msg, String secondMsg, TwoButtonDialog.OnClickListener listener) {
        if (mTwoButtonDialog != null) {
            mTwoButtonDialog.dismiss();
        }
        mTwoButtonDialog.setOnClickListener(listener);
        mTwoButtonDialog.setMsg(msg);
        mTwoButtonDialog.setSecondMsg(secondMsg);
        mTwoButtonDialog.show();
    }

    /**
     * 显示有两个按钮的提示框
     */
    public void hideTwoButtonDialog() {
        if (mTwoButtonDialog != null) {
            mTwoButtonDialog.dismiss();
        }
    }

    /**
     * 权限检查
     *
     * @param neededPermissions 需要的权限
     * @return 是否被允许
     */
    public boolean checkPermissions(String[] neededPermissions) {
        boolean granted = true;
        if (neededPermissions == null) {
            return true;
        }
        for (int i = 0; i < neededPermissions.length; i++) {
            if (!checkPermission(neededPermissions[i])) {
                granted = false;
                break;
            }
        }
        return granted;
    }

    /**
     * 权限检查
     *
     * @param neededPermission 需要的权限
     * @return 是否被允许
     */
    public boolean checkPermission(String neededPermission) {
        if (neededPermission == null) {
            return true;
        }
        return ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 权限检查，并弹窗提示，适用于必须权限授权，
     *
     * @param neededPermissions
     * @return 权限已授权
     */
    public boolean checkRequestPermissions(String[] neededPermissions) {
        boolean granted = checkPermissions(neededPermissions);
        if (!granted) {
            if (mPermissionDialog == null) {
                initPermissionDialog(neededPermissions);
            }
            if (!mPermissionDialog.isShowing()) {
                mPermissionDialog.show();
            }
        } else {
            if (mPermissionDialog != null) {
                mPermissionDialog.dismiss();
            }
        }
        return granted;
    }

    /**
     * 检查所需权限是否授权，并把没有授权的权限发起请求
     *
     * @param neededPermissions 需要的权限
     * @param listener          监听允许的权限和禁止的权限
     */
    public void checkRequestPermissions(String[] neededPermissions, OnPermissionResultListener listener) {
        this.listener = listener;
        List<String> grants = new ArrayList<String>();
        List<String> denies = new ArrayList<String>();
        if (neededPermissions == null) {
            if (listener != null) {
                listener.onResult(grants, denies);
            }
        }
        for (int i = 0; i < neededPermissions.length; i++) {
            String permission = neededPermissions[i];
            if (!checkPermission(permission)) {
                denies.add(neededPermissions[i]);
            } else {
                grants.add(neededPermissions[i]);
            }
        }
        String[] ds = new String[denies.size()];
        denies.toArray(ds);
        if (ds.length > 0) {
            ActivityCompat.requestPermissions(this, ds, ACTION_REQUEST_PERMISSIONS);
        } else {
            if (listener != null) {
                listener.onResult(grants, denies);
            }
        }
    }

    /**
     * 如果有未授权的权限，初始化弹窗，点击“去授权”发起权限请求，点击“不用了”或者返回，退出应用
     *
     * @param needPermissions
     */
    private void initPermissionDialog(final String[] needPermissions) {
        mPermissionDialog = new TwoButtonDialog(this);
        mPermissionDialog.setMsg("温馨提示");
        mPermissionDialog.setSecondMsg("请授权必须权限后再使用服务。");
        mPermissionDialog.setButtonLeftText("不用了");
        mPermissionDialog.setButtonRightText("去授权");
        mPermissionDialog.setCanceledOnTouchOutside(false);
        mPermissionDialog.setOnClickListener(new TwoButtonDialog.OnClickListener() {
            @Override
            public void onCancle(View view, Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void onConfirm(View view, Dialog dialog) {
                checkRequestPermissions(needPermissions, new OnPermissionResultListener() {
                    @Override
                    public void onResult(List<String> grants, List<String> denies) {
                    }
                });
            }
        });
        mPermissionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                boolean granted = checkPermissions(needPermissions);
                if (!granted) {
                    finish();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            if (permissions.length > 0) {
                ArrayList<String> grants = new ArrayList<>();
                ArrayList<String> denies = new ArrayList<>();
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        grants.add(permission);
                    } else {
                        denies.add(permission);
                    }
                }
                if (listener != null) {
                    listener.onResult(grants, denies);
                }
            }
        }
    }

    public interface OnPermissionResultListener {
        void onResult(List<String> grants, List<String> denies);
    }

    /**
     * 连续按两次“返回”退出当前activity
     *
     * @return
     */
    public boolean backTwiceFinish() {
        return false;
    }

    @Override
    public void onBackPressed() {
        if (backTwiceFinish()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - mBackTime <= 2 * 1000) {
                finish();
                return;
            }
            mBackTime = currentTime;
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
