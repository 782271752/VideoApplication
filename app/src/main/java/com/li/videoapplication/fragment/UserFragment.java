package com.li.videoapplication.fragment;

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

import com.li.videoapplication.Adapter.UserAdapter;
import com.li.videoapplication.R;
import com.li.videoapplication.View.RefreshListView;
import com.li.videoapplication.entity.UserEntity;
import com.li.videoapplication.utils.DialogUtils;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener,RefreshListView.IXListViewListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private RefreshListView refreshListView;
    private List<UserEntity> list;
    private List<UserEntity> responseList;
    private UserAdapter adapter;
    private int asyncType=0;
    private int pageId;
    private static final int REFRESH=0;
    private static final int LOADMORE=1;
    private SimpleDateFormat dateFormat = null;

    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        Log.e("param1",param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            Log.e("mparam1",mParam1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        init();



    }

    private void init(){
        dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.UK);
        list=new ArrayList<UserEntity>();
        responseList=new ArrayList<UserEntity>();

//        for (int i=0;i<8;i++){
//            UserEntity entity=new UserEntity();
//            entity.setImgPath("http://img5.imgtn.bdimg.com/it/u=1726668589,450500620&fm=11&gp=0.jpg");
//            entity.setTitle("LOL王者归来");
//            entity.setGrace("目前总排名"+i+"位");
//            entity.setIntroduce("该用户共上传198个视频");
//            list.add(entity);
//        }

        adapter=new UserAdapter(getActivity(),list);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_user,null);
        refreshListView=(RefreshListView)view.findViewById(R.id.user_list);
        refreshListView.setAdapter(adapter);
        refreshListView.setPullLoadEnable(true);
        refreshListView.setXListViewListener(this);
        refreshListView.setPullRefreshEnable(true);
        refreshListView.setOnItemClickListener(this);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onRefresh();
    }

    @Override
    public void onRefresh() {

        pageId=1;
        asyncType=REFRESH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new GetUserTask(mParam1,pageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new GetUserTask(mParam1,pageId+"").execute();
        }
    }

    @Override
    public void onLoadMore() {
      
        pageId+=1;
        asyncType=LOADMORE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new GetUserTask(mParam1,pageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new GetUserTask(mParam1,pageId+"").execute();
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    /**
     * 异步获取
     */
    private class GetUserTask extends AsyncTask<Void,Void,String> {

        String nickname="";
        String page="";
        public GetUserTask(String nickname,String page){
            this.page=page;
            this.nickname=nickname;
        }

        @Override
        protected String doInBackground(Void... params) {
            responseList= JsonHelper.getSearchUserList(nickname, page);
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

        }
    }
}
