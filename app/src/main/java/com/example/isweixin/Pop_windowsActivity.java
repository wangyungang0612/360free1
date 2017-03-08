package com.example.isweixin;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Pop_windowsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pop_windows);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pop_windows, menu);
		return true;
	}

}
