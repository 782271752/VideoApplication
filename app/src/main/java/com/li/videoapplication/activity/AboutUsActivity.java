package com.li.videoapplication.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.utils.DialogUtils;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.ToastUtils;

public class AboutUsActivity extends Activity implements View.OnClickListener{

    private ImageButton backBtn;
    private TextView contentIv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about_us);
        initView();
        DialogUtils.showWaitDialog(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new AboutUsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new AboutUsTask().execute();
        }

    }

    private void initView(){
        backBtn=(ImageButton)findViewById(R.id.about_us_back);
        backBtn.setOnClickListener(this);
        contentIv=(TextView)findViewById(R.id.about_us_content);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.about_us_back:
                this.finish();
                break;
        }
    }

    /**
     * 获取关于我们的内容
     */
    public class AboutUsTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            return JsonHelper.getAboutUs();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            DialogUtils.cancelWaitDialog();
            if (s.equals("")){
                ToastUtils.showToast(AboutUsActivity.this,"获取数据失败");
                return;
            }

            contentIv.setText("      "+s);

        }
    }
}
