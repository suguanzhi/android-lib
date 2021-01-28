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
import com.android.sgzcommon.take_photo.listener.OnPhotoClickListener;
import com.android.sgzcommon.take_photo.listener.OnPhotoListener;
import com.android.sgzcommon.take_photo.listener.OnPhotoUploadListener;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoGridListener;
import com.android.sgzcommon.utils.BitmapUtils;
import com.android.sgzcommon.utils.FilePathUtils;
import com.android.sgzcommon.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
    ScheduledExecutorService mExecutorService;

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
        mExecutorService = Executors.newSingleThreadScheduledExecutor();
        mPhotoUploads = new ArrayList<>();
        mTakePhoto = new GetPhotoImpl(activity);
        mTakePhoto.setPhotoListener(new OnPhotoListener() {
            @Override
            public void onPhoto(Bitmap bitmap) {
                Log.d("GetPhotosImpl", "onPhoto: ");
                final String path = FilePathUtils.getAppPictureDir(mActivity).getAbsolutePath() + File.separator + "IMG" + System.currentTimeMillis() + ".jpg";
                final PhotoUpload photoUpload = new PhotoUpload(path);
                mAdapter.addItemData(photoUpload, mPhotoUploads.size());
                mExecutorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        final boolean save = BitmapUtils.saveBimapToLocal(path, bitmap);
                        bitmap.recycle();
                        if (mActivity != null) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    photoUpload.setState(UploadEntity.STATE.STATE_UPLOAD_READY);
                                    if (!save) {
                                        photoUpload.setState(UploadEntity.STATE.STATE_LOADING_FAIL);
                                    }
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
                    }
                });
            }
        });
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

        mAdapter.setOnTakePhotoClickListener(new OnPhotoClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }

            @Override
            public void onDelete(int position, PhotoUpload photoUpload) {

            }
        });
        GridLayoutManager gridEdit = new GridLayoutManager(mActivity, mColumn);
        MarginDecoration decoration1 = new MarginDecoration(mHorizontalMargin, mVerticalMargin);
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(decoration1);
            mRecyclerView.setLayoutManager(gridEdit);
            mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    View firstChild = gridEdit.findViewByPosition(0);
                    if (firstChild != null) {
                        int width = firstChild.getWidth();
                        Log.d("GetPhotosImpl", "onScrolled: width == " + width);
                    }
                    View secondChild = gridEdit.findViewByPosition(1);
                    if (secondChild != null) {
                        int width2 = secondChild.getWidth();
                        Log.d("GetPhotosImpl", "onScrolled: width2 == " + width2);
                    }
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public List<PhotoUpload> getPhotoUploads() {
        return mPhotoUploads;
    }

    @Override
    public void setOnPhotoClickListener(OnPhotoClickListener listener) {
        mAdapter.setOnTakePhotoClickListener(listener);
    }

    @Override
    public void uploadPhotos(String url, Map<String, String> data, Map<String, String> headers, final OnPhotoUploadListener listener) {
        if (mPhotoUploads.size() == 0) {
            listener.onAllSuccess();
        } else {
            Log.d("TakePhotoGridImpl", "uploadImages: ");
            List<PhotoUpload> needUploadList = new ArrayList<>();
            for (int i = 0; i < mPhotoUploads.size(); i++) {
                final PhotoUpload image = mPhotoUploads.get(i);
                if (PhotoUpload.STATE.STATE_UPLOAD_READY == image.getState() || PhotoUpload.STATE.STATE_UPLOAD_FAIL == image.getState()) {
                    PhotoUpload photoUpload = mPhotoUploads.get(i);
                    needUploadList.add(photoUpload);
                }
            }
            if (needUploadList.size() > 0) {
                OKUploadTask.getInstance().upLoadFile(url, needUploadList, data, headers, new MUploadResultSet(), new OnUploadFileListener<PhotoUpload>() {
                    @Override
                    public void onUploadStart(String url) {

                    }

                    @Override
                    public void onUploadSuccess(String url, List<PhotoUpload> photoUploads, UploadResultSet result) {
                        if (listener != null) {
                            listener.onAllSuccess();
                        }
                    }

                    @Override
                    public void onUploadFail(String url, List<PhotoUpload> photoUploads, Exception e) {

                    }

                    @Override
                    public void onEntityStart(PhotoUpload photoUpload) {

                    }

                    @Override
                    public void onEntityValue(PhotoUpload photoUpload, int value) {

                    }

                    @Override
                    public void onEntitySuccess(PhotoUpload photoUpload) {

                    }

                    @Override
                    public void onEntityFail(PhotoUpload photoUpload, Exception e) {

                    }
                });
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
        notifyPhotoChanged();
    }

    public void onPhotoActivityResult(int requestCode, int resultCode, Intent data) {
        mTakePhoto.onActivityResult(requestCode, resultCode, data);
        Log.d("TakePhotoGridImpl", "onActivityResult: requestCode = " + requestCode);
    }

    public void onPhotoRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mTakePhoto.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void notifyPhotoChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyPhotoChanged(int position) {
        mAdapter.notifyItemChanged(position);
    }
}
