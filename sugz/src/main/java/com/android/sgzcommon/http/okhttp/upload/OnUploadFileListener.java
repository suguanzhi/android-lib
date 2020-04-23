package com.android.sgzcommon.http.okhttp.upload;

import java.io.File;

public interface OnUploadFileListener {
	void onUploadStart(File file);

	void onUploadSuccess(File file, UploadResultSet result);

	void onValue(File file,int value);

	void onUploadFail(File file,Exception e);
}