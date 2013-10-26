package com.roosi.utils.weather.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Coord implements Parcelable {
	private int lon;
	private int lat;

	private static final String LON = "lon";
	private static final String LAT = "lat";

	public static Coord valueOf(JSONObject json) throws JSONException {
		Coord object = new Coord();
		if (json.has(LON)) {
			object.setLon(json.getInt(LON));
		}
		if (json.has(LAT)) {
			object.setLat(json.getInt(LAT));
		}
		return object;
	}

	public Coord() {}

	public void setLon(int lon) {
		this.lon = lon;
	}
	public int getLon() {
		return this.lon;
	}
	public void setLat(int lat) {
		this.lat = lat;
	}
	public int getLat() {
		return this.lat;
	}
	public JSONObject toJSONObject() throws JSONException {
		JSONObject object = new JSONObject();
		object.put(LON, this.lon);
		object.put(LAT, this.lat);
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
		dest.writeInt(this.lon);
		dest.writeInt(this.lat);
	}
	public static final Parcelable.Creator<Coord> CREATOR = new Parcelable.Creator<Coord>() {
		public Coord createFromParcel(Parcel in) {
			return new Coord(in);
		}
		
		public Coord[] newArray(int size) {
			return new Coord[size];
		}
	};
	private Coord(Parcel in) {
		this.lon = in.readInt();
		this.lat = in.readInt();
	}
}
