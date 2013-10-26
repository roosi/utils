package com.roosi.utils.weather.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Snow implements Parcelable {
	private int h3;

	private static final String H3 = "h3";

	public static Snow valueOf(JSONObject json) throws JSONException {
		Snow object = new Snow();
		if (json.has(H3)) {
			object.seth3(json.getInt(H3));
		}
		return object;
	}

	public Snow() {}

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
	public static final Parcelable.Creator<Snow> CREATOR = new Parcelable.Creator<Snow>() {
		public Snow createFromParcel(Parcel in) {
			return new Snow(in);
		}
		
		public Snow[] newArray(int size) {
			return new Snow[size];
		}
	};
	private Snow(Parcel in) {
		this.h3 = in.readInt();
	}
}
