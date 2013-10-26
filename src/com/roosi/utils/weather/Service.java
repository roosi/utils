package com.roosi.utils.weather;

import com.roosi.utils.model.Property;

public class Service {

	static private Service mInstance = null;
	
	public static Service getInstance() {
		if (mInstance == null) {
			mInstance = new Service();
		}
		return mInstance;
	}

	public CurrentWeather currentWeather = new CurrentWeather();
	
	public CurrentForecast currentForecast = new CurrentForecast();
	
	public Property<Boolean> refresh = new Property<Boolean>();
	
	private Service() {
	}

}
