package com.roosi.utils.weather;

import android.os.AsyncTask;
import android.util.Log;

import com.roosi.utils.model.ListProperty;
import com.roosi.utils.net.Network;
import com.roosi.utils.net.Request;
import com.roosi.utils.weather.model.Forecast;
import com.roosi.utils.weather.model.ForecastItem;

public class CurrentForecast extends ListProperty<ForecastItem> {

	protected static final String TAG = "CurrentForecast";
		
	private static final int MAX_DAYS = 14;
	private static final int PATCH_SIZE = 7;
	
	private Network mNet = null;
	private int mId = -1;
	
	public CurrentForecast() {
		 mNet = Network.getInstance();
		 mReadOnly = true;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		mId = id;
	}
	
	@Override
	public int count() {
		int count = super.count();
		if (count >= MAX_DAYS) {
			count = MAX_DAYS;
		}
		return count;
	}

	@Override
	protected void refresh() {
		getItems(0);
	}

	@Override
	protected void refresh(int location) {
		// TODO Auto-generated method stub
		Log.d(TAG, "refresh " + location);
		getItems(location);
	}

	@Override
	protected void update() {
		// readonly
	}
	
	private void getItems(int location) {
		if (mId == -1) {
			//throw new RuntimeException("Id is not set!");
			return;
		}

		// Doesn't support paging, get previous once and more
		int count = PATCH_SIZE + location;

		if (isRefreshing() == false) {
			setRefreshing(true);

			new AsyncTask<String, Void, SearchForecastResponse>() {
	
				@Override
				protected SearchForecastResponse doInBackground(String... params) {
					Request request = new Request();
					request.setUrl(params[0]);
					SearchForecastResponse response = new SearchForecastResponse();
					
					mNet.performRequest(request, response);
					return response;
				}
				
				protected void onPostExecute(SearchForecastResponse result) {
					//Log.d(TAG, result.getContent());
					
					setRefreshing(false); 
					Forecast forecast = null;
					if (result.getCode() == 200) {
						mError = null;
						forecast = result.getData();
					}
					else {
						mError = new Error(result.getCode(), result.getContent());
					}
					propertyChanged(forecast.getList());
				};
			}.execute("http://api.openweathermap.org/data/2.5/forecast/daily?cnt=" + count + 
					"&mode=json&units=metric&id=" + mId);
		}		
	}
}
