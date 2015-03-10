package com.li.videoapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fmscreenrecorder.floatview.Caller;
import com.fmscreenrecorder.floatview.VideoInfo;
import com.li.videoapplication.Adapter.FragmentViewPagerAdapter;
import com.li.videoapplication.R;
import com.li.videoapplication.View.SyncHorizontalScrollView;
import com.li.videoapplication.fragment.AssortFragment;
import com.li.videoapplication.fragment.GiftFragment;
import com.li.videoapplication.fragment.HomeFragment;
import com.li.videoapplication.fragment.HotFragment;
import com.li.videoapplication.fragment.MissionFragment;
import com.li.videoapplication.fragment.TimeFragment;
import com.li.videoapplication.utils.SharePreferenceUtil;
import com.li.videoapplication.utils.Titles;
import com.li.videoapplication.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class NewAssortActivity extends FragmentActivity implements View.OnClickListener{

    /**滑动按钮的外围布局*/
    private RelativeLayout rl_nav;

    /**水平滑动控件*/
    private SyncHorizontalScrollView mHsv;

    /**标题选项*/
    private RadioGroup rg_nav_content;
    /**标题滚动下标*/
    private ImageView iv_nav_indicator;
    private ImageView iv_nav_left;
    private ImageView iv_nav_right;
    private ViewPager mViewPager;

    /**标题滚动下标长度*/
    private int indicatorWidth;
    public static String[] tabTitle= Titles.ASSORTTITLE;
    private LayoutInflater mInflater;
    private int currentIndicatorLeft = 0;
    private int position;
    public List<Fragment> fragments = new ArrayList<Fragment>();

    private Context context;
    private ImageButton persionBtn,searchBtn,videoBtn;
    private VideoInfo videoInfo = new VideoInfo();
    private String type_id="";
    private String name="";
    private TextView titleTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_assort);
        if (this.getIntent().getExtras()!=null){
            type_id=this.getIntent().getExtras().getString("id");
            name=this.getIntent().getExtras().getString("title");
            titleTv=(TextView)findViewById(R.id.new_assort_title);
            titleTv.setText(name);
        }
        initView();
        setListener();
    }

    private void initView(){
        context=NewAssortActivity.this;
        persionBtn=(ImageButton)findViewById(R.id.main_persion);
        persionBtn.setOnClickListener(this);
        searchBtn=(ImageButton)findViewById(R.id.main_search);
        searchBtn.setOnClickListener(this);
        videoBtn=(ImageButton)findViewById(R.id.main_video);
        videoBtn.setOnClickListener(this);
        rl_nav = (RelativeLayout)findViewById(R.id.rl_nav);
        mHsv = (SyncHorizontalScrollView)findViewById(R.id.mHsv);
        rg_nav_content = (RadioGroup)findViewById(R.id.rg_nav_content);
        iv_nav_indicator = (ImageView)findViewById(R.id.iv_nav_indicator);
        mViewPager = (ViewPager)findViewById(R.id.mViewPager);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        indicatorWidth = dm.widthPixels / 2;

        ViewGroup.LayoutParams cursor_Params = iv_nav_indicator.getLayoutParams();
        cursor_Params.width = indicatorWidth;

        iv_nav_indicator.setLayoutParams(cursor_Params);

        mHsv.setSomeParam(rl_nav, iv_nav_left, iv_nav_right, (Activity)context);


        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initNavigationHSV();

        fragments.add(HotFragment.newInstance(type_id,""));
        fragments.add(TimeFragment.newInstance(type_id, ""));

        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mViewPager,fragments);
        adapter.setOnExtraPageChangeListener(new FragmentViewPagerAdapter.OnExtraPageChangeListener(){
            @Override
            public void onExtraPageSelected(int i) {
                System.out.println("Extra...i: " + i);
            }
        });


        new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                Caller caller = new Caller();  //Caller是jar包中实现的回调接口
                caller.setOnCallListener(new Caller.OnCallListener()
                {
                    @Override
                    public void onCall(VideoInfo info)
                    {
                        videoInfo = info;
                        //ss = s;
//                        mHandler.sendEmptyMessage(3);
                        Log.e("info", info.getDisplayName() + "--" + info.getPath() + "--" + info.getTime());
                    }
                });
            }

        }).start();

    }


    private void initNavigationHSV() {

        rg_nav_content.removeAllViews();

        for(int i=0;i<=1;i++){

            RadioButton rb = (RadioButton) mInflater.inflate(R.layout.nav_radiogroup_item, null);
            rb.setId(i);
            if (i==0){
                rb.setTextColor(NewAssortActivity.this.getResources().getColor(R.color.orange));
            }
            rb.setText(tabTitle[i]);
            rb.setTextSize(15);

            rb.setLayoutParams(new ViewGroup.LayoutParams(indicatorWidth,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            rg_nav_content.addView(rb);
        }
    }


    private void setListener() {

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                if(rg_nav_content!=null && rg_nav_content.getChildCount()>position){
                    ((RadioButton)rg_nav_content.getChildAt(position)).performClick();

                    for (int i=0;i<=1;i++){
                        RadioButton tempButton = (RadioButton)findViewById(i);
                        tempButton.setTextColor(NewAssortActivity.this.getResources().getColor(R.color.black));
                    }
                    RadioButton tempButton = (RadioButton)findViewById(position);
                    tempButton.setTextColor(NewAssortActivity.this.getResources().getColor(R.color.orange));
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}

            @Override
            public void onPageScrollStateChanged(int arg0) {}
        });
        ((RadioButton) rg_nav_content.getChildAt(1)).setChecked(true);
        ((RadioButton) rg_nav_content.getChildAt(0)).setChecked(true);
        rg_nav_content.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.e("checkId",checkedId+"");
                if(rg_nav_content.getChildAt(checkedId)!=null){


                    for (int i=0;i<=1;i++){
                        RadioButton tempButton = (RadioButton)findViewById(i);
                        tempButton.setTextColor(NewAssortActivity.this.getResources().getColor(R.color.black));
                    }
                    RadioButton tempButton = (RadioButton)findViewById(checkedId);
                    tempButton.setTextColor(NewAssortActivity.this.getResources().getColor(R.color.orange));

                    TranslateAnimation animation = new TranslateAnimation(
                            currentIndicatorLeft ,
                            ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft(), 0f, 0f);
                    animation.setInterpolator(new LinearInterpolator());
                    animation.setDuration(100);
                    animation.setFillAfter(true);

                    //执行位移动画
                    iv_nav_indicator.startAnimation(animation);

                    mViewPager.setCurrentItem(checkedId);	//ViewPager 跟随一起 切换

                    //记录当前 下标的距最左侧的 距离
                    currentIndicatorLeft = ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft();

                    mHsv.smoothScrollTo(
                            (checkedId > 1 ? ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft() : 0) - ((RadioButton) rg_nav_content.getChildAt(1)).getLeft(), 0);
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_persion:

                if(SharePreferenceUtil.getUserEntity(this)!=null){
                    Intent intent=new Intent(this,PersonalActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(this,LoginActivity.class);
                    startActivity(intent);
                }


                break;
            case R.id.main_search:
//                Intent intent=new Intent(this,SearchActivity.class);
//                startActivity(intent);
                finish();
                break;
            case R.id.main_video:
//                intent=new Intent(this,TranscribeActivity.class);
//                startActivity(intent);
                Intent intent = new Intent();
                intent.setClassName(context, "com.fmscreenrecorder.LoadingActivity");
                startActivity(intent);
                break;
        }
    }


}
