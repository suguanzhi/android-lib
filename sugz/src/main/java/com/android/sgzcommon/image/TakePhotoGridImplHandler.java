package com.android.sgzcommon.image;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.sgzcommon.activity.TakePhotoActivity;
import com.android.sgzcommon.activity.utils.MUploadResultSet;
import com.android.sgzcommon.adapter.PictureGridEditAdapter;
import com.android.sgzcommon.adapter.utils.ImageObject;
import com.android.sgzcommon.http.okhttp.upload.OKUploadTask;
import com.android.sgzcommon.http.okhttp.upload.OnUploadFileListener;
import com.android.sgzcommon.http.okhttp.upload.UploadResultSet;
import com.android.sgzcommon.recycleview.BaseViewHolder;
import com.android.sgzcommon.recycleview.MarginDecoration;

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
public class TakePhotoGridImplHandler implements TakePhotoGridImpl {

    Activity mActivity;
    RecyclerView mRecyclerView;
    PictureGridEditAdapter mAdapter;

    int mColumn;
    int mHorizontalMargin;
    int mVerticalMargin;
    String mCurrentPath;
    List<ImageObject> mImageObjects;
    private static final int REQUEST_TAKE_PHOTO_CODE = 371;

    public TakePhotoGridImplHandler(Activity activity, RecyclerView recyclerView) {
        this(activity, recyclerView, 4, 5, 5);
    }

    public TakePhotoGridImplHandler(Activity activity, RecyclerView recyclerView, int column, int horizontalMargin, int verticalmargin) {
        mActivity = activity;
        mRecyclerView = recyclerView;
        mColumn = column;
        mHorizontalMargin = horizontalMargin;
        mVerticalMargin = verticalmargin;
        init();
    }

    private void init() {
        mImageObjects = new ArrayList<>();
        mAdapter = new PictureGridEditAdapter(mActivity, mImageObjects, new BaseViewHolder.OnItemtClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position < mImageObjects.size()) {
                    ImageObject imageObject = mImageObjects.get(position);

                }
            }
        }, null);
        mAdapter.setOnClickListener(new PictureGridEditAdapter.OnClickListener() {

            @Override
            public void onCameraClick(View view) {
                takePhoto();
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
    public int getImageSize() {
        return mImageObjects.size();
    }

    @Override
    public void uploadImages(String url, Map<String, String> data, final OnImageUploadListener listener) {
        if (mImageObjects.size() == 0) {
            listener.onAllSuccess();
        } else {
            Log.d("TakePhotoActivity", "uploadImages: ");
            for (int i = 0; i < mImageObjects.size(); i++) {
                final ImageObject image = mImageObjects.get(i);
                if (ImageObject.STATE_INIT == image.getState() || ImageObject.STATE_FAIL == image.getState()) {
                    image.setProgress(0);
                    String path = mImageObjects.get(i).getPath();
                    File f = new File(path);
                    if (f.exists()) {
                        OKUploadTask.getInstance().upLoadFile(url, data, new MUploadResultSet(), f, new OnUploadFileListener() {
                            @Override
                            public void onUploadStart(File file) {
                                for (int j = 0; j < mImageObjects.size(); j++) {
                                    String p = mImageObjects.get(j).getPath();
                                    if (p.equals(file.getAbsolutePath())) {
                                        mImageObjects.get(j).setState(ImageObject.STATE_UPLOADING);
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onUploadSuccess(File file, UploadResultSet result) {
                                for (int j = 0; j < mImageObjects.size(); j++) {
                                    String p = mImageObjects.get(j).getPath();
                                    if (p.equals(file.getAbsolutePath())) {
                                        mImageObjects.get(j).setState(ImageObject.STATE_SUCCESS);
                                        mAdapter.notifyItemChanged(j);
                                        if (listener != null) {
                                            listener.onSuccess(file, result);
                                        }
                                        break;
                                    }
                                }
                                for (int j = 0; j < mImageObjects.size(); j++) {
                                    if (ImageObject.STATE_SUCCESS != mImageObjects.get(j).getState()) {
                                        break;
                                    }
                                    if (j == mImageObjects.size() - 1) {
                                        if (listener != null) {
                                            listener.onAllSuccess();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onValue(File file, int value) {
                                Log.d("TakePhotoActivity", "onValue: " + value + "%");
                                for (int j = 0; j < mImageObjects.size(); j++) {
                                    String p = mImageObjects.get(j).getPath();
                                    if (p.equals(file.getAbsolutePath())) {
                                        mImageObjects.get(j).setProgress(value);
                                        mAdapter.notifyItemChanged(j);
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onUploadFail(File file, Exception e) {
                                Log.d("TakePhotoActivity", "onUploadFail: " + Log.getStackTraceString(e));
                                for (int j = 0; j < mImageObjects.size(); j++) {
                                    String p = mImageObjects.get(j).getPath();
                                    if (p.equals(file.getAbsolutePath())) {
                                        mImageObjects.get(j).setState(ImageObject.STATE_FAIL);
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

    /**
     * 调用系统照相机拍照
     */
    @Override
    public void takePhoto() {
        int result = PermissionChecker.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            File dir = mActivity.getExternalCacheDir();
            try {
                Uri uri;
                File imagesDir = new File(dir.getAbsolutePath() + File.separator + "images");
                if (!imagesDir.exists()) {
                    imagesDir.mkdirs();
                }
                mCurrentPath = imagesDir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".png";
                File file = new File(mCurrentPath);
                Log.d("TakePhotoActivity", "takePhoto: path = " + mCurrentPath);
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
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }

    }

    @Override
    public void clearImages() {
        mImageObjects.clear();
        notifyTakePhotoChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TakePhotoActivity", "onActivityResult: requestCode = " + requestCode);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TAKE_PHOTO_CODE) {//获取系统照片上传
            Log.d("TakePhotoActivity", "onActivityResult: path = " + mCurrentPath);
            File image = new File(mCurrentPath);
            if (image.exists() && image.length() > 0 && mImageObjects != null) {
                Log.d("TakePhotoActivity", "onActivityResult: 1");
                ImageObject entity = new ImageObject(0, mCurrentPath);
                mImageObjects.add(entity);
                mAdapter.notifyDataSetChanged();
            } else {
                Log.d("TakePhotoActivity", "onActivityResult: 2");
                mCurrentPath = "";
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("TakePhotoGridImplHandler", "onRequestPermissionsResult: ");
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {
                if (Manifest.permission.CAMERA.equals(permissions[0])) {
                    if (PackageManager.PERMISSION_DENIED == grantResults[0]) {
                        Toast.makeText(mActivity, "缺少照相机权限", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("TakePhotoGridImplHandler", "onRequestPermissionsResult: grant !");
                        takePhoto();
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
