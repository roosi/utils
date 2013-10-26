package com.roosi.utils.weather.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.Parcel;
import android.os.Parcelable;


public class Forecast implements Parcelable {
	private String cod;
	private int message;
	private City city;
	private int cnt;
	private List<ForecastItem> list = new ArrayList<ForecastItem>();

	private static final String COD = "cod";
	private static final String MESSAGE = "message";
	private static final String CITY = "city";
	private static final String CNT = "cnt";
	private static final String LIST = "list";

	public static Forecast valueOf(JSONObject json) throws JSONException {
		Forecast object = new Forecast();
		if (json.has(COD)) {
			object.setCod(json.getString(COD));
		}
		if (json.has(MESSAGE)) {
			object.setMessage(json.getInt(MESSAGE));
		}
		if (json.has(CITY)) {
			object.setCity(City.valueOf(json.getJSONObject(CITY)));
		}
		if (json.has(CNT)) {
			object.setCnt(json.getInt(CNT));
		}
		if (json.has(LIST)) {
			JSONArray array = json.getJSONArray(LIST);
			List<ForecastItem> list = new ArrayList<ForecastItem>();
			for (int i = 0; i < array.length(); i++) {
				list.add(ForecastItem.valueOf(array.getJSONObject(i)));
			}
			object.setList(list);
		}
		return object;
	}

	public Forecast() {}

	public void setCod(String cod) {
		this.cod = cod;
	}
	public String getCod() {
		return this.cod;
	}
	public void setMessage(int message) {
		this.message = message;
	}
	public int getMessage() {
		return this.message;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public City getCity() {
		return this.city;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public int getCnt() {
		return this.cnt;
	}
	public void setList(List<ForecastItem> list) {
		this.list = list;
	}
	public List<ForecastItem> getList() {
		return this.list;
	}
	public JSONObject toJSONObject() throws JSONException {
		JSONObject object = new JSONObject();
		object.put(COD, this.cod);
		object.put(MESSAGE, this.message);
		object.put(CITY, this.city.toJSONObject());
		object.put(CNT, this.cnt);
		object.put(LIST, new JSONArray(this.list));
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
		dest.writeString(this.cod);
		dest.writeInt(this.message);
		dest.writeParcelable(this.city, flags);
		dest.writeInt(this.cnt);
		dest.writeList(this.list);
	}
	public static final Parcelable.Creator<Forecast> CREATOR = new Parcelable.Creator<Forecast>() {
		public Forecast createFromParcel(Parcel in) {
			return new Forecast(in);
		}
		
		public Forecast[] newArray(int size) {
			return new Forecast[size];
		}
	};
	private Forecast(Parcel in) {
		this.cod = in.readString();
		this.message = in.readInt();
		this.city = in.readParcelable(City.class.getClassLoader());
		this.cnt = in.readInt();
		in.readList(this.list, ForecastItem.class.getClassLoader());
	}
}

