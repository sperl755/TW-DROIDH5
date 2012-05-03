package com.talentwire;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.util.Log;
//import com.facebook.android.*;
//import com.facebook.android.Facebook.*;

//public class FacebookActivity extends Activity {
//    Facebook facebook = new Facebook("187212574660004");
//    public String token;
//    public String[] permissions = new String[] { "email", "publish_stream", "user_work_history", "user_education_history", "friends_work_history", "friends_education_history", "friends_location"};
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//        
//        Log.d("TAG","oncreate Facebook Class");
//
//        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(FacebookActivity.this); 
//        String access_token = prefs.getString("access_token", null); 
//        Long expires = prefs.getLong("access_expires", -1);
//        
//
//        if (access_token != null && expires != -1)
//        {
//        	Log.d("TAG", "Facebook Authentification Complete");
//            facebook.setAccessToken(access_token);
//            facebook.setAccessExpires(expires);
//        	Intent i = new Intent(getApplicationContext(), StaffActivity.class);
//        	//i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        	startActivity(i);
//            StaffActivity.facebooked="bannana";
//        }
//
//
//        if (!facebook.isSessionValid())
//        {
//            facebook.authorize(FacebookActivity.this, permissions, new DialogListener() {
//            public void onComplete(Bundle values) {
//            	Log.d("TAG", "Facebook Authentification Complete");
//            	saveToken();
//            	Intent i = new Intent(getApplicationContext(), StaffActivity.class);
//            	//i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            	StaffActivity.facebooked="bannana";
//            	startActivity(i);
//            }
//            
//            public void onFacebookError(FacebookError error) {
//            	error.printStackTrace();
//            }
//
//            
//            public void onError(DialogError e) {
//            	e.printStackTrace();
//            }
//            
//            
//            public void onCancel() {}
//        });
//    }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        facebook.authorizeCallback(requestCode, resultCode, data);
//    }
//    public void saveToken(){
//    	token = facebook.getAccessToken();
//    	long token_expires = facebook.getAccessExpires();
//
//    	SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(FacebookActivity.this);
//
//    	prefs.edit().putLong("access_expires", token_expires).commit();
//
//    	prefs.edit().putString("access_token", token).commit();
//    	Log.d("TAG", "The facebook token is: "+token);
//    }
//    }



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.*;
import com.facebook.android.Facebook.*;

public class FacebookActivity extends Activity {


	Facebook facebook = new Facebook("187212574660004");
	public String[] permissions = new String[] { "email", "publish_stream", "user_work_history", "user_education_history", "friends_work_history", "friends_education_history", "friends_location"};
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /*
         * Get existing access_token if any
         */
        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
        	Intent i = new Intent(getApplicationContext(), StaffActivity.class);
        	Log.d("TAG","We still have an access token, starting StaffActivity, token is "+ access_token);
        	startActivity(i);
        }
        if(expires != 0) {
            facebook.setAccessExpires(expires);
        }
        
        /*
         * Only call authorize if the access_token has expired.
         */
        if(!facebook.isSessionValid()) {
            facebook.authorize(FacebookActivity.this, permissions, new DialogListener() {
                @Override
                public void onComplete(Bundle values) {
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("access_token", facebook.getAccessToken());
                    editor.putLong("access_expires", facebook.getAccessExpires());
                    editor.commit();
                    Log.d("TAG","in onComplete, starting StaffActivity");
                    Intent i = new Intent(getApplicationContext(), StaffActivity.class);
                	startActivity(i);
                }
    
                @Override
                public void onFacebookError(FacebookError error) {}
    
                @Override
                public void onError(DialogError e) {}
    
                @Override
                public void onCancel() {}
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG","in onActivityResult");
        facebook.authorizeCallback(requestCode, resultCode, data);
    }
}


