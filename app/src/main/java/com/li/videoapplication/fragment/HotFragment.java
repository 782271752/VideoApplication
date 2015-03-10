package com.li.videoapplication.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.li.videoapplication.Adapter.VideoAdapter;
import com.li.videoapplication.R;
import com.li.videoapplication.View.RefreshListView;
import com.li.videoapplication.activity.VideoPlayActivity;
import com.li.videoapplication.entity.VideoEntity;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HotFragment extends Fragment implements AdapterView.OnItemClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private List<VideoEntity> hotlist;
    private VideoAdapter hotAdapter;
    private List<VideoEntity> connecList;
    private RefreshListView hotListView;
    private Context context;
    private int asyncType=0;
    private static final int REFRESH=0;
    private static final int LOADMORE=1;
    private SimpleDateFormat dateFormat = null;
    private int page;
    private View view;

    public static HotFragment newInstance(String param1, String param2) {
        HotFragment fragment = new HotFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public HotFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        context=getActivity();
        dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.UK);
        hotlist=new ArrayList<VideoEntity>();
        hotAdapter=new VideoAdapter(getActivity(),hotlist);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_hot,null);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        page=1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new RecomVideoAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new RecomVideoAsync().execute();
        }
    }

    public void initView(View view){
        hotListView=(RefreshListView)view.findViewById(R.id.activity_acssort_hot_list);
        hotListView.setAdapter(hotAdapter);
        hotListView.setPullRefreshEnable(true);
        hotListView.setPullLoadEnable(true);

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
        hotListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(context, VideoPlayActivity.class);
        intent.putExtra("id",hotlist.get(i-1).getId());
        context.startActivity(intent);
    }

    /**
     * 异步获取
     */
    public class RecomVideoAsync extends AsyncTask<Void,Void,String> {



        @Override
        protected String doInBackground(Void... params) {
            connecList= JsonHelper.getVideoTypeList(context, page + "", mParam1,"flower");
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
