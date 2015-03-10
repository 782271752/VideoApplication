package com.li.videoapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.li.videoapplication.Adapter.GameAdapter;
import com.li.videoapplication.Adapter.VideoAdapter;
import com.li.videoapplication.R;
import com.li.videoapplication.View.RefreshListView;
import com.li.videoapplication.entity.Game;
import com.li.videoapplication.entity.VideoEntity;
import com.li.videoapplication.utils.DialogUtils;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GameActivity extends Activity {

    private List<Game> list;
    private GameAdapter adapter;
    private RefreshListView hotListView;
    private ImageButton back;
    private TextView titleTv;
    private String title="";
    private List<VideoEntity> hotlist;
    private List<VideoEntity> hconnectList;
    private VideoAdapter hotAdapter;
    private int hpageId=1;
    private int hotType =0;
    private static final int HOT_REFRESH =0;
    private static final int HOT_LOADMORE =1;
    private SimpleDateFormat hot_dateFormat = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);
        titleTv=(TextView)findViewById(R.id.game_title);
        back=(ImageButton)findViewById(R.id.setting_back);
        hotListView=(RefreshListView)findViewById(R.id.game_list);
        hotListView.setPullLoadEnable(true);
        hotListView.setPullRefreshEnable(true);
        hot_dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.UK);
        hotlist=new ArrayList<VideoEntity>();
        hconnectList=new ArrayList<VideoEntity>();
        hotAdapter=new VideoAdapter(this,hotlist);
//        hotListView.setPullRefreshEnable(true);
        hotListView.setAdapter(hotAdapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if (this.getIntent().getExtras()!=null){
            title=this.getIntent().getExtras().getString("title");
            titleTv.setText(title);
            DialogUtils.showWaitDialog(GameActivity.this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                new HotVideoAsync(title,"time",hpageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }else{
                new HotVideoAsync(title,"time",hpageId+"").execute();
            }

        }

        hotListView.setXListViewListener(new RefreshListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                DialogUtils.showWaitDialog(GameActivity.this);
                hpageId=1;
                hotType=HOT_REFRESH;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                    new HotVideoAsync(title,"time",hpageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    new HotVideoAsync(title,"time",hpageId+"").execute();
                }
            }

            @Override
            public void onLoadMore() {
                hpageId+=1;
                hotType=HOT_LOADMORE;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                    new HotVideoAsync(title,"time",hpageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    new HotVideoAsync(title,"time",hpageId+"").execute();
                }
            }
        });


//        hotListView.setOnItemClickListener(this);

        hotListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(GameActivity.this, VideoPlayActivity.class);
                intent.putExtra("id", hotlist.get(i - 1).getId());
                Log.d("hotlist.get(i - 1).getId()",hotlist.get(i - 1).getId());
                startActivity(intent);
            }
        });
//        DialogUtils.showWaitDialog(this);
//        new GameTask().execute();

    }

    /**
     * 热视频
     */
    public class HotVideoAsync extends AsyncTask<Void,Void,String>{

        String page="";
        String sort="";
        String key="";
        public HotVideoAsync(String key,String sort,String page){
            this.sort=sort;
            this.key=key;
            this.page=page;
        }

        @Override
        protected String doInBackground(Void... params) {
            hconnectList= JsonHelper.getSearchVideo(key,sort,page);
            if (hotlist!=null){
                return "s";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            DialogUtils.cancelWaitDialog();
            if (hotType == HOT_REFRESH){
                hotListView.setRefreshTime(hot_dateFormat.format(new Date(System.currentTimeMillis())));
                if (s.equals("s")) {
                    if (hconnectList.size()==0){
                        hotListView.setFooterText(1);
                    }else{
                        hotListView.setFooterText(0);
                        hotlist.clear();
                        hotlist.addAll(hconnectList);
                        Log.e("time",hconnectList.get(0).getTime());
                    }

                }else{
                    ToastUtils.showToast(GameActivity.this, "连接服务器失败");
                }
            }else{
                if (s.equals("s")) {
                    if (hconnectList.size()==0){
                        hotListView.setPullLoadEnable(false);
                        ToastUtils.showToast(GameActivity.this,"已经加载全部数据");
                    }else{
                        hotListView.setFooterText(0);
                        hotlist.addAll(hconnectList);
                    }

                }else{
                    ToastUtils.showToast(GameActivity.this,"连接服务器失败");
                }
            }
            hotAdapter.notifyDataSetChanged();
            hotListView.stopRefresh();
            hotListView.stopLoadMore();
            DialogUtils.cancelWaitDialog();
        }
    }

//    private class GameTask extends AsyncTask<Void,Void,String>{
//        @Override
//        protected String doInBackground(Void... voids) {
//            list= JsonHelper.getGameList();
//            if (list!=null){
//                return "s";
//            }
//            return "";
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            DialogUtils.cancelWaitDialog();
//            if (s.equals("s")){
//                adapter=new GameAdapter(GameActivity.this,list);
////                listView.setAdapter(adapter);
//            }
//        }
//    }


}
