package com.roosi.utils.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UploadsDatabase extends SQLiteOpenHelper {	
	public static final String TABLE_REQUESTS = "requests";
	private static final String DB_NAME = "uploads";	
	private static int DB_VERSION = 2;

	public UploadsDatabase(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {    	
    	db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_REQUESTS + " ("
			+ UploadManager.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ UploadManager.COLUMN_TITLE + " TEXT NOT NULL, "
			+ UploadManager.COLUMN_LOCAL_PATH + " TEXT NOT NULL, "
			+ UploadManager.COLUMN_STATUS + " INTEGER, "
			+ UploadManager.COLUMN_REASON + " INTEGER, "
			+ UploadManager.COLUMN_TAG + " TEXT, "
			+ UploadManager.COLUMN_VISIBILITY + " INTEGER"
			+ ");");	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUESTS);
		onCreate(db);
	} 	
}
