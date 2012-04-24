package com.talentwire;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceAutoStarter extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, NotifyService.class);
		context.startService(service);
	}
}
		