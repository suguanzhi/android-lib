package com.android.sgzcommon.downloadtask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class ThreadDAOImpl implements ThreadDAO {

	private DownloadDBHelper mHelper = null;
	public static final String THREAD_INFO_TABLE = "table_threadinfo";
	public static final String FINISHED_TAR_INFO_TABLE = "table_tarinfo";

	public ThreadDAOImpl(Context context){
		mHelper = new DownloadDBHelper(context);
	}
	
	@Override
	public void insertThread(ThreadInfo threadInfo) {
		ContentValues values = new ContentValues();
		values.put("thread_id", threadInfo.getId());
		values.put("url", threadInfo.getUrl());
		values.put("start", threadInfo.getStart());
		values.put("ended", threadInfo.getEnd());
		values.put("finished", threadInfo.getFinished());
		mHelper.insertData(THREAD_INFO_TABLE, values);
	}

	@Override
	public void deleteThread(String url, int thread_id) {
		mHelper.deleteData(THREAD_INFO_TABLE, "url=? AND thread_id=?", new String[]{url,thread_id + ""});
	}

	@Override
	public void updateThread(String url, int thread_id, long finished) {
		ContentValues values = new ContentValues();
		values.put("finished", finished);
		mHelper.updateData(THREAD_INFO_TABLE, values, "url=? AND thread_id=?", new String[]{url,thread_id + ""});
	}

	@Override
	public List<ThreadInfo> getThreads(String url) {
		Cursor cursor = mHelper.queryData(THREAD_INFO_TABLE, "url=?", new String[]{url});
		List<ThreadInfo> list = new ArrayList<ThreadInfo>();
		while (cursor.moveToNext()) {
			ThreadInfo threadInfo = new ThreadInfo();
			threadInfo.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
			threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
			threadInfo.setStart(cursor.getInt(cursor.getColumnIndex("start")));
			threadInfo.setEnd(cursor.getInt(cursor.getColumnIndex("ended")));
			threadInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
			list.add(threadInfo);
		}
		cursor.close();
		return list;
	}

	@Override
	public boolean isExists(String url, int thread_id) {
		
		Cursor cursor = mHelper.queryData(THREAD_INFO_TABLE, "url=? AND thread_id=?", new String[]{url,thread_id + ""});
		boolean exist = cursor.moveToNext();
		cursor.close();
		return exist;
	}
}
