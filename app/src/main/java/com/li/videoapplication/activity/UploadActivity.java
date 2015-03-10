package com.li.videoapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fmscreenrecorder.floatview.Caller;
import com.fmscreenrecorder.floatview.VideoInfo;
import com.li.videoapplication.DB.DBManager;
import com.li.videoapplication.R;
import com.li.videoapplication.Service.UploadService;
import com.li.videoapplication.entity.TranscribeVideo;
import com.li.videoapplication.utils.SharePreferenceUtil;
import com.li.videoapplication.utils.TimeUtils;
import com.li.videoapplication.utils.ToastUtils;

public class UploadActivity extends Activity implements View.OnClickListener{

    private TextView protectTv,upload_video,upload_lu;
    private ImageButton backTv;
    private VideoInfo videoInfo=new VideoInfo();
    private DBManager dbManager;
    private Button uploadBtn,cancelBtn;
    private EditText titleTv,contentTv;
    private CheckBox cb;
    private TextView fileTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_upload);
        initView();
        ExApplication.UpdateState=ExApplication.UPLOADED;
        if (ExApplication.MEMBER_ID.equals("")){
            ToastUtils.showToast(UploadActivity.this,"您当前还没登录，无法执行上传操作");
            return;
        }

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                Caller caller = new Caller();  //Caller是jar包中实现的回调接口
                caller.setOnCallListener(new Caller.OnCallListener()
                {
                    @Override
                    public void onCall(VideoInfo info)
                    {
//                        videoInfo = info;
                        //ss = s;
//                        mHandler.sendEmptyMessage(3);
//                        TranscribeVideo video=new TranscribeVideo();
//                        video.setName(info.getDisplayName());
//                        video.setPath(info.getPath() + "/" + info.getDisplayName());
//                        video.setTime(TimeUtils.secToTime(info.getTime()) + "");
//                        dbManager.addTranscribeVideo(video);
                        Log.e("info", info.getDisplayName() + "--" + info.getPath() + "--" + info.getTime());
                    }
                });
            }

        }).start();
    }

    private void initView(){
        dbManager=new DBManager(this);
        protectTv=(TextView)findViewById(R.id.register_agree_tv);
        protectTv.setOnClickListener(this);
        backTv=(ImageButton)findViewById(R.id.setting_back);
        backTv.setOnClickListener(this);
        upload_video=(TextView)findViewById(R.id.login_getCode);
        upload_video.setOnClickListener(this);

        upload_lu=(TextView)findViewById(R.id.upload_lu);
        upload_lu.setOnClickListener(this);
        uploadBtn=(Button)findViewById(R.id.upload_btn);
        uploadBtn.setOnClickListener(this);

        cancelBtn=(Button)findViewById(R.id.upload_cancel_bt);
        cancelBtn.setOnClickListener(this);
        fileTv=(TextView)findViewById(R.id.upload_filename);
        titleTv=(EditText)findViewById(R.id.upload_introduce_edt);
        contentTv=(EditText)findViewById(R.id.upload_game_edt);
        cb=(CheckBox)findViewById(R.id.register_agree_cb);
        cb.setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ExApplication.localFile!=null&&!ExApplication.localFile.getPath().equals("")){
            fileTv.setVisibility(View.VISIBLE);
            fileTv.setText("已选择文件:"+ExApplication.localFile.getPath());
        }else{
            fileTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_agree_tv:
                Intent intent=new Intent(this,ProtectActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_back:
            case R.id.upload_cancel_bt:
                this.finish();
                break;
            case R.id.upload_lu:
                intent = new Intent();
                intent.setClassName(this, "com.fmscreenrecorder.LoadingActivity");
                startActivity(intent);
                break;
            case R.id.login_getCode:
                intent=new Intent(this,UploadVideoActivity.class);
                startActivity(intent);
                break;
            case R.id.upload_btn:

                if(!cb.isChecked()){
                    return;
                }

                if (ExApplication.MEMBER_ID.equals("")){
                    ToastUtils.showToast(UploadActivity.this,"请先登录");
                    return;
                }

                if (TextUtils.isEmpty(titleTv.getText().toString().trim())){
                    ToastUtils.showToast(UploadActivity.this,"输入视频介绍");
                    return;
                }
                if (TextUtils.isEmpty(contentTv.getText().toString().trim())){
                    ToastUtils.showToast(UploadActivity.this,"输入视频介绍");
                    return;
                }


//                if (SharePreferenceUtil.getPreference(UploadActivity.this,"token").equals("")){
//                    intent=new Intent(this,WebviewActivity.class);
//                    startActivity(intent);
//                    return;
//                }


                if (ExApplication.localFile==null&&ExApplication.UpdateState==ExApplication.UPLOADED){
                    ToastUtils.showToast(UploadActivity.this,"请点击选择视频选择需要上传的视频");
                    return;
                }

                if (ExApplication.localFile==null&&ExApplication.UpdateState==ExApplication.UPLOADING){
                    ToastUtils.showToast(UploadActivity.this,"已正在上传");
                    return;
                }
                ExApplication.localFile.setTitle(titleTv.getText().toString().trim());
                ExApplication.localFile.setContent(contentTv.getText().toString().trim());
                ExApplication.localFileList.add(ExApplication.localFile);


                intent=new Intent(this, UploadService.class);
                intent.putExtra("i","1");
                startService(intent);
                ExApplication.localFile=null;
                break;
        }
    }
}
