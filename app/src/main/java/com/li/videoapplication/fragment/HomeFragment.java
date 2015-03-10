package com.li.videoapplication.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
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
import android.widget.LinearLayout;

import com.li.videoapplication.Adapter.AdImageAdapter;
import com.li.videoapplication.Adapter.HomeAdapter;
import com.li.videoapplication.R;
import com.li.videoapplication.View.CircleFlowIndicator;
import com.li.videoapplication.View.RefreshListView;
import com.li.videoapplication.View.ViewFlow;
import com.li.videoapplication.activity.ExApplication;
import com.li.videoapplication.entity.Advertisement;
import com.li.videoapplication.entity.UserEntity;
import com.li.videoapplication.entity.VideoEntity;
import com.li.videoapplication.http.DefaultMessageResponse;
import com.li.videoapplication.http.RecommendVideoResponse;
import com.li.videoapplication.http.ResponseBase;
import com.li.videoapplication.http.ServerHelper;
import com.li.videoapplication.utils.DialogUtils;
import com.li.videoapplication.utils.HttpUtils;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.SharePreferenceUtil;
import com.li.videoapplication.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.view.View.OnClickListener;

public class HomeFragment extends Fragment implements OnClickListener,RefreshListView.IXListViewListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private RefreshListView refreshListView;
    private LinearLayout layout;
    private ViewFlow viewFlow;
    private int count=3;
    CircleFlowIndicator indic;
    private int pageId;
    private SimpleDateFormat dateFormat = null;
    private AdImageAdapter imageAdapter;
    private HomeAdapter homeAdapter;
    private List<VideoEntity> homeList;
    private List<VideoEntity> reponseList;
    private List<Advertisement> adList;
    private List<VideoEntity> connecList;
    private Context context;
    private int asyncType=0;
    private static final int REFRESH=0;
    private static final int LOADMORE=1;
    private boolean isFirst=true;


    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public HomeFragment() {
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

        if (SharePreferenceUtil.getPreference(getActivity(),"token").equals("")){
            SharePreferenceUtil.setPreference(getActivity(),"token","71e59191aad5f24ca65d5d2e021466b1");
            SharePreferenceUtil.setPreference(getActivity(),"refresh","04db992f0105f535f29389dfcb617118");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new RefreshTokenTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new RefreshTokenTask().execute();
        }
    }


    private void init(){
//        ExApplication.MEMBER_ID="5";
        context=getActivity();
        dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.UK);
        adList=new ArrayList<Advertisement>();
        homeList=new ArrayList<VideoEntity>();
        reponseList=new ArrayList<VideoEntity>();
        connecList=new ArrayList<VideoEntity>();

        homeAdapter=new HomeAdapter(context,homeList);

//        for (int i=0;i<5;i++){
//            VideoEntity entity=new VideoEntity();
//            entity.setSimg_url("http://a3.qpic.cn/psb?/V11PZZFs2yMkjp/6MeIeDPd2PrxC5MnI9twjplcox0QmI8rU1r8HjLu0XQ!/b/dGzXfMnnEAAA&bo=IgGWAAAAAAADAJA!&rf=viewer_4");
//            entity.setBimg_url("http://b339.photo.store.qq.com/psb?/V11PZZFs2yMkjp/uAAW3fNEUe5.61JMpaCXwwTkBm.c5F1tJurVGMbfMio!/b/dM9vFcrdAAAA&bo=gAJAAQAAAAADB.E!&rf=viewer_4");
//            entity.setTitle("忍者必须死");
//            entity.setAll_content("忍者必须死，忍者必须死，忍者必须死");
//            entity.setPlayUrl("http://video.proc.sina.cn/video_explore/location.php?video_id=135409674&amp;vt=4&amp;vid=135409676&amp;creator_id=1001_7480_0_0&amp;table_id=36885&amp;cid=38814&amp;did=awrnsfu1496070&amp;time=1408607478507&amp;rd=0.3264670302160084");
//            entity.setTime("01:25");
//            imgList.add(entity);
////            homeList.add(entity);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view=inflater.inflate(R.layout.fragment_home,null);
        refreshListView=(RefreshListView)view.findViewById(R.id.home_list);

        View view2=inflater.inflate(R.layout.head, null);

        layout=(LinearLayout)view2.findViewById(R.id.layout);
        viewFlow = (ViewFlow)view2.findViewById(R.id.viewflow);
        indic = (CircleFlowIndicator) view2.findViewById(R.id.viewflowindic);

        viewFlow.setAdapter(imageAdapter);

        viewFlow.setmSideBuffer(4); // 实际图片张数

        viewFlow.setFlowIndicator(indic);
        viewFlow.setTimeSpan(3000);
        viewFlow.setSelection(1*1000);	//设置初始位置
        viewFlow.startAutoFlowTimer();  //启动自动播放

        refreshListView.addHeaderView(view2);
        refreshListView.setAdapter(homeAdapter);

        refreshListView.setPullLoadEnable(true);
        refreshListView.setXListViewListener(this);
        refreshListView.setPullRefreshEnable(true);
        DialogUtils.showWaitDialog(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new AdTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new AdTask().execute();
        }
        onRefresh();

        return view;
    }


    @Override
    public void onRefresh() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new AdTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new AdTask().execute();
        }
        pageId=1;
        asyncType=REFRESH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new RecomVideoAsync(pageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new RecomVideoAsync(pageId+"").execute();
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UserEntity userInfo= SharePreferenceUtil.getUserEntity(getActivity());
        if (userInfo!=null){
            ExApplication.MEMBER_ID=userInfo.getId();
            Log.e("id",ExApplication.MEMBER_ID);
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
//        new RecomVideoAsync(pageId+"").execute();
//        for (int i=0;i<5;i++){
//            VideoEntity entity=new VideoEntity();
//            entity.setSimg_url("http://a3.qpic.cn/psb?/V11PZZFs2yMkjp/6MeIeDPd2PrxC5MnI9twjplcox0QmI8rU1r8HjLu0XQ!/b/dGzXfMnnEAAA&bo=IgGWAAAAAAADAJA!&rf=viewer_4");
//            entity.setTitle("3213131312313");
//            entity.setAll_content(i+"");
////            imgList.add(entity);
//            homeList.add(entity);
//        }
//        homeAdapter.notifyDataSetChanged();
//        refreshListView.stopRefresh();
//        refreshListView.stopLoadMore();
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * 异步获取
     */
    public class RecomVideoAsync extends AsyncTask<Void,Void,String>{

        String page="";
        public RecomVideoAsync(String page){
            this.page=page;
        }

        @Override
        protected String doInBackground(Void... params) {
            connecList= JsonHelper.getVedioList(getActivity(),page);
            if (connecList!=null){
                return "s";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (isFirst){
                DialogUtils.cancelWaitDialog();
                isFirst=false;
            }

            if (asyncType == REFRESH){
                if (s.equals("s")) {
                    refreshListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
                    homeList.clear();
                    homeList.addAll(connecList);
                }else{
                    ToastUtils.showToast(getActivity(),"连接服务器失败");
                }
            }else{
                if (s.equals("s")) {
                    if (connecList.size()==0){
                        ToastUtils.showToast(getActivity(),"已经加载全部数据");
                    }else{
                        homeList.addAll(connecList);
                    }

                }else{
                    ToastUtils.showToast(getActivity(),"连接服务器失败");
                }
            }
            homeAdapter.notifyDataSetChanged();
            refreshListView.stopRefresh();
            refreshListView.stopLoadMore();
        }
    }

    private class AdTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            adList=JsonHelper.getAdvertiseList(getActivity());
            if (adList!=null){
                return "s";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("")){
                ToastUtils.showToast(getActivity(),"连接服务器失败");
                return;
            }

            if (s.equals("s")){
                imageAdapter=new AdImageAdapter(context,adList);
                viewFlow.setAdapter(imageAdapter);
            }
        }
    }

    public class RefreshTokenTask extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected Boolean doInBackground(Void... voids) {

            return JsonHelper.refreshToken(getActivity());
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean){
                Log.e("---------","refresh");
            }
            super.onPostExecute(aBoolean);
        }
    }

}
