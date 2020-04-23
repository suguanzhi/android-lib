package com.android.sgzcommon.http.okhttp.upload;

import com.android.sgzcommon.http.util.ResultSet;

import java.io.File;

public abstract class UploadResultSet extends ResultSet {
	private int progress;
	private File file;

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}