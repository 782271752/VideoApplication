package com.li.videoapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmscreenrecorder.floatview.Caller;
import com.fmscreenrecorder.floatview.VideoInfo;
import com.li.videoapplication.Adapter.VideoAdapter;
import com.li.videoapplication.R;
import com.li.videoapplication.View.RefreshListView;
import com.li.videoapplication.entity.SearchVideo;
import com.li.videoapplication.entity.VideoEntity;
import com.li.videoapplication.entity.VideoType;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AssortActivity extends Activity implements AdapterView.OnItemClickListener,View.OnClickListener{

    TextView hotTv,newTv,titleTv;
    private ImageView hot_focus_img, new_focus_img;
    private RefreshListView hotListView,newListView;
    private List<VideoEntity> hotlist;
    private List<VideoEntity> newlist;
    private VideoAdapter hotAdapter;
    private VideoAdapter newAdapter;
    private String type_id="",name="";
    private List<VideoEntity> connecList;
    private Context context;
    private int asyncType=0;
    private static final int REFRESH=0;
    private static final int LOADMORE=1;
    private SimpleDateFormat dateFormat = null;
    private int page;
    private ImageButton backBtn,transcribeBtn,personalBtn;
    private VideoInfo videoInfo=new VideoInfo();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_assort);
        init();
        initView();
        if (this.getIntent().getExtras()!=null){
            type_id=this.getIntent().getExtras().getString("id");
            name=this.getIntent().getExtras().getString("title");
            titleTv.setText(name);
        }

        page=1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new RecomVideoAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new RecomVideoAsync().execute();
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
                        videoInfo = info;
                        //ss = s;
//                        mHandler.sendEmptyMessage(3);
                        Log.e("info", info.getDisplayName() + "--" + info.getPath() + "--" + info.getTime());
                    }
                });
            }

        }).start();
    }

    private void init(){
        context=this;
        hotlist=new ArrayList<VideoEntity>();
        dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.UK);
        newlist=new ArrayList<VideoEntity>();
//        for (int i=0;i<5;i++) {
//            SearchVideo entity = new SearchVideo();
////            entity.setSimg_url("http://img5.imgtn.bdimg.com/it/u=1726668589,450500620&fm=11&gp=0.jpg");
//            entity.setFlagPath("http://www.yyjia.com/attachment/news/20140617/0939511237-1.jpg");
//            entity.setName("忍者必须死");
//            entity.setContent("忍者必须死，忍者必须死，忍者必须死");
//            entity.setFlower_count(i + "");
//            entity.setComment_count(i + "");
//            entity.setTime("01:25");
//            hotlist.add(entity);
//        }
//        for (int i=0;i<5;i++){
//            SearchVideo entity = new SearchVideo();
////            entity.setSimg_url("http://img5.imgtn.bdimg.com/it/u=1726668589,450500620&fm=11&gp=0.jpg");
//            entity.setFlagPath("http://www.yyjia.com/attachment/news/20140617/0939511237-1.jpg");
//            entity.setName("忍者必须死");
//            entity.setContent("忍者必须死，忍者必须死，忍者必须死");
//            entity.setFlower_count(i + "");
//            entity.setComment_count(i + "");
//            entity.setTime("01:25");
//            newlist.add(entity);
//        }
        hotAdapter=new VideoAdapter(AssortActivity.this,hotlist);

        newAdapter=new VideoAdapter(AssortActivity.this,newlist);
    }

    private void initView(){
        hotTv=(TextView)findViewById(R.id.activity_assrot_hotBtn);
        hotTv.setOnClickListener(this);
        newTv=(TextView)findViewById(R.id.activity_assort_newBtn);
        newTv.setOnClickListener(this);
        hot_focus_img =(ImageView)findViewById(R.id.activity_assrot_hot_foucs_tab_img);
        new_focus_img =(ImageView)findViewById(R.id.activity_assrot_new_foucs_tab_img);
        hotListView=(RefreshListView)findViewById(R.id.activity_acssort_hot_list);
        hotListView.setAdapter(hotAdapter);
        hotListView.setPullLoadEnable(true);

        backBtn=(ImageButton)findViewById(R.id.activity_assort_back);
        backBtn.setOnClickListener(this);
        transcribeBtn=(ImageButton)findViewById(R.id.activity_assort_transcribe);
        transcribeBtn.setOnClickListener(this);

        personalBtn=(ImageButton)findViewById(R.id.activity_assort_persion);
        personalBtn.setOnClickListener(this);

        titleTv=(TextView)findViewById(R.id.activity_assort_title);

        hotListView.setXListViewListener(new RefreshListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                page=1;
                asyncType = REFRESH;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                    new RecomVideoAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    new RecomVideoAsync().execute();
                }
            }

            @Override
            public void onLoadMore() {
                page+=1;
                asyncType = LOADMORE;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                    new RecomVideoAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    new RecomVideoAsync().execute();
                }
            }
        });
        hotListView.setPullRefreshEnable(true);
        hotListView.setOnItemClickListener(this);
        newListView=(RefreshListView)findViewById(R.id.activity_assort_new_list);
        newListView.setAdapter(newAdapter);
        newListView.setPullLoadEnable(true);
        newListView.setXListViewListener(new RefreshListView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {

            }
        });
        newListView.setPullRefreshEnable(true);
        newListView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_assrot_hotBtn:
                hot_focus_img.setVisibility(View.VISIBLE);
                new_focus_img.setVisibility(View.INVISIBLE);
                hotTv.setTextColor(getResources().getColor(R.color.orange));
                newTv.setTextColor(getResources().getColor(R.color.black));
                newListView.setVisibility(View.GONE);
                hotListView.setVisibility(View.VISIBLE);
                break;
            case R.id.activity_assort_newBtn:
                hot_focus_img.setVisibility(View.INVISIBLE);
                new_focus_img.setVisibility(View.VISIBLE);
                hotTv.setTextColor(getResources().getColor(R.color.black));
                newTv.setTextColor(getResources().getColor(R.color.orange));
                newListView.setVisibility(View.VISIBLE);
                hotListView.setVisibility(View.GONE);
                break;
            case R.id.activity_assort_transcribe:
                Intent intent = new Intent();
                intent.setClassName(context, "com.fmscreenrecorder.LoadingActivity");
                startActivity(intent);
                break;
            case R.id.activity_assort_back:
                this.finish();
                break;
            case R.id.activity_assort_persion:
                intent=new Intent(context,PersonalActivity.class);
                startActivity(intent);
                break;

        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.activity_acssort_hot_list:
                Intent intent=new Intent(context, VideoPlayActivity.class);
                intent.putExtra("id",hotlist.get(i-1).getId());
                context.startActivity(intent);
                break;
            case R.id.activity_assort_new_list:
                break;
        }
    }

    /**
     * 异步获取
     */
    public class RecomVideoAsync extends AsyncTask<Void,Void,String> {



        @Override
        protected String doInBackground(Void... params) {
            connecList= JsonHelper.getVideoTypeList(context,page+"",type_id,"flower");
            if (connecList!=null){
                return "s";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (asyncType == REFRESH){
                if (s.equals("s")) {
                    hotListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
                    hotlist.clear();
                    hotlist.addAll(connecList);
                }else{
                    ToastUtils.showToast(context, "连接服务器失败");
                }
            }else{
                if (s.equals("s")) {
                    if (connecList.size()==0){
                        ToastUtils.showToast(context,"已经加载全部数据");
                    }else{
                        hotlist.addAll(connecList);
                    }

                }else{
                    ToastUtils.showToast(context,"连接服务器失败");
                }
            }
            hotAdapter.notifyDataSetChanged();
            hotListView.stopRefresh();
            hotListView.stopLoadMore();
        }
    }
}
