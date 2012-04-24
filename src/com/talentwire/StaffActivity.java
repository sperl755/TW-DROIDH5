package com.talentwire;



import java.io.File;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.facebook.android.Facebook;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.talentwire.NotifyService.LocalBinder;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


public class StaffActivity extends Activity  implements LocationListener {
    /** Called when the activity is first created. */

	private ImageButton btnFacebook;
	private PullToRefreshWebView mPullRefreshWebView;
	private WebView mWebView;
    int webCounter =0;
    public static String facebooked="nd";
    Facebook facebook = new Facebook("187212574660004");
    private LinearLayout proglin;
    private int counter=0;
    private final Handler uiHandler=new Handler();
    private boolean isUpdateRequired=false;
	private NotifyService s;
    private ImageButton profButton;
    private ImageButton msgButton;
    private ImageButton talentwire;
	private ImageButton share;
	private LocationManager locationManager;
	private String provider;
	private String latituteField;
	private String longitudeField;
	private Bitmap yourSelectedImage;
    private final static int CAMERA_REQUEST_CODE = 1;
    private static final int SELECT_PHOTO = 100;
	  public static final int    GALLERY_REQUEST_CODE   = 2;
    public static ArrayList<String> topics = new ArrayList<String>(3);
    public static ArrayList<String> topids = new ArrayList<String>(3);
    private File directory;
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        setContentView(R.layout.main);
		    proglin = (LinearLayout)this.findViewById(R.id.proglin);
		    //startActivityForResult(1,1);

		    if (facebooked.equals("nd")){
	        	Intent i = new Intent(getApplicationContext(), Login.class);
	        	//i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	        	startActivity(i);
	        	finish();
	        } else {
		    
		    Intent intent = getIntent();
		    String action = intent.getAction();
		    String type = intent.getType();
		    Log.d("TAG","INTENT ACTION TYPE: "+action);
		    Log.d("TAG","INTENT  TYPE: "+type);
		    if (Intent.ACTION_SEND.equals(action) && type != null && type.startsWith("image/")) {
		        	handleSendImage(intent); // Handle single image being sent
		    }

		    
		    
		    directory = new File(Environment.getExternalStorageDirectory()+File.separator+"Talentwire");
		    directory.mkdirs();
		    

	        
	        final SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(StaffActivity.this); 
			final String fbtoken =  prefs.getString("access_token", null); 
			Log.d("TAG",fbtoken);
			AsyncStaffInfo async = new AsyncStaffInfo(this); 
	   	    async.execute(fbtoken);
	        }
	        
	        
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			// Define the criteria how to select the locatioin provider -> use
			// default
			Criteria criteria = new Criteria();
			provider = locationManager.getBestProvider(criteria, false);
			Location location = locationManager.getLastKnownLocation(provider);

			
			 
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
	        
	        
			
			// Initialize the location fields
			if (location != null) {
				System.out.println("Provider " + provider + " has been selected.");
				double lat = (double) (location.getLatitude());
				double lng = (double) (location.getLongitude());
				latituteField = (String.valueOf(lat));
				longitudeField = (String.valueOf(lng));
			} else {
				latituteField = ("Provider not available");
				longitudeField = ("Provider not available");
			}
	        
	        Log.d("TAG", "INITIAL LAT AND LONG: "+latituteField+ " " +longitudeField );
	        
	       
	        
	        mPullRefreshWebView = (PullToRefreshWebView) findViewById(R.id.firstweb);
	        Log.d("TAG","START OF APP");
	        CookieSyncManager.createInstance(this);
	        CookieSyncManager.getInstance().startSync();
	        mWebView = mPullRefreshWebView.getRefreshableView();
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.setWebViewClient(new SampleWebViewClient(){
				   @Override
				   public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
					   checkLoading();
					   if (urlNewString.contains("?header=no")){
					   Log.d("TAG","CASE1");
					   } else if (urlNewString.contains("fbconnect")){
						   Log.d("TAG","CASE2");

					   }
					   else if (urlNewString.contains("#_=_") && urlNewString.contains("code=")){
						   urlNewString = urlNewString.substring(0, urlNewString.indexOf("#_=_"));
						   Log.d("TAG", "CASE3");
					   }
					   else if (urlNewString.contains("users") || urlNewString.contains("feeds") || urlNewString.contains("conversation") || urlNewString.contains("messages") || urlNewString.contains("edit")){
						   urlNewString = urlNewString.concat("?header=no");
						   Log.d("TAG", "CASE3");
					   } 
	
					   view.loadUrl(urlNewString);
					   Log.d("TAG","LOADING URL: "+urlNewString);
					   return true;
				   
				   }
	
		        @Override
		        	public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
		        	    handler.proceed(); // Ignore SSL certificate errors
		        	}
		        }
			);
			//mWebView.loadUrl("https://m.facebook.com/dialog/oauth?client_id=187212574660004&redirect_uri=https://www.talentwire.me/facebook_authenticate?mobile=true&header=no");
			//mWebView.loadUrl("http://www.facebook.com/dialog/oauth?client_id=187212574660004&redirect_uri=https://www.talentwire.me/facebook_authenticate?header=no&mobile=true&display=touch");
			mWebView.loadUrl("https://www.talentwire.me/facebook_authenticate?header=no&mobile=true&display=touch");
			//mWebView.loadUrl("https://www.talentwire.me/facebook_authenticate?header=no&mobile=true&display=touch");


			profButton = (ImageButton)this.findViewById(R.id.profButton);
			profButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	if (StaffTasks.firstname!=null){
	            	Log.d("TAG", StaffTasks.firstname+StaffTasks.lastname);
	            	String userprofile = "https://www.talentwire.me/users/";
	            	userprofile = userprofile.concat(StaffTasks.firstname);
	            	userprofile = userprofile.concat("-");
	            	userprofile = userprofile.concat(StaffTasks.lastname);
	            	userprofile = userprofile.concat("?header=no");
	            	Log.d("TAG",userprofile);
	            	mWebView.loadUrl(userprofile);
	            	checkLoading();
	            	}
	            	}
	        });
			
	
			talentwire = (ImageButton)this.findViewById(R.id.talentwire);
			talentwire.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	        		mWebView.loadUrl("https://www.talentwire.me/user/dashboard?header=no");
	            	checkLoading();
	        		}
	        });
			share = (ImageButton)this.findViewById(R.id.share);
			share.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	 
	            	customDialog(yourSelectedImage);
	        	    /*
	    		     * Notiication Testing
	    		     */
	    		    
	                //startNotification();
	            }
	        });
			
			msgButton = (ImageButton)this.findViewById(R.id.msgButton);
			msgButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	if (StaffTasks.firstname!=null){
	            	String messages = "https://www.talentwire.me/";
	            	messages = messages.concat(StaffTasks.firstname);
	            	messages = messages.concat("-");
	            	messages = messages.concat(StaffTasks.lastname);
	            	messages = messages.concat("/messages");
	            	messages = messages.concat("?header=no");
	            	Log.d("TAG",messages);
	            	mWebView.loadUrl(messages);
	            	checkLoading();
	            	}
	            }
	        });
			/*
			 * Bind service
			 */
			doBindService();
			
			}
	 
	 	private void startNotification(){
	 		//createNotification();
	 	}
	 
		@Override
		protected void onResume() {
			super.onResume();
			locationManager.requestLocationUpdates(provider, 400, 100, this);
		}

		/* Remove the locationlistener updates when Activity is paused */
		@Override
		protected void onPause() {
			super.onPause();
			locationManager.removeUpdates(this);
		}
		

		private ServiceConnection mConnection = new ServiceConnection() {

			public void onServiceConnected(ComponentName className, IBinder binder) {
				s = ((NotifyService.LocalBinder) binder).getService();
				Toast.makeText(StaffActivity.this, "Connected",
						Toast.LENGTH_SHORT).show();
			}

			public void onServiceDisconnected(ComponentName className) {
				s = null;
			}
		};


		void doBindService() {
			bindService(new Intent(this, NotifyService.class), mConnection,
					Context.BIND_AUTO_CREATE);
		}

		public void showServiceData(View view) {
			if (s != null) {
			}
		}
		
		
		@Override
		public void onLocationChanged(Location location) {
			double lat = (double) (location.getLatitude());
			double lng = (double) (location.getLongitude());
			latituteField = (String.valueOf(lat));
			longitudeField = (String.valueOf(lng));
	        Log.d("TAG", "NEW LAT AND LONG: "+latituteField+ " " +longitudeField );
	        AsyncLocUpdate loc = new AsyncLocUpdate(latituteField, longitudeField);
	        loc.execute();
	        
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(this, "Enabled new provider " + provider,
					Toast.LENGTH_SHORT).show();

		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(this, "Disabled provider " + provider,
					Toast.LENGTH_SHORT).show();
		}
	 
	 private static class SampleWebViewClient extends WebViewClient {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				
				view.loadUrl(url);
				return true;
			}
		}
	 
	

	 private void handleSendImage(Intent intent) {
	     Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
	     if (imageUri != null) {
	         try {
				Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
				customDialog(bitmap);
				Log.d("TAG","Handling the URI and starting custom dialog");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

	     }
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
				  if (json_data_level1.getString("topic").equals("Do") || json_data_level1.getString("topic").equals("Trend") || json_data_level1.getString("topic").equals("Mentor")){
				  String topic = json_data_level1.getString("topic");
				  String topic_id = json_data_level1.getString("topic_id");
				  topics.add(topic);
				  topids.add(topic_id);
				  }
				  //Log.d("TAG", "TOPICS"+topic);
			  }
	        } catch (JSONException e) {
				e.printStackTrace();
			}
		}
	 
	 public void customDialog(final Bitmap selected){
		 final Dialog dialog = new Dialog(this);

		if(selected!=null){
        dialog.setContentView(R.layout.customalert);
        dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		} else {
		dialog.setContentView(R.layout.customaletertnopic);
        dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		}
		dialog.getWindow().setGravity(Gravity.TOP);
		
        dialog.setTitle("Share");
        final EditText postbox = (EditText) dialog.findViewById(R.id.postbox);
        ImageView image = (ImageView)dialog.findViewById(R.id.image);
        ImageButton camera = (ImageButton) dialog.findViewById(R.id.camera);
        ImageButton share = (ImageButton) dialog.findViewById(R.id.share);
        final Spinner catselect = (Spinner) dialog.findViewById(R.id.catselect);

        postbox.setText("What are you working on?");
        postbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if (postbox.getText().toString().equals("What are you working on?")){
            		postbox.setText("");
            	}
            }
        });
        
        if (selected!=null){
        	image.setImageBitmap(selected);
        } else {
        	Log.d("TAG","Custom Dialog with no image");
        }
        
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item);
        catselect.setAdapter(adapter);
        if (topics.size()!=0){
        	for(int i=0; i<3;i++){
        		adapter.add(topics.get(i));
        		}
        	} else {
        	Toast.makeText(getApplicationContext(), "Please wait, loading subscriptions", 0).show();
        	AsyncSubs subs = new AsyncSubs();
        	subs.execute();
        	Log.d("TAG","No subs, getting them async");
        }
        

        share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if (selected==null){
                    post(postbox.getText().toString(), topids.get(catselect.getSelectedItemPosition()));
            	} else if (selected!=null){
            		postImage(postbox.getText().toString(), topids.get(catselect.getSelectedItemPosition()),selected);
            	}
                dialog.dismiss();
            }
        });
        
        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//Toast.makeText(getApplicationContext(), "Coming soon!", 0).show();
            	imageDialog();
            	dialog.dismiss();
            }
        });
        if (topics.size()!=0){
        dialog.show();
        } else {
        	Toast.makeText(getApplicationContext(), "Loading subscriptions", 0);
        }
	 }
        
   	 public void imageDialog(){
		 final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.imagedialog);
        dialog.setTitle("Upload");
        Button gallery = (Button) dialog.findViewById(R.id.selectGallery);
        Button camera = (Button) dialog.findViewById(R.id.takePhoto);

        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	onBtnTakePicture();
            	dialog.dismiss();
            }
        });
        
        
        gallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	onBtnPickGallery();
            	
            	dialog.dismiss();
            }
        });
        dialog.show();
	}
	 
		private void post(String topost, String topicid){
			//Photo p = null;
			Log.d("TAG", topost+" was just posted in topic id "+topicid);
			PostAsync post = new PostAsync(topost,topicid);
			post.execute();
			mWebView.reload();

		}
		
		public void onBtnPickGallery() {
		    Intent intent = new Intent();
		    intent.setType("image/*");
		    intent.setAction(Intent.ACTION_GET_CONTENT);
		    startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
		}

		public void onBtnTakePicture() {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE); 
	      	//PostImage img = new PostImage(photo);
        	//img.execute();
		}

		protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
		    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
		    Log.d("TAG","IN ACTIVITY RESULT REQUEST CODE IS"+requestCode);
		    switch(requestCode) { 
		    case GALLERY_REQUEST_CODE:
		        if(resultCode == RESULT_OK){  
		            Uri selectedImage = imageReturnedIntent.getData();
		            String[] filePathColumn = {MediaStore.Images.Media.DATA};

		            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		            cursor.moveToFirst();

		            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		            String filePath = cursor.getString(columnIndex);
		            cursor.close();
		            
		            File f = new File(filePath);
		            Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
		            customDialog(yourSelectedImage);
		        }
		    case CAMERA_REQUEST_CODE:
		    	if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {  
		            Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data"); 
		            Uri u;
		            Bitmap bitmap = null;
					//File f = new File(new URI(u.toString()));
					if (imageReturnedIntent != null) {
						u = imageReturnedIntent.getData();
						try {
							//bitmap = Media.getBitmap(this.getContentResolver(),u);
							bitmap = (Bitmap) imageReturnedIntent.getExtras().get("data"); 
							try {
								FileOutputStream fos = new FileOutputStream(
										directory + "/" + "test.png");
								bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
								
							} catch (Exception e) {
								//log exception
								e.printStackTrace();
							}
					}catch (Exception e) {
						//log exception
						e.printStackTrace();
				}
		    	}
		            Bitmap bm = BitmapFactory.decodeFile(directory+"/test.png");
		            Log.d("TAG","Still in staffactivity, image size is:"+ StaffTasks.getSizeInBytes(bm));
					customDialog(bm);

				}
		    	}
		}


		private void postImage(String topost, String topicid, Bitmap f){
		    PostImage post = new PostImage(f,topost,topicid);
		    post.execute();
			
		}
		   public void reload() {

			    Intent intent = getIntent();
			    overridePendingTransition(0, 0);
			    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			    finish();

			    overridePendingTransition(0, 0);
			    startActivity(intent);
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
										sleep(1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
				            	  }
				              }
				      }.start();
				      }catch (Exception e) {}
				      counter++;
			  }

	 private class PostAsync extends AsyncTask<Void, Void, Void>
	    {

	        ProgressDialog mProgressDialog;
			private String topicid;
			private String topost;
	        @Override
	        protected void onPostExecute(Void result) {
	            mProgressDialog.dismiss();
	        }
	        public PostAsync(String topost, String topicid) {
	    		super();
	    		this.topicid = topicid;
	    		this.topost = topost;

	    	}

			@Override
	        protected void onPreExecute() {
	            mProgressDialog = ProgressDialog.show(StaffActivity.this, "Posting", topost);
	        }

	        @Override
	        protected Void doInBackground(Void... params) {
				StaffTasks.createFeed("", "", "0", topicid, "", "0", topost, getApplicationContext());
				
	            return null;
	        }
	    }
	 
	 public class PostImage extends AsyncTask<Void, Void, Void>
	    {

	        ProgressDialog mProgressDialog;
	        private Bitmap image;
			//private Bitmap image;
			private String topost;
			private String topicid;
	        @Override
	        protected void onPostExecute(Void result) {
	            mProgressDialog.dismiss();
	        }

			public PostImage(Bitmap f, String topost, String topicid) {
	    		super();
	    		this.image = f;
	    		this.topicid = topicid;
	    		this.topost = topost;

			}
			@Override
	        protected void onPreExecute() {
	            mProgressDialog = ProgressDialog.show(StaffActivity.this, "Posting", topost);
	        }

	        @Override
	        protected Void doInBackground(Void... params) {
				try {
					StaffTasks.executeMultipartPost(image,topicid,topost,getApplicationContext());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
	            return null;
	        }
	    }
	 
	 
	 
	 private class AsyncLocUpdate extends AsyncTask<Void, Void, Void>
	    {

			private String mobile_latitude;
			private String mobile_longitude;
	        @Override
	        protected void onPostExecute(Void result) {
	        }
	        public AsyncLocUpdate(String mobile_latitude, String mobile_longitude) {
	    		super();
	    		this.mobile_latitude = mobile_latitude;
	    		this.mobile_longitude = mobile_longitude;

	    	}

			@Override
	        protected void onPreExecute() {
	        }

	        @Override
	        protected Void doInBackground(Void... params) {
				StaffTasks.updateLocation(mobile_latitude, mobile_longitude, getApplicationContext());
	            return null;
	        }
	    }
	 private class AsyncSubs extends AsyncTask<Void, Void, Void>
	    {
	        @Override
	        protected void onPostExecute(Void result) {
	        }

	    	

			@Override
	        protected void onPreExecute() {
	        }

	        @Override
	        protected Void doInBackground(Void... params) {
				parseSubs(StaffTasks.getSubs(getApplicationContext()));
	            return null;
	        }
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
	        mWebView.goBack();
	        if (mWebView.getUrl().contains("dashboard")){
	        	Log.d("TAG","On the dash URL is "+mWebView.getUrl());
	        	finish();
	        }
	    	return;
	    }
}