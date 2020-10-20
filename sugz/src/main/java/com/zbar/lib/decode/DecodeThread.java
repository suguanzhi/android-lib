package com.zbar.lib.decode;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.zbar.lib.DecodeCaptureManager;

import java.util.concurrent.CountDownLatch;

/**
 * 作者: 陈涛(1076559197@qq.com)
 * 
 * 时间: 2014年5月9日 下午12:24:34
 *
 * 版本: V_1.0.0
 *
 * 描述: 解码线程
 */
final class DecodeThread extends Thread {

	private Context mContext;
	private Handler mHandler;
	private DecodeCaptureManager mCaptureManager;
	private final CountDownLatch handlerInitLatch;

	DecodeThread(Context context,DecodeCaptureManager captureManager) {
		mContext =context;
		mCaptureManager = captureManager;
		handlerInitLatch = new CountDownLatch(1);
	}

	Handler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
		}
		return mHandler;
	}

	@Override
	public void run() {
		Looper.prepare();
		mHandler = new DecodeHandler(mContext,mCaptureManager);
		handlerInitLatch.countDown();
		Looper.loop();
	}

}
