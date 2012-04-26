package com.talentwire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Login extends Activity {
 private ImageButton logo;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        logo = (ImageButton)this.findViewById(R.id.loginbutton);
        logo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent i = new Intent(getApplicationContext(), FacebookActivity.class);
            	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	startActivity(i);
            	finish();
            }
        });
    }
}
