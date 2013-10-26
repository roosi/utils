package com.roosi.utils.weather.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class City implements Parcelable {
	private int id;
	private String name;
	private Coord coord;
	private String country;
	private int population;

	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String COORD = "coord";
	private static final String COUNTRY = "country";
	private static final String POPULATION = "population";

	public static City valueOf(JSONObject json) throws JSONException {
		City object = new City();
		if (json.has(ID)) {
			object.setId(json.getInt(ID));
		}
		if (json.has(NAME)) {
			object.setName(json.getString(NAME));
		}
		if (json.has(COORD)) {
			object.setCoord(Coord.valueOf(json.getJSONObject(COORD)));
		}
		if (json.has(COUNTRY)) {
			object.setCountry(json.getString(COUNTRY));
		}
		if (json.has(POPULATION)) {
			object.setPopulation(json.getInt(POPULATION));
		}
		return object;
	}

	public City() {}

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
	public void setCoord(Coord coord) {
		this.coord = coord;
	}
	public Coord getCoord() {
		return this.coord;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountry() {
		return this.country;
	}
	public void setPopulation(int population) {
		this.population = population;
	}
	public int getPopulation() {
		return this.population;
	}
	public JSONObject toJSONObject() throws JSONException {
		JSONObject object = new JSONObject();
		object.put(ID, this.id);
		object.put(NAME, this.name);
		object.put(COORD, this.coord.toJSONObject());
		object.put(COUNTRY, this.country);
		object.put(POPULATION, this.population);
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
		dest.writeString(this.name);
		dest.writeParcelable(this.coord, flags);
		dest.writeString(this.country);
		dest.writeInt(this.population);
	}
	public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
		public City createFromParcel(Parcel in) {
			return new City(in);
		}
		
		public City[] newArray(int size) {
			return new City[size];
		}
	};
	private City(Parcel in) {
		this.id = in.readInt();
		this.name = in.readString();
		this.coord = in.readParcelable(Coord.class.getClassLoader());
		this.country = in.readString();
		this.population = in.readInt();
	}
}
