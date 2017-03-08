package com.example.isweixin.tc2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.isweixin.tc3.Utils;

public class TelephonyService extends Service {
	private TelephonyManager telephonyManager;

	public IBinder onBind(Intent arg0) {

		return null;
	}

	public void onCreate() {
		// 取得TELEPHONY服务
		telephonyManager = (TelephonyManager) super
				.getSystemService(Context.TELEPHONY_SERVICE);

		telephonyManager.listen(new MyPhoneStateListener(),
				PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}

	private class MyPhoneStateListener extends PhoneStateListener {

		public void onCallStateChanged(int state, String incomingNumber) {
			if (state == TelephonyManager.CALL_STATE_IDLE)// 空闲
			{
				Log.i("huahua", "CALL_STATE_IDLE");
				Utils.getCallLogs();
			} else if (state == TelephonyManager.CALL_STATE_RINGING)// 来电
				
			{
				Log.i("huahua", "CALL_STATE_RINGING");
			} else if (state == TelephonyManager.CALL_STATE_OFFHOOK)// 去电,通话中
			{
				Log.i("huahua", "CALL_STATE_OFFHOOK");
			}
		}

	}

	public void onDestroy() {

		super.onDestroy();
	}

	public int onStartCommand(Intent intent, int flags, int startId) {

		return super.onStartCommand(intent, flags, startId);
	}

}
