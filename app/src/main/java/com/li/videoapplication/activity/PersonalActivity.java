package com.li.videoapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.li.videoapplication.Adapter.HomeAdapter;
import com.li.videoapplication.DB.DBManager;
import com.li.videoapplication.R;
import com.li.videoapplication.View.CircularImage;
import com.li.videoapplication.View.MyListView;
import com.li.videoapplication.entity.UserEntity;
import com.li.videoapplication.entity.VideoEntity;
import com.li.videoapplication.utils.SharePreferenceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PersonalActivity extends Activity implements View.OnClickListener{

    private CircularImage headImg;
    private MyListView refreshListView;
//    private List<VideoEntity> imgList;
    private List<VideoEntity> homeList;
    private List<VideoEntity> reponseList;
    private Context context;
    private SimpleDateFormat dateFormat = null;
    private HomeAdapter homeAdapter;
    private ImageButton backBtn;
    private ScrollView scrollView;
    private TextView settingTv,downloadTv,collecTv,uploadTv,giftTv;
    private UserEntity user;
    private TextView nicknameTv;
    private ExApplication exApplication;
    private DBManager dbManager;
    private List<VideoEntity> dblist;
    private TextView moreTv;
    private TextView titileTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_personal);
        user= SharePreferenceUtil.getUserEntity(this);




        if (SharePreferenceUtil.getUserEntity(this)==null){
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
            this.finish();
        }else{
            ExApplication.MEMBER_ID=user.getId();
        }
        init();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (SharePreferenceUtil.getUserEntity(this)!=null){

            if (ExApplication.headImg!=null){
                headImg.setImageBitmap(ExApplication.headImg);
            }else{
                exApplication.imageLoader.displayImage(SharePreferenceUtil.getUserEntity(this).getImgPath(), headImg,exApplication.getOptions());
            }
            nicknameTv.setText(SharePreferenceUtil.getUserEntity(this).getTitle());

            homeList.clear();
            dblist=dbManager.getRecordVideo();
            if (dblist.size()>5){
                for (int i=0;i<4;i++){
                    homeList.add(dblist.get(i));
                }
            }else{
                homeList.addAll(dblist);
            }

            homeAdapter=new HomeAdapter(context,homeList);
            refreshListView.setAdapter(homeAdapter);
        }else{
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
            this.finish();
        }

    }

    private void init(){
        context=PersonalActivity.this;
        dbManager=new DBManager(context);
        exApplication=new ExApplication(this);
        dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.UK);
        dblist=new ArrayList<VideoEntity>();
        homeList=new ArrayList<VideoEntity>();
        reponseList=new ArrayList<VideoEntity>();


    }
    private void initView(){
        headImg=(CircularImage)findViewById(R.id.person_head_img);
        headImg.setOnClickListener(this);

        refreshListView=(MyListView)findViewById(R.id.person_list);
        titileTv=(TextView)findViewById(R.id.person_title);

        backBtn=(ImageButton)findViewById(R.id.person_back);
        backBtn.setOnClickListener(this);
        titileTv.setFocusable(true);
        titileTv.setFocusableInTouchMode(true);
        titileTv.requestFocus();
        scrollView=(ScrollView)findViewById(R.id.person_scrollview);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        settingTv=(TextView)findViewById(R.id.personal_setting);
        settingTv.setOnClickListener(this);
        nicknameTv=(TextView)findViewById(R.id.personal_nickname);

        downloadTv=(TextView)findViewById(R.id.personal_download);
        downloadTv.setOnClickListener(this);

        collecTv=(TextView)findViewById(R.id.personal_collect);
        collecTv.setOnClickListener(this);
        uploadTv=(TextView)findViewById(R.id.personal_upload);
        uploadTv.setOnClickListener(this);

        moreTv=(TextView)findViewById(R.id.personal_read_more);
        moreTv.setOnClickListener(this);

        giftTv=(TextView)findViewById(R.id.personal_gift);
        giftTv.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.person_back:
                this.finish();
                break;
            case R.id.person_head_img:
                Intent intent=new Intent(this,PersonalInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.personal_setting:
                intent=new Intent(this,SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.personal_download:
                intent =new Intent(this,DownLoadActivity.class);
                startActivity(intent);
                break;
            case R.id.personal_collect:
                intent=new Intent(this,CollectActivity.class);
                startActivity(intent);
                break;
            case R.id.personal_upload:
                intent=new Intent(this,UploadActivity.class);
                startActivity(intent);
                break;
            case R.id.personal_read_more:
                intent=new Intent(this,RecordActivity.class);
                startActivity(intent);
                break;
            case R.id.personal_gift:
                intent=new Intent(this,MyGiftActivity.class);
                startActivity(intent);
                break;
        }
    }
}
