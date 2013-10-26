package com.roosi.utils.net;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonObjectResponse extends Response {

	private JSONObject mJsonObject = null;

	public JsonObjectResponse() {
	}

	@Override
	public void setContent(String content) {
		super.setContent(content);
		try {
			mJsonObject = new JSONObject(content);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public JSONObject getJson() {
		return mJsonObject;
	}
}
