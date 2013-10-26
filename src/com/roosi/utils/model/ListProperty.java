package com.roosi.utils.model;

import java.util.List;

public class ListProperty<T> extends Property<List<T>> {

	public ListProperty() {
	}
	
	public T get(int index) {
		if (mValue == null) {
			refresh();
			return null;
		}
		else {
			if (index >= mValue.size()) {
				refresh(index);
				return null;
			}
		}
		return mValue.get(index);
	}

	public int count() {
		if (mValue == null) {
			return 1;
		}
		return mValue.size() + 1;
	}
	
	/**
	 * Called when the value of the property should be refreshed from the original source.
	 * 
	 * Default implementation doesn't do anything.
	 * 
	 * @param index the index of element to refresh.
	 */
	protected void refresh(int index) {};
}
