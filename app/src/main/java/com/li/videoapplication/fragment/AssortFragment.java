package com.li.videoapplication.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.GridView;
import android.widget.TextView;

import com.li.videoapplication.Adapter.AdImageAdapter;
import com.li.videoapplication.Adapter.AssortAdapter;
import com.li.videoapplication.Adapter.GameTypeAdapter;
import com.li.videoapplication.activity.AssortActivity;
import com.li.videoapplication.R;
import com.li.videoapplication.activity.GameActivity;
import com.li.videoapplication.activity.NewAssortActivity;
import com.li.videoapplication.entity.Game;
import com.li.videoapplication.entity.GameType;
import com.li.videoapplication.entity.VideoType;
import com.li.videoapplication.utils.DialogUtils;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class AssortFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private List<VideoType> list;
    private AssortAdapter adapter;

    private List<Game> glist;
    private GameTypeAdapter gAdapter;

    private GridView assortGv,gameGv;
    private TextView assortTv,gameTv;


    public static AssortFragment newInstance(String param1, String param2) {
        AssortFragment fragment = new AssortFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public AssortFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        init();
        Log.e("oncreat", "oncreate");
    }

    public void init(){
        list=new ArrayList<VideoType>();
//        for (int i=0;i<5;i++){
//            AssortEntity entity=new AssortEntity();
//            entity.setName("忍者必须死2");
//            entity.setImgPath("http://t11.baidu.com/it/u=1670506858,1528222640&fm=58");
//            list.add(entity);
//        }

        adapter=new AssortAdapter(getActivity(),list);

        glist=new ArrayList<Game>();
        gAdapter=new GameTypeAdapter(getActivity(),glist);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new GetVideoTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new GetVideoTask().execute();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new GetGameTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new GetGameTask().execute();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e("oncreatView", "oncreateview");

        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_assort,null);
        assortGv=(GridView)view.findViewById(R.id.assort_assort_gv);
        assortGv.setAdapter(adapter);
        assortGv.setOnItemClickListener(this);

        assortTv=(TextView)view.findViewById(R.id.assort_assortTv);
        assortTv.setFocusable(true);
        assortTv.setFocusableInTouchMode(true);
        assortTv.requestFocus();
        assortTv.setOnClickListener(this);

        gameGv=(GridView)view.findViewById(R.id.assort_game_gv);
        gameGv.setAdapter(gAdapter);
        gameGv.setOnItemClickListener(this);
        gameGv.clearFocus();

        gameTv=(TextView)view.findViewById(R.id.assort_gameTv);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        assortTv.setFocusable(true);
        assortTv.setFocusableInTouchMode(true);
        assortTv.requestFocus();
        Log.e("onActivityCreated", "onActivityCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        assortTv.setFocusable(true);
        assortTv.setFocusableInTouchMode(true);
        assortTv.requestFocus();
        assortTv.setOnClickListener(this);
        gameGv.clearFocus();
        Log.e("onresume", "onresume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("onpause", "onpause");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.assort_assortTv:
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.assort_assort_gv:
                Intent intent=new Intent(getActivity(), NewAssortActivity.class);
                intent.putExtra("id",list.get(i).getId());
                intent.putExtra("title",list.get(i).getName());
                startActivity(intent);
                break;
            case R.id. assort_game_gv:

                intent=new Intent(getActivity(), GameActivity.class);
                intent.putExtra("title",glist.get(i).getName());
                startActivity(intent);

                break;
        }
    }

    /**
     * 获取视频分类列表
     */
    private class GetVideoTask extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... params) {
            list= JsonHelper.getVideoType();
            if (list!=null){
                return "s";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("")){
                ToastUtils.showToast(getActivity(), "连接服务器失败");
                return;
            }

            if (s.equals("s")){
                adapter=new AssortAdapter(getActivity(),list);
                assortGv.setAdapter(adapter);
            }
        }
    }

    /**
     * 获取游戏分类列表
     */
    private class GetGameTask extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... params) {
            glist= JsonHelper.getGameList();
            if (glist!=null){
                return "s";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("")){
                ToastUtils.showToast(getActivity(), "连接服务器失败");
                return;
            }

            if (s.equals("s")){
                gAdapter=new GameTypeAdapter(getActivity(),glist);
                gameGv.setAdapter(gAdapter);
            }
        }
    }

    private BroadcastReceiver mGattUpdateReceiver=new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();
            System.out.println("action = " + action);
            if ("assort".equals(action)) {
                assortTv.setFocusable(true);
                assortTv.setFocusableInTouchMode(true);
                assortTv.requestFocus();
                gameGv.clearFocus();
            }

        }

    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("assort");
        return intentFilter;
    }
}
