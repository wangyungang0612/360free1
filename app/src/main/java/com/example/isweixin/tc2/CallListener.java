package com.example.isweixin.tc2;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallListener extends PhoneStateListener {
	private static final String TAG = "sms";
	private static int lastetState = TelephonyManager.CALL_STATE_IDLE; // 最后的状态
	private Context context;
	TelephonyManager tManager;

	public CallListener(Context context) {
		super();
		this.context = context;
	}

	public void onCallStateChanged(int state, String incomingNumber) {
		Log.v(TAG, "CallListener call state changed : " + incomingNumber);
		String m = null;
		AudioManager am = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);

		if (incomingNumber.equals("15695555683")) {
//			am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			// try {
			// // 获取android.os.ServiceManager类的对象的getService()方法
			// Method method = Class.forName("android.os.ServiceManager")
			// .getMethod("getService", String.class);
			// // 获取远程TELEPHONY_SERVICE的IBinder对象的代理
			// IBinder binder = (IBinder) method.invoke(null,
			// new Object[] { });
			// // 将IBinder对象的代理转换为ITelephony对象
			// ITelephony telephony = ITelephony.Stub.asInterface(binder);
			// // 挂断电话
			// telephony.endCall();
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}
		// 如果当前状态为空闲,上次状态为响铃中的话,则认为是未接来电
		if (lastetState == TelephonyManager.CALL_STATE_RINGING
				&& state == TelephonyManager.CALL_STATE_IDLE) {
			sendSmgWhenMissedCall(incomingNumber);
		}
		// 最后改变当前值
		lastetState = state;
	}

	private void sendSmgWhenMissedCall(String incomingNumber) {
		// 未接来电处理(发短信,发email等)
		Log.i("DIAL", "DIAL");
		Intent intent2 = new Intent();
		intent2.setAction("huahua.action.notification");
		context.sendBroadcast(intent2);
	}

}