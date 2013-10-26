package com.roosi.utils.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class ExpandedGridView extends GridView {

	public ExpandedGridView(Context context) {
		super(context);
	}

	public ExpandedGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ExpandedGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Calculate entire height by providing a very large height hint.
        // View.MEASURED_SIZE_MASK represents the largest height possible.
        int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
