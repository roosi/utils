package com.roosi.utils.net;

public class Response {

	private int mCode = -1;
	private String mContent = null;

	public Response() {
	}

	public void setCode(int code) {
		mCode = code;
	}
	
	public int getCode() {
		return mCode;
	}
	
	public void setContent(String content) {
		mContent = content;
	}
	
	public String getContent() {
		return mContent;
	}

}
