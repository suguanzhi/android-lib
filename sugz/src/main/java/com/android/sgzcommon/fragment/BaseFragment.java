package com.android.sgzcommon.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
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
import com.android.sgzcommon.take_photo.listener.OnTakePhotoListener;
import com.android.sgzcommon.utils.SystemUtil;
import com.android.sgzcommon.volley.VolleyManager;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    protected boolean isInited;
    protected boolean isVisiableToUser;
    private String mCurrentPath;
    private File mPhotoDir;

    protected Point mWindowSize;
    protected Context mContext;
    protected Activity mActivity;
    protected BitmapCache mCache;
    protected RequestQueue mQueue;
    protected ImageLoader mImageLoader;
    protected PopupWindow mPopupWindow;
    private OnTakePhotoListener mPhotoListener;
    private DatePickDialog mDatePickDialog;
    private LoadingDialog mLoadingDialog;

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
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void createPhotoDir() {
        mPhotoDir = new File(mActivity.getExternalCacheDir().getAbsolutePath() + File.separator + "takephoto");
        if (!mPhotoDir.exists()) {
            mPhotoDir.mkdirs();
        }
    }

    /**
     * 调用系统照相机拍照
     */
    protected void takePhoto(String path, OnTakePhotoListener listener) {
        mCurrentPath = path;
        mPhotoListener = listener;
        if (TextUtils.isEmpty(mCurrentPath)) {
            createPhotoDir();
            mCurrentPath = mPhotoDir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".png";
        }
        int result = PermissionChecker.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            Log.d("BaseFragment", "takePhoto: PERMISSION_GRANTED");
            try {
                Uri uri;
                Log.d("BaseFragment", "takePhoto: path = " + mCurrentPath);
                File file = new File(mCurrentPath);
                if (Build.VERSION.SDK_INT >= 24) {
                    //Android 7.0及以上获取文件 Uri
                    uri = FileProvider.getUriForFile(mActivity, mActivity.getPackageName(), file);
                } else {
                    uri = Uri.fromFile(file);
                }
                //调取系统拍照
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                mActivity.startActivityForResult(intent, REQUEST_TAKE_PHOTO_CODE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d("BaseFragment", "takePhoto: PERMISSION_DENIED");
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("BaseFragment", "onActivityResult: requestCode = " + requestCode);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TAKE_PHOTO_CODE) {//获取系统照片上传
            Log.d("BaseFragment", "onActivityResult: path = " + mCurrentPath);
            if (mPhotoListener != null) {
                if (!TextUtils.isEmpty(mCurrentPath)) {
                    File image = new File(mCurrentPath);
                    if (image.exists()) {
                        mPhotoListener.onPhoto(image);
                    } else {
                        mPhotoListener.onPhoto(null);
                    }
                } else {
                    mPhotoListener.onPhoto(null);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {
                if (Manifest.permission.CAMERA.equals(permissions[0])) {
                    if (PackageManager.PERMISSION_DENIED == grantResults[0]) {
                        Toast.makeText(mActivity, "缺少照相机权限", Toast.LENGTH_SHORT).show();
                    } else {
                        takePhoto(null, mPhotoListener);
                    }
                }
            }
        }
    }


}
