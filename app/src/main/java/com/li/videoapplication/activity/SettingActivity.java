package com.li.videoapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.li.videoapplication.Adapter.DownloadVideoAdapter;
import com.li.videoapplication.DB.DBManager;
import com.li.videoapplication.R;
import com.li.videoapplication.entity.Update;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.ToastUtils;
import com.li.videoapplication.utils.UpdateUtils;

import java.util.Set;

public class SettingActivity extends Activity implements View.OnClickListener{

    private TextView infoTv,aboutUsTv,feedbackTv,bussinnessTv,settingTv,updateTv;
    private ImageButton backBtn;
    private DBManager dbManager;
    private ExApplication exApplication;
    private Update update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView(){
        dbManager=new DBManager(this);
        update=new Update();
        exApplication=new ExApplication(this);
        infoTv=(TextView)findViewById(R.id.setting_info);
        infoTv.setOnClickListener(this);

        backBtn=(ImageButton)findViewById(R.id.setting_back);
        backBtn.setOnClickListener(this);

        aboutUsTv=(TextView)findViewById(R.id.setting_about);
        aboutUsTv.setOnClickListener(this);

        feedbackTv=(TextView)findViewById(R.id.setting_feedback);
        feedbackTv.setOnClickListener(this);

        bussinnessTv=(TextView)findViewById(R.id.setting_bussiness);
        bussinnessTv.setOnClickListener(this);

        settingTv=(TextView)findViewById(R.id.setting_setting);
        settingTv.setOnClickListener(this);

        updateTv=(TextView)findViewById(R.id.setting_update);
        updateTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.setting_info:
                Intent intent=new Intent(this,PersonalInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_back:
                this.finish();
                break;
            case R.id.setting_about:
                intent=new Intent(this,AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_feedback:
                intent=new Intent(this,FeedBackContentActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_bussiness:
                intent=new Intent(this,BussinessActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_setting:
                Dialog dialog = new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("温馨提示")
                        .setMessage("是否清除缓存？")
                        .setIcon(getResources().getDrawable(R.drawable.icon))
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbManager.delectAllVideo();
                                exApplication.clear();
                                ToastUtils.showToast(SettingActivity.this,"清除缓存成功");
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create();
                dialog.show();
                break;
            case R.id.setting_update:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new UpdateTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    new UpdateTask().execute();
                }
                break;
        }
    }



    private class UpdateTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            update= JsonHelper.getUpdate();
            if (update!=null){
                return "s";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("")){
                ToastUtils.showToast(SettingActivity.this,"检查更新失败");
                return;
            }
            if (s.equals("s")){
                if (UpdateUtils.getLocalVersionCode(SettingActivity.this)<Integer.parseInt(update.getCode())){
                    UpdateUtils.showVersionDialog(update.getDownload_url(), SettingActivity.this);
                }else{
                    Toast.makeText(SettingActivity.this,"当前已经是最新版本", Toast.LENGTH_LONG).show();
                }
            }
        }
    }




}
