package com.roosi.utils.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.roosi.utils.util.Cache;
import com.roosi.utils.util.ImageDownloader;
import com.roosi.utils.util.ImageDownloader.OnDownloadAsyncListener;

public class HttpImageView extends ImageView {

	protected static final String TAG = "HttpImageView";
	protected Context mContext;
	protected Cache mCache = null;
	protected View mView = null;
	protected ImageDownloader mLoader = null;
	
	public HttpImageView(Context context) {
		super(context);
		mContext = context;
	}

	public HttpImageView(Context context, Cache cache) {
		super(context);
		mContext = context;
		mCache = cache;
	}

	public HttpImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public void setCache(Cache cache) {
		mCache = cache;
	}
	
   /**
    * Sets a bitmap specified by http uri as the content of this ImageView.
    * Utilizes cache if set, otherwise downloads the bitmap from network.
    * The downloaded bitmap is saved to the non-persistent cache.
    *
    * @param uri
    */
	public void setImageHttpUri(Uri uri) {
		
		if (uri != null) {
			Bitmap bitmap = null;
			if (mCache != null) {
				bitmap = mCache.getBitmap(uri.toString(), false);
			}
			
			if (bitmap == null) {
				if (mLoader != null) {
					mLoader.cancel();
				}
				mLoader = new ImageDownloader(mContext);
				mLoader.setOnDownloadAsyncListener(new OnDownloadAsyncListener() {
						
					@Override
					public void onDownloadAsyncReady(String url, Object object, Bitmap bitmap) {
			    		if (bitmap != null) {
			    			setImageBitmap(bitmap);
			    			if (mCache != null) {
			    				mCache.setBitmap(url, bitmap);
			    			}
			    		}					
					}
				});
				mLoader.downloadAsync(uri.toString(), null, true);			
			}
			else {
				setImageBitmap(bitmap);			
			}
		}
		else {
			if (mLoader != null) {
				mLoader.cancel();
				mLoader = null;
			}
			setImageBitmap(null);
		}
	}

   /**
    * Sets a bitmap specified by http uri as the content of this ImageView.
    * Utilizes cache if set, otherwise downloads the bitmap from network.
    * The downloaded bitmap is saved to the non-persistent cache. 
    * Provided view is hidden after the bitmap is set. This could be used to
    * show some indication while downloading is in progress.
    *
    * @param uri
    * 
    * @param view
    */	
	public void setImageHttpUri(Uri uri, View view) {

		if (uri != null) {
			mView = view;
			Bitmap bitmap = null;
			if (mCache != null) {
				bitmap = mCache.getBitmap(uri.toString(), false);
			}
			
			if (bitmap == null) {
				if (mLoader != null) {
					mLoader.cancel();
				}
				mLoader = new ImageDownloader(mContext);
				mLoader.setOnDownloadAsyncListener(new OnDownloadAsyncListener() {

					@Override
					public void onDownloadAsyncReady(String url, Object object, Bitmap bitmap) {
			    		if (bitmap != null) {
			    			if (mView != null) {
				    			mView.setVisibility(View.GONE);
			    			}
			    			setImageBitmap(bitmap);
			    			if (mCache != null) {
			    				mCache.setBitmap(url, bitmap);
			    			}
			    		}					
					}
				});
				mLoader.downloadAsync(uri.toString(), null, true);			
			}
			else {
				if (mView != null) {
					mView.setVisibility(View.GONE);
				}
				setImageBitmap(bitmap);			
			}
		}
		else {
			if (mLoader != null) {
				mLoader.cancel();
				mLoader = null;
			}			
			setImageBitmap(null);
		}
	}
}
