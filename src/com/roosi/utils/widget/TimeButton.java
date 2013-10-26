package com.roosi.utils.widget;

import java.text.DateFormat;
import java.util.Date;

import android.app.TimePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class TimeButton extends Button {

	private static final String TAG = "TimeButton";

	private final DateFormat mTimeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
	private Date mDate = null;
	private TimePickerDialog mDialog = null; 

	public TimeButton(Context context) {
		super(context);
		init();
	}
	
	public TimeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TimeButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void setDate(Date date) {
		mDate = date;
		setText(mTimeFormat.format(mDate));
	}
	
	public Date getDate() {
		return mDate;
	}

	public void setToNow() {
		mDate.setTime(System.currentTimeMillis());
		setText(mTimeFormat.format(mDate));
	}

	/**
	 * Returns the string representation of time for the default Locale
	 * and type SHORT.
	 */
	public String toLocaleString() {
		return mTimeFormat.format(mDate);
	}

	private void init() {
		mDate = new Date();
		setText(mTimeFormat.format(mDate));
		
    	mDialog = new TimePickerDialog(getContext(), 
    			mTimeSetListener, 
    			mDate.getHours(), 
    			mDate.getMinutes(), 
    			true);

		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openDatePicker();
			}
		});
	}

    private void openDatePicker() {
    	mDialog.updateTime(mDate.getHours(), mDate.getMinutes());
    	mDialog.show();
    }
    
    private TimePickerDialog.OnTimeSetListener mTimeSetListener = 
    	new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        	mDate.setHours(hourOfDay);
        	mDate.setMinutes(minute);
        	
        	setText(mTimeFormat.format(mDate));
		}
    };
}
