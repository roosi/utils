package com.roosi.utils;

import java.text.DateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.roosi.utils.model.Property.Error;
import com.roosi.utils.weather.CurrentForecast;
import com.roosi.utils.weather.Service;
import com.roosi.utils.weather.model.ForecastItem;
import com.roosi.utils.weather.model.Weather;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements Observer {

	protected static final String TAG = "MainActivity";
	private TextView mPlaceText = null;
	private TextView mDescriptionText = null;
	private TextView mTemperatureText = null;
	private AbsListView mForecastList = null;
	private Service mService = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mPlaceText = (TextView) findViewById(R.id.placeText);
		mDescriptionText = (TextView) findViewById(R.id.descriptionText);
		mTemperatureText = (TextView) findViewById(R.id.temperatureText);
		mForecastList = (AbsListView) findViewById(R.id.forecastGrid);
		
		mService = Service.getInstance();
		ForecastAdapter adapter = new ForecastAdapter(this, 
				mService.currentForecast, android.R.layout.two_line_list_item);
		mForecastList.setAdapter(adapter);

		mService.currentWeather.addObserver(this);
		mService.currentForecast.addObserver(this);
		
		mService.currentWeather.setQuery("Jyvaskyla,fi");
		updateWeather(mService.currentWeather.get());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh:
			mService.currentWeather.clear();
			mService.currentForecast.clear();
			updateWeather(mService.currentWeather.get());
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		Log.d(TAG, "update " + observable);
		if (observable == mService.currentWeather) {
			if (data != null) {
				Weather weather = (Weather) data;
				updateWeather(weather);
				
				mService.currentForecast.setId(weather.getId());
			}
			else {
				showError(mService.currentWeather.getError());
			}
		}
	}	

	private void updateWeather(Weather weather) {
		if (weather != null) {
			mPlaceText.setText(weather.getName());
			mDescriptionText.setText(weather.getWeather().get(0).getDescription());
			mTemperatureText.setText(String.valueOf(weather.getMain().getTemp()));
		}
	}
	
	private void showError(Error error) {
		// TODO Auto-generated method stub	
	}
	
	public class ForecastAdapter extends ArrayAdapter<ForecastItem> implements Observer {

		private CurrentForecast mCurrentForecast = null;
		private int mResource = -1;

		public ForecastAdapter(Context context, CurrentForecast forecast, int resource) {
			super(context, resource);
			mResource = resource;
			mCurrentForecast  = forecast;
			mCurrentForecast.addObserver(this);
		}
		
		@Override
		public int getCount() {
			if (mCurrentForecast == null) {
				return 0;
			}
			return mCurrentForecast.count();
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}
		
		@Override
		public int getItemViewType(int position) {
			
			int type = 0;
			ForecastItem item = mCurrentForecast.get(position);
			if (item != null) {
				// loading type, waiting for new item
				type = 1;
			}
			return type;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {			
			View view = null;			
			
			ForecastItem item = mCurrentForecast.get(position);
			if (item != null) {
				if (convertView == null) {				
					view = getLayoutInflater().inflate(mResource, null);
				}
				else {
					view = convertView;
				}
							
				TextView text1 = (TextView) view.findViewById(android.R.id.text1);
				TextView text2 = (TextView) view.findViewById(android.R.id.text2);
				

				long timeInMS = item.getDt() * (long)1000;
				Date date = new Date(timeInMS);
				String forecast = item.getWeather().get(0).getDescription() + " / " + 
						String.valueOf(item.getTemp().getDay());
				
				final DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
				text1.setText(df.format(date));
				text2.setText(forecast);
			}
			else {
				// loading type, waiting for new item
				if (convertView == null) {
					view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
				}
				else {
					view = convertView;
				}
					
				TextView text1 = (TextView) view.findViewById(android.R.id.text1);
				text1.setText("loading...");
			}
			
			return view;
		}

		@Override
		public void update(Observable observable, Object data) {
			notifyDataSetChanged();
		}
	}
}
