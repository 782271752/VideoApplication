package com.li.videoapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.li.videoapplication.R;

public class FeedBackNameActivity extends Activity implements View.OnClickListener{

    private ImageButton backBtn;
    private Button completeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_feed_back_name);
        initView();
    }

    private void initView(){
        backBtn=(ImageButton)findViewById(R.id.feedback_name_back);
        backBtn.setOnClickListener(this);

        completeBtn=(Button)findViewById(R.id.feedback_name_completeBtn);
        completeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.feedback_name_back:
                this.finish();
                break;
            case R.id.feedback_name_completeBtn:
                Intent intent=new Intent(this,FeedBackContentActivity.class);
                startActivity(intent);
                break;
        }
    }
}
