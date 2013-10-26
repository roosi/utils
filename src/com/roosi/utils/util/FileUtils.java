package com.roosi.utils.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * The class provides utility methods to access private and private cache files.
 */

public class FileUtils {
	private static final String TAG = "FileUtils";
	private static final int FILE_BUFFER_SIZE = 1024;
	static final Object[] sDataLock = new Object[0];

	public static void saveFile(Context context, String url, InputStream is) {
		String name = url.substring(url.lastIndexOf('/') + 1, url.length());
		FileOutputStream fos;
		
		if (name != null && name.length() != 0) {
			try {
				synchronized (sDataLock) {	
					fos = context.openFileOutput(name, Context.MODE_PRIVATE);
					byte[] buffer = new byte[FILE_BUFFER_SIZE];
					int read = 0;
					while ((read = is.read(buffer)) != -1) {
						fos.write(buffer, 0, read);
					}
					fos.close();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void saveCacheFile(Context context, String url, InputStream is) {
		String name = url.substring(url.lastIndexOf('/') + 1, url.length());
		FileOutputStream fos;
		
		if (name != null && name.length() != 0) {
			try {
				synchronized (sDataLock) {
					File file = new File(context.getCacheDir(), name);
					fos = new FileOutputStream(file);
					byte[] buffer = new byte[FILE_BUFFER_SIZE];
					int read = 0;
					while ((read = is.read(buffer)) != -1) {
						fos.write(buffer, 0, read);
					}
					fos.close();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void deleteFile(Context context, String url) {
		String name = url.substring(url.lastIndexOf('/') + 1, url.length());

		if (name != null && name.length() != 0) {
			synchronized (sDataLock) {
				context.deleteFile(name);
			}
		}
	}

	public static void deleteCacheFile(Context context, String url) {
		String name = url.substring(url.lastIndexOf('/') + 1, url.length());

		if (name != null && name.length() != 0) {
			synchronized (sDataLock) {
				File file = new File(context.getCacheDir(), name);
				file.delete();
			}
		}
	}

	public static Bitmap retrieveBitmap(Context context, String url) {
		String name = url.substring(url.lastIndexOf('/') + 1, url.length());
		FileInputStream fis = null;
		Bitmap bitmap = null;

		if (name != null && name.length() != 0) {
			try {
				synchronized (sDataLock) {
					fis = context.openFileInput(name);
					if (fis != null) {
						bitmap = BitmapFactory.decodeStream(fis);
					}
					fis.close();
				}
			} catch (FileNotFoundException e) {
				//Log.d(TAG, "File not found: " + name);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				Log.d(TAG, "Out of memory: " + url);
			}
		}

		return bitmap;
	}
	
	public static Bitmap retrieveCacheBitmap(Context context, String url) {
		String name = url.substring(url.lastIndexOf('/') + 1, url.length());
		FileInputStream fis = null;
		Bitmap bitmap = null;

		if (name != null && name.length() != 0) {
			try {
				synchronized (sDataLock) {
					File file = new File(context.getCacheDir(), name);
					fis = new FileInputStream(file);
					if (fis != null) {
						bitmap = BitmapFactory.decodeStream(fis);
					}
					fis.close();
				}
			} catch (FileNotFoundException e) {
				//Log.d(TAG, "File not found: " + name);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				Log.d(TAG, "Out of memory: " + url);
			}
		}

		return bitmap;
	}
}
