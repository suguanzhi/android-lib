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
import com.android.sgzcommon.take_photo.listener.OnPhotoUploadListener;
import com.android.sgzcommon.take_photo.listener.OnTakePhotoListListener;
import com.android.sgzcommon.utils.BitmapUtils;
import com.android.sgzcommon.utils.FilePathUtils;
import com.android.sgzcommon.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sgz on 2020/1/10.
 */
final public class GetPhotoListImpl extends GetPhotoImpl {

    RecyclerView mRecyclerView;
    PictureGridEditAdapter mAdapter;
    ScheduledExecutorService mExecutorService;
    OnTakePhotoListListener mTakePhotoListListener;

    List<PhotoUpload> mPhotoUploadList;

    public GetPhotoListImpl(Activity activity, RecyclerView recyclerView) {
        this(activity, recyclerView, 4, 5, 5);
    }

    public GetPhotoListImpl(Activity activity, RecyclerView recyclerView, int column, int horizontalMargin, int verticalmargin) {
        super(activity);
        mRecyclerView = recyclerView;
        mPhotoUploadList = new ArrayList<>();
        mExecutorService = Executors.newSingleThreadScheduledExecutor();
        mAdapter = new PictureGridEditAdapter(mActivity, mPhotoUploadList, new BaseRecyclerviewAdapter.OnItemtClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    if (position < mPhotoUploadList.size()) {
                        PhotoUpload photoUpload = mPhotoUploadList.get(position);
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
        GridLayoutManager gridEdit = new GridLayoutManager(mActivity, column);
        MarginDecoration margin = new MarginDecoration(horizontalMargin, verticalmargin);
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(margin);
            mRecyclerView.setLayoutManager(gridEdit);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onPhoto(Bitmap bitmap) {
        Log.d("GetPhotoListImpl", "onPhoto: ");
        final String path = FilePathUtils.getAppPictureDir(mActivity).getAbsolutePath() + File.separator + "IMG" + System.currentTimeMillis() + ".jpg";
        final PhotoUpload photoUpload = new PhotoUpload(path);
        mAdapter.addItemData(photoUpload);
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
                            for (int i = 0; i < mPhotoUploadList.size(); i++) {
                                if (path.equals(mPhotoUploadList.get(i).getPath())) {
                                    position = i;
                                    break;
                                }
                            }
                            if (position >= 0 && position < mPhotoUploadList.size()) {
                                mAdapter.notifyItemChanged(position);
                            }
                            if (mTakePhotoListListener != null) {
                                mTakePhotoListListener.onPhotos(mPhotoUploadList);
                            }
                        }
                    });
                }
            }
        });
    }

    public List<PhotoUpload> getPhotoUploads() {
        return mPhotoUploadList;
    }

    public void setOnPhotoClickListener(OnPhotoClickListener listener) {
        mAdapter.setOnTakePhotoClickListener(listener);
    }

    /**
     *
     * @param url
     * @param data
     * @param headers
     * @param listener
     */
    public void uploadPhotos(String url, Map<String, String> data, Map<String, String> headers, final OnPhotoUploadListener listener) {
        if (mPhotoUploadList.size() == 0) {
            listener.onAllSuccess();
        } else {
            Log.d("TakePhotoGridImpl", "uploadImages: ");
            List<PhotoUpload> needUploadList = new ArrayList<>();
            for (int i = 0; i < mPhotoUploadList.size(); i++) {
                final PhotoUpload image = mPhotoUploadList.get(i);
                if (PhotoUpload.STATE.STATE_UPLOAD_READY == image.getState() || PhotoUpload.STATE.STATE_UPLOAD_FAIL == image.getState()) {
                    PhotoUpload photoUpload = mPhotoUploadList.get(i);
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

    /**
     *
     * @param listener
     */
    public void setOnPhotoListListener(OnTakePhotoListListener listener) {
        mTakePhotoListListener = listener;
    }

    /**
     *
     */
    public void clearPhotos() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (PhotoUpload upload : mPhotoUploadList) {
                        FileUtils.deleteFile(upload.getPath());
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPhotoUploadList.clear();
        notifyPhotoChanged();
    }

    /**
     *
     */
    public void notifyPhotoChanged() {
        mAdapter.notifyDataSetChanged();
    }

    /**
     *
     * @param position
     */
    public void notifyPhotoChanged(int position) {
        mAdapter.notifyItemChanged(position);
    }
}
