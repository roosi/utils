package com.roosi.utils.weather.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.Parcel;
import android.os.Parcelable;

public class ForecastItem implements Parcelable {
	private int dt;
	private Temp temp;
	private int pressure;
	private int humidity;
	private List<WeatherItem> weather = new ArrayList<WeatherItem>();
	private int speed;
	private int deg;
	private int clouds;
	private int rain;

	private static final String DT = "dt";
	private static final String TEMP = "temp";
	private static final String PRESSURE = "pressure";
	private static final String HUMIDITY = "humidity";
	private static final String WEATHER = "weather";
	private static final String SPEED = "speed";
	private static final String DEG = "deg";
	private static final String CLOUDS = "clouds";
	private static final String RAIN = "rain";

	public static ForecastItem valueOf(JSONObject json) throws JSONException {
		ForecastItem object = new ForecastItem();
		if (json.has(DT)) {
			object.setDt(json.getInt(DT));
		}
		if (json.has(TEMP)) {
			object.setTemp(Temp.valueOf(json.getJSONObject(TEMP)));
		}
		if (json.has(PRESSURE)) {
			object.setPressure(json.getInt(PRESSURE));
		}
		if (json.has(HUMIDITY)) {
			object.setHumidity(json.getInt(HUMIDITY));
		}
		if (json.has(WEATHER)) {
			JSONArray array = json.getJSONArray(WEATHER);
			List<WeatherItem> list = new ArrayList<WeatherItem>();
			for (int i = 0; i < array.length(); i++) {
				list.add(WeatherItem.valueOf(array.getJSONObject(i)));
			}
			object.setWeather(list);
		}
		if (json.has(SPEED)) {
			object.setSpeed(json.getInt(SPEED));
		}
		if (json.has(DEG)) {
			object.setDeg(json.getInt(DEG));
		}
		if (json.has(CLOUDS)) {
			object.setClouds(json.getInt(CLOUDS));
		}
		if (json.has(RAIN)) {
			object.setRain(json.getInt(RAIN));
		}
		return object;
	}

	public ForecastItem() {}

	public void setDt(int dt) {
		this.dt = dt;
	}
	public int getDt() {
		return this.dt;
	}
	public void setTemp(Temp temp) {
		this.temp = temp;
	}
	public Temp getTemp() {
		return this.temp;
	}
	public void setPressure(int pressure) {
		this.pressure = pressure;
	}
	public int getPressure() {
		return this.pressure;
	}
	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}
	public int getHumidity() {
		return this.humidity;
	}
	public void setWeather(List<WeatherItem> weather) {
		this.weather = weather;
	}
	public List<WeatherItem> getWeather() {
		return this.weather;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getSpeed() {
		return this.speed;
	}
	public void setDeg(int deg) {
		this.deg = deg;
	}
	public int getDeg() {
		return this.deg;
	}
	public void setClouds(int clouds) {
		this.clouds = clouds;
	}
	public int getClouds() {
		return this.clouds;
	}
	public void setRain(int rain) {
		this.rain = rain;
	}
	public int getRain() {
		return this.rain;
	}
	public JSONObject toJSONObject() throws JSONException {
		JSONObject object = new JSONObject();
		object.put(DT, this.dt);
		object.put(TEMP, this.temp.toJSONObject());
		object.put(PRESSURE, this.pressure);
		object.put(HUMIDITY, this.humidity);
		object.put(WEATHER, new JSONArray(this.weather));
		object.put(SPEED, this.speed);
		object.put(DEG, this.deg);
		object.put(CLOUDS, this.clouds);
		object.put(RAIN, this.rain);
		return object;
	}
	@Override
	public String toString() {
		return getClass().getName() + "["+"]";
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.dt);
		dest.writeParcelable(this.temp, flags);
		dest.writeInt(this.pressure);
		dest.writeInt(this.humidity);
		dest.writeList(this.weather);
		dest.writeInt(this.speed);
		dest.writeInt(this.deg);
		dest.writeInt(this.clouds);
		dest.writeInt(this.rain);
	}
	public static final Parcelable.Creator<ForecastItem> CREATOR = new Parcelable.Creator<ForecastItem>() {
		public ForecastItem createFromParcel(Parcel in) {
			return new ForecastItem(in);
		}
		
		public ForecastItem[] newArray(int size) {
			return new ForecastItem[size];
		}
	};
	private ForecastItem(Parcel in) {
		this.dt = in.readInt();
		this.temp = in.readParcelable(Temp.class.getClassLoader());
		this.pressure = in.readInt();
		this.humidity = in.readInt();
		in.readList(this.weather, WeatherItem.class.getClassLoader());
		this.speed = in.readInt();
		this.deg = in.readInt();
		this.clouds = in.readInt();
		this.rain = in.readInt();
	}
}
