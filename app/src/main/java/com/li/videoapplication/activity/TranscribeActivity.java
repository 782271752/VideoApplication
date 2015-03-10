package com.li.videoapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;

import com.li.videoapplication.Adapter.TranscribeAdapter;
import com.li.videoapplication.R;
import com.li.videoapplication.View.MyListView;
import com.li.videoapplication.View.RefreshListView;
import com.li.videoapplication.entity.TranscribeEntity;

import java.util.ArrayList;
import java.util.List;

public class TranscribeActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener,RefreshListView.IXListViewListener{

    private RefreshListView refreshListView;
    private MyListView recentLv;
    private List<TranscribeEntity> list;
    private List<TranscribeEntity> responseList;
    private TranscribeAdapter adapter;
    private View view;
    private LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_transcribe);
        init();
        initView();
    }

    private void init(){
        list=new ArrayList<TranscribeEntity>();
        for (int i=0;i<5;i++){
            TranscribeEntity entity=new TranscribeEntity();
            entity.setImgPath("http://t11.baidu.com/it/u=1670506858,1528222640&fm=58");
            entity.setTitle("忍者必须死2");
            list.add(entity);
        }
        adapter=new TranscribeAdapter(TranscribeActivity.this,list);
    }

    private  void initView(){

        inflater=LayoutInflater.from(TranscribeActivity.this);
        view=inflater.inflate(R.layout.transcribe_head,null);

        initHeadView(view);

        refreshListView=(RefreshListView)findViewById(R.id.transcribe_list);
        refreshListView.addHeaderView(view);
        refreshListView.setAdapter(adapter);
        refreshListView.setPullLoadEnable(true);
        refreshListView.setXListViewListener(this);
        refreshListView.setPullRefreshEnable(false);
        refreshListView.setOnItemClickListener(this);
    }

    private void initHeadView(View view){
        recentLv=(MyListView)view.findViewById(R.id.transcribe_head_recent);
        recentLv.setAdapter(adapter);
    }
    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


}
