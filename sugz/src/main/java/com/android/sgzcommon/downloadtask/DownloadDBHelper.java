package com.android.sgzcommon.downloadtask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DownloadDBHelper extends SQLiteOpenHelper {

    private Context mContext;
    private static final int DEFAULT_VERSION = 1;
    public static final String DB_NAME = "download.db";
    private SQLiteDatabase mDB;

    public DownloadDBHelper(Context context) {
        super(context, DB_NAME, null, DEFAULT_VERSION);
        init(context);
    }

    public DownloadDBHelper(Context context, String name) {
        super(context, name, null, DEFAULT_VERSION);
        init(context);
    }

    public DownloadDBHelper(Context context, String name, int version) {
        super(context, name, null, version);
        init(context);
    }

    public DownloadDBHelper(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        init(context);
    }

    public void init(Context context) {
        mContext = context;
        mDB = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ThreadDAOImpl.FINISHED_TAR_INFO_TABLE + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,tar_name varchar(30),adddate date);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ThreadDAOImpl.THREAD_INFO_TABLE + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,thread_id integer,url varchar(200),start integer,ended integer,finished integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public synchronized long insertData(String table, ContentValues contentValues) {
        return mDB.insert(table, null, contentValues);
    }

    public synchronized int updateData(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return mDB.update(table, values, whereClause, whereArgs);
    }

    public synchronized Cursor queryData(String table) {
        return mDB.query(table, null, null, null, null, null, null);
    }

    public synchronized Cursor queryData(String table, String selection, String[] selectionArgs) {
        return mDB.query(table, null, selection, selectionArgs, null, null, null);
    }

    public synchronized Cursor queryData(String table, String orderby) {
        return mDB.query(table, null, null, null, null, null, orderby + " date desc");
    }

    public synchronized Cursor queryData(String table, String selection, String[] selectionArgs, String orderby) {
        return mDB.query(table, null, selection, selectionArgs, null, null, orderby);
    }

    public synchronized int deleteAllData(String table) {
        return mDB.delete(table, null, null);
    }

    public synchronized int deleteData(String table, String whereClause, String[] whereArgs) {
        return mDB.delete(table, whereClause, whereArgs);
    }
}
