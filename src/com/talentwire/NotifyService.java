package com.talentwire;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;

public class NotifyService extends Service {

	    private NotificationManager mNM;
	    private final Handler uiHandler=new Handler();
	    private boolean isUpdateRequired=false;
		//private final IBinder mBinder = new MyBinder();


	    @Override
	    public void onCreate() {
	    	 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	         StrictMode.setThreadPolicy(policy);

	    mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        final SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); 
		final String fbtoken =  prefs.getString("access_token", null); 
		//AsyncStaffInfo async = new AsyncStaffInfo(this); 
   	    //async.execute(fbtoken);
		if (prefs.getString("staffkey", null)==null){
   	    StaffTasks.getInfo(fbtoken, getApplicationContext());
		}
		//checkMessages();
   	    AsyncMessage mess = new AsyncMessage();
   	    mess.execute();
	    Log.d("TAG","In onCreate of Notify Service");

	    }
	    
	    public void checkMessages(){
		   	 try{
		         new Thread(){
		             public void run() {
		                 initializeApp();
		                 uiHandler.post( new Runnable(){
		                     @Override
		                     public void run() {
		                         if(isUpdateRequired){
		                         }else{
		                        	Log.d("TAG","Getting the messages from the server task");
		                     	    parseInbox(StaffTasks.getMessages(getApplicationContext()));
		                         }
		                     }
		                 } );
		             }
		             public void initializeApp(){
		           	  while (StaffTasks.donelogin==null) {
		           		  try {
								sleep(1);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
		           	  }
		             }
		     }.start();
		     }catch (Exception e) {}
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
		          showNotification(subject,sender_name);
		          //setTexts(body, subject, sender_name,id, i, jresult.length());
				} 
		  		} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		   }

	    

	    @Override
	    public int onStartCommand(Intent intent, int flags, int startId) {
			checkMessages();
	    	Log.d("TAG","IN onStartCommand SECTION");
	        return START_STICKY;
	    }

	    @Override
	    public void onDestroy() {
	        // Cancel the persistent notification.
	        mNM.cancelAll();
	    }


	    @Override
	    public IBinder onBind(Intent arg0) {
	        Log.d("TAG","IN ONBIND SECTION");

	    	return mBinder;
	    }

	     private final IBinder mBinder = new LocalBinder();


	     private void showNotification(String message, String sender) {
	 			NotificationManager notificationManager = (NotificationManager) 
	 						getSystemService(NOTIFICATION_SERVICE);
	 			Notification notification = new Notification(R.drawable.icon,
	 					"You recieved a notification in Talentwire", System.currentTimeMillis());
	 			// Hide the notification after its selected
	 			notification.flags |= Notification.FLAG_AUTO_CANCEL;

	 			Intent intent = new Intent(this, StaffActivity.class);
	 			PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
	 			notification.setLatestEventInfo(this, message,
	 					"from "+sender, activity);
	 			notification.number += 1;
	 			notificationManager.notify(0, notification);
	 			Log.d("TAG","Just created a notification: "+message+""+sender);
	 		}	     

	     public class LocalBinder extends Binder {
	            NotifyService getService() {
	    	        Log.d("TAG","IN LOCALBINDER SECTION");
	                return NotifyService.this;

	            }
	        }
	     
	     private class AsyncMessage extends AsyncTask<Void, Void, Void>
		    {
			 //   private ProgressDialog dialog;

			

				@Override
		        protected void onPreExecute() {
			        //dialog = ProgressDialog.show(ShareImage.this, "Please wait", "Fetching your subscriptions", true);

		        }
				
			    @Override
		        protected Void doInBackground(Void... params) {
			    	if (StaffTasks.donelogin!=null){
             	    parseInbox(StaffTasks.getMessages(getApplicationContext()));
			    	}
             	    return null;
		        }
			    
		        @Override
		        protected void onPostExecute(Void result) {
		        	//dialog.dismiss();
			        //runArrayAdapter();
		        }
		       
		    }
}