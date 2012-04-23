package com.talentwire;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class NotifyService extends Service {

	    private NotificationManager mNM;


	    @Override
	    public void onCreate() {
	    mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	    Log.d("TAG","Notify service has been started");
	    parseInbox(StaffTasks.getMessages(getApplicationContext()));
	    }
	    //if (triggerSatisfied) showNotification();
	    //}
	    
	    
		   public  void parseInbox(String inbox) {
				  JSONArray jresult;
				  JSONObject json_data = null;
				  JSONTokener tokener = new JSONTokener(inbox);
		          try {
					jresult = new JSONArray(tokener);

		          Log.d("TAG", "IN PARSE MESSAGE RESPONSE");

				  for (int i=0; i<jresult.length(); i++) { //Runs through the messages for as long as the array is

		    	  json_data = jresult.getJSONObject(i); //For each json object

		          String body = json_data.getString("body");
		          String subject = json_data.getString("subject");
		          String sender_name = json_data.getString("sender_name");
		          String created_at = json_data.getString("created_at");
		          String id = json_data.getString("id");
		          
		          //setTexts(body, subject, sender_name,id, i, jresult.length());
				} 
		  		} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		   }

	    

	    @Override
	    public int onStartCommand(Intent intent, int flags, int startId) {
	        return START_STICKY;
	    }

	    @Override
	    public void onDestroy() {
	        // Cancel the persistent notification.
	        mNM.cancelAll();
	    }


	    @Override
	    public IBinder onBind(Intent arg0) {
	        return mBinder;
	    }

	     private final IBinder mBinder = new LocalBinder();


	     private void showNotification() {
	 			NotificationManager notificationManager = (NotificationManager) 
	 						getSystemService(NOTIFICATION_SERVICE);
	 			Notification notification = new Notification(R.drawable.icon,
	 					"You recieved a on Talentwire", System.currentTimeMillis());
	 			// Hide the notification after its selected
	 			notification.flags |= Notification.FLAG_AUTO_CANCEL;

	 			Intent intent = new Intent(this, StaffActivity.class);
	 			PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
	 			notification.setLatestEventInfo(this, "You recieved a on Talentwire",
	 					"Sam says, space dog", activity);
	 			notification.number += 1;
	 			notificationManager.notify(0, notification);

	 		}	     

	     public class LocalBinder extends Binder {
	            NotifyService getService() {
	                return NotifyService.this;
	            }
	        }
}