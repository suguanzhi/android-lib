package com.android.sgzcommon.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.android.sgzcommon.activity.utils.MUploadResultSet;
import com.android.sgzcommon.adapter.PictureGridAdapter;
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

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sgz on 2019/5/10 0010.
 */
public abstract class TakePhotoActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView mEditRecyclerView;
    protected PictureGridAdapter mAdapter;
    protected PictureGridEditAdapter mEditAdapter;
    private String mCurrentPath;
    private List<String> mUrls;
    protected List<ImageObject> mImageObjects;
    private static final int REQUEST_TAKE_PHOTO_CODE = 371;

    protected abstract int getContentViewId();

    protected abstract int getImagesGridLayoutId();

    protected abstract int getImagesGridEditLayoutId();

    protected abstract void onPictureClick(String url);

    protected abstract void onPictureEditClick(String path);

    protected int getColumn() {
        return 4;
    }

    protected int getHorizontalMargin() {
        return 5;
    }

    protected int getVerticalMargin() {
        return 5;
    }

    protected void setImageUrls(List<String> urls) {
        mUrls.clear();
        if (urls != null) {
            mUrls.addAll(urls);
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        mUrls = new ArrayList<>();
        int column = getColumn();
        if (column <= 0) {
            column = 4;
        }
        int horizontalMargin = getHorizontalMargin();
        if (horizontalMargin <= 0) {
            horizontalMargin = 5;
        }
        int verticalMargin = getVerticalMargin();
        if (verticalMargin <= 0) {
            verticalMargin = 5;
        }
        mRecyclerView = findViewById(getImagesGridLayoutId());
        if (mRecyclerView != null) {
            GridLayoutManager grid = new GridLayoutManager(this, column);
            MarginDecoration decoration = new MarginDecoration(this, column, horizontalMargin, verticalMargin);
            mRecyclerView.addItemDecoration(decoration);
            mRecyclerView.setLayoutManager(grid);
            mAdapter = new PictureGridAdapter(this, mUrls, new BaseViewHolder.OnItemtClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (position < mUrls.size()) {
                        onPictureClick(mUrls.get(position));
                        Intent intent = new Intent(TakePhotoActivity.this, PhotoViewActivity.class);
                        intent.putExtra("path", mUrls.get(position));
                        startActivity(intent);
                    }
                }
            }, null);
            mRecyclerView.setAdapter(mAdapter);
        }

        mEditRecyclerView = findViewById(getImagesGridEditLayoutId());
        if (mEditRecyclerView != null) {
            GridLayoutManager gridEdit = new GridLayoutManager(this, 4);
            MarginDecoration decoration1 = new MarginDecoration(this, 4, 5, 5);
            mEditRecyclerView.addItemDecoration(decoration1);
            mEditRecyclerView.setLayoutManager(gridEdit);

            mImageObjects = new ArrayList<>();
            mEditAdapter = new PictureGridEditAdapter(this, mImageObjects, new BaseViewHolder.OnItemtClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (position < mImageObjects.size()) {
                        ImageObject imageObject = mImageObjects.get(position);
                        onPictureEditClick(imageObject.getPath());
                    }
                }
            }, null);
            mEditAdapter.setOnClickListener(new PictureGridEditAdapter.OnClickListener() {

                @Override
                public void onCameraClick(View view) {
                    takePhoto();
                }
            });
            mEditRecyclerView.setAdapter(mEditAdapter);
        }
    }

    protected void uploadImages(String url, Map<String, String> data, final OnImageUploadListener listener) {
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
                                    mEditAdapter.notifyItemChanged(j);
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
                                    mEditAdapter.notifyItemChanged(j);
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
                                    mEditAdapter.notifyItemChanged(j);
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

    /**
     * 调用系统照相机拍照
     */
    private void takePhoto() {
        File dir = getExternalCacheDir();
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
                uri = FileProvider.getUriForFile(this, getPackageName(), file);
            } else {
                uri = Uri.fromFile(file);
            }
            //调取系统拍照
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, REQUEST_TAKE_PHOTO_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                mEditAdapter.notifyDataSetChanged();
            } else {
                Log.d("TakePhotoActivity", "onActivityResult: 2");
                mCurrentPath = "";
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public interface OnImageUploadListener {

        void onAllSuccess();

        void onSuccess(File file, UploadResultSet result);

        void onFail(File file, Exception e);
    }
}
