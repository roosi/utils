package com.roosi.utils.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class NodeScrollView extends HorizontalScrollView {

	private static final String TAG = "NodeScrollView";

	private static final int NODE_HEIGHT = 48;
	
	private TableRow mRow = null;
	private View mSelected = null;
	private int mNodeBgResID = 0;

	public NodeScrollView(Context context) {
		super(context);
		init();
	}
	
	public NodeScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public NodeScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {						
		TableLayout.LayoutParams params = new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT);
		
		mRow = new TableRow(getContext());		
		addView(mRow, params);
	}

	public void setNodeBackgroundResource(int resid) {
		mNodeBgResID  = resid;
	}
	
	public void addNode(String name, Object tag) {
		TextView node = new TextView(getContext());
		node.setGravity(Gravity.CENTER_VERTICAL);
		node.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
		node.setPadding(dpsToPxls(8), dpsToPxls(8), dpsToPxls(8), dpsToPxls(8));
		node.setHeight(dpsToPxls(NODE_HEIGHT));		
		node.setBackgroundResource(mNodeBgResID);
		node.setTextColor(getResources().getColor(android.R.color.white));
		node.setText(name);
		node.setTag(tag);
		node.setId(mRow.getChildCount());		

		mRow.addView(node);

		node.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handleClick(v);
			}
		});
		
		scrollToEnd();
	}

	public int getNodeCount() {
		return mRow.getChildCount();
	}

	public String getNode(int index) {
		TextView node = (TextView) mRow.getChildAt(index);
		return node.getText().toString();
	}

	public void scrollToEnd() {
		postDelayed(new Runnable() {
		    public void run() {
		        fullScroll(HorizontalScrollView.FOCUS_RIGHT);
		    }
		}, 100L);
		
		final TextView node = (TextView)mRow.getChildAt(mRow.getChildCount() - 1);
		if (mSelected != null) {
			mSelected.setEnabled(true);	
		}
        mSelected = node;
        mSelected.setEnabled(false);
	}

	public void clear() {
		mRow.removeAllViews();
	}
	
	public interface OnNodeChangedListener {
		void onNodeChanged(String name, Object tag);
	}
	
	protected OnNodeChangedListener mListener = null;
	
	public void setOnNodeChangedListener(OnNodeChangedListener listener) {
		mListener = listener;
	}
	
	protected void handleClick(View view) {
		TextView node = (TextView)view;

        node.setEnabled(false);
        
        if (mSelected != null) {
        	mSelected.setEnabled(true);
        }
        mSelected = node;
        
        // remove following nodes
        int i = node.getId() + 1;
        for (; i < mRow.getChildCount(); i++) {
            mRow.removeViewAt(i);			
		}
				
		if (mListener != null) {
			mListener.onNodeChanged((String)node.getText(), node.getTag());
		}		
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
}
