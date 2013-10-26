package com.roosi.utils.debug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class CrashReport implements Thread.UncaughtExceptionHandler {
	
	private static final String TAG = "CustomExceptionHandler";
	private static final String STACK_TRACE = "stack_trace";
	private static CrashReport mInstance = null;
	private Context mContext = null;
	private String mEmail = null;
	private Dialog mDialog = null;
	private UncaughtExceptionHandler mDefaultExceptionHandler = null;

	public static CrashReport getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new CrashReport(context);
		}
		return mInstance ;
	}
	
	private CrashReport(Context context) {
		mContext = context;

        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// get stack trace
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		
		// save to preferences
		SharedPreferences preferences =
		mContext.getSharedPreferences(TAG, 0);
		Editor editor = preferences.edit();
		editor.putString(STACK_TRACE, sw.toString() + "\n" + getLog());
		editor.commit();

        //android.os.Process.killProcess(android.os.Process.myPid());
        //System.exit(10);

		mDefaultExceptionHandler.uncaughtException(thread, ex);
	}
	
	public void send() {
		final SharedPreferences preferences = mContext.getSharedPreferences(TAG, 0);
		final String trace = preferences.getString(STACK_TRACE, null);
		if (trace != null) {
			String application = "unknown";
			String version = "unknown";
			try {
				PackageInfo info =
				mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
				version = info.versionName + " / " + info.versionCode;
				application = mContext.getString(info.applicationInfo.labelRes);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			
			final String subject = application + " crashed";
			final String body = 
					application + ": " + version + "\n" +
					"Android version: " + android.os.Build.VERSION.RELEASE + "\n" +
					"Device: " + android.os.Build.MODEL + "\n" +
					"\nStack trace:\n" + trace +
					"\nPlease write a description how the crash happened.\n";
			
			mDialog = new AlertDialog.Builder(mContext)
			.setTitle("Application crashed")
		    .setMessage("Do you want to help us to make the application better by sending the crash report to us?")
		    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	Intent intent = new Intent(Intent.ACTION_SEND);
		        	intent.setType("message/rfc822");
		        	intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getEmail()});
		        	intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		        	intent.putExtra(Intent.EXTRA_TEXT,body);
		        	try {
		        		mContext.startActivity(Intent.createChooser(intent, "Send crash report"));
		        	} catch (android.content.ActivityNotFoundException e) {
		        		e.printStackTrace();
		        	}
					// clear stack trace
					Editor editor = preferences.edit();
					editor.clear();
					editor.commit();
		        }
		     })
		    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
					// clear stack trace
					Editor editor = preferences.edit();
					editor.clear();
					editor.commit();
		        }
		     })
		     .setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					Editor editor = preferences.edit();
					editor.clear();
					editor.commit();
					dialog.dismiss();
				}
			})
		     .show();                
		}
	}

	public boolean isSending() {
		return mDialog != null ? true : false;
	}

	public boolean isClean() {
		boolean clean = true;		
		final SharedPreferences preferences = mContext.getSharedPreferences(TAG, 0);
		final String trace = preferences.getString(STACK_TRACE, null);
		if (trace != null) {
			clean = false;
		}
		return clean;
	}

	public void cancel() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) {
		mEmail = email;
	}
	
	private String getLog() {
		StringBuilder log=new StringBuilder();
	    try {
	        Process process = Runtime.getRuntime().exec("logcat -d -t 100");
	        BufferedReader bufferedReader = new BufferedReader(
	        		new InputStreamReader(process.getInputStream()));
	        
	        String line;
	        while ((line = bufferedReader.readLine()) != null) {
	        	log.append(line + "\n");
	        }
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    return log.toString();
	}
}
