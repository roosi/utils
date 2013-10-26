package com.roosi.utils.weather.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Wind implements Parcelable {
	private int speed;
	private int deg;

	private static final String SPEED = "speed";
	private static final String DEG = "deg";

	public static Wind valueOf(JSONObject json) throws JSONException {
		Wind object = new Wind();
		if (json.has(SPEED)) {
			object.setSpeed(json.getInt(SPEED));
		}
		if (json.has(DEG)) {
			object.setDeg(json.getInt(DEG));
		}
		return object;
	}

	public Wind() {}

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
	public JSONObject toJSONObject() throws JSONException {
		JSONObject object = new JSONObject();
		object.put(SPEED, this.speed);
		object.put(DEG, this.deg);
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
		dest.writeInt(this.speed);
		dest.writeInt(this.deg);
	}
	public static final Parcelable.Creator<Wind> CREATOR = new Parcelable.Creator<Wind>() {
		public Wind createFromParcel(Parcel in) {
			return new Wind(in);
		}
		
		public Wind[] newArray(int size) {
			return new Wind[size];
		}
	};
	private Wind(Parcel in) {
		this.speed = in.readInt();
		this.deg = in.readInt();
	}
}

