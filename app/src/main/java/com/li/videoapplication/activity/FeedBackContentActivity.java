package com.li.videoapplication.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.li.videoapplication.R;
import com.li.videoapplication.utils.CompleteTaskUtils;
import com.li.videoapplication.utils.DialogUtils;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.ToastUtils;

public class FeedBackContentActivity extends Activity implements View.OnClickListener{

    private ImageButton backBtn;
    private EditText contentEdt,emailEdt;
    private Button completeBtn,cancelBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_feed_back_content);
        initView();
    }

    private void initView(){
        backBtn=(ImageButton)findViewById(R.id.feedback_content_back);
        backBtn.setOnClickListener(this);

        contentEdt=(EditText)findViewById(R.id.feedback_content_edt);
        emailEdt=(EditText)findViewById(R.id.feedback_email_edt);
        completeBtn=(Button)findViewById(R.id.feedback_content_completeBtn);
        completeBtn.setOnClickListener(this);
        cancelBtn=(Button)findViewById(R.id.feedback_content_cancelBtn);
        cancelBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.feedback_content_back:
                this.finish();
                break;

            case R.id.feedback_content_completeBtn:
                if (TextUtils.isEmpty(contentEdt.getText().toString().trim())){
                    ToastUtils.showToast(this,"请输入反馈内容");
                    return;
                }

                DialogUtils.showWaitDialog(this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                    new FeedBackTask(ExApplication.MEMBER_ID,contentEdt.getText().toString().trim(),
                            emailEdt.getText().toString().trim()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    new FeedBackTask(ExApplication.MEMBER_ID,contentEdt.getText().toString().trim(),
                            emailEdt.getText().toString().trim()).execute();
                }


                break;
            case R.id.feedback_content_cancelBtn:
                finish();
                break;
        }
    }

    /**
     * 异步提交反馈内容
     */
    private class FeedBackTask extends AsyncTask<Void,Void,String>{
        String id="";
        String content="";
        String email="";

        public FeedBackTask(String id,String content,String email){
            this.id=id;
            this.content=content;
            this.email=email;
        }

        @Override
        protected String doInBackground(Void... params) {
            return JsonHelper.getFeekBack(id,content,email);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            DialogUtils.cancelWaitDialog();
            if (s.equals("s")){

                ToastUtils.showToast( FeedBackContentActivity.this,"提交反馈成功");
                contentEdt.setText("");
                emailEdt.setText("");

                CompleteTaskUtils utils;
                utils=new CompleteTaskUtils(FeedBackContentActivity.this,"17");
                utils.completeMission();

            }else{
                ToastUtils.showToast( FeedBackContentActivity.this,"提交反馈失败");
            }

        }
    }
}
