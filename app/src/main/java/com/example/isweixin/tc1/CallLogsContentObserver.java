package com.example.isweixin.tc1;


import com.example.isweixin.tc3.Utils;

import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

public class CallLogsContentObserver extends ContentObserver{

	public CallLogsContentObserver(Handler handler) {
		super(handler);
	}
	
	public void onChange(boolean selfChange) {
		Log.i("huahua", "通话记录数据库发生了变化");
		Utils.getCallLogs();
	}

}
