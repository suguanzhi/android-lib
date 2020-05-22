package com.android.sgzcommon.http.okhttp.upload;

public interface OnUploadFileListener<V> {
	void onUploadStart(V v);

	void onUploadSuccess(V v, UploadResultSet result);

	void onValue(V v,int value);

	void onUploadFail(V v,Exception e);
}