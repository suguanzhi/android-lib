package com.android.sgzcommon.take_photo;

import android.content.Intent;

import androidx.annotation.NonNull;

/**
 * Created by sgz on 2020/1/15.
 */
public interface TakePhotoGridImpl extends TakePhotoGrid {

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
