package com.roosi.utils.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.roosi.utils.R;

@SuppressLint("NewApi")
public class UserAgreementActivity extends Activity {
	
	public static final String BUTTONS = "buttons";
	
	private boolean mButtons = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		WebView webView = new WebView(this);
		setContentView(webView);
		webView.loadUrl("file:///android_asset/agreement.html");

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mButtons = extras.getBoolean(BUTTONS, false);
		}

		getActionBar().setDisplayHomeAsUpEnabled(!mButtons);
		
		// default
		setResult(RESULT_FIRST_USER);
	}
	
	@Override
	public void onBackPressed() {
		if (mButtons) {
			setResult(RESULT_CANCELED);
		}
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_user_agreement, menu);
		return mButtons;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.accept) {
			setResult(RESULT_OK);
		}
		else if (item.getItemId() == R.id.cancel) {
			setResult(RESULT_CANCELED);
		}
		
		finish();
		return true;
	}
}
