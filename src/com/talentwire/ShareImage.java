package com.talentwire;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.talentwire.StaffActivity.PostImage;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class ShareImage extends Activity {
	private ImageView image;
	private Bitmap bitmap;

    public static ArrayList<String> topics = new ArrayList<String>(3);
    public static ArrayList<String> topids = new ArrayList<String>(3);
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
			topids = StaffActivity.topids;
			topics = StaffActivity.topics;
	       
	        
	        setContentView(R.layout.shareimage);

	        final ImageView imageView = (ImageView) this.findViewById(R.id.image);
	        final EditText postbox = (EditText) this.findViewById(R.id.postbox);
	        ImageButton share = (ImageButton) this.findViewById(R.id.share);
	        final Spinner catselect = (Spinner) this.findViewById(R.id.catselect);
	        image = (ImageView)this.findViewById(R.id.image);


		    Intent intent = getIntent();
		    String action = intent.getAction();
		    String type = intent.getType();
		    Log.d("TAG","INTENT ACTION TYPE: "+action);
		    Log.d("TAG","INTENT  TYPE: "+type);
		    if (Intent.ACTION_SEND.equals(action) && type != null && type.startsWith("image/")) {
		        	Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
		   	     if (imageUri != null) {
		   	         try {
		   				 bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
		   				 if (bitmap!=null){
		   			        	image.setImageBitmap(bitmap);
		   			        }				Log.d("TAG","Handling the URI and starting custom dialog");
		   			} catch (FileNotFoundException e) {
		   				e.printStackTrace();
		   			} catch (IOException e) {
		   				e.printStackTrace();
		   			}

		    }

		    

	        
	       
	        
	        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item);
	        catselect.setAdapter(adapter);
	        if (topics.size()!=0){
	        for(int i=0; i<topics.size();i++){
	        	adapter.add(topics.get(i));
	        }
	        }
	        share.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            		postImage(postbox.getText().toString(), topids.get(catselect.getSelectedItemPosition()),bitmap);
	            	//finish();
	            }
	        });
		    }
		 }
		    private void postImage(String topost, String topicid, Bitmap f){
			    PostImage post = new PostImage(f,topost,topicid);
			    post.execute();
				
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
			            mProgressDialog = ProgressDialog.show(ShareImage.this, "Posting", topost);
			        }

			        @Override
			        protected Void doInBackground(Void... params) {
						try {
							StaffTasks.executeMultipartPost(image,topicid,topost,getApplicationContext());
						} catch (Exception e) {
							e.printStackTrace();
						}
						finish();
			            return null;
			        }
			    }
				@Override
				protected void onResume() {
					super.onResume();
					 if (topics.size()==0){
				        	image.destroyDrawingCache();
				        	AsyncSubs subs = new AsyncSubs();
				        	subs.execute();
				        	Log.d("TAG","No subs, getting them async");
				        }
				        
				}
				private class AsyncSubs extends AsyncTask<Void, Void, Void>
			    {
					Dialog dialog = new Dialog(ShareImage.this);

			        @Override
			        protected void onPostExecute(Void result) {
			        	dialog.dismiss();
			        }

			    	

					@Override
			        protected void onPreExecute() {
						dialog.show();
						dialog.setTitle("Please wait");
			        }

			        @Override
			        protected Void doInBackground(Void... params) {
						parseSubs(StaffTasks.getSubs(getApplicationContext()));
			            return null;
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

}
