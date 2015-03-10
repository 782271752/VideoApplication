package com.li.videoapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.li.videoapplication.Adapter.MissionAdapter;
import com.li.videoapplication.R;
import com.li.videoapplication.View.RefreshListView;
import com.li.videoapplication.entity.MissionEntity;

import java.util.ArrayList;
import java.util.List;

public class MissionMoreActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener,RefreshListView.IXListViewListener{


    private RefreshListView refreshListView;
    private List<MissionEntity> list;
    private List<MissionEntity> responseList;
    private MissionAdapter adapter;
    private ImageButton backBtn,persionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mission_more);
        init();
        initView();
    }

    private void init(){
        list=new ArrayList<MissionEntity>();
        for (int i=0;i<5;i++){
            MissionEntity entity=new MissionEntity();
            entity.setTitle(i+"");
            entity.setContent(i+"");
            entity.setGift(i+"");
            entity.setImgPath("http://t11.baidu.com/it/u=1670506858,1528222640&fm=58");
            list.add(entity);
        }
        adapter=new MissionAdapter(MissionMoreActivity.this,list);
    }

    private  void initView(){
        refreshListView=(RefreshListView)findViewById(R.id.mission_more_list);
        refreshListView.setAdapter(adapter);
        refreshListView.setPullLoadEnable(true);
        refreshListView.setXListViewListener(this);
        refreshListView.setPullRefreshEnable(true);
        refreshListView.setOnItemClickListener(this);

        backBtn=(ImageButton)findViewById(R.id.mission_more_back);
        backBtn.setOnClickListener(this);
        persionBtn=(ImageButton)findViewById(R.id.mission_more_persion);
        persionBtn.setOnClickListener(this);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mission_more_back:
                this.finish();
                break;
            case R.id.mission_more_persion:
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
