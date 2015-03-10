package com.li.videoapplication.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.fmscreenrecorder.floatview.Caller;
import com.fmscreenrecorder.floatview.VideoInfo;
import com.li.videoapplication.Adapter.MissionAdapter;
import com.li.videoapplication.activity.ExApplication;
import com.li.videoapplication.activity.FeedBackContentActivity;
import com.li.videoapplication.activity.MainActivity;
import com.li.videoapplication.activity.MissionMoreActivity;
import com.li.videoapplication.R;
import com.li.videoapplication.View.RefreshListView;
import com.li.videoapplication.activity.PersonalInfoActivity;
import com.li.videoapplication.activity.SearchActivity;
import com.li.videoapplication.activity.UploadActivity;
import com.li.videoapplication.entity.MissionEntity;
import com.li.videoapplication.utils.CompleteTaskUtils;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MissionFragment extends Fragment implements MainActivity.OnRefreshListener,View.OnClickListener,AdapterView.OnItemClickListener,RefreshListView.IXListViewListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1="";
    private String mParam2;

    private View view;
    private RefreshListView refreshListView;
    private List<MissionEntity> list;
    private List<MissionEntity> responseList;
    private MissionAdapter adapter;
    private TextView moreTv;
    private int asyncType=0;
    private int pageId;
    private static final int REFRESH=0;
    private static final int LOADMORE=1;
    private SimpleDateFormat dateFormat = null;
    private VideoInfo videoInfo=new VideoInfo();
    private static boolean isRunning=false;


    public static MissionFragment newInstance(String param1, String param2) {
        MissionFragment fragment = new MissionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public MissionFragment() {
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
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    private void init(){
        dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.UK);
        list=new ArrayList<MissionEntity>();
        responseList=new ArrayList<MissionEntity>();
//        for (int i=0;i<5;i++){
//            MissionEntity entity=new MissionEntity();
//            entity.setTitle("录制第"+i+"步视频");
//            entity.setContent("录制一部超过"+i+"分钟的视频上传");
//            entity.setGift("乐园币"+i+"枚");
//            entity.setImgPath("http://t11.baidu.com/it/u=1670506858,1528222640&fm=58");
//            list.add(entity);
//        }
        adapter=new MissionAdapter(getActivity(),list);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onRefresh();
//        new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//
//                Caller caller = new Caller();  //Caller是jar包中实现的回调接口
//                caller.setOnCallListener(new Caller.OnCallListener()
//                {
//                    @Override
//                    public void onCall(VideoInfo info)
//                    {
//                        videoInfo = info;
//                        //ss = s;
////                        mHandler.sendEmptyMessage(3);
//                        Log.e("info", info.getDisplayName() + "--" + info.getPath() + "--" + info.getTime());
//                        Log.e("ischeck",isCheckOnList+"--"+timeLength+"");
//                        if (isCheckOnList){
//
//                            isCheckOnList=false;
//                            if (info.getTime()-timeLength>0){
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
//                                    new CompleteMissionTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                                }else{
//                                    new CompleteMissionTask().execute();
//                                }
//                            }else{
////                                ToastUtils.showToast(getActivity(),"时间太短");
//                            }
//                        }
//                    }
//                });
//            }
//
//        }).start();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_mission,null);
        refreshListView=(RefreshListView)view.findViewById(R.id.mission_list);
//        View view2=inflater.inflate(R.layout.mission_head, null);
//        view2.setVisibility(View.GONE);
//        moreTv=(TextView)view2.findViewById(R.id.mission_head_more);
//        moreTv.setOnClickListener(this);
//        if (mParam1.equals("")){
//            refreshListView.addHeaderView(view2);
//        }
        ((MainActivity)getActivity()).setOnRefreshListener(this);
        refreshListView.setAdapter(adapter);
        refreshListView.setPullLoadEnable(false);
        refreshListView.setXListViewListener(this);
        refreshListView.setPullRefreshEnable(true);
        refreshListView.setOnItemClickListener(this);
        return view;
    }


    @Override
    public void onRefresh() {
        pageId=1;
        asyncType=REFRESH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new GetMissionTask("",pageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new GetMissionTask("",pageId+"").execute();
        }
    }

    @Override
    public void onLoadMore() {
        pageId+=1;
        asyncType=LOADMORE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new GetMissionTask("",pageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new GetMissionTask("",pageId+"").execute();
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mission_head_more:
//                Intent intent=new Intent(getActivity(), MissionMoreActivity.class);
//                startActivity(intent);
                break;
        }
    }

    /**
     * 任务的时长
     */
    private int timeLength=0;
    /**
     * 是否点击了任务列表
     */
    private boolean isCheckOnList=false;
    /**
     * 任务Id
     */
    private String missionId="";
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
         if (!ExApplication.MEMBER_ID.equals("")){
////             isCheckOnList=true;
//             ExApplication.type=ExApplication.MISSIONFRAGMENTONCLICK;
//             ExApplication.timeLength=list.get(i-1).getTaskTimeLength();
//             ExApplication.missionId=list.get(i-1).getId();
//             Intent intent = new Intent();
//             intent.setClassName(getActivity(), "com.fmscreenrecorder.LoadingActivity");
//             startActivity(intent);





             if(responseList.get(i-1).getId().equals("10")){
                 Intent intent=new Intent(getActivity(),SearchActivity.class);
                 startActivity(intent);
             }else if(responseList.get(i-1).getId().equals("11")){
                 ((MainActivity)getActivity()).setCheck();
             }else if (responseList.get(i-1).getId().equals("12")) {
                 ((MainActivity)getActivity()).setCheck();
             }else if (responseList.get(i-1).getId().equals("13")) {
                 ((MainActivity)getActivity()).setCheck();
             }else if (responseList.get(i-1).getId().equals("14")) {
                 Intent intent=new Intent(getActivity(),UploadActivity.class);
                 startActivity(intent);
             }else if (responseList.get(i-1).getId().equals("15")) {

                 if (!ExApplication.MEMBER_ID.equals("")){
                     CompleteTaskUtils utils;
                     utils=new CompleteTaskUtils(getActivity(),"15");
                     utils.completeMission();
                     onRefresh();
                 }

             }else if (responseList.get(i-1).getId().equals("16")) {

                 Intent intent=new Intent(getActivity(),PersonalInfoActivity.class);
                 startActivity(intent);

             }else if (responseList.get(i-1).getId().equals("17")) {
                 Intent intent=new Intent(getActivity(),FeedBackContentActivity.class);
                 startActivity(intent);
             }


         }else{
             ToastUtils.showToast(getActivity(),"请先登录");
         }
    }

    @Override
    public void refresh() {
        onRefresh();
    }


    private class GetMissionTask extends AsyncTask<Void,Void,String> {

        String nickname="";
        String page="";
        public GetMissionTask(String nickname,String page){
            this.page=page;
            this.nickname=nickname;
        }

        @Override
        protected String doInBackground(Void... params) {
            responseList= JsonHelper.getMissionList(nickname, page);
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
//                        ToastUtils.showToast(getActivity(),"没有找到相关数据");
                        refreshListView.setFooterText(1);
                    }else{
                        refreshListView.setFooterText(0);
                        list.addAll(responseList);
                    }
                }else{
                    ToastUtils.showToast(getActivity(), "连接服务器失败");
                }
            }else{
                if (s.equals("s")) {
                    if (responseList.size()==0){
                        refreshListView.setPullLoadEnable(false);
                        ToastUtils.showToast(getActivity(),"已经加载全部数据");
                    }else{
                        refreshListView.setFooterText(0);
                        list.addAll(responseList);
                    }

                }else{
                    ToastUtils.showToast(getActivity(),"连接服务器失败");
                }
            }
            adapter.notifyDataSetChanged();
            refreshListView.stopRefresh();
            refreshListView.stopLoadMore();
//            DialogUtils.cancelWaitDialog();
        }
    }



}
