package com.roosi.utils.net;

public class Network {

	private static final String TAG = "Network";

    public interface IPerformRequestService {
    	void performRequest(Request request, Response response);
    }

	private static Network mInstance = null;
	
	public static Network getInstance() {
	
		if (mInstance == null) {
			mInstance = new Network();
		}
		return mInstance;
	}

	private IPerformRequestService mPerformRequestService = null;
	
	protected Network() {
	}
	
	public void setPerformRequestService(IPerformRequestService service) {
		mPerformRequestService = service;
	}

	public void performRequest(Request request, Response response) {
		mPerformRequestService.performRequest(request, response);
	}
}


