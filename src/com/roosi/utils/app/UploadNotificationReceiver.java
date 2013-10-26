package com.roosi.utils.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

public class UploadNotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {		
		long id = intent.getLongExtra(UploadManager.EXTRA_UPLOAD_ID, -1);
		
        UploadManager.Query query = new UploadManager.Query();                
        query.setFilterById(id);

        Cursor c = UploadManager.getInstance(context).query(query);
        String path = null;
        int status = UploadManager.STATUS_SUCCESSFUL;
        if (c.moveToFirst()) {
        	path = c.getString(c.getColumnIndex(UploadManager.COLUMN_LOCAL_PATH));
        	status = c.getInt(c.getColumnIndex(UploadManager.COLUMN_STATUS));
        }
        c.close();        		

        UploadManager.getInstance(context).remove(id);

        if (status == UploadManager.STATUS_FAILED) {
        	//TODO
        }
	}
}
