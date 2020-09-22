package com.android.sgzcommon.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.sgzcommon.activity.utils.EmptyEntity;
import com.android.sgzcommon.downloadtask.DownloadTask;
import com.android.sgzcommon.downloadtask.OnDownloadListener;
import com.android.sgzcommon.service.entity.VersionDownload;
import com.android.sgzcommon.utils.FilePathUtil;
import com.android.sgzcommon.utils.SystemUtil;
import com.android.sugz.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

/**
 * Created by sgz on 2017/1/3.
 */

public class BaseService extends Service {

    private boolean isDownloading;
    protected Context mContext;
    private NotificationManager mManager;
    private NotificationChannel mNotificationChannel;
    private Notification mNotification;
    private RemoteViews mContentView;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EmptyEntity emptyEntity) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(VersionDownload versionDownload) {
        downloadApk(versionDownload);
    }

    /**
     * @param versionDownload
     */
    private void downloadApk(VersionDownload versionDownload) {
        if (isDownloading) {
            return;
        }
        isDownloading = true;
        int iconResId = versionDownload.getResId();
        if (0 == iconResId) {
            iconResId = R.drawable.ic_sgz_download;
        }
        final int resId = iconResId;
        boolean isToast = versionDownload.isNeedToast();
        String url = versionDownload.getUrl();
        int verCode = versionDownload.getVerCode();
        String apkName = mContext.getPackageName().replace(".", "_") + "_" + verCode;
        final String savePath = FilePathUtil.getAppDownloadDir(mContext).getAbsolutePath() + File.separator + apkName + ".apk";
        Log.d("BaseService", "downloadApk: url == " + url);
        DownloadTask.getInstance(mContext).download(url, savePath, new OnDownloadListener() {
            @Override
            public void onDownloadStart(DownloadTask.DownloadInfo fileInfo) {
                Log.d("BaseService", "onDownloadStart: ");
            }

            @Override
            public void onDownloadPause(DownloadTask.DownloadInfo fileInfo) {
                Log.d("BaseService", "onDownloadPause: ");
            }

            @Override
            public void onDownloadSuccess(DownloadTask.DownloadInfo fileInfo) {
                Log.d("BaseService", "onDownloadSuccess: ");
                updateNotification(100, resId);
                installApk(savePath);
                isDownloading = false;
            }

            @Override
            public void onDownloadFail(DownloadTask.DownloadInfo fileInfo, Exception e) {
                isDownloading = false;
                if (isToast) {
                    Toast.makeText(mContext, "新版本下载失败！", Toast.LENGTH_SHORT).show();
                }
                Log.d("BaseService", "onDownloadFail: " + Log.getStackTraceString(e));
            }

            @Override
            public void onDownlaodProgress(int progress) {
                updateNotification(progress, resId);
            }
        });
    }

    /**
     * 安装apk
     */
    private void installApk(String apkPath) {
        File apkfile = new File(apkPath);
        if (!apkfile.exists()) {
            return;
        }
        try {
            SystemUtil.installAPK(mContext, getPackageName(), apkPath);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * @param progress
     * @param resId
     */
    private void updateNotification(int progress, int resId) {
        try {
            if (mManager == null) {
                mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            }
            if (mManager != null) {
                String channelId = "default";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String channelName = "下载进度";
                    if (mNotificationChannel == null) {
                        mNotificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                        mManager.createNotificationChannel(mNotificationChannel);
                    }
                }
                if (mContentView == null) {
                    mContentView = new RemoteViews(getPackageName(), R.layout.layout_sgz_download_notification);
                }
                mContentView.setProgressBar(R.id.pb_progress, 100, progress, false);
                mContentView.setTextViewText(R.id.tv_progress, progress + "%");
                if (mNotification == null) {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
                    builder.setCustomContentView(mContentView);
                    builder.setWhen(System.currentTimeMillis());
                    builder.setSmallIcon(R.drawable.ic_sgz_download);
                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), resId));
                    mNotification = builder.build();
                    mNotification.contentView.setImageViewResource(R.id.iv_icon, resId);
                }
                if (100 == progress) {
                    mManager.cancel(1);
                } else {
                    mManager.notify(1, mNotification);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (mManager != null) {
                mManager.cancel(1);
            }
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
