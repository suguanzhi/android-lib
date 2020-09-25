package com.android.sgzcommon.service.entity;

import androidx.annotation.IdRes;

/**
 * @author sgz
 * @date 2020/9/21
 * 注意：版本更新需要添加权限 <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/>
 */
public class VersionDownload {
    private int verCode;
    //通知图标资源id
    private int resId;
    private boolean isNeedToast;
    private String url;

    public VersionDownload(String url, int verCode) {
        this(url, verCode, 0);
    }

    /**
     * @param url     下载地址
     * @param verCode 新版本版本号
     * @param resId   通知栏显示的图标资源id
     */
    public VersionDownload(String url, int verCode, @IdRes int resId) {
        this.url = url;
        this.verCode = verCode;
        this.resId = resId;
    }

    public String getUrl() {
        return url;
    }

    public int getVerCode() {
        return verCode;
    }

    /**
     * 获取通知图标资源id
     *
     * @return
     */
    public int getResId() {
        return resId;
    }

    public boolean isNeedToast() {
        return isNeedToast;
    }

    public void setNeedToast(boolean needToast) {
        isNeedToast = needToast;
    }
}
