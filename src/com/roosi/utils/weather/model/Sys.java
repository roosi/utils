package com.roosi.utils.weather.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Sys implements Parcelable {
	private String country;
	private int sunrise;
	private int sunset;

	private static final String COUNTRY = "country";
	private static final String SUNRISE = "sunrise";
	private static final String SUNSET = "sunset";

	public static Sys valueOf(JSONObject json) throws JSONException {
		Sys object = new Sys();
		if (json.has(COUNTRY)) {
			object.setCountry(json.getString(COUNTRY));
		}
		if (json.has(SUNRISE)) {
			object.setSunrise(json.getInt(SUNRISE));
		}
		if (json.has(SUNSET)) {
			object.setSunset(json.getInt(SUNSET));
		}
		return object;
	}

	public Sys() {}

	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountry() {
		return this.country;
	}
	public void setSunrise(int sunrise) {
		this.sunrise = sunrise;
	}
	public int getSunrise() {
		return this.sunrise;
	}
	public void setSunset(int sunset) {
		this.sunset = sunset;
	}
	public int getSunset() {
		return this.sunset;
	}
	public JSONObject toJSONObject() throws JSONException {
		JSONObject object = new JSONObject();
		object.put(COUNTRY, this.country);
		object.put(SUNRISE, this.sunrise);
		object.put(SUNSET, this.sunset);
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
		dest.writeString(this.country);
		dest.writeInt(this.sunrise);
		dest.writeInt(this.sunset);
	}
	public static final Parcelable.Creator<Sys> CREATOR = new Parcelable.Creator<Sys>() {
		public Sys createFromParcel(Parcel in) {
			return new Sys(in);
		}
		
		public Sys[] newArray(int size) {
			return new Sys[size];
		}
	};
	private Sys(Parcel in) {
		this.country = in.readString();
		this.sunrise = in.readInt();
		this.sunset = in.readInt();
	}
}
