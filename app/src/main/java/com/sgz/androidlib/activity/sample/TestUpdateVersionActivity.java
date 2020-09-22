package com.sgz.androidlib.activity.sample;

import android.os.Bundle;
import android.util.Log;

import com.android.sgzcommon.activity.BaseActivity;
import com.android.sgzcommon.downloadtask.DownloadTask;
import com.android.sgzcommon.downloadtask.OnDownloadListener;
import com.android.sgzcommon.utils.FilePathUtil;
import com.android.sgzcommon.utils.SystemUtil;
import com.android.sgzcommon.view.TitleBar;
import com.sgz.androidlib.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author sgz
 * @date 2020/9/21
 */
public class TestUpdateVersionActivity extends BaseActivity {

    @BindView(R.id.tb_title)
    TitleBar mTbTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_update_version);
        ButterKnife.bind(this);
        String packageName = mContext.getPackageName();
        String apkName = packageName.replace(".", "_") + "_" + 110;
        String d = mContext.getExternalFilesDir(null).getAbsolutePath();
        Log.d("UpdateVersionActivity", "onEventMainThread: d == " + d);
        final String url ="http://192.168.0.129:80/upload/files/20200921/盛马桂v1_1_0_1600656839357.apk";
        final String savePath = FilePathUtil.getAppDownloadDir(mContext).getAbsolutePath() + File.separator + apkName + ".apk";
        Log.d("UpdateVersionActivity", "onEventMainThread: savePath == " + savePath);
        DownloadTask.getInstance(mContext).download(url, savePath, new OnDownloadListener() {
            @Override
            public void onDownloadStart(DownloadTask.DownloadInfo fileInfo) {
                Log.d("UpdateVersionActivity", "onDownloadStart: ");
            }

            @Override
            public void onDownloadPause(DownloadTask.DownloadInfo fileInfo) {
                Log.d("UpdateVersionActivity", "onDownloadPause: ");
            }

            @Override
            public void onDownloadSuccess(DownloadTask.DownloadInfo fileInfo) {
                Log.d("UpdateVersionActivity", "onDownloadSuccess: ");
                installApk(savePath);
            }

            @Override
            public void onDownloadFail(DownloadTask.DownloadInfo fileInfo, Exception e) {
                Log.d("UpdateVersionActivity", "onDownloadFail: " + Log.getStackTraceString(e));
            }

            @Override
            public void onDownlaodProgress(int progress) {
                Log.d("UpdateVersionActivity", "onDownlaodProgress: progress == " + progress);
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
            Log.d("TestUpdateVersionActivity", "installApk: ");
            SystemUtil.installAPK(mContext, getPackageName(), apkPath);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
