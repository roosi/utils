package com.roosi.utils.app;

import java.io.File;
import java.util.List;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.Toast;

import com.roosi.utils.R;

public class UploadManager {
	private static final String TAG = "UploadManager";

    /**
     * An identifier for a particular upload, unique across the system.  Clients use this ID to
     * make subsequent calls related to the upload.
     */
    public final static String COLUMN_ID = BaseColumns._ID;

    /**
     * The client-supplied title for this upload.  This will be displayed in system notifications.
     * Defaults to the empty string.
     */
    public final static String COLUMN_TITLE = "title";

    /**
     * Path to be uploaded.
     */
    public final static String COLUMN_LOCAL_PATH = "local_path";

    /**
     * Control whether a system notification is posted by the upload manager while uploading.
     */
    public final static String COLUMN_VISIBILITY = "visibility";

    /**
     * Tag associated with a request.
     */
    public final static String COLUMN_TAG = "tag";

    /**
     * Current status of the download, as one of the STATUS_* constants.
     */
    public final static String COLUMN_STATUS = "status";

    /**
     * Value of {@link #COLUMN_STATUS} when the upload is waiting to start.
     */
    public final static int STATUS_PENDING = 1 << 0;

    /**
     * Value of {@link #COLUMN_STATUS} when the upload is currently running.
     */
    public final static int STATUS_RUNNING = 1 << 1;

    /**
     * Value of {@link #COLUMN_STATUS} when the upload is waiting to retry or resume.
     */
    public final static int STATUS_PAUSED = 1 << 2;

    /**
     * Value of {@link #COLUMN_STATUS} when the upload has successfully completed.
     */
    public final static int STATUS_SUCCESSFUL = 1 << 3;

    /**
     * Value of {@link #COLUMN_STATUS} when the upload has failed (and will not be retried).
     */
    public final static int STATUS_FAILED = 1 << 4;

    /**
     * Provides more detail on the status of the upload.  Its meaning depends on the value of
     * {@link #COLUMN_STATUS}.
     *
     * When {@link #COLUMN_STATUS} is {@link #STATUS_FAILED}, this indicates the type of error that
     * occurred.  If an HTTP error occurred, this will hold the HTTP status code as defined in RFC
     * 2616.  Otherwise, it will hold one of the ERROR_* constants.
     */
    public final static String COLUMN_REASON = "reason";    

    /**
     * Broadcast intent action sent by the upload manager when a upload completes.
     */
    public final static String ACTION_UPLOAD_COMPLETE = 
    		"com.roosi.utils.intent.action.UPLOAD_COMPLETE";

    /**
     * Broadcast intent action sent by the upload manager when the user clicks on a running
     * download from a system notification.
     */
    public final static String ACTION_NOTIFICATION_CLICKED =
            "com.roosi.utils.intent.action.UPLOAD_NOTIFICATION_CLICKED";

    /**
     * Intent extra included with {@link #ACTION_UPLOAD_COMPLETE} intents, indicating the ID (as a
     * long) of the upload that just completed.
     */
    public static final String EXTRA_UPLOAD_ID = "extra_upload_id";

	private static final String NOTIFICATION_TAG = "com.roosi.utils.UploadManager";

	public static class Request {
		
		private File mFile;
		private CharSequence mTitle;
		private boolean mShowNotification = true;
		private long mId = -1;
		private int mStatus = -1;
		private int mReason = -1;
		private String mTag = null;
		
        /**
         * @param uri the file URI to upload.
         */
        public Request(File file) {
            if (file == null) {
                throw new NullPointerException();
            }
            mFile = file;
            mStatus = STATUS_PENDING;
        }
        
        /**
         * Set the title of this download, to be displayed in notifications (if enabled).  
         * If no title is given, a default one will be assigned based on the filename, once 
         * the upload starts.
         * @return this object
         */
        public Request setTitle(CharSequence title) {
            mTitle = title;
            return this;
        }
        
        /**
         * Control whether a system notification is posted by the upload manager while this
         * upload is running. If enabled, the upload manager posts notifications about uploads
         * through the system {@link android.app.NotificationManager}. By default, a notification is
         * shown.
         *
         * @param show whether the upload manager should show a notification for this upload.
         * @return this object
         */
        public Request setShowRunningNotification(boolean show) {
            mShowNotification = show;
            return this;
        }       
        
        /**
         * Sets the tag associated with this request. Tags can be used to store data within 
         * a request without resorting to another data structure.
         * 
         * @param an String to tag the request with
         */
        public void setTag (String tag) {
        	mTag = tag;
        }
        
        /**
         * Returns this request's tag.
         * 
         * @return the Object stored in this request as a tag
         */
        public String getTag() {
        	return mTag ;
        }
        
        public File getFile() {
        	return mFile;
        }

        protected String getTitle() {
        	return mTitle.toString();
        }
        
        protected String getLocalPath() {
        	return mFile.getPath();
        }

        protected void setId(long id) {
			mId = id;
		}
        
        protected long getId() {
			return mId;
		}
        
        protected boolean getShowRunningNotification() {
            return mShowNotification;
        }

        protected int getStatus() {
			return mStatus;
		}

		public int getReason() {
			return mReason;
		}

		protected void setStatus(int status) {
			mStatus = status;
		}

		protected void setReason(int reason) {
			mReason = reason;
		}
	}
	
	/**
     * This class may be used to filter upload manager queries.
     */
    public static class Query {    	
        private long[] mIds = null;

        /**
         * Include only the downloads with the given IDs.
         * @return this object
         */
        public Query setFilterById(long... ids) {
            mIds = ids;
            return this;
        }
    }

    public interface IUploadFileService {
    	int uploadFile(Request request);
    }

	private static UploadManager mInstance = null;
	
	public static UploadManager getInstance(Context context) {
	
		if (mInstance == null) {
			mInstance = new UploadManager(context);
		}
		return mInstance;
	}
	
	private Context mContext = null;
	private UploadRequestDataAccess mDataAccess = null;
	private IUploadFileService mUploadFileService = null;
	
	private UploadManager(Context context) {
		mContext = context;
		mDataAccess = new UploadRequestDataAccess(context);
	}
	
	public void setUploadFileService(IUploadFileService uploadFileService) {
		mUploadFileService = uploadFileService;
	}

    /**
     * Enqueue a new upload.  The upload will start automatically once the upload manager is
     * ready to execute it and connectivity is available.
     *
     * @param request the parameters specifying this upload
     * @return an ID for the upload, unique across the system.  This ID is used to make future
     * calls related to this upload.
     */
    public long enqueue(Request request) {
		Log.d(TAG, "enqueue");    	

		if (mUploadFileService == null) 
			throw new NullPointerException();

		mDataAccess.open();
    	long id = mDataAccess.insert(request);
    	mDataAccess.close();
    	request.mId = id;

    	if (Utils.isOnline(mContext)) {
    		Toast.makeText(mContext, 
    				mContext.getString(R.string.uploading_started), 
    				Toast.LENGTH_LONG).show();    		
        	new UploadTask(mContext, mDataAccess, mUploadFileService).execute(request);
    	}
    	else {
    		Toast.makeText(mContext, 
    				mContext.getString(R.string.uploading_starts_when_service_is_available), 
    				Toast.LENGTH_LONG).show();
    		Utils.setConnectivityReceiverEnabled(mContext, true);
    	}

        return id;
    }
    
    /**
     * Cancel uploads and remove them from the upload manager.  Each upload will be stopped if
     * it was running, and it will no longer be accessible through the upload manager.  If a file
     * was already uploaded to external storage, it will not be deleted.
     *
     * @param ids the IDs of the uploads to remove
     * @return the number of uploads actually removed
     */
    public int remove(long... ids) {
        if (ids == null || ids.length == 0) {
            // called with nothing to remove!
            throw new IllegalArgumentException("input param 'ids' can't be null");
        }
       
        NotificationManager mgr = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        for (long id : ids) {
        	mgr.cancel(NOTIFICATION_TAG, (int) id);
        	mDataAccess.open();
        	mDataAccess.delete(id);
        	mDataAccess.close();
		}  

        return 0;
    }
    
    /**
     * Query the upload manager about uploads that have been requested.
     * @param query parameters specifying filters for this query
     * @return a Cursor over the result set of uploads, with columns consisting of all the
     * COLUMN_* constants.
     */
    public Cursor query(Query query) {
    	mDataAccess.open();
        Cursor cursor = mDataAccess.query(query.mIds);
        mDataAccess.close();
        return cursor;
    }

    /**
     * Restart the given uploads, which must have already completed (successfully or not).  This
     * method will only work when called from within the upload manager's process.
     * @param ids the IDs of the uploads
     * @hide
     */
    public void restartUpload(long... ids) {
    	//TODO
    }

	protected void restartUpload() {
		Log.d(TAG, "restartUpload");

    	if (Utils.isOnline(mContext)) {
    		mDataAccess.open();
    		List<Request> requests = mDataAccess.queryAll();

			//TODO execute all in once    		
			for (Request request : requests) {
				if (request.mStatus == STATUS_PENDING) {
					request.mStatus = STATUS_RUNNING;
					mDataAccess.update(request);
					
					new UploadTask(mContext, 
							mDataAccess, 
							mUploadFileService).execute(request);
				}
			}

    		mDataAccess.close();
		}

		Utils.setConnectivityReceiverEnabled(mContext, false);
	}

    static private class UploadTask extends AsyncTask<Request, Void, Integer> {
    	private static final String TAG = "UploadTask";
    	
    	private Context mContext = null;
    	private Request mRequest = null;
    	private NotificationManager mNotifyManager = null;
    	private Builder mBuilder = null;
    	private UploadRequestDataAccess mDataAccess = null;
    	private IUploadFileService mUploadFileService = null;
    	
    	public UploadTask(Context context, 
    			UploadRequestDataAccess dataAccess, 
    			IUploadFileService uploadFileService) {
			mContext = context;
			mDataAccess = dataAccess;
			mUploadFileService = uploadFileService;
		}    	

		@Override
		protected Integer doInBackground(Request... params) {
			mRequest = params[0];
			
			Log.d(TAG, "doInBackground " + (int) mRequest.mId);
			
			if (mRequest.mShowNotification) {
				// setup notification for progress indicator
				mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
	
				mBuilder = new NotificationCompat.Builder(mContext);
				mBuilder.setContentTitle(mRequest.mTitle == null ? mRequest.mFile.getName() : mRequest.mTitle)
				    .setContentText(mContext.getString(R.string.document_uploading_in_progress))
				    .setSmallIcon(R.drawable.ic_action_av_upload);
				mBuilder.setAutoCancel(false);				
		        mBuilder.setProgress(0, 0, true);
		        
				final Intent notificationIntent = new Intent(ACTION_NOTIFICATION_CLICKED);
				notificationIntent.putExtra(EXTRA_UPLOAD_ID, mRequest.mId);
				final PendingIntent intent = PendingIntent.getBroadcast(mContext, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);			
				mBuilder.setContentIntent(intent);
		        
		        mNotifyManager.notify(NOTIFICATION_TAG, (int) mRequest.mId, mBuilder.build());
			}
			
			
			return mUploadFileService.uploadFile(mRequest);
		}		

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			
		    Intent intent = new Intent(ACTION_UPLOAD_COMPLETE);		    
			intent.putExtra(EXTRA_UPLOAD_ID, mRequest.mId);
		    
			if (result == 200) {
				mRequest.mStatus = STATUS_SUCCESSFUL;
				
				if (mRequest.mShowNotification) {
					Log.d(TAG, "Cancel notification " + (int) mRequest.mId);
					mBuilder.setProgress(0,0,false);
					mBuilder.setAutoCancel(true);
					mBuilder.setContentText(mContext.getString(R.string.document_uploaded_succesfully));
		            mNotifyManager.cancel(NOTIFICATION_TAG, (int) mRequest.mId);
				}
			}
			else {
				mRequest.mStatus = STATUS_FAILED;
				mRequest.mReason = result;
				
				if (mRequest.mShowNotification) {
		            // removes the progress bar
					mBuilder.setProgress(0,0,false);
					mBuilder.setContentText(mContext.getString(R.string.document_uploading_error));
					mBuilder.setAutoCancel(true);					
				}				
			}
		    
			mDataAccess.open();
			mDataAccess.update(mRequest);
			mDataAccess.close();

			if (mRequest.mShowNotification) {
	            mNotifyManager.notify(NOTIFICATION_TAG, (int) mRequest.mId, mBuilder.build());
			}

		    try {
		        Uri tone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		        Ringtone r = RingtoneManager.getRingtone(mContext, tone);
		        if (r != null) {
		        	r.play();
		        }
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }

		    mContext.sendBroadcast(intent);
		}		
    }
}
