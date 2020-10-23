package com.android.sgzcommon.take_photo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.android.sgzcommon.activity.PhotoViewActivity;
import com.android.sgzcommon.activity.utils.MUploadResultSet;
import com.android.sgzcommon.http.okhttp.upload.OKUploadTask;
import com.android.sgzcommon.http.okhttp.upload.OnUploadFileListener;
import com.android.sgzcommon.http.okhttp.upload.UploadEntity;
import com.android.sgzcommon.http.okhttp.upload.UploadResultSet;
import com.android.sgzcommon.recycleview.BaseRecyclerviewAdapter;
import com.android.sgzcommon.recycleview.MarginDecoration;
import com.android.sgzcommon.take_photo.adapter.PictureGridEditAdapter;
import com.android.sgzcommon.take_photo.entity.PhotoUpload;
import com.android.sgzcommon.take_photo.listener.OnDeletePhotoListener;
import com.android.sgzcommon.take_photo.listener.OnPhotoListener;
import com.android.sgzcommon.take_photo.listener.OnPhotoUploadListener;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoClickListener;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoGridListener;
import com.android.sgzcommon.utils.BitmapUtils;
import com.android.sgzcommon.utils.FilePathUtils;
import com.android.sgzcommon.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sgz on 2020/1/10.
 */
final public class GetPhotosImpl implements GetPhotos {

    Activity mActivity;
    RecyclerView mRecyclerView;
    PictureGridEditAdapter mAdapter;
    OnTakePhotoGridListener mListener;
    GetPhotoImpl mTakePhoto;

    int mColumn;
    int mHorizontalMargin;
    int mVerticalMargin;
    List<PhotoUpload> mPhotoUploads;

    public GetPhotosImpl(Activity activity, RecyclerView recyclerView) {
        this(activity, recyclerView, 4, 5, 5);
    }

    public GetPhotosImpl(Activity activity, RecyclerView recyclerView, int column, int horizontalMargin, int verticalmargin) {
        mColumn = column;
        mActivity = activity;
        mRecyclerView = recyclerView;
        mHorizontalMargin = horizontalMargin;
        mVerticalMargin = verticalmargin;
        mTakePhoto = new GetPhotoImpl(activity);
        mTakePhoto.setPhotoListener(new OnPhotoListener() {
            @Override
            public void onPhoto(Bitmap bitmap) {
                Log.d("GetPhotosImpl", "onPhoto: ");
                final String path = FilePathUtils.getAppPictureDir(mActivity).getAbsolutePath() + File.separator + "IMG" + System.currentTimeMillis() + ".jpg";
                final PhotoUpload photoUpload = new PhotoUpload(path);
                mAdapter.addItemData(photoUpload, mPhotoUploads.size());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final boolean save = BitmapUtils.saveBimapToLocal(path, bitmap);
                        bitmap.recycle();
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UploadEntity.STATE state = UploadEntity.STATE.STATE_START;
                                if (!save) {
                                    state = UploadEntity.STATE.STATE_FAIL;
                                }
                                photoUpload.setState(state);
                                int position = -1;
                                for (int i = 0; i < mPhotoUploads.size(); i++) {
                                    if (path.equals(mPhotoUploads.get(i).getPath())) {
                                        position = i;
                                        break;
                                    }
                                }
                                if (position >= 0 && position < mPhotoUploads.size()) {
                                    mAdapter.notifyItemChanged(position);
                                }
                                if (mListener != null) {
                                    mListener.onPhotos(mPhotoUploads);
                                }
                            }
                        });
                    }
                }).start();
            }
        });
        init();
    }

    private void init() {
        mPhotoUploads = new ArrayList<>();
        mAdapter = new PictureGridEditAdapter(mActivity, mPhotoUploads, new BaseRecyclerviewAdapter.OnItemtClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    if (position < mPhotoUploads.size()) {
                        PhotoUpload photoUpload = mPhotoUploads.get(position);
                        String path = photoUpload.getPath();
                        Intent intent = new Intent(mActivity, PhotoViewActivity.class);
                        intent.putExtra("path", path);
                        mActivity.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null);

        mAdapter.setOnTakePhotoClickListener(new OnTakePhotoClickListener() {
            @Override
            public void onClick(View view) {
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
    public void addPhotoUploads(List<? extends PhotoUpload> photoUploads) {
        if (photoUploads != null) {
            mPhotoUploads.addAll(photoUploads);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public List<PhotoUpload> getPhotoUploads() {
        return mPhotoUploads;
    }

    @Override
    public void setOnTakePhotoClickListener(OnTakePhotoClickListener listener) {
        mAdapter.setOnTakePhotoClickListener(listener);
    }

    @Override
    public void setOnDeletePhotoListener(OnDeletePhotoListener listener) {
        mAdapter.setOnDeletePhotoListener(listener);
    }

    @Override
    public void uploadPhotos(String url, Map<String, String> data, Map<String, String> headers, final OnPhotoUploadListener listener) {
        if (mPhotoUploads.size() == 0) {
            listener.onAllSuccess();
        } else {
            Log.d("TakePhotoGridImpl", "uploadImages: ");
            for (int i = 0; i < mPhotoUploads.size(); i++) {
                final PhotoUpload image = mPhotoUploads.get(i);
                if (PhotoUpload.STATE.STATE_START == image.getState() || PhotoUpload.STATE.STATE_FAIL == image.getState()) {
                    final PhotoUpload photoUpload = mPhotoUploads.get(i);
                    OKUploadTask.getInstance().upLoadFile(url, photoUpload, data, headers, new MUploadResultSet(), new OnUploadFileListener<PhotoUpload>() {
                        @Override
                        public void onUploadStart(PhotoUpload upload) {
                            Log.d("TakePhotoGridImpl", "onUploadStart: ");
                        }

                        @Override
                        public void onUploadSuccess(PhotoUpload upload, UploadResultSet result) {
                            Log.d("TakePhotoGridImpl", "onUploadSuccess: ");
                            int position = upload.getPosition();
                            if (listener != null) {
                                listener.onSuccess(upload, result);
                                for (int j = 0; j < mPhotoUploads.size(); j++) {
                                    if (PhotoUpload.STATE.STATE_SUCCESS != mPhotoUploads.get(j).getState()) {
                                        break;
                                    }
                                    if (j == mPhotoUploads.size() - 1) {
                                        if (listener != null) {
                                            listener.onAllSuccess();
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onValue(PhotoUpload upload, int value) {
                            Log.d("TakePhotoGridImpl", "onValue: " + value + "%");
                        }

                        @Override
                        public void onUploadFail(PhotoUpload upload, Exception e) {
                            Log.d("TakePhotoGridImpl", "onUploadFail: " + Log.getStackTraceString(e));
                        }
                    });
                }
            }
        }
    }

    public void setOnPhotoGridListener(OnTakePhotoGridListener listener) {
        mListener = listener;
    }

    /**
     * 调用系统照相机拍照
     */
    @Override
    public void takePhoto() {
        mTakePhoto.takePhoto();
    }

    @Override
    public void choosePhoto() {
        mTakePhoto.choosePhoto();
    }

    @Override
    public void clearPhotos() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (PhotoUpload upload : mPhotoUploads) {
                        FileUtils.deleteFile(upload.getPath());
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPhotoUploads.clear();
        notifyTakePhotoChanged();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTakePhoto.onActivityResult(requestCode, resultCode, data);
        Log.d("TakePhotoGridImpl", "onActivityResult: requestCode = " + requestCode);
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mTakePhoto.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
