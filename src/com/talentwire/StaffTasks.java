package com.talentwire;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class StaffTasks{
	private static String key;
	private static String staffkey;
	private static String user_id;
	private static String messages;
    private static Facebook facebook = new Facebook("187212574660004");
	private static String name;
	private static String id;
	private static ImageView user_picture;
	private static String facebook_uid;
	private static String email;
	private static String link;
	private static String birthday = "00/00/00";
	private static String gender;
	private static String locale;
	private static int numfriends;
	private static String appliedjobs;
	private static String friendnum;
	private static String proposals;
	private static String capabilities;
	private static String profdetails;
	private static Bitmap userpic;
	public static String subs;
	public static String firstname;
	public static String lastname;
	public static String donelogin=null;
	public static String getProfDetails(Context c) {
		try {
		URI url = new URI("https://talentwire.me/apis/"+user_id+"/profile_details");
		
	    HttpGet get = new HttpGet(url);
	    HttpClient client = new MyHttpClient(c);
	    ResponseHandler<String> responseHandler=new BasicResponseHandler();
	    profdetails = client.execute(get, responseHandler);
	    Log.d("TAG", "RESPONSE STRING FROM HTTPS GET PROFILE DETAILS IS: "+profdetails);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
	    } catch (URISyntaxException e) {
			e.printStackTrace();
		}
	    return profdetails;
	}
	
	public static String getSubs(Context c) {
		SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(c); 
        String key = prefs.getString("staffkey", null);
		subs = null;
		try {
		URI url = new URI("https://talentwire.me/apis/user/"+key+"/get/subscriptions");
		
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));
		 
		HttpParams paramz = new BasicHttpParams();
		paramz.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
		paramz.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(30));
		paramz.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
		HttpProtocolParams.setVersion(paramz, HttpVersion.HTTP_1_1);
		 
		ClientConnectionManager cm = new SingleClientConnManager(paramz, schemeRegistry);
		DefaultHttpClient client = new DefaultHttpClient(cm, paramz);
		
	    HttpGet get = new HttpGet(url);
	    //HttpClient client = new MyHttpClient(c);
	    ResponseHandler<String> responseHandler=new BasicResponseHandler();
	    subs = client.execute(get, responseHandler);
	    Log.d("TAG", "RESPONSE STRING FROM GET SUBS: "+subs);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
	    } catch (URISyntaxException e) {
			e.printStackTrace();
		}
	    return subs;
	}
	
	public static String subScribe(String topicid, Context c) {
		String subs = null;
		try {
			URI url = new URI("https://talentwire.me/apis/topic/"+topicid+"/subscribe/"+staffkey);
		
	    HttpGet get = new HttpGet(url);
	    HttpClient client = new MyHttpClient(c);
	    ResponseHandler<String> responseHandler=new BasicResponseHandler();
	    subs = client.execute(get, responseHandler);
	    Log.d("TAG", "RESPONSE STRING FROM SUBSRIBE TO TOPIC "+subs+" topic id is "+topicid);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
	    } catch (URISyntaxException e) {
			e.printStackTrace();
		}
	    return subs;
	}
	
	public static String getOath(Context c) {
		String oath = null;
		try {
		URI url = new URI("https://talentwire.me/apis/get_session_key/"+staffkey);

	    HttpGet get = new HttpGet(url);
	    HttpClient client = new MyHttpClient(c);
	    ResponseHandler<String> responseHandler=new BasicResponseHandler();
	    subs = client.execute(get, responseHandler);
	    Log.d("TAG", "RESPONSE STRING FROM OATH IS "+oath);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
	    } catch (URISyntaxException e) {
			e.printStackTrace();
		}
	    return oath;
	}
	
	   public static void createFeed(String url_title,String url_description,String share_to_friend,String post_to,String url_address,String share_to_career_team,String feed, Context c) {
			SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(c); 
	        String key = prefs.getString("staffkey", null);
	        String result = null;
	        Log.d("TAG",key);
		    HttpClient client = new MyHttpClient(c);
	    	        
	    	        HttpPost post = new HttpPost("https://talentwire.me/apis/create_my_feed");
	    	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	    	        nameValuePairs.add(new BasicNameValuePair("url_title", url_title));
	    	        nameValuePairs.add(new BasicNameValuePair("url_description", url_description));
	    	        nameValuePairs.add(new BasicNameValuePair("share_to_friend", share_to_friend));
	    	        nameValuePairs.add(new BasicNameValuePair("post_to", post_to));
	    	        nameValuePairs.add(new BasicNameValuePair("url_address", url_address));
	    	        nameValuePairs.add(new BasicNameValuePair("share_to_career_team", share_to_career_team));
	    	        nameValuePairs.add(new BasicNameValuePair("feed", feed));
	    	        nameValuePairs.add(new BasicNameValuePair("session_key", key));
	 
	    	        //nameValuePairs.add(new BasicNameValuePair("upload_file", upload_file));
	    	        //nameValuePairs.add(new BasicNameValuePair("richmedia_type", richmedia_type));
	    	        //nameValuePairs.add(new BasicNameValuePair("text_richmedia_value", text_richmedia_value));


	    	        //nameValuePairs.add(new BasicNameValuePair("upload_file", upload_file));

	    	        try {
	    	        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	    			ResponseHandler<String> responseHandler=new BasicResponseHandler();
	    			result = client.execute(post, responseHandler);
	    			Log.d("TAG","TEST RESULTS FROM POST TO FEED IS: "+result);
	    			//parseResponse(responseBody);

	    			} catch (ClientProtocolException e) {
	    				e.printStackTrace();
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			} catch (ParseException e) {
	    				e.printStackTrace();
	    			}
	   }
	
	    public static void uploadUserPhoto(File image, Context c) {
	    	SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(c); 
	        String key = prefs.getString("staffkey", null);
	        String result = null;
	        Log.d("TAG",key);
		    HttpClient client = new MyHttpClient(c);
	        try {

	            HttpPost httppost = new HttpPost("https://talentwire.me/apis/create_my_feed");
			//	StaffTasks.createFeed("", "", "0", topicid, "", "0", topost, getApplicationContext());

	            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);  
	            //MultipartEntity multipartEntity = new MultipartEntity();
	            multipartEntity.addPart("feed", new StringBody("Here's sitch witha  fresh pic"));
	            multipartEntity.addPart("url_title", new StringBody(""));
	            multipartEntity.addPart("upload_file", new FileBody(image));
	            multipartEntity.addPart("session_key", new StringBody(key));
	            multipartEntity.addPart("url_address", new StringBody(""));
	            multipartEntity.addPart("post_to", new StringBody("1"));
	            multipartEntity.addPart("share_to_friend", new StringBody("0"));
	            multipartEntity.addPart("share_to_career_team", new StringBody("0"));
	            multipartEntity.addPart("url_description", new StringBody(""));
	            multipartEntity.addPart("richmedia_type", new StringBody("1"));
	            //multipartEntity.addPart("text_richmedia_value", new StringBody("1"));
	            httppost.setEntity(multipartEntity);

	            ResponseHandler<String> responseHandler=new BasicResponseHandler();
    			result = client.execute(httppost, responseHandler);
    			Log.d("TAG","TEST RESULTS FROM POST PICTURE : "+result);
	            } catch (ClientProtocolException e) {
	                Log.d("TAG : "+e, e.getMessage());         
	            } catch (IOException e) {
	                Log.d("TAG : "+e, e.getMessage());

	            } 
	    }
	    
	    public static long getSizeInBytes(Bitmap bitmap) {
	    	int SDK_INT = android.os.Build.VERSION.SDK_INT;

	        if(SDK_INT>=12) {
	            return bitmap.getByteCount();
	            //return bitmap.getRowBytes() * bitmap.getHeight();

	        } else {
	            return bitmap.getRowBytes() * bitmap.getHeight();
	        }
	    }
	    

	    public static void executeMultipartPost(Bitmap image, String topicid, String topost, Context c) throws Exception {
	    		String boundary = "------WebKitFormBoundary4QuqLuM1cE5lMwCy";
	            Log.d("TAG","Now in StaffTasks, image size is:"+ getSizeInBytes(image));

//	        	SchemeRegistry schemeRegistry = new SchemeRegistry();
//	    		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//	    		schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));
//	    		 
//	    		HttpParams paramz = new BasicHttpParams();
//	    		paramz.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
//	    		paramz.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(30));
//	    		paramz.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
//	    		HttpProtocolParams.setVersion(paramz, HttpVersion.HTTP_1_1);
//	    		 
//	    		ClientConnectionManager cm = new SingleClientConnManager(paramz, schemeRegistry);
//	    		DefaultHttpClient client = new DefaultHttpClient(cm, paramz);
	            
	    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
    			image.compress(CompressFormat.JPEG, 90, bos);
    			byte[] data = bos.toByteArray();
    			try
    			{
    				SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(c); 
    				String key = prefs.getString("staffkey", null);
        			URL post_url = new URL("https://www.talentwire.me/apis/create_my_feed");
        			
        			HttpURLConnection feed_connection = (HttpURLConnection) post_url.openConnection();
        			//feed_connection.
        			//feed_connection.setChunkedStreamingMode(0);
        			feed_connection.setDoOutput(true);//make a POST Method as defualt is GET
        			feed_connection.setRequestMethod("POST");
        			feed_connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        			
        			
        			//Now the actual composing of the multi part form
        			String session_key = new String("--" + boundary + "\r\n" + "Content-Disposition: form-data; name=\"session_key\"\r\n\r\n" + key + "\r\n");
        			String feed = new String("--" + boundary + "\r\n" + "Content-Disposition: form-data; name=\"feed\"\r\n\r\n" + topost + "\r\n");
        			String sh_career = new String("--" + boundary + "\r\n" + "Content-Disposition: form-data; name=\"share_to_career_team\"\r\n\r\n" + 0 + "\r\n");
        			String sh_friend = new String("--" + boundary + "\r\n" + "Content-Disposition: form-data; name=\"share_to_friend\"\r\n\r\n" + 0 + "\r\n");
        			String url_t = new String("--" + boundary + "\r\n" + "Content-Disposition: form-data; name=\"url_title\"\r\n\r\n"+"\r\n");
        			String url_d = new String("--" + boundary + "\r\n" + "Content-Disposition: form-data; name=\"url_description\"\r\n\r\n"+"\r\n");
        			String url_a = new String("--" + boundary + "\r\n" + "Content-Disposition: form-data; name=\"url_address\"\r\n\r\n"+"\r\n");
        			String topic_id = new String("--" + boundary + "\r\n" + "Content-Disposition: form-data; name=\"post_to\"\r\n\r\n" + topicid + "\r\n");
        			
        			
        			if (data != null) {
        				String file_param_constant = "image";
        				Log.d("TAG","Image is here");
        				String image_part_1 = new String("--" + boundary + "\r\n" + "Content-Disposition: form-data; name=\"richmedia_type\"\r\n\r\n" + "1\r\n");
            			String image_part_2 = new String("--" + boundary + "\r\n" + "Content-Disposition: form-data; name=\"" + file_param_constant + "\"; filename=\"image.jpg\"\r\n" + "Content-Type: image/jpeg\r\n\r\n");
            			String missing_link = new String ("\r\n");
            			String image_part_3 = new String("--" + boundary + "--\r\n");
            			//feed_connection.setRequestProperty("Content-Length", String.valueOf((session_key.length() + feed.length() + sh_career.length() + sh_friend.length() + url_t.length() + url_d.length() + url_a.length() + topic_id.length() + image_part_1.length() + image_part_2.length() + missing_link.length() + image_part_3.length())));
            			feed_connection.setFixedLengthStreamingMode(session_key.length() + feed.length() + sh_career.length() + sh_friend.length() + url_t.length() + url_d.length() + url_a.length() + topic_id.length() + image_part_1.length() + image_part_2.length() + missing_link.length() + image_part_3.length() +data.length);
            			Log.d("TAG", "CONTENT LEGNTH WITH IMG:"+String.valueOf((session_key.length() + feed.length() + sh_career.length() + sh_friend.length() + url_t.length() + url_d.length() + url_a.length() + topic_id.length() + image_part_1.length() + image_part_2.length() + missing_link.length() + image_part_3.length() +data.length)));
            			ByteArrayOutputStream form_output 	= new ByteArrayOutputStream();
            			OutputStream form_stream 			= new BufferedOutputStream(feed_connection.getOutputStream());
            			form_output.write(session_key.getBytes());
            			form_output.write(feed.getBytes());
            			form_output.write(sh_career.getBytes());
            			form_output.write(sh_friend.getBytes());
            			form_output.write(url_t.getBytes());
            			form_output.write(url_d.getBytes());
            			form_output.write(url_a.getBytes());
            			form_output.write(topic_id.getBytes());
        				form_output.write(image_part_1.getBytes());
        				form_output.write(image_part_2.getBytes());
            			form_output.write(data);
            			form_output.write(missing_link.getBytes());
            			
            			form_output.write(image_part_3.getBytes());

            			form_stream.write(form_output.toByteArray());
            			Log.d("TAG","Feed-Connection content legnth :"+Integer.toString(feed_connection.getContentLength()));
            			form_stream.close();
            			feed_connection.disconnect();
        			}else {
        				String image_part_1 = new String("--" + boundary + "\r\n" + "Content-Disposition: form-data; name=\"richmedia_type\"\r\n\r\n" + "0\r\n");
        				Log.d("TAG", "CONTENT LEGNTH:"+String.valueOf((session_key.length() + feed.length() + sh_career.length() + sh_friend.length() + url_t.length() + url_d.length() + url_a.length() + getSizeInBytes(image) + topic_id.length())));
        				feed_connection.setRequestProperty("Content-Length NO IMG", String.valueOf((session_key.length() + feed.length() + sh_career.length() + sh_friend.length() + url_t.length() + url_d.length() + url_a.length() + topic_id.length())));
        				ByteArrayOutputStream form_output 	= new ByteArrayOutputStream();
            			OutputStream form_stream 			= new BufferedOutputStream(feed_connection.getOutputStream());
            			form_output.write(session_key.getBytes());
            			form_output.write(feed.getBytes());
            			form_output.write(sh_career.getBytes());
            			form_output.write(sh_friend.getBytes());
            			form_output.write(url_t.getBytes());
            			form_output.write(url_d.getBytes());
            			form_output.write(url_a.getBytes());
            			form_output.write(topic_id.getBytes());
        				form_output.write(image_part_1.getBytes());
            			form_stream.write(form_output.toByteArray());
            			Log.d("TAG","Feed-Connection content legnth :"+ Integer.toString(feed_connection.getContentLength()));
            			form_stream.close();
            			feed_connection.disconnect();
        			}
        			//write form to connection
        			
        			if (feed_connection.getResponseCode() != HttpURLConnection.HTTP_OK)
        			{
    	    			Log.d("TAG","CRAP BROKE MAN");
    	    			throw new Exception("Carp hit the fan an connection didnt get a OKAY Error Code: " + feed_connection.getResponseMessage() +  feed_connection.getResponseCode());
        			}
        			
        			InputStream recieved_information = feed_connection.getInputStream();

    			}
    			catch (Exception the_e)
    			{
    				Log.d("TAG","Exception:"+ the_e.getMessage());
    			}
    			
	    	}
	    
	    public static void dordiePart(Bitmap image, String topicid, String topost, Context c){
	    	SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(c); 
    		String key = prefs.getString("staffkey", null);
    	    String responseString = "";
	    	{
	            try {
	                //InputStream is = c.getAssets().open("data.xml");
	                String imageName = System.currentTimeMillis() + ".jpg";
	    			HttpClient httpClient = new MyHttpClient(c);
	    			//DefaultHttpClient httpClient = new DefaultHttpClient();

	                HttpPost postRequest = new HttpPost("https://test.talentwire.me/apis/create_my_feed");
		    		if (image==null){
		    			Log.d("TAG", "NULL IMAGE");
		    		}
	    			ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    			image.compress(CompressFormat.JPEG, 75, bos);
	    			byte[] data = bos.toByteArray();
	    			ByteArrayBody bab = new ByteArrayBody(data, imageName);
	                //InputStreamBody isb = new InputStreamBody(new ByteArrayInputStream(data),"uploadedFile");
	                //InputStreamBody isb = new InputStreamBody(new ByteArrayInputStream(data), "uploadedFile") {
	                //	   public long getContentLength() {
	                //		      return getContentLength();
	                //		   }
	                //};
	    			Log.d("TAG","BITMAP SIZE IS: "+bab.getContentLength());
	                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
	    			reqEntity.addPart("upload_file", bab);
	    			reqEntity.addPart("feed", new StringBody(topost));
	    			reqEntity.addPart("url_title", new StringBody(""));
	    			reqEntity.addPart("session_key", new StringBody(key));
	    			reqEntity.addPart("url_address", new StringBody(""));
	    			reqEntity.addPart("post_to", new StringBody(topicid));
	    			reqEntity.addPart("share_to_friend", new StringBody("0"));
	    			reqEntity.addPart("share_to_career_team", new StringBody("0"));
	    			reqEntity.addPart("url_description", new StringBody(""));
	    			reqEntity.addPart("richmedia_type", new StringBody("2"));
	    			//reqEntity.addPart("upload_file", bab);
		            postRequest.setEntity(reqEntity);
		            HttpResponse response = httpClient.execute(postRequest);
		            postRequest.getAllHeaders();
		            //Log.d("TAG",postRequest.getHeaders("upload_file"));
		            BufferedReader reader = new BufferedReader(
		              new InputStreamReader(
		                  response.getEntity().getContent(), "UTF-8"));
		            String sResponse;
		            StringBuilder s = new StringBuilder();
		            while ((sResponse = reader.readLine()) != null) {
		               s = s.append(sResponse);
		            }
		            responseString = s.toString();
		          Log.d("TAG", "Response: " + responseString);
		        } catch (Exception e) {
		            Log.e(e.getClass().getName(), e.getMessage());
		        }
		      }
	    	image.recycle();
		    }
	    
	    public static void stackPart(){
	   
	    }


	    	/*public static void executeMultipartPost(Bitmap image, String topicid, String topost, Context c) throws Exception {
		    	SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(c); 
	    		String key = prefs.getString("staffkey", null);
	    		String result = null;
	    		try {
	    			ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    			image.compress(CompressFormat.JPEG, 75, bos);
	    			byte[] data = bos.toByteArray();
	    			HttpClient httpClient = new MyHttpClient(c);
	    			HttpPost postRequest = new HttpPost("https://talentwire.me/apis/create_my_feed");
	    			ByteArrayBody bab = new ByteArrayBody(data, "photo.jpg");
	    			MultipartEntity reqEntity = new MultipartEntity(
	    					HttpMultipartMode.BROWSER_COMPATIBLE);
	    			//postRequest.
	    			//postRequest.setHeader( "Content-Type", "multipart/form-data" );
	    			reqEntity.addPart("upload_file", bab);
	    			reqEntity.addPart("feed", new StringBody(topost));
	    			reqEntity.addPart("url_title", new StringBody(""));
	    			reqEntity.addPart("session_key", new StringBody(key));
	    			reqEntity.addPart("url_address", new StringBody(""));
	    			reqEntity.addPart("post_to", new StringBody(topicid));
	    			reqEntity.addPart("share_to_friend", new StringBody("0"));
	    			reqEntity.addPart("share_to_career_team", new StringBody("0"));
	    			reqEntity.addPart("url_description", new StringBody(""));
	    			reqEntity.addPart("richmedia_type", new StringBody("1"));
	    			postRequest.setEntity(reqEntity);
	    
	    			Log.d("TAG","HTTP BODY FOR TONY PEPPERONI "+reqEntity.toString());
	    			ResponseHandler<String> responseHandler=new BasicResponseHandler();
	    			result = httpClient.execute(postRequest, responseHandler);
	    			Log.d("TAG","TEST RESULTS FROM POST PICTURE : "+result);

	    		} catch (Exception e) {
	    			// handle exception here
	    			Log.e(e.getClass().getName(), e.getMessage());
	    		}
	    	}*/
	    

	   
	public static String getProposals(Context c) {
		try {
		URI url = new URI("https://talentwire.me/apis/"+user_id+"/list_proposal");
		
	    HttpGet get = new HttpGet(url);
	    HttpClient client = new MyHttpClient(c);
	    ResponseHandler<String> responseHandler=new BasicResponseHandler();
	    proposals = client.execute(get, responseHandler);
	    //parseResponse(responseBody);
	    Log.d("TAG", "RESPONSE STRING FROM HTTPS GET PROPOSAL IS: "+proposals);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
	    } catch (URISyntaxException e) {
			e.printStackTrace();
		}
	    return proposals;
	}
   
	   //Make this async or get it passed from other activity...
   private static void getCapabilities(Context c) {
		SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(c); 
        String key = prefs.getString("staffkey", null);
        Log.d("TAG",key);
    	        MyHttpClient client = new MyHttpClient(c);
    	        
    	        HttpPost post = new HttpPost("https://talentwire.me/apis/capability_list");
    	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
    	        nameValuePairs.add(new BasicNameValuePair("session_key", key));
    	        nameValuePairs.add(new BasicNameValuePair("user_id", user_id));

    	        try {
    	        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    			ResponseHandler<String> responseHandler=new BasicResponseHandler();
    			capabilities = client.execute(post, responseHandler);
    			Log.d("TAG","TEST RESULTS FROM HTTPS CAPABILITY LIST IS: "+capabilities);
    			//parseResponse(responseBody);

    			} catch (ClientProtocolException e) {
    				e.printStackTrace();
    			} catch (IOException e) {
    				e.printStackTrace();
    			} catch (ParseException e) {
    				e.printStackTrace();
    			}
   }
   public static String checkInCheckOut(Context c, String status_notes, String is_manual, String checkin_or_checkout, String start_datetime, String end_datetime, String contract_id) {
		SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(c); 
       String key = prefs.getString("staffkey", null);
       Log.d("TAG",key);
       String checkreturn=null;
   	        MyHttpClient client = new MyHttpClient(c);
   	        
   	        HttpPost post = new HttpPost("https://talentwire.me/apis/checkin_checkout");
   	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
   	        nameValuePairs.add(new BasicNameValuePair("session_key", key));
   	        nameValuePairs.add(new BasicNameValuePair("status_notes", status_notes));
   	        nameValuePairs.add(new BasicNameValuePair("is_manual", is_manual));
   	        nameValuePairs.add(new BasicNameValuePair("checkin_or_checkout", checkin_or_checkout));
   	        nameValuePairs.add(new BasicNameValuePair("start_datetime", start_datetime));
   	        nameValuePairs.add(new BasicNameValuePair("end_datetime", end_datetime));
   	        nameValuePairs.add(new BasicNameValuePair("contract_id", contract_id));


   	        try {
   	        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
   			ResponseHandler<String> responseHandler=new BasicResponseHandler();
   			checkreturn = client.execute(post, responseHandler);
   			Log.d("TAG","TEST RESULTS FROM CHECKIN CHEKCOUT IS: "+checkreturn);
   			//parseResponse(responseBody);

   			} catch (ClientProtocolException e) {
   				e.printStackTrace();
   			} catch (IOException e) {
   				e.printStackTrace();
   			} catch (ParseException e) {
   				e.printStackTrace();
   			}
   			return checkreturn;
  }
   
   
   
   private void viewApplicationStatus(String appid, Context c) {
		SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(c); 
       String key = prefs.getString("staffkey", null);
       Log.d("TAG",key);
       
       MyHttpClient client = new MyHttpClient(c);
       HttpPost post = new HttpPost("https://talentwire.me/apis/"+appid+"/view_applciation");
       List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
       nameValuePairs.add(new BasicNameValuePair("session_key", key));
       try {
       post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		ResponseHandler<String> responseHandler=new BasicResponseHandler();
		String responseBody = client.execute(post, responseHandler);
		Log.d("TAG","TEST RESULTS FROM HTTPS VIEW APPLICATION STATUS WITH APP ID: "+appid+" IS: "+responseBody);
		
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}		
   
public static String jobDiscovery(Context c) {
	String discjobs = null;
    Log.d("TAG",staffkey);
    MyHttpClient client = new MyHttpClient(c);
    
    HttpPost post = new HttpPost("https://talentwire.me/apis/job_suggestion");
    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
    nameValuePairs.add(new BasicNameValuePair("session_key", staffkey));

    try {
    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	ResponseHandler<String> responseHandler=new BasicResponseHandler();
	discjobs = client.execute(post, responseHandler);
	//DashboardActivity.discjobs = discjobs;
	Log.d("TAG","TEST RESULTS FROM HTTPS JOB DISCOVERY IS: "+discjobs);
	//parseResponse(discjobs,0);
	} catch (ClientProtocolException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ParseException e) {
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return discjobs;

}	



public static String updateLocation(String mobile_latitude, String mobile_longitude, Context c) {
	String locationreturn = null;
	if (staffkey!=null) {
    Log.d("TAG",staffkey);
    MyHttpClient client = new MyHttpClient(c);
    
    HttpPost post = new HttpPost("https://talentwire.me/apis/update_mob_location");
    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
    nameValuePairs.add(new BasicNameValuePair("session_key", staffkey));
    nameValuePairs.add(new BasicNameValuePair("mobile_latitude", mobile_latitude));
    nameValuePairs.add(new BasicNameValuePair("mobile_longitude", mobile_longitude));

    try {
    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	ResponseHandler<String> responseHandler=new BasicResponseHandler();
	locationreturn = client.execute(post, responseHandler);
	Log.d("TAG","RESULTS FROM UPDATE LOCATION IS : "+locationreturn);
	} catch (ClientProtocolException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ParseException e) {
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return locationreturn;
	}
	return "no key yet";
}	

public static String getAvailability(Context c) {
    String responseBody = "bannana";
    String t = "true";
    try {
	URI url = new URI("https://talentwire.me/apis/"+user_id+"/get_availability");
	
    HttpGet get = new HttpGet(url);
    HttpClient client = new MyHttpClient(c);
    ResponseHandler<String> responseHandler=new BasicResponseHandler();
    responseBody = client.execute(get, responseHandler);
 	
    Log.d("TAG", "RESPONSE STRING FROM HTTPS GET AVAILABILITY: "+responseBody);
	} catch (ClientProtocolException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ParseException e) {
		e.printStackTrace();
    } catch (URISyntaxException e) {
		e.printStackTrace();
	}
    return responseBody;
    
}	
public static String getMessages(Context c) {
    Log.d("TAG",key);
    		String inbox = null;
	        MyHttpClient client = new MyHttpClient(c);
	        HttpPost post = new HttpPost("https://talentwire.me/apis/inbox");
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("session_key", key));
	        try {
	        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			ResponseHandler<String> responseHandler=new BasicResponseHandler();
			inbox = client.execute(post, responseHandler);
			Log.d("TAG","TEST RESULTS FROM HTTPS INBOX IS: "+inbox);
		
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return inbox;
}
public static String viewContracts(Context c) {
	SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(c); 
	String contracts = null; 
    String key = prefs.getString("staffkey", null);
	       Log.d("TAG",key);
	        MyHttpClient client = new MyHttpClient(c);
	        
	        HttpPost post = new HttpPost("https://talentwire.me/apis/my_job_contracts");
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("session_key", key));
	        try {
	        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			ResponseHandler<String> responseHandler=new BasicResponseHandler();
			contracts = client.execute(post, responseHandler);
			Log.d("TAG","TEST RESULTS FROM HTTPS VIEW CONTRACTS IS: "+contracts);
			
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		return contracts;
}
public static String viewAppliedJobs(Context c) {
	SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(c); 
    String key = prefs.getString("staffkey", null);
    appliedjobs = null;
	       Log.d("TAG",key);
	        MyHttpClient client = new MyHttpClient(c);
	        
	        HttpPost post = new HttpPost("https://talentwire.me/apis/applied_jobs");
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("session_key", key));
	        try {
	        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			ResponseHandler<String> responseHandler=new BasicResponseHandler();
			appliedjobs = client.execute(post, responseHandler);
			Log.d("TAG","TEST RESULTS FROM HTTPS VIEW APPLIED JOBS IS: "+appliedjobs);
			//parseResponse(responseBody);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return appliedjobs;
}		
public static Bitmap getUserPic(){
  		userpic=null;
  	  	URL url1 = null;
  	  	try {
    
        url1 = new URL("http://graph.facebook.com/"+facebook_uid+"/picture?type=large");
  		userpic = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
  		} catch (IOException e) {
  			e.printStackTrace();
  		}
  		return userpic;
}

public static String getInfo(String facebook_key, Context c){
  	Bundle params = new Bundle();
  	Bitmap icon = null;
  	if (facebook_key!=null){
  		Log.d("TAG", facebook_key);
  		params.putString(Facebook.TOKEN, facebook_key);
  		JSONObject json_data = null;
  		SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(c); 
		Editor editor = prefs.edit();
		
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));
		 
		HttpParams paramz = new BasicHttpParams();
		paramz.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
		paramz.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(30));
		paramz.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
		HttpProtocolParams.setVersion(paramz, HttpVersion.HTTP_1_1);
		 
		ClientConnectionManager cm = new SingleClientConnManager(paramz, schemeRegistry);
		DefaultHttpClient client = new DefaultHttpClient(cm, paramz);
     
	    try
	    {
	    	
	        JSONObject response = Util.parseJson(facebook.request("me", params));
	        name = response.getString("name");	        
	        id = response.getString("id");

	    	//user_picture.setScaleType( ScaleType.CENTER_CROP );
	    	//user_picture.setImageBitmap(mIcon1);
	    	 
	        facebook_uid = response.getString("id");
	        email = response.getString("email");
	        firstname = response.getString("first_name");
	        lastname = response.getString("last_name");
	        link = response.getString("link");
	        if (response.has("birthday")==true){
	        birthday = response.getString("birthday");
	        }
	        gender = response.getString("gender");
	        locale = response.getString("locale");
	    	Log.d("TAG",name);
	    	Log.d("TAG",email);
	    	Log.d("TAG",firstname);
	    	Log.d("TAG",lastname);
	    	Log.d("TAG",birthday);
	    	Log.d("TAG",locale);
	    	Log.d("TAG",gender);
	    	Log.d("TAG",facebook_uid);
	        
	   	Log.d("TAG", "RUNNING RAJ's FACEBOOK LOGIN  API");
		
	
		editor.remove("staffkey");
		editor.commit();
		String responseBody = null;
		HttpPost post = new HttpPost("https://talentwire.me/apis/fb_login");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("name", name));
		nameValuePairs.add(new BasicNameValuePair("first_name", firstname));
		nameValuePairs.add(new BasicNameValuePair("last_name", lastname));
		nameValuePairs.add(new BasicNameValuePair("birthday", birthday));
		nameValuePairs.add(new BasicNameValuePair("locale", locale));
		nameValuePairs.add(new BasicNameValuePair("gender", gender));
		nameValuePairs.add(new BasicNameValuePair("facebook_uid", facebook_uid));
		nameValuePairs.add(new BasicNameValuePair("facebook_session_key", facebook_key));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			//HttpClient client = new MyHttpClient(c);
			ResponseHandler<String> responseHandler=new BasicResponseHandler();
			responseBody = client.execute(post, responseHandler);
			Log.d("TAG",responseBody);	

			JSONObject jresult;
			JSONTokener tokener = new JSONTokener(responseBody);
			jresult = new JSONObject(tokener);
			Log.d("TAG", "IN PARSE RESPONSE");

			staffkey = jresult.getString("session_key");
			user_id = jresult.getString("user_id");
	  		
			prefs.edit().putString("facebook_uid", facebook_uid).commit();
	   	prefs.edit().putString("staffkey", staffkey).commit();
	   	prefs.edit().putString("staffuser", user_id).commit();

	       key = prefs.getString("staffkey", null);
	       donelogin = "baanana";
	   	Log.d("TAG", "The StaffitToMe key for your generated session is "+staffkey+" And the userid is "+user_id);
		} catch (ParseException e) {
		e.printStackTrace();
		} catch (IOException e) {
		e.printStackTrace();
		} catch (JSONException e) {
		e.printStackTrace();
	    }
	    catch (FacebookError e)
	    {
	        e.printStackTrace();
	    }
  	}
		return name;
	}



public static String getFriendCount(String facebook_key) throws IOException, FacebookError{
 Bundle params = new Bundle();
 friendnum = null;
 Log.d("TAG", "INSIDE CONNECTION #");
	params.putString(Facebook.TOKEN, facebook_key);
    try
    {
    	  JSONObject response = Util.parseJson(facebook.request("/me/friends", params, "GET")); // Get a friend information from facebook
    	  JSONArray jArray = response.getJSONArray("data");
    	  numfriends = jArray.length();
    	  friendnum = Integer.toString(numfriends);
    	  Log.d("TAG","FRIEND COUNT IS: "+numfriends);
    }
    	  catch (JSONException e)
  	    {
  	        e.printStackTrace();
  	    }		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FacebookError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return friendnum;
    }

public static void setAvailability(String availstatus, Context c) {
	SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(c); 
    String key = prefs.getString("staffkey", null);
    Log.d("TAG",key);
    MyHttpClient client = new MyHttpClient(c);
    
    HttpPost post = new HttpPost("https://talentwire.me/apis/available");
    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
    nameValuePairs.add(new BasicNameValuePair("session_key", key));
    nameValuePairs.add(new BasicNameValuePair("lat","34.4358333"));
    nameValuePairs.add(new BasicNameValuePair("long","-119.8266667"));
    nameValuePairs.add(new BasicNameValuePair("is_available",availstatus));

    try {
    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	ResponseHandler<String> responseHandler=new BasicResponseHandler();
	String responseBody = client.execute(post, responseHandler);
	Log.d("TAG","TEST RESULTS FROM HTTPS AVAILABILITY IS: "+responseBody);
	
	} catch (ClientProtocolException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ParseException e) {
		e.printStackTrace();
	}


}

public static String getMessageDetail(String messageid, Context c) {
	SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(c); 
    String key = prefs.getString("staffkey", null);
    Log.d("TAG",key);
    MyHttpClient client = new MyHttpClient(c);
    String responseBody = null;
    
    HttpPost post = new HttpPost("https://talentwire.me/apis/"+messageid+"/view_message");
    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
    nameValuePairs.add(new BasicNameValuePair("session_key", key));
    try {
    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	ResponseHandler<String> responseHandler=new BasicResponseHandler();
	responseBody = client.execute(post, responseHandler);
	Log.d("TAG","TEST RESULTS FROM HTTPS VIEW MESSAGE DETAILS WITH MESSAGE ID "+messageid+" IS: "+responseBody);
    } catch (ClientProtocolException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ParseException e) {
		e.printStackTrace();
	} 
	return responseBody;
}
	public static Bitmap downloadBitmap(String url) {
	
	Bitmap bip=null;
  	URL url1 = null;
  	try {
		url1 = new URL(url);
	bip = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return bip;
	}
	public static String getCompanyDetail(String companyid, Context c) {
			String companydetail = null;
		try {
			URI url = new URI("https://talentwire.me/apis/"+companyid+"/view_company");
			
		    HttpGet get = new HttpGet(url);
		    HttpClient client = new MyHttpClient(c);
		    ResponseHandler<String> responseHandler=new BasicResponseHandler();
		    companydetail = client.execute(get, responseHandler);
		    Log.d("TAG", "RESPONSE STRING FROM HTTPS VIEW COMPANY IS :"+companydetail);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
		    } catch (URISyntaxException e) {
				e.printStackTrace();
			}
		    return companydetail;
	}	
	
	 public static String search(String terms, String lng, String lat, String rad, Context c) {
	    	SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(c); 
	        String key = prefs.getString("staffkey", null);
	        Log.d("TAG",key);
	        String responseBody = null;
	        String trimmed = terms.trim();
	        String getUrl = ("https://talentwire.me/apis/search/"+trimmed+"?latitude="+lat+"&longitude="+lng+"&distance="+rad);
	        Log.d("TAG","URL USED FOR SEARCHING: "+getUrl);
	        HttpClient client = new MyHttpClient(c);
	        HttpGet get = new HttpGet(getUrl);
         HttpResponse responseGet = null;
			try {
				ResponseHandler<String> responseHandler=new BasicResponseHandler();
				responseBody = client.execute(get, responseHandler);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			return responseBody;
         }
 
public String returnJobs(){
	return appliedjobs;
}
public String returnFriendNum(){
	return friendnum;
}
public String returnProposal(){
	return proposals;
}
public String returnProfInfo(){
	return profdetails;
}
public String returnCapabilities(){
	
	return capabilities;
}
public String returnMessages(){
	return messages;
}
public Bitmap returnProfImage(){
	return userpic;
}
}
