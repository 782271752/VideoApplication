package com.li.videoapplication.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.li.videoapplication.Adapter.UploadAdapter;
import com.li.videoapplication.Adapter.UploadVideoAdapter;
import com.li.videoapplication.R;
import com.li.videoapplication.View.RefreshListView;
import com.li.videoapplication.entity.LocalFile;
import com.li.videoapplication.utils.DialogUtils;
import com.li.videoapplication.utils.FileSelectUtils;
import com.li.videoapplication.utils.ToastUtils;
import com.li.videoapplication.utils.VideoUtils;

import java.util.ArrayList;
import java.util.List;

public class UploadVideoActivity extends Activity implements View.OnClickListener{

    private ImageButton backBtn;
    private List<LocalFile> list;
    private UploadAdapter adapter;
    private RefreshListView videoLv;
    private List<Bitmap> bplist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_upload_video);

        initView();
        DialogUtils.showWaitDialog(this);

        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... voids) {
                list= FileSelectUtils.getFileList();
                for (int i=0;i<list.size();i++){
                    bplist.add(VideoUtils.getVideoThumbnail(list.get(i).getPath(),60,60, MediaStore.Video.Thumbnails.MICRO_KIND));
                }
                return "s";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                DialogUtils.cancelWaitDialog();
                if (s.equals("s")){
                    adapter=new UploadAdapter(UploadVideoActivity.this,list,bplist);
                    videoLv.setAdapter(adapter);
                }


            }
        }.execute();
    }

    private void initView(){
        backBtn=(ImageButton)findViewById(R.id.setting_back);
        videoLv=(RefreshListView)findViewById(R.id.upload_video_list);

        videoLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ExApplication.localFile=list.get(i-1);
                ExApplication.videoBitmap=bplist.get(i-1);
                UploadVideoActivity.this.finish();
                ToastUtils.showToast(UploadVideoActivity.this,"选择成功");
            }
        });
        videoLv.setPullRefreshEnable(false);
        videoLv.setPullLoadEnable(false);
        backBtn.setOnClickListener(this);
        bplist=new ArrayList<Bitmap>();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.setting_back:
                finish();
                break;
        }
    }
}
