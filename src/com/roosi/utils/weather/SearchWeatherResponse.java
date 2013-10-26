package com.roosi.utils.weather;

import org.json.JSONException;
import org.json.JSONObject;

import com.roosi.utils.net.Response;
import com.roosi.utils.weather.model.Weather;

public class SearchWeatherResponse extends Response {
	
	private Weather mData = null;

	public SearchWeatherResponse() {
	}

	@Override
	public void setContent(String content) {
		super.setContent(content);
		try {
			mData = Weather.valueOf(new JSONObject(content));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public Weather getData() {
		return mData;
	}
}
