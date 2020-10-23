package com.android.sgzcommon.take_photo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.android.sgzcommon.take_photo.listener.OnPhotoListener;

import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

/**
 * @author sgz
 * @date 2020/6/24
 */
public class GetPhotoImpl implements GetPhoto {
    Uri uri;
    Activity mActivity;
    ContentResolver mContentResolver;
    OnPhotoListener mListener;

    public GetPhotoImpl(Activity activity) {
        mActivity = activity;
        mContentResolver = activity.getContentResolver();
    }

    @Override
    public void takePhoto() {
        int result = PermissionChecker.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            Log.d("TakePhotoGridImpl", "takePhoto: PERMISSION_GRANTED");
            try {
                String fileName = createFileName();
                ContentValues contentValues = new ContentValues();
                //设置文件名
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                //设置文件类型
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
                //执行insert操作，向系统文件夹中添加文件
                //EXTERNAL_CONTENT_URI代表外部存储器，该值不变
                uri = mContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                //调取系统拍照
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
                    mActivity.startActivityForResult(intent, REQUEST_TAKE_PHOTO_CODE);
                } else {
                    throw new IllegalArgumentException("intent resolveActivity null.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d("TakePhotoGridImpl", "takePhoto: PERMISSION_DENIED");
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    /**
     * @return
     */
    private String createFileName() {
        return "IMG" + System.currentTimeMillis() + ".jpg";
    }

    @Override
    public void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        mActivity.startActivityForResult(intent, REQUEST_CHOOSE_PHOTO_CODE);
    }

    public void setPhotoListener(OnPhotoListener listener) {
        mListener = listener;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TakePhotoGridImpl", "onActivityResult: requestCode = " + requestCode);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO_CODE || REQUEST_CHOOSE_PHOTO_CODE == requestCode) {
                Bitmap bitmap = null;
                if (requestCode == REQUEST_TAKE_PHOTO_CODE) {
                    try {
                        ParcelFileDescriptor parcelFd = mContentResolver.openFileDescriptor(uri, "r");
                        bitmap = BitmapFactory.decodeFileDescriptor(parcelFd.getFileDescriptor());
                        parcelFd.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Uri uri = data.getData();
                    try {
                        InputStream is = mContentResolver.openInputStream(uri);
                        bitmap = BitmapFactory.decodeStream(is);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mListener.onPhoto(bitmap);
            }
        }
    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {
                if (Manifest.permission.CAMERA.equals(permissions[0])) {
                    if (PackageManager.PERMISSION_DENIED == grantResults[0]) {
                        Toast.makeText(mActivity, "缺少照相机权限", Toast.LENGTH_SHORT).show();
                    } else {
                        takePhoto();
                    }
                }
            }
        }
    }
}
