package com.roosi.utils.weather.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.os.Parcel;
import android.os.Parcelable;

public class Weather implements Parcelable {
	private Coord coord;
	private Sys sys;
	private List<WeatherItem> weather = new ArrayList<WeatherItem>();
	private String base;
	private Main main;
	private Wind wind;
	private Rain rain;
	private Snow snow;
	private Clouds clouds;
	private int dt;
	private int id;
	private String name;
	private int cod;

	private static final String COORD = "coord";
	private static final String SYS = "sys";
	private static final String WEATHER = "weather";
	private static final String BASE = "base";
	private static final String MAIN = "main";
	private static final String WIND = "wind";
	private static final String RAIN = "rain";
	private static final String SNOW = "snow";
	private static final String CLOUDS = "clouds";
	private static final String DT = "dt";
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String COD = "cod";

	public static Weather valueOf(JSONObject json) throws JSONException {
		Weather object = new Weather();
		if (json.has(COORD)) {
			object.setCoord(Coord.valueOf(json.getJSONObject(COORD)));
		}
		if (json.has(SYS)) {
			object.setSys(Sys.valueOf(json.getJSONObject(SYS)));
		}
		if (json.has(WEATHER)) {
			JSONArray array = json.getJSONArray(WEATHER);
			List<WeatherItem> list = new ArrayList<WeatherItem>();
			for (int i = 0; i < array.length(); i++) {
				list.add(WeatherItem.valueOf(array.getJSONObject(i)));
			}
			object.setWeather(list);
		}
		if (json.has(BASE)) {
			object.setBase(json.getString(BASE));
		}
		if (json.has(MAIN)) {
			object.setMain(Main.valueOf(json.getJSONObject(MAIN)));
		}
		if (json.has(WIND)) {
			object.setWind(Wind.valueOf(json.getJSONObject(WIND)));
		}
		if (json.has(RAIN)) {
			object.setRain(Rain.valueOf(json.getJSONObject(RAIN)));
		}
		if (json.has(SNOW)) {
			object.setSnow(Snow.valueOf(json.getJSONObject(SNOW)));
		}
		if (json.has(CLOUDS)) {
			object.setClouds(Clouds.valueOf(json.getJSONObject(CLOUDS)));
		}
		if (json.has(DT)) {
			object.setDt(json.getInt(DT));
		}
		if (json.has(ID)) {
			object.setId(json.getInt(ID));
		}
		if (json.has(NAME)) {
			object.setName(json.getString(NAME));
		}
		if (json.has(COD)) {
			object.setCod(json.getInt(COD));
		}
		return object;
	}

	public Weather() {}

	public void setCoord(Coord coord) {
		this.coord = coord;
	}
	public Coord getCoord() {
		return this.coord;
	}
	public void setSys(Sys sys) {
		this.sys = sys;
	}
	public Sys getSys() {
		return this.sys;
	}
	public void setWeather(List<WeatherItem> weather) {
		this.weather = weather;
	}
	public List<WeatherItem> getWeather() {
		return this.weather;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getBase() {
		return this.base;
	}
	public void setMain(Main main) {
		this.main = main;
	}
	public Main getMain() {
		return this.main;
	}
	public void setWind(Wind wind) {
		this.wind = wind;
	}
	public Wind getWind() {
		return this.wind;
	}
	public void setRain(Rain rain) {
		this.rain = rain;
	}
	public Rain getRain() {
		return this.rain;
	}
	public void setSnow(Snow snow) {
		this.snow = snow;
	}
	public Snow getSnow() {
		return this.snow;
	}
	public void setClouds(Clouds clouds) {
		this.clouds = clouds;
	}
	public Clouds getClouds() {
		return this.clouds;
	}
	public void setDt(int dt) {
		this.dt = dt;
	}
	public int getDt() {
		return this.dt;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return this.id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
	public void setCod(int cod) {
		this.cod = cod;
	}
	public int getCod() {
		return this.cod;
	}
	public JSONObject toJSONObject() throws JSONException {
		JSONObject object = new JSONObject();
		object.put(COORD, this.coord.toJSONObject());
		object.put(SYS, this.sys.toJSONObject());
		object.put(WEATHER, new JSONArray(this.weather));
		object.put(BASE, this.base);
		object.put(MAIN, this.main.toJSONObject());
		object.put(WIND, this.wind.toJSONObject());
		object.put(RAIN, this.rain.toJSONObject());
		object.put(SNOW, this.snow.toJSONObject());
		object.put(CLOUDS, this.clouds.toJSONObject());
		object.put(DT, this.dt);
		object.put(ID, this.id);
		object.put(NAME, this.name);
		object.put(COD, this.cod);
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
		dest.writeParcelable(this.coord, flags);
		dest.writeParcelable(this.sys, flags);
		dest.writeList(this.weather);
		dest.writeString(this.base);
		dest.writeParcelable(this.main, flags);
		dest.writeParcelable(this.wind, flags);
		dest.writeParcelable(this.rain, flags);
		dest.writeParcelable(this.snow, flags);
		dest.writeParcelable(this.clouds, flags);
		dest.writeInt(this.dt);
		dest.writeInt(this.id);
		dest.writeString(this.name);
		dest.writeInt(this.cod);
	}
	public static final Parcelable.Creator<Weather> CREATOR = new Parcelable.Creator<Weather>() {
		public Weather createFromParcel(Parcel in) {
			return new Weather(in);
		}
		
		public Weather[] newArray(int size) {
			return new Weather[size];
		}
	};
	private Weather(Parcel in) {
		this.coord = in.readParcelable(Coord.class.getClassLoader());
		this.sys = in.readParcelable(Sys.class.getClassLoader());
		in.readList(this.weather, WeatherItem.class.getClassLoader());
		this.base = in.readString();
		this.main = in.readParcelable(Main.class.getClassLoader());
		this.wind = in.readParcelable(Wind.class.getClassLoader());
		this.rain = in.readParcelable(Rain.class.getClassLoader());
		this.snow = in.readParcelable(Snow.class.getClassLoader());
		this.clouds = in.readParcelable(Clouds.class.getClassLoader());
		this.dt = in.readInt();
		this.id = in.readInt();
		this.name = in.readString();
		this.cod = in.readInt();
	}
}