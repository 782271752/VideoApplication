package com.li.videoapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AbsoluteLayout;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.li.videoapplication.R;
import com.li.videoapplication.entity.KeyWord;
import com.li.videoapplication.entity.ViewPosition;
import com.li.videoapplication.utils.DialogUtils;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.SearchKeyManager;
import com.li.videoapplication.utils.ToastUtils;

import java.util.List;

public class SearchActivity extends Activity implements View.OnClickListener{


    private ImageView searchBtn;
    // 盛放关键字的layout的宽和高
    public int width = 0;
    public int height = 0;
    public AbsoluteLayout layout;
    private SearchKeyManager keyManager;
    private String[] searchKey = {};//{ "保卫萝卜2", "天天酷跑", "现代战争5", "急速飞车", "忍者必须死", "打飞机", "愤怒的小鸟"};
    private boolean isViewCreated;
    private ImageButton backBtn,personBtn;
    private EditText edt;
    private List<KeyWord> list;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
        getView();
        DialogUtils.showWaitDialog(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new GetKeyWordTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new GetKeyWordTask().execute();
        }
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!isViewCreated) {
                    height = layout.getMeasuredHeight();
                    width = layout.getMeasuredWidth();
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    ViewPosition vp = new ViewPosition();
                    vp.setStartX(0);
                    vp.setStartY(0);
                    vp.setEndX(width);
                    vp.setEndY(height);
                    layout.setTag(vp);
                    keyManager = new SearchKeyManager(searchKey,
                            SearchActivity.this);
                    keyManager.startAnimation();
                    isViewCreated = true;

                }
            }
        });

    }

    private void getView() {
        searchBtn=(ImageView)findViewById(R.id.search_searchBtn);
        searchBtn.setOnClickListener(this);
        layout = (AbsoluteLayout) findViewById(R.id.absoluteLayout1);
        backBtn=(ImageButton)findViewById(R.id.search_back);
        backBtn.setOnClickListener(this);
        edt=(EditText)findViewById(R.id.search_edt);
        personBtn=(ImageButton)findViewById(R.id.search_persion);
        personBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
//        if(isViewCreated){
//            keyManager = new SearchKeyManager(searchKey,
//                    SearchActivity.this);
//            keyManager.startAnimation();
//        }
        super.onResume();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_searchBtn:

                if (TextUtils.isEmpty(edt.getText().toString().trim())){
                    ToastUtils.showToast(this,"请输入搜索内容");
                    return;
                }

                Intent intent=new Intent(this,SearchResultActivity.class);
                intent.putExtra("key",edt.getText().toString().trim());
                startActivity(intent);
                break;
            case R.id.search_back:
                this.finish();
                break;
            case R.id.search_persion:
                intent=new Intent(this,PersonalActivity.class);
                startActivity(intent);
                break;
        }
    }

//    private Handler mHandler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            ViewTreeObserver vto = layout.getViewTreeObserver();
//            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    if (!isViewCreated) {
//                        height = layout.getMeasuredHeight();
//                        width = layout.getMeasuredWidth();
//                        DisplayMetrics dm = new DisplayMetrics();
//                        getWindowManager().getDefaultDisplay().getMetrics(dm);
//                        ViewPosition vp = new ViewPosition();
//                        vp.setStartX(0);
//                        vp.setStartY(0);
//                        vp.setEndX(width);
//                        vp.setEndY(height);
//                        layout.setTag(vp);
//                        keyManager = new SearchKeyManager(searchKey,
//                                SearchActivity.this);
//                        keyManager.startAnimation();
//                        isViewCreated = true;
//
//                    }
//                }
//            });
//        }
//    };

    /**
     * 或取关键字
     */
    private class GetKeyWordTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            list= JsonHelper.getKeyWord();
            if (list!=null){
                return "s";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            DialogUtils.cancelWaitDialog();
            if (s.equals("")){
                ToastUtils.showToast(SearchActivity.this,"获取不到关键字");
                return;
            }

            if (s.equals("s")){

                if (list.size()>8){
                    searchKey=new String[8];
                    for (int i=0;i<8;i++){
                        searchKey[i]=list.get(i).getWord();
                    }
                }else{
                    searchKey=new String[list.size()];
                    for (int i=0;i<list.size();i++){
                        searchKey[i]=list.get(i).getWord();
                    }
                }
                if(isViewCreated){
                    keyManager = new SearchKeyManager(searchKey,
                            SearchActivity.this);
                    keyManager.startAnimation();
                }
                onResume();

            }
        }
    }
}
