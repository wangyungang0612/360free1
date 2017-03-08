package com.example.tctc1002;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.isweixin.MainActivity;
import com.example.isweixin.R;
import com.example.isweixin.tc1.ChatActivity;

public class bc1 extends BroadcastReceiver {
	private static final String NOTIFICATION_SERVICE = null;

	public void onReceive(Context arg0, Intent arg1) {

		NotificationManager notificationManager = (NotificationManager) arg0
				.getSystemService("notification");
		
			Notification notification = new Notification();
			notification.icon = R.drawable.ic_launcher;
			notification.tickerText = "您有一个未接电话";
			notification.defaults = Notification.DEFAULT_SOUND;
			Intent intent = new Intent(arg0, ChatActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(arg0, 0,
					intent, 0);
			notification.setLatestEventInfo(arg0, "未接电话", "您有一个未接电话",
					pendingIntent);
			notificationManager.notify(0, notification);
			Log.i("rec", "hkdakdjadha");
	}

}
