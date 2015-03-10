package com.li.videoapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.li.videoapplication.Adapter.DownloadVideoAdapter;
import com.li.videoapplication.Adapter.UploadVideoAdapter;
import com.li.videoapplication.DB.DBManager;
import com.li.videoapplication.R;
import com.li.videoapplication.entity.DownloadVideo;
import com.li.videoapplication.entity.TranscribeVideo;
import com.li.videoapplication.utils.UpdateUtils;
import com.li.videoapplication.utils.UploadUtils;
import com.li.videoapplication.utils.TextTypeUtils;

import java.util.ArrayList;
import java.util.List;

public class TranscribeUploadActivity extends Activity implements View.OnClickListener,UploadVideoAdapter.OnCheckListener{

    private ImageButton backBtn;
    private GridView videoGv;
    private List<TranscribeVideo> list;
    private UploadVideoAdapter adapter;
    private DBManager dbManager;
    private TextView delectTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_transcribe_upload);
        initView();
    }

    private void initView() {
        list = new ArrayList<TranscribeVideo>();
        dbManager = new DBManager(this);
        list = dbManager.getTranscribeVideo();
        backBtn = (ImageButton) findViewById(R.id.download_back);
        backBtn.setOnClickListener(this);
        videoGv = (GridView) findViewById(R.id.upload_gv);
        adapter = new UploadVideoAdapter(this, list,this);
        videoGv.setAdapter(adapter);

        delectTv=(TextView)findViewById(R.id.download_delect);
        delectTv.setTypeface(TextTypeUtils.getTypeface(this));
        delectTv.setOnClickListener(this);

    }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.download_back:
                    this.finish();
                    break;
                case R.id.download_delect:
                    Dialog dialog = new AlertDialog.Builder(TranscribeUploadActivity.this)
                            .setTitle("温馨提示")
                            .setMessage("是否删除选中视频")
                            .setIcon(getResources().getDrawable(R.drawable.icon))
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    for (int j=0;j< UploadUtils.getProductList().size();j++){
                                        list.remove(UploadUtils.getProductList().get(j));
                                    }
                                    dbManager.delectTranscribeView(UploadUtils.getProductList());


                                    adapter.notifyDataSetChanged();

                                    UploadUtils.clearAllCollectProduce();
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
        UploadUtils.isCheckState=false;
        UploadUtils.clearAllCollectProduce();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK){

            if (UploadUtils.getProductList().size()==0&&delectTv.getVisibility()==View.VISIBLE){
                UploadUtils.isCheckState=false;
                UploadUtils.clearAllCollectProduce();
                delectTv.setVisibility(View.GONE);
                return false;
            }else if (delectTv.getVisibility()==View.GONE) {
                return super.onKeyDown(keyCode, event);
            }else{
                Dialog dialog = new AlertDialog.Builder(TranscribeUploadActivity.this)
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
                                UploadUtils.clearAllCollectProduce();
                                UploadUtils.isCheckState=false;
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
