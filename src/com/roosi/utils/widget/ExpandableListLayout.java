package com.roosi.utils.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A view that shows items in a vertical list. The items come from the BaseAdapter 
 * associated with this view.
 * Note! When adding items to adapter, all views are removed and redrawn.  
 */

public class ExpandableListLayout extends LinearLayout {
	private static final String TAG = "ExpandableListLayout";

    private BaseAdapter mAdapter = null;
    private LinearLayout mList = null;
	private RelativeLayout mHeader = null;
	private Context mContext = null;
	private ImageView mIcon = null;
	private TextView mTitle = null;
	private int mHideResId = 0;
	private int mShowResId = 0;

	public ExpandableListLayout(Context context) {
		super(context);
		init(context);
	}

	public ExpandableListLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public ExpandableListLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		
		mContext = context;
		mHeader = new RelativeLayout(mContext);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		
		mTitle = new TextView(mContext);
		mTitle.setText("header");
		mTitle.setTextAppearance(mContext, android.R.style.TextAppearance_Medium);
		//params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		mHeader.setVisibility(View.GONE);
		mHeader.addView(mTitle);
		
		mIcon = new ImageView(mContext);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		params.setMargins(pxlsToDps(8), pxlsToDps(8), pxlsToDps(8), pxlsToDps(8));
		mHeader.addView(mIcon, params);

		mHeader.setOnClickListener(mOnClickHeaderListener);
		addView(mHeader);

        mList = new LinearLayout(mContext);
        mList.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mList.setOrientation(VERTICAL);
        addView(mList);
	}

	private int pxlsToDps(int pxls) {		
		final float scale = getResources().getDisplayMetrics().density;
		int dps = (int) (pxls / scale + 0.5f);
		return dps;
	}

	private int dpsToPxls(int dps) {		
		final float scale = getResources().getDisplayMetrics().density;
		int pixels = (int) (dps * scale + 0.5f);
		return pixels;
	}

	/**
	 * Sets the string value of the title TextView.
	 * @param resid
	 */
	public final void setTitleText (int resid) {
		mTitle.setText(resid);
		mHeader.setVisibility(View.VISIBLE);
	}

	/**
	 * Sets the indicator icons to appear on the right of the title text. 
	 */
	public void setIndicatorIcons (int hideResId, int showResId) {
		mHideResId = hideResId;
		mShowResId = showResId;
	}

	/**
	 * Sets the adapter that provides the data and the views to represent 
	 * the data in this widget.
	 */
    public void setAdapter(BaseAdapter adapter) {
        if (adapter == null) {
        	mList.removeAllViews();
        }
        else {
        	registerAdapter(adapter);
        }
    }

    /**
     * Returns the adapter currently associated with this widget.
     */
    public BaseAdapter getAdapter() {
    	return mAdapter;
    }

    /**
     * Returns the view at the specified position in the list.
     */
    public View getView (int position) {
    	return mList.getChildAt(position);
    }

    public void show() {
    	mList.setVisibility(View.VISIBLE);
    	changeIndicatorIcon();
    }

    public void hide() {
    	mList.setVisibility(View.GONE);
    	changeIndicatorIcon();
    }
    
    public boolean isHidden() {
    	if (mList.getVisibility() == View.GONE) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
	private void toggleVisibility() {
		if (isHidden()) {
			show();
		}
		else {
			hide();
		}
	}
	
    private void changeIndicatorIcon() {
    	if (mAdapter.getCount() > 0) {    	
			if (isHidden()) {
				mIcon.setBackgroundResource(mShowResId);
			}
			else {
				mIcon.setBackgroundResource(mHideResId);
			}
    	}
    	else  {
    		// hide icon
    		mIcon.setBackgroundResource(0);
    	}
	}

    private void registerAdapter(BaseAdapter adapter) {

    	if (mAdapter != null) {
    		mAdapter.unregisterDataSetObserver(mDataSetObserver);
    	}
        mAdapter = adapter;
        mAdapter.registerDataSetObserver(mDataSetObserver);
    }

    private class PageDataSetObserver extends DataSetObserver {
    	@Override
    	public void onChanged() {
            bindDataToLayout();
    	}
    }
    private PageDataSetObserver mDataSetObserver = new PageDataSetObserver();
    
    private void bindDataToLayout() {
    	mList.removeAllViews();
        int viewCount = mAdapter.getCount();

        for (int i = 0; i < viewCount; i++) {
            View view = mAdapter.getView(i, null, this);            
            mList.addView(view);
        }
        
        changeIndicatorIcon();
    }    
    
    private class OnClickHeaderListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			toggleVisibility();
		} 
    }
    private OnClickHeaderListener mOnClickHeaderListener = new OnClickHeaderListener();

}
