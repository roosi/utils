package com.roosi.utils.weather.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Clouds implements Parcelable {
	private int all;

	private static final String ALL = "all";

	public static Clouds valueOf(JSONObject json) throws JSONException {
		Clouds object = new Clouds();
		if (json.has(ALL)) {
			object.setAll(json.getInt(ALL));
		}
		return object;
	}

	public Clouds() {}

	public void setAll(int all) {
		this.all = all;
	}
	public int getAll() {
		return this.all;
	}
	public JSONObject toJSONObject() throws JSONException {
		JSONObject object = new JSONObject();
		object.put(ALL, this.all);
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
		dest.writeInt(this.all);
	}
	public static final Parcelable.Creator<Clouds> CREATOR = new Parcelable.Creator<Clouds>() {
		public Clouds createFromParcel(Parcel in) {
			return new Clouds(in);
		}
		
		public Clouds[] newArray(int size) {
			return new Clouds[size];
		}
	};
	private Clouds(Parcel in) {
		this.all = in.readInt();
	}
}
