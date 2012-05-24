package com.talentwire;

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
        mPrefs = getSharedPreferences("facebook", MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
        	Intent i = new Intent(this, StaffActivity.class);
        	Log.d("TAG","We still have an access token, starting StaffActivity, token is "+ access_token);
        	startActivity(i);
        }
        if(expires != 0) {
            facebook.setAccessExpires(expires);
            Log.d("TAG","Setting facebook access expires");
        }
        
        /*
         * Only call authorize if the access_token has expired.
         */
        if(!facebook.isSessionValid()) {
            facebook.authorize(FacebookActivity.this, permissions, new DialogListener() {
                @Override
                public void onComplete(Bundle values) {
                	onCompleteFunctions();
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
    
    public void onCompleteFunctions(){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("access_token", facebook.getAccessToken());
        editor.putLong("access_expires", facebook.getAccessExpires());
        editor.commit();
        Log.d("TAG","in onComplete, starting StaffActivity");
        Intent i = new Intent(this, StaffActivity.class);
    	startActivity(i);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG","in onActivityResult");
        facebook.authorizeCallback(requestCode, resultCode, data);
    }
}


