package com.li.videoapplication.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.li.videoapplication.Adapter.VideoAdapter;
import com.li.videoapplication.R;
import com.li.videoapplication.View.RefreshListView;
import com.li.videoapplication.activity.VideoPlayActivity;
import com.li.videoapplication.entity.SearchVideo;
import com.li.videoapplication.entity.VideoEntity;
import com.li.videoapplication.utils.DialogUtils;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VideoFragment extends Fragment  implements View.OnClickListener,AdapterView.OnItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private Button hotBtn,newBtn;
    private RefreshListView hotListView;
    private RefreshListView newListView;
    private List<VideoEntity> hotlist;
    private List<VideoEntity> newlist;
    private VideoAdapter hotAdapter;
    private VideoAdapter newAdapter;

    private int hpageId;
    private List<VideoEntity> hconnectList;
    private int hotType =0;
    private static final int HOT_REFRESH =0;
    private static final int HOT_LOADMORE =1;
    private SimpleDateFormat hot_dateFormat = null;

    private int npageId;
    private List<VideoEntity> nconnectList;
    private int newType =0;
    private static final int NEW_REFRESH =0;
    private static final int NEW_LOADMORE =1;
    private SimpleDateFormat new_dateFormat = null;



    // TODO: Rename and change types and number of parameters
    public static VideoFragment newInstance(String param1, String param2) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        init();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        onRefresh();

        hpageId=1;
        hotType=HOT_REFRESH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new HotVideoAsync(mParam1,"flower",hpageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new HotVideoAsync(mParam1,"flower",hpageId+"").execute();
        }

        npageId=1;
        newType=NEW_REFRESH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new NewVideoAsync(mParam1,"time",npageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new NewVideoAsync(mParam1,"time",npageId+"").execute();
        }

    }

    private void init(){
        hot_dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.UK);
        new_dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.UK);
        hotlist=new ArrayList<VideoEntity>();
        hconnectList=new ArrayList<VideoEntity>();
        newlist=new ArrayList<VideoEntity>();
//        for (int i=0;i<5;i++){
//            VideoEntity entity=new VideoEntity();
//            entity.setSimg_url("http://img5.imgtn.bdimg.com/it/u=1726668589,450500620&fm=11&gp=0.jpg");
//            entity.setBimg_url("http://www.yyjia.com/attachment/news/20140617/0939511237-1.jpg");
//            entity.setTitle("忍者必须死");
//            entity.setAll_content("忍者必须死，忍者必须死，忍者必须死");
//            entity.setFlower(i+"");
//            entity.setComment(i+"");
//            entity.setTime("01:25");
//            hotlist.add(entity);
//        }
//        for (int i=0;i<5;i++){
//            VideoEntity entity=new VideoEntity();
//            entity.setSimg_url("http://img5.imgtn.bdimg.com/it/u=1726668589,450500620&fm=11&gp=0.jpg");
//            entity.setBimg_url("http://c.hiphotos.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=1f5f57372a34349b600b66d7a8837eab/7e3e6709c93d70cf01b51c20fadcd100baa12b4b.jpg");
//            entity.setTitle("现在战争4");
//            entity.setAll_content("现在战争4，现在战争4，现在战争4");
//            entity.setFlower(i+"");
//            entity.setComment(i+"");
//            entity.setTime("01:25");
//            newlist.add(entity);
//        }
        hotAdapter=new VideoAdapter(getActivity(),hotlist);

        newAdapter=new VideoAdapter(getActivity(),newlist);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_video,null);
        hotBtn=(Button)view.findViewById(R.id.video_hotBtn);
        hotBtn.setOnClickListener(this);
        newBtn=(Button)view.findViewById(R.id.video_newBtn);
        newBtn.setOnClickListener(this);

        hotListView=(RefreshListView)view.findViewById(R.id.video_hot_list);
        hotListView.setAdapter(hotAdapter);
        hotListView.setPullLoadEnable(true);
//        hotListView.setXListViewListener(this);

        hotListView.setXListViewListener(new RefreshListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                DialogUtils.showWaitDialog(getActivity());
                hpageId=1;
                hotType=HOT_REFRESH;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                    new HotVideoAsync(mParam1,"flower",hpageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    new HotVideoAsync(mParam1,"flower",hpageId+"").execute();
                }
            }

            @Override
            public void onLoadMore() {
                hpageId+=1;
                hotType=HOT_LOADMORE;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                    new HotVideoAsync(mParam1,"flower",hpageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    new HotVideoAsync(mParam1,"flower",hpageId+"").execute();
                }
            }
        });

        hotListView.setPullRefreshEnable(true);
        hotListView.setOnItemClickListener(this);
        newListView=(RefreshListView)view.findViewById(R.id.video_newest_list);
        newListView.setAdapter(newAdapter);
        newListView.setPullLoadEnable(true);
//        newListView.setXListViewListener(this);
        newListView.setXListViewListener(new RefreshListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                npageId=1;
                newType=NEW_REFRESH;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                    new NewVideoAsync(mParam1,"time",npageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    new NewVideoAsync(mParam1,"time",npageId+"").execute();
                }
            }

            @Override
            public void onLoadMore() {
                npageId+=1;
                newType=NEW_LOADMORE;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                    new NewVideoAsync(mParam1,"time",npageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    new NewVideoAsync(mParam1,"time",npageId+"").execute();
                }
            }
        });
        newListView.setPullRefreshEnable(true);
        newListView.setOnItemClickListener(this);
        return view;
    }


//    @Override
//    public void onRefresh() {
//        pageId=1;
//        hotType=HOT_REFRESH;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
//            new HotVideoAsync(mParam1,"flower",pageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        }else{
//            new HotVideoAsync.execute();
//        }
//    }
//
//    @Override
//    public void onLoadMore() {
//
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.video_hotBtn:
                hotListView.setVisibility(View.VISIBLE);
                newListView.setVisibility(View.GONE);
                hotBtn.setBackgroundResource(R.drawable.left_pressed);
                hotBtn.setTextColor(getResources().getColor(R.color.white));
                newBtn.setBackgroundResource(R.drawable.right_normal);
                newBtn.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.video_newBtn:
                hotListView.setVisibility(View.GONE);
                newListView.setVisibility(View.VISIBLE);
                hotBtn.setBackgroundResource(R.drawable.left_normal);
                hotBtn.setTextColor(getResources().getColor(R.color.black));
                newBtn.setBackgroundResource(R.drawable.right_pressed);
                newBtn.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
           switch (adapterView.getId()){
               case R.id.video_hot_list:
                   Intent intent=new Intent(getActivity(), VideoPlayActivity.class);
                   intent.putExtra("id",hotlist.get(i-1).getId());
                   getActivity().startActivity(intent);
                   break;
               case R.id.video_newest_list:
                   intent=new Intent(getActivity(), VideoPlayActivity.class);
                   intent.putExtra("id",newlist.get(i-1).getId());
                   getActivity().startActivity(intent);
                   break;
           }
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
            if (hotType == HOT_REFRESH){
                hotListView.setRefreshTime(hot_dateFormat.format(new Date(System.currentTimeMillis())));
                if (s.equals("s")) {
                    if (hconnectList.size()==0){
                        hotListView.setFooterText(1);
                    }else{
                        hotListView.setFooterText(0);
                        hotlist.clear();
                        hotlist.addAll(hconnectList);
                    }


                }else{
                    ToastUtils.showToast(getActivity(), "连接服务器失败");
                }
            }else{
                if (s.equals("s")) {
                    if (hconnectList.size()==0){
                        hotListView.setPullLoadEnable(false);
                        ToastUtils.showToast(getActivity(),"已经加载全部数据");
                    }else{
                        hotListView.setFooterText(0);
                        hotlist.addAll(hconnectList);
                    }

                }else{
                    ToastUtils.showToast(getActivity(),"连接服务器失败");
                }
            }
            hotAdapter.notifyDataSetChanged();
            hotListView.stopRefresh();
            hotListView.stopLoadMore();
            DialogUtils.cancelWaitDialog();
        }
    }



    /**
     * 最新视频
     */
    public class NewVideoAsync extends AsyncTask<Void,Void,String>{

        String page="";
        String sort="";
        String key="";
        public NewVideoAsync(String key,String sort,String page){
            this.sort=sort;
            this.key=key;
            this.page=page;
        }

        @Override
        protected String doInBackground(Void... params) {
            nconnectList= JsonHelper.getSearchVideo(key,sort,page);
            if (newlist!=null){
                return "s";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (newType == NEW_REFRESH){
                if (s.equals("s")) {
                    newListView.setRefreshTime(new_dateFormat.format(new Date(System.currentTimeMillis())));
                    if (nconnectList.size()==0){
                        newListView.setFooterText(1);
                    }else{
                        newListView.setFooterText(0);
                        newlist.clear();
                        newlist.addAll(nconnectList);
                    }


                }else{
                    ToastUtils.showToast(getActivity(), "连接服务器失败");
                }
            }else{
                if (s.equals("s")) {
                    if (nconnectList.size()==0){
                        newListView.setPullLoadEnable(false);
                        ToastUtils.showToast(getActivity(),"已经加载全部数据");
                    }else{
                        newListView.setFooterText(0);
                        newlist.addAll(nconnectList);
                    }

                }else{
                    ToastUtils.showToast(getActivity(),"连接服务器失败");
                }
            }
            newAdapter.notifyDataSetChanged();
            newListView.stopRefresh();
            newListView.stopLoadMore();
        }
    }

}
