package com.roosi.utils.weather.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Rain implements Parcelable {
	private int h3;

	private static final String H3 = "h3";

	public static Rain valueOf(JSONObject json) throws JSONException {
		Rain object = new Rain();
		if (json.has(H3)) {
			object.seth3(json.getInt(H3));
		}
		return object;
	}

	public Rain() {}

	public void seth3(int h3) {
		this.h3 = h3;
	}
	public int geth3() {
		return this.h3;
	}
	public JSONObject toJSONObject() throws JSONException {
		JSONObject object = new JSONObject();
		object.put(H3, this.h3);
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
		dest.writeInt(this.h3);
	}
	public static final Parcelable.Creator<Rain> CREATOR = new Parcelable.Creator<Rain>() {
		public Rain createFromParcel(Parcel in) {
			return new Rain(in);
		}
		
		public Rain[] newArray(int size) {
			return new Rain[size];
		}
	};
	private Rain(Parcel in) {
		this.h3 = in.readInt();
	}
}
