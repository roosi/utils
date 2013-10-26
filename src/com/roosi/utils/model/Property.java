package com.roosi.utils.model;

import java.util.Observable;

public class Property<T> extends Observable {
	
	private boolean mUpdating = false;
	private boolean mRefreshing = false;
	protected T mValue = null;
	protected boolean mReadOnly = false;
	protected Error mError = null;
	
	public static class Error {
		int code;
		String description;

		public Error(int code, String description) {
			this.code = code;
			this.description = description;
		}
	}
	
	public Property() {
	}
	
	protected boolean isRefreshing() {
		return mRefreshing;
	}

	protected void setRefreshing(boolean refreshing) {
		mRefreshing = refreshing;
	}
	
	protected boolean isUpdating() {
		return mUpdating;
	}

	protected void setUpdating(boolean updating) {
		mUpdating = updating;
	}
	
	public void set(T value) {
		if (mReadOnly) {
			throw new RuntimeException("Readonly property!");
		}
		mValue = value;	
		update();
	}

	public T get() {
		if (mValue == null) {
			refresh();
		}
		return mValue;
	}
	
	public void clear() {
		propertyChanged(null);
	}
	
	public Error getError() {
		return mError;
	}

	protected void setError(Error error) {
		mError = error;
	}
		
	protected void propertyChanged(T value) {
		mValue = value;
		setChanged();
		notifyObservers(mValue);
	}

	/**
	 * Called when the value of the property should be refreshed from the original source.
	 * 
	 * Default implementation doesn't do anything.
	 */
	protected void refresh() {};

	/**
	 * Called when the value of the property should be updated to the original source.
	 * 
	 * Default implementation notifies observers that value has changed.
	 */
	protected void update() {
		propertyChanged(mValue);
	};	
}
