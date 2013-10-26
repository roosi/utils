package com.roosi.utils.weather;

import android.os.AsyncTask;
import android.util.Log;

import com.roosi.utils.model.Property;
import com.roosi.utils.net.Network;
import com.roosi.utils.net.Request;
import com.roosi.utils.weather.model.Weather;

public class CurrentWeather extends Property<Weather> {

	protected static final String TAG = "CurrentWeather";
	private Network mNet = null;
	private String mQuery = null;
	
	public CurrentWeather() {
		 mNet = Network.getInstance();
		 mReadOnly = true;
	}

	public void setQuery(String q) {
		if (mQuery != q) {
			mQuery = q;
			mValue = null;
		}
	}
	
	@Override
	protected void refresh() {
		if (mQuery == null) {
			throw new RuntimeException("Query is not set!");
		}
		if (isRefreshing() == false) {
			setRefreshing(true);

			new AsyncTask<String, Void, SearchWeatherResponse>() {
	
				@Override
				protected SearchWeatherResponse doInBackground(String... params) {
					Request request = new Request();
					request.setUrl(params[0]);
					SearchWeatherResponse response = new SearchWeatherResponse();
					
					mNet.performRequest(request, response);
					return response;
				}
				
				protected void onPostExecute(SearchWeatherResponse result) {
					//Log.d(TAG, result.getContent());
					
					setRefreshing(false); 
					Weather weather = null;
					if (result.getCode() == 200) {
						mError = null;
						weather = result.getData();
					}
					else {
						mError = new Error(result.getCode(), result.getContent());
					}
					propertyChanged(weather);
				};
			}.execute("http://api.openweathermap.org/data/2.5/weather?units=metric&q=" + mQuery);
		}
	}
	
	@Override
	protected void update() {
		// readonly
	}
}
