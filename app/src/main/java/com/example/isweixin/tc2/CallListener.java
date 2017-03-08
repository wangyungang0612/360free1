package com.example.isweixin.tc2;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallListener extends PhoneStateListener {
	private static final String TAG = "sms";
	private static int lastetState = TelephonyManager.CALL_STATE_IDLE; // ����״̬
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
			// // ��ȡandroid.os.ServiceManager��Ķ����getService()����
			// Method method = Class.forName("android.os.ServiceManager")
			// .getMethod("getService", String.class);
			// // ��ȡԶ��TELEPHONY_SERVICE��IBinder����Ĵ���
			// IBinder binder = (IBinder) method.invoke(null,
			// new Object[] { });
			// // ��IBinder����Ĵ���ת��ΪITelephony����
			// ITelephony telephony = ITelephony.Stub.asInterface(binder);
			// // �Ҷϵ绰
			// telephony.endCall();
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}
		// �����ǰ״̬Ϊ����,�ϴ�״̬Ϊ�����еĻ�,����Ϊ��δ������
		if (lastetState == TelephonyManager.CALL_STATE_RINGING
				&& state == TelephonyManager.CALL_STATE_IDLE) {
			sendSmgWhenMissedCall(incomingNumber);
		}
		// ���ı䵱ǰֵ
		lastetState = state;
	}

	private void sendSmgWhenMissedCall(String incomingNumber) {
		// δ�����紦��(������,��email��)
		Log.i("DIAL", "DIAL");
		Intent intent2 = new Intent();
		intent2.setAction("huahua.action.notification");
		context.sendBroadcast(intent2);
	}

}