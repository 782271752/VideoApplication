package com.li.videoapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.os.Handler;
import android.app.Activity;

import com.li.videoapplication.R;


public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(intent);
                    WelcomeActivity.this.finish();

            }
        },3000);




    }


}
