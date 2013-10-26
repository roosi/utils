package com.roosi.utils.weather;

public class Service {

	static private Service mInstance = null;
	
	public static Service getInstance() {
		if (mInstance == null) {
			mInstance = new Service();
		}
		return mInstance;
	}

	public CurrentWeather currentWeather;
	
	public CurrentForecast currentForecast;
	
	private Service() {
		currentWeather = new CurrentWeather();
		currentForecast = new CurrentForecast();
	}

}
