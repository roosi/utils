package com.roosi.utils.weather.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Temp implements Parcelable {
	private int day;
	private int min;
	private int max;
	private int night;
	private int eve;
	private int morn;

	private static final String DAY = "day";
	private static final String MIN = "min";
	private static final String MAX = "max";
	private static final String NIGHT = "night";
	private static final String EVE = "eve";
	private static final String MORN = "morn";

	public static Temp valueOf(JSONObject json) throws JSONException {
		Temp object = new Temp();
		if (json.has(DAY)) {
			object.setDay(json.getInt(DAY));
		}
		if (json.has(MIN)) {
			object.setMin(json.getInt(MIN));
		}
		if (json.has(MAX)) {
			object.setMax(json.getInt(MAX));
		}
		if (json.has(NIGHT)) {
			object.setNight(json.getInt(NIGHT));
		}
		if (json.has(EVE)) {
			object.setEve(json.getInt(EVE));
		}
		if (json.has(MORN)) {
			object.setMorn(json.getInt(MORN));
		}
		return object;
	}

	public Temp() {}

	public void setDay(int day) {
		this.day = day;
	}
	public int getDay() {
		return this.day;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public int getMin() {
		return this.min;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public int getMax() {
		return this.max;
	}
	public void setNight(int night) {
		this.night = night;
	}
	public int getNight() {
		return this.night;
	}
	public void setEve(int eve) {
		this.eve = eve;
	}
	public int getEve() {
		return this.eve;
	}
	public void setMorn(int morn) {
		this.morn = morn;
	}
	public int getMorn() {
		return this.morn;
	}
	public JSONObject toJSONObject() throws JSONException {
		JSONObject object = new JSONObject();
		object.put(DAY, this.day);
		object.put(MIN, this.min);
		object.put(MAX, this.max);
		object.put(NIGHT, this.night);
		object.put(EVE, this.eve);
		object.put(MORN, this.morn);
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
		dest.writeInt(this.day);
		dest.writeInt(this.min);
		dest.writeInt(this.max);
		dest.writeInt(this.night);
		dest.writeInt(this.eve);
		dest.writeInt(this.morn);
	}
	public static final Parcelable.Creator<Temp> CREATOR = new Parcelable.Creator<Temp>() {
		public Temp createFromParcel(Parcel in) {
			return new Temp(in);
		}
		
		public Temp[] newArray(int size) {
			return new Temp[size];
		}
	};
	private Temp(Parcel in) {
		this.day = in.readInt();
		this.min = in.readInt();
		this.max = in.readInt();
		this.night = in.readInt();
		this.eve = in.readInt();
		this.morn = in.readInt();
	}
}