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
import com.li.videoapplication.entity.Business;
import com.li.videoapplication.utils.DialogUtils;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.ToastUtils;

import java.util.List;

public class BussinessActivity extends Activity implements View.OnClickListener{

    private ImageButton backBtn;
    private List<Business> list;
    private TextView phoneTv,emailTv,addressTv,websiteTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bussiness);
        initView();;
        DialogUtils.showWaitDialog(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new BussinTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new BussinTask().execute();
        }
    }

    private void initView(){
        backBtn=(ImageButton)findViewById(R.id.bussiness_back);
        backBtn.setOnClickListener(this);
        phoneTv=(TextView)findViewById(R.id.bussiness_phone);
        emailTv=(TextView)findViewById(R.id.bussiness_email);
        addressTv=(TextView)findViewById(R.id.bussiness_address);
        websiteTv=(TextView)findViewById(R.id.bussiness_website);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bussiness_back:
                this.finish();
                break;
        }
    }

    private class BussinTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            list= JsonHelper.getBusiness();
            if (list!=null){
                return "s";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            DialogUtils.cancelWaitDialog();
            if (s.equals("")){
                ToastUtils.showToast(BussinessActivity.this,"连接不到服务器");
                return;
            }

            emailTv.setText(list.get(0).getEmail());
            phoneTv.setText(list.get(0).getPhone());
            addressTv.setText(list.get(0).getAddress());
            websiteTv.setText(list.get(0).getWebsite());

        }
    }
}
