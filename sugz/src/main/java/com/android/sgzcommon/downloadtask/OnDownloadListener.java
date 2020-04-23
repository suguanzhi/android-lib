package com.android.sgzcommon.downloadtask;

public interface OnDownloadListener {
        /**
         * 下载开始
         * @param fileInfo
         */
        public void onDownloadStart(DownloadTask.DownloadInfo fileInfo);


        /**
         *下载暂停
         * @param fileInfo
         */
        public void onDownloadPause(DownloadTask.DownloadInfo fileInfo);


        /**
         *下载完成
         * @param fileInfo
         */
        public void onDownloadSuccess(DownloadTask.DownloadInfo fileInfo);

        /**
         *下载失败
         * @param fileInfo
         * @param e
         */
        public void onDownloadFail(DownloadTask.DownloadInfo fileInfo, Exception e);

        /**
         *下载进度
         * @param progress
         */
        public void onDownlaodProgress(int progress);
    }