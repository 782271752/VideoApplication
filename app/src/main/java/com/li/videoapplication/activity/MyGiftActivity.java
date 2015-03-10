package com.li.videoapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.li.videoapplication.Adapter.GiftAdapter;
import com.li.videoapplication.R;
import com.li.videoapplication.View.RefreshListView;
import com.li.videoapplication.entity.GiftEntity;
import com.li.videoapplication.utils.DialogUtils;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.ToastUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyGiftActivity extends Activity  implements View.OnClickListener,AdapterView.OnItemClickListener,RefreshListView.IXListViewListener{

    private RefreshListView refreshListView;
    private List<GiftEntity> list;
    private List<GiftEntity> responseList;
    private GiftAdapter adapter;
    private int asyncType=0;
    private int pageId;
    private static final int REFRESH=0;
    private static final int LOADMORE=1;
    private SimpleDateFormat dateFormat = null;
    private ImageButton backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gift);

        initView();
        onRefresh();
    }

    private void initView(){
        dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.UK);
        list=new ArrayList<GiftEntity>();
        responseList=new ArrayList<GiftEntity>();
        adapter=new GiftAdapter(this,list,true);
        refreshListView=(RefreshListView)findViewById(R.id.gift_list);
        refreshListView.setAdapter(adapter);
        refreshListView.setPullLoadEnable(true);
        refreshListView.setXListViewListener(this);
        refreshListView.setPullRefreshEnable(true);
        refreshListView.setOnItemClickListener(this);
        backBtn=(ImageButton)findViewById(R.id.activity_gift_back);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onRefresh() {
        pageId=1;
        asyncType=REFRESH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new GetGiftTask("",pageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new GetGiftTask("",pageId+"").execute();
        }
    }

    @Override
    public void onLoadMore() {
        pageId+=1;
        asyncType=LOADMORE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new GetGiftTask("",pageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new GetGiftTask("",pageId+"").execute();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_gift_back:
                this.finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(MyGiftActivity.this, GiftDetailActivity.class);
        intent.putExtra("gift",(Serializable)list.get(position-1));
        startActivity(intent);
    }

    /**
     * 异步获取
     */
    private class GetGiftTask extends AsyncTask<Void,Void,String> {

        String nickname="";
        String page="";
        public GetGiftTask(String nickname,String page){
            this.page=page;
            this.nickname=nickname;
        }

        @Override
        protected String doInBackground(Void... params) {
            responseList= JsonHelper.getMyPakage(nickname, page);
            if (responseList!=null){
                return "s";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (asyncType == REFRESH){
                if (s.equals("s")) {
                    refreshListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
                    list.clear();
                    if (responseList.size()==0){
                        ToastUtils.showToast(MyGiftActivity.this, "没有找到相关数据");
                    }else{
                        list.addAll(responseList);
                    }
                }else{
                    ToastUtils.showToast(MyGiftActivity.this, "连接服务器失败");
                }
            }else{
                if (s.equals("s")) {
                    if (responseList.size()==0){
                        ToastUtils.showToast(MyGiftActivity.this,"已经加载全部数据");
                    }else{
                        list.addAll(responseList);
                    }

                }else{
                    ToastUtils.showToast(MyGiftActivity.this,"连接服务器失败");
                }
            }
            adapter.notifyDataSetChanged();
            refreshListView.stopRefresh();
            refreshListView.stopLoadMore();
//            DialogUtils.cancelWaitDialog();
        }
    }
}
