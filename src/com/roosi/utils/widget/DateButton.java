package com.roosi.utils.widget;

import java.text.DateFormat;
import java.util.Date;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class DateButton extends Button {

	private static final String TAG = "DateButton";
	
	private final DateFormat mDateFormat = DateFormat.getDateInstance();
	private Date mDate = null;
	private DatePickerDialog mDialog = null; 

	public DateButton(Context context) {
		super(context);
		init();
	}
	
	public DateButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DateButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void setDate(Date date) {
		mDate = date;
		setText(mDateFormat.format(mDate));
	}
	
	public Date getDate() {
		return mDate;
	}

	public void setToNow() {
		mDate.setTime(System.currentTimeMillis());
		setText(mDateFormat.format(mDate));
	}

	/**
	 * Returns the string representation of date for the default Locale.
	 */
	public String toLocaleString() {
		return mDateFormat.format(mDate);
	}
	
	private void init() {
		mDate = new Date();
		setText(mDateFormat.format(mDate));
		
    	mDialog = new DatePickerDialog(getContext(), 
    			mDateSetListener, 
    			mDate.getYear() + 1900, 
    			mDate.getMonth(), 
    			mDate.getDate());

		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openDatePicker();
			}
		});
	}

    private void openDatePicker() {
    	mDialog.updateDate(mDate.getYear() + 1900, mDate.getMonth(), mDate.getDate());
    	mDialog.show();
    }
    
    private DatePickerDialog.OnDateSetListener mDateSetListener = 
    	new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, 
                              int monthOfYear, int dayOfMonth) {

        	mDate.setDate(dayOfMonth);
        	mDate.setMonth(monthOfYear);
        	mDate.setYear(year - 1900);
        	
        	setText(mDateFormat.format(mDate));
        }
    };
}
