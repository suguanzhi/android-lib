package com.android.sgzcommon.take_photo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.sgzcommon.take_photo.listener.OnTakePhotoListener;
import com.android.sgzcommon.utils.BitmapUtils;

import java.io.File;
import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.PermissionChecker;

/**
 * @author sgz
 * @date 2020/6/24
 */
public class TakePhotoImpl implements TakePhoto {

    String mPath;
    Activity mActivity;
    OnTakePhotoListener mListener;

    public TakePhotoImpl(Activity activity) {
        mActivity = activity;
        getPhotoDir();
    }

    public File getPhotoDir() {
        File dir = new File(mActivity.getExternalCacheDir().getAbsolutePath() + File.separator + "takephoto");
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
        mPath = path;
        if (TextUtils.isEmpty(mPath)) {
            mPath = getPhotoPath();
        }
        int result = PermissionChecker.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            Log.d("TakePhotoGridImpl", "takePhoto: PERMISSION_GRANTED");
            try {
                Uri uri;
                Log.d("TakePhotoGridImpl", "takePhoto: path = " + mPath);
                File file = new File(mPath);
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
    public void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        mActivity.startActivityForResult(intent, REQUEST_CHOOSE_PHOTO_CODE);
    }

    public void setOnTakePhotoListener(OnTakePhotoListener listener) {
        mListener = listener;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TakePhotoGridImpl", "onActivityResult: requestCode = " + requestCode);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO_CODE) {
                Log.d("TakePhotoGridImpl", "onActivityResult: path = " + mPath);
                File photo = new File(mPath);
                if (photo.exists()) {
                    Log.d("TakePhotoGridImpl", "onActivityResult: 1");
                } else {
                    Log.d("TakePhotoGridImpl", "onActivityResult: 5");
                    mPath = "";
                }
                mListener.onPhoto(photo);
            } else if (REQUEST_CHOOSE_PHOTO_CODE == requestCode) {
                File photo = null;
                Uri uri = data.getData();
                if (uri != null) {
                    try {
                        InputStream is = mActivity.getContentResolver().openInputStream(uri);
                        Bitmap srcBitmap = BitmapFactory.decodeStream(is);
                        if (srcBitmap != null) {
                            mPath = getPhotoPath();
                            boolean save = BitmapUtils.saveBimapToLocal(mPath, srcBitmap);
                            if (save) {
                                photo = new File(mPath);
                            } else {
                                mPath = "";
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mListener.onPhoto(photo);
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
