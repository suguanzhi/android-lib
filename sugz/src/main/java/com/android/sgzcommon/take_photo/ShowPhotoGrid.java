package com.android.sgzcommon.take_photo;

import java.util.List;

/**
 * Created by sgz on 2020/1/10.
 */
public interface ShowPhotoGrid {

    void setImageUrls(List<String> urls);

    void notifyPhotoChanged();

}
