package com.talentwire;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.ImageView;

public class AsyncBitmap extends AsyncTask<String, Integer, Bitmap> implements
		OnCancelListener {
	private final ImageView mImageView;
	private final Context mContext;

	public AsyncBitmap(ImageView imageView, Context context) {
		super();
		mImageView = imageView;
		mContext = context;
	}

	@Override
	protected Bitmap doInBackground(String... parameters) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		publishProgress(5000);
		Bitmap b = StaffTasks.downloadBitmap(parameters[0]);
		publishProgress(10000);
		return b;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// initialize progress bar

	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	
	}

	@Override
	protected void onPostExecute(Bitmap b) {
		mImageView.setImageBitmap(b);
	}

	public void onCancel(DialogInterface arg0) {
		// we UI has received the order to cancel, let's notify the worker
		// thread to cancel NOW !
		this.cancel(true);

	}

}
