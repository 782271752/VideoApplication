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
import android.widget.TextView;

import com.li.videoapplication.Adapter.MissionAdapter;
import com.li.videoapplication.activity.MissionMoreActivity;
import com.li.videoapplication.R;
import com.li.videoapplication.View.RefreshListView;
import com.li.videoapplication.entity.MissionEntity;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SerachMissionFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener,RefreshListView.IXListViewListener{
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

    public static SerachMissionFragment newInstance(String param1, String param2) {
        SerachMissionFragment fragment = new SerachMissionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public SerachMissionFragment() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_mission,null);
        refreshListView=(RefreshListView)view.findViewById(R.id.mission_list);
        View view2=inflater.inflate(R.layout.mission_head, null);
        moreTv=(TextView)view2.findViewById(R.id.mission_head_more);
        moreTv.setOnClickListener(this);
        if (mParam1.equals("")){
            refreshListView.addHeaderView(view2);
        }

        refreshListView.setAdapter(adapter);
        refreshListView.setPullLoadEnable(true);
        refreshListView.setXListViewListener(this);
        refreshListView.setPullRefreshEnable(true);
        refreshListView.setOnItemClickListener(this);
        return view;
    }


    @Override
    public void onRefresh() {
//        DialogUtils.showWaitDialog(getActivity());
        pageId=1;
        asyncType=REFRESH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new GetMissionTask(mParam1,pageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new GetMissionTask(mParam1,pageId+"").execute();
        }
    }

    @Override
    public void onLoadMore() {
//        DialogUtils.showWaitDialog(getActivity());
        pageId+=1;
        asyncType=LOADMORE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new GetMissionTask(mParam1,pageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new GetMissionTask(mParam1,pageId+"").execute();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mission_head_more:
                Intent intent=new Intent(getActivity(), MissionMoreActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    /**
     * 异步获取
     */
    private class GetMissionTask extends AsyncTask<Void,Void,String> {

        String nickname="";
        String page="";
        public GetMissionTask(String nickname,String page){
            this.page=page;
            this.nickname=nickname;
        }

        @Override
        protected String doInBackground(Void... params) {
            responseList= JsonHelper.getSearchMissionList(nickname, page);
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
                        ToastUtils.showToast(getActivity(),"已经加载全部数据");
                        refreshListView.setPullLoadEnable(false);
                    }else{
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
