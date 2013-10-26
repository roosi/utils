package com.roosi.utils.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ConnectivityReceiver extends BroadcastReceiver {

	private static final String TAG = "ConnectivityReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Utils.isOnline(context)) {
			Log.d(TAG, "onReceive: online");

			Intent service = new Intent(context, UploadService.class);
			context.startService(service);
		}
		else {
			Log.d(TAG, "onReceive: offline"); 
		}
	}
}
