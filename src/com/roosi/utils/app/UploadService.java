package com.roosi.utils.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UploadService extends Service {
	private static final String TAG = "UploadService";
	
	public UploadService() {
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");

		UploadManager mgr = UploadManager.getInstance(getApplicationContext());
		mgr.setUploadFileService(new UploadFileServiceImp(getApplicationContext()));
		mgr.restartUpload();

		return Service.START_STICKY;
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
