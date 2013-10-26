package com.roosi.utils.util;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

public class Launcher {

	public static void getDirections(Context context, String name, double longitude, double latitude) {
        Intent intent = new Intent(
        		Intent.ACTION_VIEW, 
        		Uri.parse("http://maps.google.com/maps?daddr=" 
        				+ latitude + "," + longitude + 
        				" (" + name + ")"));
        // choose maps over browser
        intent.setClassName("com.google.android.apps.maps", 
        		"com.google.android.maps.MapsActivity");
        context.startActivity(intent);		
	}

	public static void getDirections(Context context, String address) {
        Intent intent = new Intent(
        		Intent.ACTION_VIEW, 
        		Uri.parse("http://maps.google.com/maps?daddr=" + address));
        // choose maps over browser
        intent.setClassName("com.google.android.apps.maps", 
        		"com.google.android.maps.MapsActivity");
        context.startActivity(intent);
	}

	public static void navigateTo(Context context, String address) {
        Intent intent = new Intent(
        		Intent.ACTION_VIEW, 
        		Uri.parse("google.navigation:q=" + address));
        context.startActivity(intent);
	}

	public static void showFacebookWall(Context context, String id) {
		Intent intent = new Intent(
				Intent.ACTION_VIEW, 
				Uri.parse("fb://profile/" + id + "/wall"));
		context.startActivity(intent);
	}

	public static void showFacebookPage(Context context, String uri) {
		if (uri.startsWith("fb://")) {
			Intent intent = new Intent(
					Intent.ACTION_VIEW, 
					Uri.parse(uri));
			context.startActivity(intent);
		}
		else {
			showWebPage(context, uri);
		}
	}

	public static void showWebPage(Context context, String uri) {
		if (!uri.startsWith("http")) {
			uri = "http://" + uri;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		context.startActivity(intent);
	}

	public static void sendEmail(Context context, String to, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822"); // text/plain
        intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{to});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT   , body);
        context.startActivity(Intent.createChooser(intent, null));
	}

	public static void viewDialer(Context context, String number) {
        Uri uri = Uri.parse("tel:" + number); 
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(intent);
	}
	
	public static void callToNumber(Context context, String number) {
        Uri uri = Uri.parse("tel:" + number); 
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        context.startActivity(intent);
	}

	public static boolean isIntentAvailable(Context context, Intent intent) {
		final PackageManager packageManager = context.getPackageManager();
		List<ResolveInfo> list = 
			packageManager.queryIntentActivities(
					intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
}
