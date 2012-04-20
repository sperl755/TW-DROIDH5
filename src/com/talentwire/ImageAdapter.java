package com.talentwire;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {

            R.drawable.sample_0, /*Error here: sample_0 cannot be resolved and is not a field*/
            R.drawable.sample_1,/*Error here: sample_1 cannot be resolved and is not a field*/
            R.drawable.sample_2,/*Error here: sample_2 cannot be resolved and is not a field*/ 
           R.drawable.sample_3,/*Error here: sample_3 cannot be resolved and is not a field*/
            R.drawable.sample_4,/*Error here: sample_4 cannot be resolved and is not a field*/ 
           R.drawable.sample_5/*Error here: sample_5 cannot be resolved and is not a field*/,
            R.drawable.sample_6, 
           /*Error here: sample_6 cannot be resolved and is not a field*/
    };
}