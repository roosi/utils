package com.roosi.utils.util;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.roosi.utils.R;
import com.roosi.utils.util.ImageDownloader.OnDownloadAsyncListener;

/**
 * The class provides cache for images. Images are specified using 
 * a network url. File name in the url must be unique. The image is
 * downloaded from network, if not found in heap, resources or 
 * private files.
 */

public class Cache {
	private static final String TAG = "Cache";
	private static final int CACHE_SIZE = 100;
	private static final String INVALID_URL = "invalid_url";

	private static Cache mInstance = null;
	private Map<String, SoftReference<Bitmap>> mCache;
	private Context mContext = null;
	private boolean mCheckResources = true;

	public static Cache getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new Cache(context);
		}
		return mInstance;
	}

	public void setCheckResources(boolean checkResources) {
		mCheckResources = checkResources;
	}

	public boolean getCheckResources() {
		return mCheckResources;
	}

	private Cache(Context context){
		mContext = context;
		mCache = Collections.synchronizedMap(new BitmapCache(CACHE_SIZE));
		
		Bitmap bitmap = retrieveBitmapFromResources(mContext, R.drawable.ic_launcher);
		mCache.put(INVALID_URL, new SoftReference<Bitmap>(bitmap));
	}

   /**
    * Checks if the bitmap specified by the url already exists in heap.
    * 
    * @param url
    *
    * @return boolean
    */
	public boolean exists(String url) {
		return mCache.containsKey(url);
	}
	
   /**
    * Adds new bitmap to cache.
    *
    * @param url
    * @param bitmap 
    */
	public void setBitmap(String url, Bitmap bitmap) {
		if (isValidUrl(url) == false || bitmap == null) {
			Log.w(TAG, "url is not valid " + url);
		}
    	if (mCache.containsKey(url) == false) {
    		mCache.put(url, new SoftReference<Bitmap>(bitmap));
    	}
	}
	

   /**
    * Returns the bitmap specified by the url. If not in cache 
    * or invalid url, default image is returned for debugging
    * purposes. The method runs in the caller's thread.
    *
    * @param url
    * 
    * @return Bitmap
    */
	public Bitmap getBitmap(String url) {
		return getBitmap(url, true);
	}
	
	public Bitmap getBitmap(String url, boolean noImage) {
		if (isValidUrl(url) == false) {
			Log.w(TAG, "url is not valid " + url);
			return getInvalidUrlBitmap();
		}

		Bitmap bitmap = null;
		
		// try from heap cache
    	if (mCache.containsKey(url) == true) {
    		bitmap = mCache.get(url).get();
    		if (bitmap == null) {
    			// gc has removed bitmap
    			mCache.remove(url);
    		}
    	}

    	if (bitmap == null) {
    		if (mCheckResources) {
	    		// try from resources
	    		bitmap = retrieveBitmapFromResources(mContext, url);
    		}
    		
    		// try from file system
    		if (bitmap == null) {
    			bitmap = FileUtils.retrieveBitmap(mContext, url);

    			if (bitmap == null) {
    				bitmap = FileUtils.retrieveCacheBitmap(mContext, url);
    			}
    		}
			// add to heap cache
    		if (bitmap != null) {
    			setBitmap(url, bitmap);
    		} 
    		else if (noImage){
    			bitmap = getInvalidUrlBitmap();
    		}    			
    	}

		return bitmap;    	
	}

   /**
    * Returns the bitmap to indicate that the url is invalid.
    * This is for debugging purposes.
    * 
    * @return Bitmap
    */
	public Bitmap getInvalidUrlBitmap() {
		Bitmap bitmap = mCache.get(INVALID_URL).get();

		if (bitmap == null) {
			bitmap = retrieveBitmapFromResources(mContext, R.drawable.ic_launcher);
			mCache.remove(INVALID_URL);
			mCache.put(INVALID_URL, new SoftReference<Bitmap>(bitmap));
		}

		return bitmap;
	}

   /**
    * Sets the bitmap to provided ImageView. If not in cache, 
    * the image will be downloaded from the network and added
    * to cache. The method runs in the own thread.
    *
    * @param url
    * @param view 
    * 
    * @return Bitmap
    */
	public void setImageBitmap(String url, ImageView view) {
		if (isValidUrl(url) == false) {
			Log.w(TAG, "url is not valid " + url);
			view.setImageResource(R.drawable.ic_launcher);
			return;
		}

		Bitmap bitmap = null;
		
		// try from heap cache
    	if (mCache.containsKey(url) == true) {
    		bitmap = mCache.get(url).get();
    		if (bitmap == null) {
    			// gc has removed bitmap
    			mCache.remove(url);
    		}
    		else {
    			view.setImageBitmap(bitmap);
    		}
    	}

    	if (bitmap == null) {
    		if (mCheckResources) {
	    		// try from resources
	    		bitmap = retrieveBitmapFromResources(mContext, url);
    		}
    		
    		// try from file system
    		if (bitmap == null) {
    			bitmap = FileUtils.retrieveBitmap(mContext, url);
    			
    			if (bitmap == null) {
    				bitmap = FileUtils.retrieveCacheBitmap(mContext, url);
    			}

    			// download from network
    			if (bitmap == null) {	
		    		ImageDownloader loader = new ImageDownloader(mContext);
		    		loader.setOnDownloadAsyncListener(new OnDownloadAsyncListener() {
						
						@Override
						public void onDownloadAsyncReady(String url, Object object, Bitmap bitmap) {
				    		if (bitmap != null) {
				    			setBitmap(url, bitmap);
				    		}					
						}
					});

		    		loader.downloadAsync(url, view);
    			}
    		}
			// add to heap cache
    		if (bitmap != null) {
    			setBitmap(url, bitmap);
    			view.setImageBitmap(bitmap);
    		}
    	}
	}	

	public void clear() {
		mCache.clear();
		/*
		String fileNames[] = mContext.fileList();
		for(String fileName : fileNames) {
			mContext.deleteFile(fileName);
		}
		*/
		File files[] = mContext.getCacheDir().listFiles();
		for(File file : files) {
			file.delete();
		}

	}

	private static Bitmap retrieveBitmapFromResources(Context context, String url) {
		Bitmap bitmap = null;
		String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
		String resourceName = null;
		if (fileName != null && fileName.length() != 0) {
			int end = fileName.lastIndexOf('.');
			if (end == -1) {
				end = fileName.length();
			}
			resourceName = fileName.substring(0, end);
		}

		if (resourceName != null && resourceName.length() != 0) {
			int id = context.getResources().getIdentifier(
					resourceName, 
					"drawable", 
					context.getPackageName());
			
			if (id != 0) {
				bitmap = BitmapFactory.decodeResource(context.getResources(), id);
			}
		}

		return bitmap;
	}

	private static Bitmap retrieveBitmapFromResources(Context context, int id) {		
		Bitmap bitmap = null;

		if (id != 0) {
			bitmap = BitmapFactory.decodeResource(context.getResources(), id);
		}
			
		return bitmap;
	}

	private static boolean isValidUrl(String url) {
		boolean result = false;
		
		if (url != null && url.length() != 0) {
			String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
			if (fileName != null && fileName.length() != 0) {
				int end = fileName.lastIndexOf('.');
				if (end == -1) {
					end = fileName.length();
				}
				String resourceName = fileName.substring(0, end);
				if (resourceName != null && resourceName.length() != 0) {
					result = true;
				}
			}
		}

		return result;
	}

	class BitmapCache extends LinkedHashMap<String, SoftReference<Bitmap>> {
		
		private final int mMaxEntries;
		public BitmapCache(int maxEntries) {
			super(maxEntries, 1.0f, true);
			mMaxEntries = maxEntries;
		}
		
		@Override
		protected boolean removeEldestEntry(
				java.util.Map.Entry<String, SoftReference<Bitmap>> eldest) {
			return super.size() > mMaxEntries;
		}
	}
}
