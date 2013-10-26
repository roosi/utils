package com.roosi.utils.model;

import java.util.List;

public abstract class ListProperty<T> extends Property<List<T>> {

	public ListProperty() {
	}
	
	public T get(int location) {
		if (mValue == null) {
			refresh();
			return null;
		}
		else {
			if (location >= mValue.size()) {
				refresh(location);
				return null;
			}
		}
		return mValue.get(location);
	}

	public int count() {
		if (mValue == null) {
			return 1;
		}
		return mValue.size() + 1;
	}
	
	protected abstract void refresh(int location);
}
