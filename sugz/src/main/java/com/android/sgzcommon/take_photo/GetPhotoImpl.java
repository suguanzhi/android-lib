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
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.sgzcommon.take_photo.listener.OnPhotoListener;
import com.android.sgzcommon.utils.BitmapUtils;
import com.android.sgzcommon.utils.FilePathUtils;

import java.io.File;
import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

/**
 * @author sgz
 * @date 2020/6/24
 */
public class GetPhotoImpl implements GetPhoto {
    Uri uri;
    String mPhotoPath;
    Activity mActivity;
    ContentResolver mContentResolver;
    OnPhotoListener mListener;

    public GetPhotoImpl(Activity activity) {
        mActivity = activity;
        mContentResolver = activity.getContentResolver();
        getPhotoDir();
    }

    public File getPhotoDir() {
        File dir = new File(FilePathUtils.getAppPictureDir(mActivity).getAbsolutePath());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    private String getPhotoPath() {
        return getPhotoDir().getAbsolutePath() + File.separator + System.currentTimeMillis() + ".png";
    }

    @Override
    public void takePhoto(@Nullable String path) {
        mPhotoPath = path;
        if (TextUtils.isEmpty(path)) {
            mPhotoPath = getPhotoPath();
        }
        int result = PermissionChecker.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            Log.d("TakePhotoGridImpl", "takePhoto: PERMISSION_GRANTED");
            try {
                File f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                Log.d("TakePhotoGridImpl", "takePhoto: path = " + mPhotoPath);
                Log.d("TakePhotoGridImpl", "takePhoto: f.path = " + f.getAbsolutePath());
                File file = new File(mPhotoPath);
                ContentValues contentValues = new ContentValues();
                //设置文件名
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, file.getName());
                //兼容Android Q和以下版本
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
                    //RELATIVE_PATH是相对路径不是绝对路径
                    //DCIM是系统文件夹，关于系统文件夹可以到系统自带的文件管理器中查看，不可以写没存在的名字
                    contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                } else {
                    //Android Q以下版本
                    contentValues.put(MediaStore.Images.Media.DATA, mPhotoPath);
                }
                //设置文件类型
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
                //执行insert操作，向系统文件夹中添加文件
                //EXTERNAL_CONTENT_URI代表外部存储器，该值不变
                uri = mContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
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
    public void choosePhoto() {
        mPhotoPath = null;
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        mActivity.startActivityForResult(intent, REQUEST_CHOOSE_PHOTO_CODE);
    }

    public void setOnTakePhotoListener(OnPhotoListener listener) {
        mListener = listener;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TakePhotoGridImpl", "onActivityResult: requestCode = " + requestCode);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO_CODE) {
                Bitmap bitmap = null;
                try {
                    ParcelFileDescriptor parcelFd = mContentResolver.openFileDescriptor(uri, "r");
                    if (parcelFd != null) {
                        bitmap = BitmapFactory.decodeFileDescriptor(parcelFd.getFileDescriptor());
                        if (bitmap != null) {
                            Log.d("TakePhotoGridImpl", "onActivityResult: bitmap != null   size == " + bitmap.getWidth() + ";" + bitmap.getHeight());
                        } else {
                            Log.d("TakePhotoGridImpl", "onActivityResult: bitmap == null");
                        }
                        parcelFd.close();
                    } else {
                        Log.d("TakePhotoGridImpl", "onActivityResult:parcelFd == null ");
                    }
                } catch (Exception e) {
                    Log.d("TakePhotoGridImpl", "onActivityResult: e == " + Log.getStackTraceString(e));
                    e.printStackTrace();
                }
                mListener.onPhoto(bitmap, mPhotoPath);
            } else if (REQUEST_CHOOSE_PHOTO_CODE == requestCode) {
                Bitmap bitmap = null;
                Uri uri = data.getData();
                if (uri != null) {
                    try {
                        InputStream is = mContentResolver.openInputStream(uri);
                        bitmap = BitmapFactory.decodeStream(is);
                        if (bitmap != null) {
                            mPhotoPath = getPhotoPath();
                            boolean save = BitmapUtils.saveBimapToLocal(mPhotoPath, bitmap);
                            if (!save) {
                                mPhotoPath = null;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mListener.onPhoto(bitmap, mPhotoPath);
            }
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
}