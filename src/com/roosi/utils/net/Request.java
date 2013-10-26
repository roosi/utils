package com.roosi.utils.net;

//TODO support for post method
public class Request {

	//TODO to URI
	protected String mUrl;
	protected String mData;

	public Request() {		
	}

	public Request(String url) {
		mUrl = url;
	}

	public void setUrl(String url) {
		mUrl = url;
	}
	
	public String getUrl() {
		return mUrl;
	}
	
	public void setData(String data) {
		mData = data;
	}
	
	public String getData() {
		return mData;
	}
}
