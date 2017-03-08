package com.example.isweixin;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class Contacts_listActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts_list);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("person");
		
	}


}
