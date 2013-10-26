package com.roosi.utils.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

import com.roosi.utils.app.UploadManager.IUploadFileService;
import com.roosi.utils.app.UploadManager.Request;

public final class UploadFileServiceImp implements IUploadFileService {
	private static final String TAG = "UploadFileServiceImp";

	private Context mContext = null;
	
	public UploadFileServiceImp(Context context) {
		mContext = context;
	}

	@Override
	public int uploadFile(Request request) {
		int result = 404;
				
		final File file = request.getFile();
		
        byte payload[] = new byte[(int) file.length()];

		try {
	        FileInputStream fis;
			fis = new FileInputStream(file);
	        fis.read(payload);
	        fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        String mimeType = Utils.getMimeType(file.getPath());
        
        Log.d(TAG, "uploadFile " + file.getPath() + " " + mimeType);

		if (payload != null) {
			//TODO
		}

		return result;
	}		
}
