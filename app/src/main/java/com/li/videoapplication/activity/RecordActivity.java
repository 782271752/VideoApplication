package com.li.videoapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.li.videoapplication.Adapter.DownloadVideoAdapter;
import com.li.videoapplication.Adapter.RecordAdapter;
import com.li.videoapplication.DB.DBManager;
import com.li.videoapplication.R;
import com.li.videoapplication.entity.DownloadVideo;
import com.li.videoapplication.entity.VideoEntity;
import com.li.videoapplication.utils.RecordCheckUtils;
import com.li.videoapplication.utils.RecordCheckUtils;
import com.li.videoapplication.utils.TextTypeUtils;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends Activity implements View.OnClickListener,RecordAdapter.OnCheckListener{
    private ImageButton backBtn;
    private GridView videoGv;
    private List<VideoEntity> list;
    private RecordAdapter adapter;
    private DBManager dbManager;
//    private TextView delectTv;
    private TextView titleTv;

    private ImageView delectTv;


    private LinearLayout bottomLayout;
    private Button delect,cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_down_load);
        initView();
    }

    private void initView(){
        list=new ArrayList<VideoEntity>();
        dbManager=new DBManager(this);
        list=dbManager.getRecordVideo();
        backBtn=(ImageButton)findViewById(R.id.download_back);
        backBtn.setOnClickListener(this);
        videoGv=(GridView)findViewById(R.id.download_gv);
        adapter=new RecordAdapter(this,list,this);
        videoGv.setAdapter(adapter);

        delectTv=(ImageView)findViewById(R.id.download_delect_img);
//        delectTv.setTypeface(TextTypeUtils.getTypeface(this));
        delectTv.setOnClickListener(this);

        bottomLayout=(LinearLayout)findViewById(R.id.down_load_bottom);
        delect=(Button)findViewById(R.id.download_delect_btn);
        delect.setOnClickListener(this);

        cancel=(Button)findViewById(R.id.download_cancel_btn);
        cancel.setOnClickListener(this);

//        delectTv=(TextView)findViewById(R.id.download_delect);
//        delectTv.setTypeface(TextTypeUtils.getTypeface(this));
//        delectTv.setOnClickListener(this);
        titleTv=(TextView)findViewById(R.id.down_load_title);
        titleTv.setText("观看记录");

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.download_back:
                this.finish();
                break;
            case R.id.download_delect_img:

               RecordCheckUtils.isCheckState=true;
                delectTv.setVisibility(View.GONE);
                bottomLayout.setVisibility(View.VISIBLE);

//                Dialog dialog = new AlertDialog.Builder(DownLoadActivity.this)
//                        .setTitle("温馨提示")
//                        .setMessage("是否删除选中视频")
//                        .setIcon(getResources().getDrawable(R.drawable.icon))
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
////                                dbManager.delectDownloadVideo(list.get(position).getPlayUrl());
////                                list=dbManager.getDownloadVideo();
////                                adapter=new DownloadVideoAdapter(DownLoadActivity.this,list);
////                                videoGv.setAdapter(adapter);
//
//                                for (int j=0;j<RecordCheckUtils.getProductList().size();j++){
//                                    list.remove(RecordCheckUtils.getProductList().get(j));
//                                }
//                                dbManager.delectDownloadVideo(RecordCheckUtils.getProductList());
//
//
//                                adapter.notifyDataSetChanged();
//
//                                RecordCheckUtils.clearAllCollectProduce();
//                            }
//                        })
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        })
//                        .create();
//                dialog.show();
                break;
            case R.id.download_delect_btn:
                for (int j=0;j<RecordCheckUtils.getProductList().size();j++){
                    list.remove(RecordCheckUtils.getProductList().get(j));
                }
                dbManager.delRecordVideo(RecordCheckUtils.getProductList());
                adapter.notifyDataSetChanged();
                RecordCheckUtils.clearAllCollectProduce();
                break;
            case R.id.download_cancel_btn:
                bottomLayout.setVisibility(View.GONE);
                delectTv.setVisibility(View.VISIBLE);
                RecordCheckUtils.clearAllCollectProduce();
                RecordCheckUtils.isCheckState=false;
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onCheck() {
        delectTv.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RecordCheckUtils.isCheckState=false;
        RecordCheckUtils.clearAllCollectProduce();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK){

            if (RecordCheckUtils.getProductList().size()==0&&delectTv.getVisibility()==View.VISIBLE){
                RecordCheckUtils.isCheckState=false;
                RecordCheckUtils.clearAllCollectProduce();
                delectTv.setVisibility(View.GONE);
                return false;
            }else if (delectTv.getVisibility()==View.GONE) {
                return super.onKeyDown(keyCode, event);
            }else{
                Dialog dialog = new AlertDialog.Builder(RecordActivity.this)
                        .setTitle("温馨提示")
                        .setMessage("是否取消选中")
                        .setIcon(getResources().getDrawable(R.drawable.icon))
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dbManager.delectDownloadVideo(list.get(position).getPlayUrl());
//                                list=dbManager.getDownloadVideo();
//                                adapter=new DownloadVideoAdapter(DownLoadActivity.this,list);
//                                videoGv.setAdapter(adapter);
                                RecordCheckUtils.clearAllCollectProduce();
                                RecordCheckUtils.isCheckState=false;
                                adapter.notifyDataSetChanged();
                                delectTv.setVisibility(View.GONE);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                dialog.show();
                return  false;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
