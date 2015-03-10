package com.li.videoapplication.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.li.videoapplication.R;
import com.li.videoapplication.entity.UserEntity;
import com.li.videoapplication.utils.CompleteTaskUtils;
import com.li.videoapplication.utils.DialogUtils;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.SharePreferenceUtil;
import com.li.videoapplication.utils.ToastUtils;

public class SetAreaActivity extends Activity {


    private ImageButton backBtn;
    private EditText edt;
    private Button loginBtn;
    private UserEntity user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set_area);
        user= SharePreferenceUtil.getUserEntity(this);
        backBtn=(ImageButton)findViewById(R.id.person_info_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edt=(EditText)findViewById(R.id.login_username_edt);
        edt.setText(this.getIntent().getExtras().getString("area"));
        loginBtn=(Button)findViewById(R.id.login_login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edt.getText().toString())){
                    ToastUtils.showToast(SetAreaActivity.this,"地区不能为空");
                    return;
                }
                DialogUtils.showWaitDialog(SetAreaActivity.this);
                new UpdateInfoTask().execute();
            }
        });



    }

    private class UpdateInfoTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            return JsonHelper.getUpdateInfo(ExApplication.MEMBER_ID,"","","","",edt.getText().toString(),"");
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            DialogUtils.cancelWaitDialog();
            if (s.equals("s")){
                ToastUtils.showToast(SetAreaActivity.this,"修改成功");

                CompleteTaskUtils utils;
                utils=new CompleteTaskUtils(SetAreaActivity.this,"16");
                utils.completeMission();
            }else{
                ToastUtils.showToast(SetAreaActivity.this,"修改失败");
            }
        }

    }
}
