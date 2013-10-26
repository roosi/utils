package com.roosi.utils.util;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.RejectedExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 * The class provides synchronous and asynchronous methods 
 * to download an image specified by a url. Synchronous one 
 * returns a bitmap and asynchronous one sets a bitmap to 
 * ImageView. The image is saved in private files.
 */

public class ImageDownloader {
	private static final String TAG = "ImageDownloader";
	private String mUrl = null;
	private Object mObject = null;
	private Context mContext = null;
	private BitmapDownloaderTask mTask = null;
	
	public ImageDownloader(Context context){
		mContext = context;
	}
	
   /**
	* Downloads the image specified by the url and set the result to
	* provided ImageView. The method runs in the own thread, see
	* OnDownloadAsyncListener for result.
	*
	* @param url
	* @param imageView 
	*/
    public void downloadAsync(String url, ImageView imageView) {
    	mUrl = url;
    	mObject = null;
		mTask = new BitmapDownloaderTask(imageView, false);
		mTask.execute(url);
    }

   /**
	* Downloads the image specified by the url. The method runs in 
	* the own thread, see OnDownloadAsyncListener for result.
	*
	* @param url
	* @param object  
	*/
    public void downloadAsync(String url, Object object) {
    	mUrl = url;
    	mObject = object;
    	mTask = new BitmapDownloaderTask(null, false);
    	mTask.execute(url);
    }

    /**
	* Downloads the image specified by the url. The method runs in 
	* the own thread, see OnDownloadAsyncListener for result. The image
	* is saved to the application's private or private cache storage
	* depending on nonPersistent.
	*
	* @param url
	* @param object
	* @param nonPersistent
	*/    
    public void downloadAsync(String url, Object object, boolean nonPersistent) {
    	mUrl = url;
    	mObject = object;
    	mTask = new BitmapDownloaderTask(null, nonPersistent);
    	try {
        	mTask.execute(url);	
		} catch (RejectedExecutionException e) {
			// TODO: handle exception
			Log.w(TAG, "Execution rejected for bitmap " + url + e.toString());
		}
    }

    public interface OnDownloadAsyncListener {
    	public abstract void onDownloadAsyncReady(String url, Object object, Bitmap bitmap);
    }
    
    private OnDownloadAsyncListener mOnDownloadAsyncListener = null;
    
    public void setOnDownloadAsyncListener(OnDownloadAsyncListener listener) {
    	mOnDownloadAsyncListener = listener;
    }

   /**
	* Downloads the image specified by the url and set the result to
	* provided ImageView. The method runs in the caller's thread.
	*
	* @param url
	* @param imageView 
	*/
    public Bitmap downloadSync(String url, ImageView imageView) {
    	return downloadBitmap(url);
    }

    public void cancel() {
    	mTask.cancel(true);
    }

    class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> mImageViewReference;
        private final boolean mNonPersistent;

        public BitmapDownloaderTask(ImageView imageView, boolean nonPersistent) {
        	if (imageView != null) {
        		mImageViewReference = new WeakReference<ImageView>(imageView);
        	} 
        	else {
        		mImageViewReference = null;
        	}
        	mNonPersistent = nonPersistent;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
     	    final String url = params[0];
     	    final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
    	    HttpClientParams.setRedirecting(client.getParams(), true);

    	    HttpGet getRequest = null;
    	    try {
    	    	getRequest = new HttpGet(url);
    	        HttpResponse response = client.execute(getRequest);
    	        final int statusCode = response.getStatusLine().getStatusCode();
    	        if (statusCode != HttpStatus.SC_OK) { 
    	            Log.w(TAG, "Error " + statusCode + " while retrieving bitmap from " + url); 
    	            return null;
    	        }
    	        
    	        final HttpEntity entity = response.getEntity();
    	        if (entity != null) {
    	            InputStream inputStream = null;
	            	if (isCancelled()) {
	            		return null;
	            	}
    	            try {
    	            	if (entity.getContentLength() > 0) {
    		                inputStream = entity.getContent();
    		                if (mNonPersistent) {
    		                	FileUtils.saveCacheFile(mContext, url, inputStream);
    		                } 
    		                else {
    		                	FileUtils.saveFile(mContext, url, inputStream);
    		                }
        	            	if (isCancelled()) {
        	            		inputStream.close();
        	            		return null;
        	            	}
    		                if (mNonPersistent) {
    		                	return FileUtils.retrieveCacheBitmap(mContext, url);
    		                } 
    		                else {
    		                	return FileUtils.retrieveBitmap(mContext, url);
    		                }
    	            	}
    	            } finally {
    	                if (inputStream != null) {
    	                    inputStream.close();  
    	                }
    	                entity.consumeContent();
    	            }
    	        }
    	    } catch (Exception e) {
    	    	if (getRequest != null) {
    	    		getRequest.abort();
    	    	}
    	        Log.w(TAG, "Error while retrieving bitmap from " + url + e.toString());
    	    } finally {
    	        if (client != null) {
    	            client.close();
    	        }
    	    }
    	    return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (mImageViewReference != null) {
                ImageView imageView = mImageViewReference.get();
                if (imageView != null) {
                	if (bitmap != null) {
                		imageView.setImageBitmap(bitmap);
                	}
                }
            }
            if (mOnDownloadAsyncListener != null) {
            	mOnDownloadAsyncListener.onDownloadAsyncReady(mUrl, mObject, bitmap);
            }            
        }
    }

	private Bitmap downloadBitmap(String url) {
	    final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
	    HttpClientParams.setRedirecting(client.getParams(), true);

	    HttpGet getRequest = null;
	    try {
	    	getRequest = new HttpGet(url);
	        HttpResponse response = client.execute(getRequest);
	        final int statusCode = response.getStatusLine().getStatusCode();
	        if (statusCode != HttpStatus.SC_OK) { 
	            Log.w(TAG, "Error " + statusCode + " while retrieving bitmap from " + url); 
	            return null;
	        }
	        
	        final HttpEntity entity = response.getEntity();
	        if (entity != null) {
	            InputStream inputStream = null;
	            try {
	            	if (entity.getContentLength() > 0) {
		                inputStream = entity.getContent();
		                FileUtils.saveFile(mContext, url, inputStream);
		                return FileUtils.retrieveBitmap(mContext, url);	            		
	            	}
	            } finally {
	                if (inputStream != null) {
	                    inputStream.close();  
	                }
	                entity.consumeContent();
	            }
	        }
	    } catch (Exception e) {
	    	if (getRequest != null) {
	    		getRequest.abort();
	    	}
	        Log.w(TAG, "Error while retrieving bitmap from " + url + e.toString());
	    } finally {
	        if (client != null) {
	            client.close();
	        }
	    }
	    return null;
	}	
}
