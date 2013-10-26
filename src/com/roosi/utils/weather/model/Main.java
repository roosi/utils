package com.roosi.utils.weather.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Main implements Parcelable {
	private int temp;
	private int pressure;
	private int humidity;
	private int temp_min;
	private int temp_max;

	private static final String TEMP = "temp";
	private static final String PRESSURE = "pressure";
	private static final String HUMIDITY = "humidity";
	private static final String TEMP__MIN = "temp_min";
	private static final String TEMP__MAX = "temp_max";

	public static Main valueOf(JSONObject json) throws JSONException {
		Main object = new Main();
		if (json.has(TEMP)) {
			object.setTemp(json.getInt(TEMP));
		}
		if (json.has(PRESSURE)) {
			object.setPressure(json.getInt(PRESSURE));
		}
		if (json.has(HUMIDITY)) {
			object.setHumidity(json.getInt(HUMIDITY));
		}
		if (json.has(TEMP__MIN)) {
			object.setTemp_min(json.getInt(TEMP__MIN));
		}
		if (json.has(TEMP__MAX)) {
			object.setTemp_max(json.getInt(TEMP__MAX));
		}
		return object;
	}

	public Main() {}

	public void setTemp(int temp) {
		this.temp = temp;
	}
	public int getTemp() {
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
	public void setTemp_min(int temp_min) {
		this.temp_min = temp_min;
	}
	public int getTemp_min() {
		return this.temp_min;
	}
	public void setTemp_max(int temp_max) {
		this.temp_max = temp_max;
	}
	public int getTemp_max() {
		return this.temp_max;
	}
	public JSONObject toJSONObject() throws JSONException {
		JSONObject object = new JSONObject();
		object.put(TEMP, this.temp);
		object.put(PRESSURE, this.pressure);
		object.put(HUMIDITY, this.humidity);
		object.put(TEMP__MIN, this.temp_min);
		object.put(TEMP__MAX, this.temp_max);
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
		dest.writeInt(this.temp);
		dest.writeInt(this.pressure);
		dest.writeInt(this.humidity);
		dest.writeInt(this.temp_min);
		dest.writeInt(this.temp_max);
	}
	public static final Parcelable.Creator<Main> CREATOR = new Parcelable.Creator<Main>() {
		public Main createFromParcel(Parcel in) {
			return new Main(in);
		}
		
		public Main[] newArray(int size) {
			return new Main[size];
		}
	};
	private Main(Parcel in) {
		this.temp = in.readInt();
		this.pressure = in.readInt();
		this.humidity = in.readInt();
		this.temp_min = in.readInt();
		this.temp_max = in.readInt();
	}
}