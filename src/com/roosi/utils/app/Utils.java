package com.roosi.utils.app;

import java.net.URLConnection;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.webkit.MimeTypeMap;

public final class Utils {
	
	public static boolean isOnline(Context context) {
	    ConnectivityManager cm =
	        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	public static void setConnectivityReceiverEnabled(Context context, boolean enabled) {			
		final ComponentName connectivityReceiver = new ComponentName(context, 
				ConnectivityReceiver.class);

		int state =  PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
		if (enabled) {
			state = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
		}
		
		context.getPackageManager().setComponentEnabledSetting(connectivityReceiver, state, 
				PackageManager.DONT_KILL_APP);
	}
	
	public static String getMimeType(String url)
	{
	    String type = null;
	    String extension = MimeTypeMap.getFileExtensionFromUrl(url);
	    if (extension != null) {
	        MimeTypeMap mime = MimeTypeMap.getSingleton();
	        type = mime.getMimeTypeFromExtension(extension);
	        if (type == null) {
	        	type = URLConnection.guessContentTypeFromName(url);
	        }
	    }
	    return type;
	}

}
