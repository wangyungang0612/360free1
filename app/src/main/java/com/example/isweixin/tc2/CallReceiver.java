package com.example.isweixin.tc2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver{
	public void onReceive(Context context, Intent intent) {
//		Log.i("sms", "CallReceiver Start...");
//		TelephonyManager telephony = (TelephonyManager) context
//				.getSystemService(Context.TELEPHONY_SERVICE);
//		
//		CallListener customPhoneListener = new CallListener(context);
// 
//		telephony.listen(customPhoneListener,
//				PhoneStateListener.LISTEN_CALL_STATE);
// 
//		Bundle bundle = intent.getExtras();
//		String phoneNr = bundle.getString("incoming_number");
//		
//		Log.i("sms", "CallReceiver Phone Number : " + phoneNr);
	}
}
