package com.roosi.utils.app;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.roosi.utils.app.UploadManager.Request;

public class UploadRequestDataAccess {

	private static final String TAG = "UploadRequestDataAccess";
	
	private SQLiteDatabase mDatabase;
	private UploadsDatabase mDbHelper;
	  
	public UploadRequestDataAccess(Context context) {
		mDbHelper = new UploadsDatabase(context);
	}

	public void open() {
		mDatabase = mDbHelper.getWritableDatabase();
	}

	public void close() {
		//mDbHelper.close();
	}
	
	public long insert(Request request) {
		Log.d(TAG, "insert " + request);
		ContentValues values = requestToContentValues(request);

		long rowId = mDatabase.insert(UploadsDatabase.TABLE_REQUESTS, null, values);
		if (rowId < 0) {
			Log.d(TAG, "insert failed");
		}

		return rowId;
	}
	
	public boolean update(Request request) {
		boolean result = true;
		ContentValues values = requestToContentValues(request);

		long rowId = mDatabase.update(UploadsDatabase.TABLE_REQUESTS, values,
				UploadManager.COLUMN_ID + "=" + request.getId(), null);
		if (rowId < 0) {
			Log.d(TAG, "update failed");
			result = false;
		}

		return result;
	}
	
	public Request query(long id) {
		Cursor cursor = mDatabase.query(UploadsDatabase.TABLE_REQUESTS, 
				null, UploadManager.COLUMN_ID + "=" + id + "", 
				null, null, null, null);
		
		cursor.moveToFirst();
		Request request = null;
		try {
			request = cursorToRequest(cursor);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		cursor.close();		
		return request;
	}
	
	public Cursor query(long[] mIds) {
        List<String> selectionParts = new ArrayList<String>();
        String[] selectionArgs = null;

        if (mIds != null) {
            selectionParts.add(getWhereClauseForIds(mIds));
            selectionArgs = getWhereArgsForIds(mIds);
        }
        String selection = joinStrings(" AND ", selectionParts);
        
		return mDatabase.query(UploadsDatabase.TABLE_REQUESTS, null, selection, selectionArgs, null, null, null);
	}

    private String joinStrings(String joiner, Iterable<String> parts) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String part : parts) {
            if (!first) {
                builder.append(joiner);
            }
            builder.append(part);
            first = false;
        }
        return builder.toString();
    }

    /**
     * Get a parameterized SQL WHERE clause to select a bunch of IDs.
     */
    static String getWhereClauseForIds(long[] ids) {
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("(");
        for (int i = 0; i < ids.length; i++) {
            if (i > 0) {
                whereClause.append("OR ");
            }
            whereClause.append(UploadManager.COLUMN_ID);
            whereClause.append(" = ? ");
        }
        whereClause.append(")");
        return whereClause.toString();
    }

    /**
     * Get the selection args for a clause returned by {@link #getWhereClauseForIds(long[])}.
     */
    static String[] getWhereArgsForIds(long[] ids) {
        String[] whereArgs = new String[ids.length];
        for (int i = 0; i < ids.length; i++) {
            whereArgs[i] = Long.toString(ids[i]);
        }
        return whereArgs;
    }

	public List<Request> queryAll() {
		List<Request> requests = new ArrayList<Request>();
		
		Cursor cursor = mDatabase.query(UploadsDatabase.TABLE_REQUESTS, null, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Request request = null;
			try {
				request = cursorToRequest(cursor);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (request != null) {
				requests.add(request);
			}
			cursor.moveToNext();
		}
		
		cursor.close();
		return requests;
	}
	
	public boolean delete(Request request) {
		return delete(request.getId());
	}

	public boolean delete(long id) {
		boolean result = true;
		final String selection = UploadManager.COLUMN_ID + "=" + id;

		int rowsAffected = mDatabase.delete(UploadsDatabase.TABLE_REQUESTS, selection, null);
		if (rowsAffected != 1) {
			Log.d(TAG, "delete failed");
			result = false;
		}
		return result;
	}
	
	private ContentValues requestToContentValues(Request request) {
		ContentValues values = new ContentValues();
		values.put(UploadManager.COLUMN_TITLE, request.getTitle());		
		values.put(UploadManager.COLUMN_LOCAL_PATH, request.getLocalPath());
		values.put(UploadManager.COLUMN_VISIBILITY, request.getShowRunningNotification() ? 1 : 0);
		values.put(UploadManager.COLUMN_STATUS, request.getStatus());
		values.put(UploadManager.COLUMN_REASON, request.getReason());
		values.put(UploadManager.COLUMN_TAG, request.getTag());
		return values;
	}

	private Request cursorToRequest(Cursor cursor) throws ParseException {
		String path = cursor.getString(cursor.getColumnIndex(UploadManager.COLUMN_LOCAL_PATH)); 		
		Request request = new Request(new File(path));
		request.setId(cursor.getInt(cursor.getColumnIndex(UploadManager.COLUMN_ID)));
		request.setTitle(cursor.getString(cursor.getColumnIndex(UploadManager.COLUMN_TITLE)));
		request.setShowRunningNotification(cursor.getInt(cursor.getColumnIndex(UploadManager.COLUMN_VISIBILITY)) == 1 ? true : false);
		request.setStatus(cursor.getInt(cursor.getColumnIndex(UploadManager.COLUMN_STATUS)));
		request.setReason(cursor.getInt(cursor.getColumnIndex(UploadManager.COLUMN_REASON)));
		request.setTag(cursor.getString(cursor.getColumnIndex(UploadManager.COLUMN_TAG)));
		return request;
	}
}
