package com.android.sgzcommon.take_photo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.sgzcommon.activity.PhotoViewActivity;
import com.android.sgzcommon.activity.utils.MUploadResultSet;
import com.android.sgzcommon.take_photo.listener.OnPhotoListener;
import com.android.sgzcommon.take_photo.listener.OnPhotoUploadListener;
import com.android.sgzcommon.take_photo.utils.PhotoUpload;
import com.android.sgzcommon.http.okhttp.upload.OKUploadTask;
import com.android.sgzcommon.http.okhttp.upload.OnUploadFileListener;
import com.android.sgzcommon.http.okhttp.upload.UploadResultSet;
import com.android.sgzcommon.recycleview.BaseViewHolder;
import com.android.sgzcommon.recycleview.MarginDecoration;
import com.android.sgzcommon.take_photo.adapter.PictureGridEditAdapter;
import com.android.sgzcommon.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sgz on 2020/1/10.
 */
final public class TakePhotoGridImpl implements TakePhotoGrid {

    Activity mActivity;
    RecyclerView mRecyclerView;
    PictureGridEditAdapter mAdapter;
    OnPhotoListener mListener;
    File mPhotoDir;

    int mColumn;
    int mHorizontalMargin;
    int mVerticalMargin;
    String mCurrentPath;
    List<PhotoUpload> mPhotoUploads;
    static final int REQUEST_TAKE_PHOTO_CODE = 371;

    public TakePhotoGridImpl(Activity activity, RecyclerView recyclerView) {
        this(activity, recyclerView, 4, 5, 5);
    }

    public TakePhotoGridImpl(Activity activity, RecyclerView recyclerView, int column, int horizontalMargin, int verticalmargin) {
        mActivity = activity;
        mRecyclerView = recyclerView;
        mColumn = column;
        mHorizontalMargin = horizontalMargin;
        mVerticalMargin = verticalmargin;
        createPhotoDir();
        init();
    }

    private void createPhotoDir() {
        mPhotoDir = new File(mActivity.getExternalCacheDir().getAbsolutePath() + File.separator + "takephoto");
        if (!mPhotoDir.exists()) {
            mPhotoDir.mkdirs();
        }
    }

    private void init() {
        mPhotoUploads = new ArrayList<>();
        mAdapter = new PictureGridEditAdapter(mActivity, mPhotoUploads, new BaseViewHolder.OnItemtClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position < mPhotoUploads.size()) {
                    PhotoUpload photoUpload = mPhotoUploads.get(position);
                    String path = photoUpload.getPath();
                    Intent intent = new Intent(mActivity, PhotoViewActivity.class);
                    intent.putExtra("path", path);
                    mActivity.startActivity(intent);
                }
            }
        }, null);
        mAdapter.setOnClickListener(new PictureGridEditAdapter.OnClickListener() {

            @Override
            public void onCameraClick(View view) {
                takePhoto(null);
            }
        });
        if (mRecyclerView != null) {
            GridLayoutManager gridEdit = new GridLayoutManager(mActivity, mColumn);
            MarginDecoration decoration1 = new MarginDecoration(mColumn, mHorizontalMargin, mVerticalMargin);
            mRecyclerView.addItemDecoration(decoration1);
            mRecyclerView.setLayoutManager(gridEdit);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public int getPhotoSize() {
        return mPhotoUploads.size();
    }

    @Override
    public void uploadPhotos(String url, Map<String, String> data, final OnPhotoUploadListener listener) {
        if (mPhotoUploads.size() == 0) {
            listener.onAllSuccess();
        } else {
            Log.d("TakePhotoGridImpl", "uploadImages: ");
            for (int i = 0; i < mPhotoUploads.size(); i++) {
                final PhotoUpload image = mPhotoUploads.get(i);
                if (PhotoUpload.STATE_INIT == image.getState() || PhotoUpload.STATE_FAIL == image.getState()) {
                    image.setProgress(0);
                    String path = mPhotoUploads.get(i).getPath();
                    File f = new File(path);
                    if (f.exists()) {
                        OKUploadTask.getInstance().upLoadFile(url, data, new MUploadResultSet(), f, new OnUploadFileListener() {
                            @Override
                            public void onUploadStart(File file) {
                                Log.d("TakePhotoGridImpl", "onUploadStart: ");
                                for (int j = 0; j < mPhotoUploads.size(); j++) {
                                    String p = mPhotoUploads.get(j).getPath();
                                    if (p.equals(file.getAbsolutePath())) {
                                        mPhotoUploads.get(j).setState(PhotoUpload.STATE_UPLOADING);
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onUploadSuccess(File file, UploadResultSet result) {
                                Log.d("TakePhotoGridImpl", "onUploadSuccess: ");
                                for (int j = 0; j < mPhotoUploads.size(); j++) {
                                    String p = mPhotoUploads.get(j).getPath();
                                    if (p.equals(file.getAbsolutePath())) {
                                        mPhotoUploads.get(j).setState(PhotoUpload.STATE_SUCCESS);
                                        mAdapter.notifyItemChanged(j);
                                        if (listener != null) {
                                            listener.onSuccess(file, result);
                                        }
                                        break;
                                    }
                                }
                                for (int j = 0; j < mPhotoUploads.size(); j++) {
                                    if (PhotoUpload.STATE_SUCCESS != mPhotoUploads.get(j).getState()) {
                                        break;
                                    }
                                    if (j == mPhotoUploads.size() - 1) {
                                        if (listener != null) {
                                            listener.onAllSuccess();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onValue(File file, int value) {
                                Log.d("TakePhotoGridImpl", "onValue: " + value + "%");
                                for (int j = 0; j < mPhotoUploads.size(); j++) {
                                    String p = mPhotoUploads.get(j).getPath();
                                    if (p.equals(file.getAbsolutePath())) {
                                        mPhotoUploads.get(j).setProgress(value);
                                        mAdapter.notifyItemChanged(j);
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onUploadFail(File file, Exception e) {
                                Log.d("TakePhotoGridImpl", "onUploadFail: " + Log.getStackTraceString(e));
                                for (int j = 0; j < mPhotoUploads.size(); j++) {
                                    String p = mPhotoUploads.get(j).getPath();
                                    if (p.equals(file.getAbsolutePath())) {
                                        mPhotoUploads.get(j).setState(PhotoUpload.STATE_FAIL);
                                        mAdapter.notifyItemChanged(j);
                                        if (listener != null) {
                                            listener.onFail(file, e);
                                        }
                                        break;
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    public void setOnPhotoListener(OnPhotoListener listener) {
        mListener = listener;
    }

    /**
     * 调用系统照相机拍照
     */
    @Override
    public void takePhoto(String path) {
        mCurrentPath = path;
        if (TextUtils.isEmpty(mCurrentPath)) {
            createPhotoDir();
            mCurrentPath = mPhotoDir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".png";
        }
        int result = PermissionChecker.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            Log.d("TakePhotoGridImpl", "takePhoto: PERMISSION_GRANTED");
            try {
                Uri uri;
                Log.d("TakePhotoGridImpl", "takePhoto: path = " + mCurrentPath);
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
            Log.d("TakePhotoGridImpl", "takePhoto: PERMISSION_DENIED");
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void clearPhotos() {
        mPhotoUploads.clear();
        if (mPhotoDir != null && mPhotoDir.exists()) {
            FileUtil.deleteFile(mPhotoDir);
        }
        notifyTakePhotoChanged();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TakePhotoGridImpl", "onActivityResult: requestCode = " + requestCode);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TAKE_PHOTO_CODE) {//获取系统照片上传
            Log.d("TakePhotoGridImpl", "onActivityResult: path = " + mCurrentPath);
            File image = new File(mCurrentPath);
            PhotoUpload upload = null;
            if (image.exists()) {
                Log.d("TakePhotoGridImpl", "onActivityResult: 1");
                upload = new PhotoUpload(0, mCurrentPath);
                mPhotoUploads.add(upload);
            } else {
                Log.d("TakePhotoGridImpl", "onActivityResult: 5");
                mCurrentPath = "";
            }
            mListener.onPhoto(mPhotoUploads, upload);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {
                if (Manifest.permission.CAMERA.equals(permissions[0])) {
                    if (PackageManager.PERMISSION_DENIED == grantResults[0]) {
                        Toast.makeText(mActivity, "缺少照相机权限", Toast.LENGTH_SHORT).show();
                    } else {
                        takePhoto(null);
                    }
                }
            }
        }
    }


    @Override
    public void notifyTakePhotoChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyTakePhotoChanged(int position) {
        mAdapter.notifyItemChanged(position);
    }
}
