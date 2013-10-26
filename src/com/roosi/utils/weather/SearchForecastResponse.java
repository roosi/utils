package com.roosi.utils.weather;

import org.json.JSONException;
import org.json.JSONObject;

import com.roosi.utils.net.Response;
import com.roosi.utils.weather.model.Forecast;

public class SearchForecastResponse extends Response {
	
	private Forecast mData = null;

	public SearchForecastResponse() {
	}

	@Override
	public void setContent(String content) {
		super.setContent(content);
		try {
			mData = Forecast.valueOf(new JSONObject(content));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public Forecast getData() {
		return mData;
	}
}
