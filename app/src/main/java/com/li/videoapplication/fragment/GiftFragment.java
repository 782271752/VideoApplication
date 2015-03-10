package com.li.videoapplication.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.li.videoapplication.Adapter.GiftAdapter;
import com.li.videoapplication.activity.GiftDetailActivity;
import com.li.videoapplication.R;
import com.li.videoapplication.View.RefreshListView;
import com.li.videoapplication.entity.GiftEntity;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.ToastUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GiftFragment extends Fragment  implements View.OnClickListener,AdapterView.OnItemClickListener,RefreshListView.IXListViewListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private RefreshListView refreshListView;
    private List<GiftEntity> list;
    private List<GiftEntity> responseList;
    private GiftAdapter adapter;
    private int asyncType=0;
    private int pageId;
    private static final int REFRESH=0;
    private static final int LOADMORE=1;
    private SimpleDateFormat dateFormat = null;


    // TODO: Rename and change types and number of parameters
    public static GiftFragment newInstance(String param1, String param2) {
        GiftFragment fragment = new GiftFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public GiftFragment() {
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
        list=new ArrayList<GiftEntity>();
        responseList=new ArrayList<GiftEntity>();
        adapter=new GiftAdapter(getActivity(),list,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_gift,null);
        refreshListView=(RefreshListView)view.findViewById(R.id.gift_list);
        refreshListView.setAdapter(adapter);
        refreshListView.setPullLoadEnable(true);
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
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(getActivity(), GiftDetailActivity.class);
        intent.putExtra("gift",(Serializable)list.get(i-1));
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
            responseList= JsonHelper.getPakage(nickname,page);
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
//                        ToastUtils.showToast(getActivity(), "没有找到相关数据");
                    }else{
                        list.addAll(responseList);
                    }
                }else{
                    ToastUtils.showToast(getActivity(), "连接服务器失败");
                }
            }else{
                if (s.equals("s")) {
                    if (responseList.size()==0){
                        ToastUtils.showToast(getActivity(),"已经加载全部数据");
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
