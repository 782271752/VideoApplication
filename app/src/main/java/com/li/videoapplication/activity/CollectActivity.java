package com.li.videoapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.li.videoapplication.Adapter.CollectAdapter;
import com.li.videoapplication.Adapter.HomeAdapter;
import com.li.videoapplication.R;
import com.li.videoapplication.View.RefreshListView;
import com.li.videoapplication.entity.Advertisement;
import com.li.videoapplication.entity.VideoEntity;
import com.li.videoapplication.utils.CollectCheckUtil;
import com.li.videoapplication.utils.DialogUtils;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CollectActivity extends Activity implements RefreshListView.IXListViewListener,View.OnClickListener{

    private int pageId;
    private SimpleDateFormat dateFormat = null;
    private CollectAdapter homeAdapter;
    private List<VideoEntity> homeList;
    private List<VideoEntity> connecList;
    private Context context;
    private int asyncType=0;
    private static final int REFRESH=0;
    private static final int LOADMORE=1;
    private RefreshListView refreshListView;
    private ImageButton backbtn;
    private ImageView delectTv;
    private LinearLayout bottomLayout;
    private Button delect,cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_collect);
        init();
        initView();
    }
    private void init(){
        context=CollectActivity.this;
        dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.UK);
        homeList=new ArrayList<VideoEntity>();
        connecList=new ArrayList<VideoEntity>();

        homeAdapter=new CollectAdapter(context,homeList);
    }
    private void initView(){

        backbtn=(ImageButton)findViewById(R.id.activity_collect_back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        refreshListView=(RefreshListView)findViewById(R.id.collecy_list);
        refreshListView.setAdapter(homeAdapter);

        refreshListView.setPullLoadEnable(true);
        refreshListView.setXListViewListener(this);
        refreshListView.setPullRefreshEnable(true);

        delectTv=(ImageView)findViewById(R.id.download_delect_img);
//        delectTv.setTypeface(TextTypeUtils.getTypeface(this));
        delectTv.setOnClickListener(this);
        bottomLayout=(LinearLayout)findViewById(R.id.down_load_bottom);
        delect=(Button)findViewById(R.id.download_delect_btn);
        delect.setOnClickListener(this);

        cancel=(Button)findViewById(R.id.download_cancel_btn);
        cancel.setOnClickListener(this);

        onRefresh();
    }

    @Override
    public void onRefresh() {
        pageId=1;
        asyncType=REFRESH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new RecomVideoAsync(pageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new RecomVideoAsync(pageId+"").execute();
        }
    }

    @Override
    public void onLoadMore() {
        pageId+=1;
        asyncType=LOADMORE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new RecomVideoAsync(pageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new RecomVideoAsync(pageId+"").execute();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.download_delect_img:

                CollectCheckUtil.isCheck=true;
                delectTv.setVisibility(View.GONE);
                bottomLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.download_delect_btn:

                DialogUtils.showWaitDialog(CollectActivity.this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                    new DelectVideoAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    new DelectVideoAsync().execute();
                }


                break;
            case R.id.download_cancel_btn:
                bottomLayout.setVisibility(View.GONE);
                delectTv.setVisibility(View.VISIBLE);
                CollectCheckUtil.clearAllCollectProduce();
                CollectCheckUtil.isCheck=false;
//                homeAdapter.notifyDataSetChanged();
                onRefresh();
                break;
        }
    }

    /**
     *
     */
    public class DelectVideoAsync extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected Boolean doInBackground(Void... voids) {
            String ids="";
            for (int i=0;i<CollectCheckUtil.getProductList().size();i++){
                ids=CollectCheckUtil.getProductList().get(i).getId()+","+ids;
            }
            return JsonHelper.cancelCollect(ids);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            DialogUtils.cancelWaitDialog();
            if (aBoolean){
                ToastUtils.showToast(CollectActivity.this,"取消收藏成功");
                for (int j=0;j<CollectCheckUtil.getProductList().size();j++){
                    homeList.remove(CollectCheckUtil.getProductList().get(j));
                }
                homeAdapter.notifyDataSetChanged();
                CollectCheckUtil.clearAllCollectProduce();
            }
        }
    }

    /**
     * 异步获取
     */
    public class RecomVideoAsync extends AsyncTask<Void,Void,String> {

        String page="";
        public RecomVideoAsync(String page){
            this.page=page;
        }

        @Override
        protected String doInBackground(Void... params) {
            connecList= JsonHelper.getCollectList(CollectActivity.this,ExApplication.MEMBER_ID,page);
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
                    refreshListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
                    homeList.clear();
                    homeList.addAll(connecList);
                }else{
                    ToastUtils.showToast(CollectActivity.this, "连接服务器失败");
                }
            }else{
                if (s.equals("s")) {
                    if (connecList.size()==0){
                        ToastUtils.showToast(CollectActivity.this,"已经加载全部数据");
                    }else{
                        homeList.addAll(connecList);
                    }

                }else{
                    ToastUtils.showToast(CollectActivity.this,"连接服务器失败");
                }
            }
            homeAdapter.notifyDataSetChanged();
            refreshListView.stopRefresh();
            refreshListView.stopLoadMore();
        }
    }



}
