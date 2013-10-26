package com.roosi.utils.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

import com.roosi.utils.net.Network.IPerformRequestService;

public class PerformRequestServiceImp implements IPerformRequestService {

	private static final String TAG = "PerformRequestServiceImp";
	
	public PerformRequestServiceImp() {
	}

	@Override
	public void performRequest(Request request, Response response) {
		Log.d(TAG, "performRequest: " + request.getUrl());

	    try {
	        URL u = new URL(request.getUrl());
	        HttpURLConnection c = (HttpURLConnection) u.openConnection();
	        
	        String data = request.getData();
	        if (data != null) {
	        	c.setDoOutput(true);
	        	c.setFixedLengthStreamingMode(data.getBytes().length);

	        	OutputStreamWriter wr = new OutputStreamWriter(c.getOutputStream());
	        	wr.write(data);
	        	wr.flush();
	        	wr.close();
	        }	       

	        int status = c.getResponseCode();
	        Log.d(TAG, "performRequest " + status);
	        response.setCode(status);
	        switch (status) {
	            case 200:
	                BufferedReader br = new BufferedReader(
	                		new InputStreamReader(c.getInputStream()));
	                StringBuilder sb = new StringBuilder();
	                String line;
	                while ((line = br.readLine()) != null) {
	                    sb.append(line+"\n");
	                }
	                br.close();
	                response.setContent(sb.toString());
	        }
	        c.disconnect();
	    } 
	    catch (MalformedURLException e) {
	    	e.printStackTrace();
	    } 
	    catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
}
