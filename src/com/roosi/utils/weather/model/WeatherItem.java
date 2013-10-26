package com.roosi.utils.weather.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class WeatherItem implements Parcelable {
	private int id;
	private String main;
	private String description;
	private String icon;

	private static final String ID = "id";
	private static final String MAIN = "main";
	private static final String DESCRIPTION = "description";
	private static final String ICON = "icon";

	public static WeatherItem valueOf(JSONObject json) throws JSONException {
		WeatherItem object = new WeatherItem();
		if (json.has(ID)) {
			object.setId(json.getInt(ID));
		}
		if (json.has(MAIN)) {
			object.setMain(json.getString(MAIN));
		}
		if (json.has(DESCRIPTION)) {
			object.setDescription(json.getString(DESCRIPTION));
		}
		if (json.has(ICON)) {
			object.setIcon(json.getString(ICON));
		}
		return object;
	}

	public WeatherItem() {}

	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return this.id;
	}
	public void setMain(String main) {
		this.main = main;
	}
	public String getMain() {
		return this.main;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return this.description;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getIcon() {
		return this.icon;
	}
	public JSONObject toJSONObject() throws JSONException {
		JSONObject object = new JSONObject();
		object.put(ID, this.id);
		object.put(MAIN, this.main);
		object.put(DESCRIPTION, this.description);
		object.put(ICON, this.icon);
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
		dest.writeInt(this.id);
		dest.writeString(this.main);
		dest.writeString(this.description);
		dest.writeString(this.icon);
	}
	public static final Parcelable.Creator<WeatherItem> CREATOR = new Parcelable.Creator<WeatherItem>() {
		public WeatherItem createFromParcel(Parcel in) {
			return new WeatherItem(in);
		}
		
		public WeatherItem[] newArray(int size) {
			return new WeatherItem[size];
		}
	};
	private WeatherItem(Parcel in) {
		this.id = in.readInt();
		this.main = in.readString();
		this.description = in.readString();
		this.icon = in.readString();
	}
}

