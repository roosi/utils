package com.roosi.utils.widget;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;


public class OnEndOfListListener implements OnScrollListener {

	private static final String TAG = "OnEndOfListListener";
	private int mLastTotalItemCount = 0;
	private boolean mEnabled = true;

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {				

		if (mEnabled) {
			if (firstVisibleItem > 0 && totalItemCount > 0) {
				if (totalItemCount == (firstVisibleItem + visibleItemCount) && 
					mLastTotalItemCount < totalItemCount) {
					mLastTotalItemCount = totalItemCount;
					onEndOfList();
				}
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	/*
	 * Called when the list has reached the end i.e. last visible item. 
	 * It is called only once for certain total item count.
	 */
	public void onEndOfList() {
	}
	
	public void reset() {
		mLastTotalItemCount = 0;
		mEnabled = true;
	}

	public void setEnabled(boolean enabled) {
		mEnabled  = enabled;
	}
}
