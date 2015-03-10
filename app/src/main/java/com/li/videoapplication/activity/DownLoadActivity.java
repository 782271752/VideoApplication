package com.li.videoapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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
import com.li.videoapplication.DB.DBManager;
import com.li.videoapplication.R;
import com.li.videoapplication.entity.DownloadVideo;
import com.li.videoapplication.utils.ImgCheckUtils;
import com.li.videoapplication.utils.TextTypeUtils;

import java.util.ArrayList;
import java.util.List;

public class DownLoadActivity extends Activity implements View.OnClickListener,DownloadVideoAdapter.OnCheckListener{
    private ImageButton backBtn;
    private GridView videoGv;
    private List<DownloadVideo> list;
    private DownloadVideoAdapter adapter;
    private DBManager dbManager;
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
        list=new ArrayList<DownloadVideo>();
        dbManager=new DBManager(this);
        list=dbManager.getDownloadVideo();
        backBtn=(ImageButton)findViewById(R.id.download_back);
        backBtn.setOnClickListener(this);
        videoGv=(GridView)findViewById(R.id.download_gv);
        adapter=new DownloadVideoAdapter(this,list,this);
        videoGv.setAdapter(adapter);
        delectTv=(ImageView)findViewById(R.id.download_delect_img);
//        delectTv.setTypeface(TextTypeUtils.getTypeface(this));
        delectTv.setOnClickListener(this);

        bottomLayout=(LinearLayout)findViewById(R.id.down_load_bottom);
        delect=(Button)findViewById(R.id.download_delect_btn);
        delect.setOnClickListener(this);

        cancel=(Button)findViewById(R.id.download_cancel_btn);
        cancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.download_back:
                this.finish();
                break;
            case R.id.download_delect_img:

                ImgCheckUtils.isCheckState=true;
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
//                                for (int j=0;j<ImgCheckUtils.getProductList().size();j++){
//                                    list.remove(ImgCheckUtils.getProductList().get(j));
//                                }
//                                dbManager.delectDownloadVideo(ImgCheckUtils.getProductList());
//
//
//                                adapter.notifyDataSetChanged();
//
//                                ImgCheckUtils.clearAllCollectProduce();
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
                for (int j=0;j<ImgCheckUtils.getProductList().size();j++){
                    list.remove(ImgCheckUtils.getProductList().get(j));
                }
                dbManager.delectDownloadVideo(ImgCheckUtils.getProductList());
                adapter.notifyDataSetChanged();
                ImgCheckUtils.clearAllCollectProduce();
                break;
            case R.id.download_cancel_btn:
                bottomLayout.setVisibility(View.GONE);
                delectTv.setVisibility(View.VISIBLE);
                ImgCheckUtils.clearAllCollectProduce();
                ImgCheckUtils.isCheckState=false;
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
        ImgCheckUtils.isCheckState=false;
        ImgCheckUtils.clearAllCollectProduce();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode==KeyEvent.KEYCODE_BACK){
//
//            if (ImgCheckUtils.getProductList().size()==0&&delectTv.getVisibility()==View.VISIBLE){
//                ImgCheckUtils.isCheckState=false;
//                ImgCheckUtils.clearAllCollectProduce();
//                delectTv.setVisibility(View.GONE);
//                return false;
//            }else if (delectTv.getVisibility()==View.GONE) {
//                return super.onKeyDown(keyCode, event);
//            }else{
//                Dialog dialog = new AlertDialog.Builder(DownLoadActivity.this)
//                        .setTitle("温馨提示")
//                        .setMessage("是否取消选中")
//                        .setIcon(getResources().getDrawable(R.drawable.icon))
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
////                                dbManager.delectDownloadVideo(list.get(position).getPlayUrl());
////                                list=dbManager.getDownloadVideo();
////                                adapter=new DownloadVideoAdapter(DownLoadActivity.this,list);
////                                videoGv.setAdapter(adapter);
//                                ImgCheckUtils.clearAllCollectProduce();
//                                ImgCheckUtils.isCheckState=false;
//                                adapter.notifyDataSetChanged();
//                                delectTv.setVisibility(View.GONE);
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
//                return  false;
//            }
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
