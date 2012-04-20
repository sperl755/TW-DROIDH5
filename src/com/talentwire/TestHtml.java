package com.talentwire;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


import com.facebook.android.Facebook;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;

public class TestHtml extends Activity {


	private static final int TAKE_PICTURE = 0;
	private PullToRefreshWebView mPullRefreshWebView;
	private WebView mWebView;
	private ImageButton share;
	private ImageView testcam;
	private Uri imageUri;
    private final Handler uiHandler=new Handler();
    private boolean isUpdateRequired=false;
    private ArrayList<String> topics = new ArrayList<String>();
    private ArrayList<String> topids = new ArrayList<String>();
    private ImageButton profButton;
    private ImageButton talentwire;
    private int count=0;
    private LinearLayout proglin;
    private int counter=0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testhtml);
		
        final SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(TestHtml.this); 
		final String fbtoken =  prefs.getString("access_token", null); 
		Log.d("TAG",fbtoken);
		AsyncStaffInfo async = new AsyncStaffInfo(this); 
   	    async.execute(fbtoken);
	    proglin = (LinearLayout)this.findViewById(R.id.proglin);


	   	 try{
	         new Thread(){
	             public void run() {
	                 initializeApp();
	                 uiHandler.post( new Runnable(){
	                     @Override
	                     public void run() {
	                         if(isUpdateRequired){
	                         }else{
	                        	parseSubs(StaffTasks.subs);
	                         }
	                     }
	                 } );
	             }
	             public void initializeApp(){
	           	  while (StaffTasks.subs==null) {
	           		  try {
							sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
	           	  }
	             }
	     }.start();
	     }catch (Exception e) {}

   	       	
   	    
		mPullRefreshWebView = (PullToRefreshWebView) findViewById(R.id.pull_refresh_webview);

		mWebView = mPullRefreshWebView.getRefreshableView();
		CookieSyncManager.createInstance(this);
		CookieSyncManager.getInstance().startSync();
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.setWebViewClient(new SampleWebViewClient(){
			   @Override
			   public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
					//checkLoading();
					  /*  if (count==0){
					 
					   //urlNewString=  urlNewString.substring(0, 256);
					   urlNewString = urlNewString.concat("&header=no");
					   view.loadUrl(urlNewString);
					   Log.d("TAG",urlNewString);
					   count ++;
				   } else { 
				   urlNewString = urlNewString.concat("?header=no");
				   view.loadUrl(urlNewString);
				   Log.d("TAG",urlNewString);
				   }
				   */
				   return true;
			   }
			
		        @Override
		        public void onReceivedHttpAuthRequest(WebView view,
		                HttpAuthHandler handler, String host, String realm) {
		            super.onReceivedHttpAuthRequest(view, handler, host, realm);
		        }

	
		        @Override
		        	public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
		        	    handler.proceed(); // Ignore SSL certificate errors
		        	}
		        }

		    );
		//mWebView.loadUrl("https://www.google.com");
		//mWebView.loadUrl("https://www.facebook.com/syassami");
		//mWebView.loadUrl("https://www.facebook.com/dialog/oauth?client_id=187212574660004&redirect_uri=https://talentwire.me/facebook_authenticate?mobile=true");
		//mWebView.loadUrl("https://www.facebook.com/dialog/oauth?client_id=187212574660004&redirect_uri=https://talentwire.me/facebook_authenticate?mobile=true&header=no");
		//mWebView.loadUrl("https://talentwire.me/facebook_authenticate?code="+fbtoken+"&mobile=true&header=no");
		//mWebView.loadUrl("https://talentwire.me/user/dashboard?header=no");
		//mWebView.loadUrl("https://apps.facebook.com/talentwire/");
		//mWebView.loadUrl("https://talentwire.me/users/Shayan-Yassami&header=no");
		//mWebView.loadUrl("https://m.facebook.com/login.php?app_id=187212574660004&scope=email&redirect_uri=https://www.talentwire.me/facebook_authenticate?mobile=true&header=no");
		
		profButton = (ImageButton)this.findViewById(R.id.profButton);
		profButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String fname =  prefs.getString("firstname", null); 
            	String lname =  prefs.getString("lastname", null);
            	mWebView.loadUrl("https://talentwire.me/users/"+fname+"-"+lname+"?header=no");
            	checkLoading();
            	}
        });
		
		share = (ImageButton)this.findViewById(R.id.share);
		share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	customDialog();
            }
        });
		talentwire = (ImageButton)this.findViewById(R.id.talentwire);
		talentwire.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	checkLoading();
        		mWebView.loadUrl("https://talentwire.me/user/dashboard?header=no");
        		}
        });
		  proglin.setVisibility(View.GONE);
      	  mWebView.setVisibility(View.VISIBLE);
	}
	
	public void parseSubs(String stuff){
		JSONArray jresult;
		JSONObject json_data = null;
		JSONObject json_data_level1 = null;
		  JSONTokener tokener = new JSONTokener(stuff);
        try {
			jresult = new JSONArray(tokener);

		  for (int i=0; i<jresult.length(); i++) { 
			  json_data = jresult.getJSONObject(i);
			  json_data_level1 =json_data.getJSONObject("subscription");
			  String topic = json_data_level1.getString("topic");
			  String topic_id = json_data_level1.getString("topic_id");
			  topics.add(topic);
			  topids.add(topic_id);
			  //Log.d("TAG", "TOPICS"+topic);
		  }
        } catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void goToProf(String fname, String lname, String fbtoken){
		checkLoading();
		mWebView.loadUrl("https://talentwire.me/users/Shayan-Yassami&header=no");
	}
	
	public void customDialog(){
		 final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.customalert);
        dialog.setTitle("Share");
        final ImageView imageView = (ImageView) dialog.findViewById(R.id.image);
        final EditText postbox = (EditText) dialog.findViewById(R.id.postbox);
        ImageButton camera = (ImageButton) dialog.findViewById(R.id.camera);
        ImageButton share = (ImageButton) dialog.findViewById(R.id.share);
        final Spinner catselect = (Spinner) dialog.findViewById(R.id.catselect);

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item);
        catselect.setAdapter(adapter);
        if (topics.size()!=0){
        for(int i=0; i<topics.size();i++){
        	adapter.add(topics.get(i));
        }
        }

        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	takePhoto(v);
            }
        });
        
        share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                post(postbox.getText().toString(), topids.get(catselect.getSelectedItemPosition()));
                dialog.cancel();
            }
        });
                
        dialog.show();

	}
	

	public void checkLoading(){
		if (counter==0){

      	  counter++;
		} else
			
		
		      mWebView.setVisibility(View.INVISIBLE);
	    	  proglin.setVisibility(View.VISIBLE);
			   try{
			          new Thread(){
			              public void run() {
			                  initializeApp();
			                  uiHandler.post( new Runnable(){
			                      @Override
			                      public void run() {
			                          if(isUpdateRequired){
			                          }else{
			                        	  proglin.setVisibility(View.GONE);
			                        	  mWebView.setVisibility(View.VISIBLE);
			                          }
			                      }
			                  } );
			              }
			              public void initializeApp(){
			            	  while (mWebView.getProgress()<69) {
			            		  try {
									sleep(1);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
			            	  }
			              }
			      }.start();
			      }catch (Exception e) {}
			      counter++;
		  }

	public void takePhoto(View view) {
	    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
	    File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
	    intent.putExtra(MediaStore.EXTRA_OUTPUT,
	            Uri.fromFile(photo));
	    imageUri = Uri.fromFile(photo);
	    startActivityForResult(intent, TAKE_PICTURE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    switch (requestCode) {
	    case TAKE_PICTURE:
	        if (resultCode == Activity.RESULT_OK) {
	            Uri selectedImage = imageUri;
	            getContentResolver().notifyChange(selectedImage, null);
	            

	            

	            //ImageView imageView = (ImageView) findViewById(R.id.testcam);
	            ContentResolver cr = getContentResolver();
	            Bitmap bitmap;
	            try {
	                 bitmap = android.provider.MediaStore.Images.Media
	                 .getBitmap(cr, selectedImage);
	                
	                //imageView.setImageBitmap(bitmap);
	                Toast.makeText(this, selectedImage.toString(),
	                        Toast.LENGTH_LONG).show();
	            } catch (Exception e) {
	                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
	                        .show();
	                Log.e("Camera", e.toString());
	                
	                
	            }

	        }
	    }
	}

	private static class SampleWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	private void post(String topost, String topicid){
		//Photo p = null;
		Log.d("TAG", topost+" was just posted in topic id "+topicid);
		StaffTasks.createFeed("", "", "0", topicid, "", "0", topost, getApplicationContext());
		//String url_title,String url_description,String share_to_friend,String post_to,String url_address,String share_to_career_team,String feed, Context c
		mWebView.reload();
	}
	
	  @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event)  {
	        if (  Integer.valueOf(android.os.Build.VERSION.SDK) < 7 //Instead use android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
	                && keyCode == KeyEvent.KEYCODE_BACK
	                && event.getRepeatCount() == 0) {
	            // Take care of calling this method on earlier versions of
	            // the platform where it doesn't exist.
	            onBackPressed();
	        }

	        return super.onKeyDown(keyCode, event);
	    }

	    @Override
	    public void onBackPressed() {
	    	checkLoading();
	        mWebView.goBack();
	        return;
	    }
	}
