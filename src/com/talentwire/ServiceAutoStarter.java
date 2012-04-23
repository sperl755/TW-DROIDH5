package com.talentwire;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServiceAutoStarter extends BroadcastReceiver {
	  @Override
	  public void onReceive(Context context, Intent intent) {
		Log.d("TAG","Broadcast reciever started");
	    context.startService(new Intent(context, NotifyService.class));
	  }
	}