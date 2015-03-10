package com.li.videoapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.cyberplayer.core.BVideoView;
import com.fmscreenrecorder.floatview.Caller;
import com.fmscreenrecorder.floatview.VideoInfo;
import com.li.videoapplication.Adapter.CommentAdapter;
import com.li.videoapplication.DB.DBManager;
import com.li.videoapplication.R;
import com.li.videoapplication.Service.DownLoadService;
import com.li.videoapplication.View.CircularImage;
import com.li.videoapplication.View.RefreshListView;
import com.li.videoapplication.View.VerticalSeekBar;
import com.li.videoapplication.entity.CommentEntity;
import com.li.videoapplication.entity.DownloadVideo;
import com.li.videoapplication.entity.VedioDetail;
import com.li.videoapplication.entity.VideoEntity;
import com.li.videoapplication.utils.CompleteTaskUtils;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.Screen;
import com.li.videoapplication.utils.TimeUtils;
import com.li.videoapplication.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class VideoPlayActivity extends Activity implements View.OnClickListener,
        BVideoView.OnPreparedListener, BVideoView.OnCompletionListener, BVideoView.OnErrorListener,
        View.OnTouchListener,RefreshListView.IXListViewListener{

    //2014-11-2 23:12 新加入的web放大按钮
    private Button webZoomBtn;


    private Button mZoomButton;
    private ImageView playBtn;
    private View view;
    private LayoutInflater inflater;
    private BVideoView mVV = null;
    private SeekBar mProgress = null;
    private TextView mDuration = null;
    private TextView mCurPosition = null;
    private Button mPlayBtn = null;
    private RelativeLayout mController = null;
    private RelativeLayout mHeaderWrapper;
    private String AK = "GKotMsyWiwZO530yUGyiDEF3";
    private String SK = "ZY8T4a3SHYloQql1";
    private final int UI_EVENT_UPDATE_CURRPOSITION = 1;
    private RelativeLayout mViewHolder = null;
    private int mCurrentScape;
    public static final int LANDSCAPE = Configuration.ORIENTATION_LANDSCAPE;
    public static final int PORTRAIT = Configuration.ORIENTATION_PORTRAIT;
    private int mLastPos = 0;
    private VerticalSeekBar soundSb;
    public AudioManager audiomanage;
    private int maxVolume, currentVolume;
    private String url="";
    private String id="";
    private RefreshListView refreshListView;
    private List<CommentEntity> list;
    private List<CommentEntity> connList;
    private CommentAdapter adapter;

    private RelativeLayout titleLayout,commentLayout;
    private ImageButton backBtn,transcribBtn,persionBtn;

    private TextView flowerTv,collectTv,downloadTv;
    private SimpleDateFormat dateFormat = null;
    private int asyncType=0;
    private static final int REFRESH=0;
    private static final int LOADMORE=1;
    private int pageId=0;

    private Button installBtn;

    /**
     * 2014.8.21 16：54
     * 播放横竖屏切换的头部和尾部控件
     */

    //小部件
    private RelativeLayout sTitleLayout;
    private CircularImage sheadImg;
    private TextView sIntroduceTv;
    private TextView sName;
    private RelativeLayout sPlayLayout;

    //大部件
    private RelativeLayout bTitleLayout;
    private CircularImage bheadImg;
    private TextView bName;
    private TextView bIntroduceTv;
    private ImageView shareIv,downloadIv,collectIv,fllowerIv;

    private RelativeLayout bPlayLayout;
    private ImageView bPlayBtn,bUpBtn,bDownBtn;
    private Button bZoomBtn;
    private TextView bCurrentTv,btotalTv;
    private SeekBar bProgress;
    GestureDetector gestureDetector;

    private int count = 0;
    // 第一次点击的时间 long型
    private long firstClick = 0;
    // 最后一次点击的时间
    private long lastClick = 0;


    /**
     * 判断是否刚开始
     */
    private boolean isBeginning=true;

    private VedioDetail detail=new VedioDetail();

    private ImageView shareBtn;

    private Button submitBtn;
    private EditText commentEdt;

    private DBManager dbManager;
    private VideoInfo videoInfo=new VideoInfo();

    private TextView titleTv;
    private ExApplication exApplication;


    private String recId="";

    MediaPlayer player;
    WebView webview;
    String file_url="file:///android_asset/v.html?";
    private ImageView newHeadImg;
    private TextView newIntroduceTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_play);
        dbManager=new DBManager(this);
        exApplication=new ExApplication(this);
////        先判断是否打开： 0为禁止 1为允许
//        int flag =Settings.System.getInt(getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, 0);
////        打开关闭，关闭打开：
//        Settings.System.putInt(getContentResolver(),Settings.System.ACCELEROMETER_ROTATION,1);

        if (VideoPlayActivity.this.getIntent().getExtras()!=null){
            id=VideoPlayActivity.this.getIntent().getStringExtra("id");
            recId=id;
            titleTv=(TextView)findViewById(R.id.video_play_title);
            titleTv.setText(VideoPlayActivity.this.getIntent().getStringExtra("title"));
        }


        initView();
        initPlayer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new GetVedioDetail(id).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new GetVedioDetail(id).execute();
        }

        onRefresh();

//        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Log.i("info", "landscape");
//            titleLayout.setVisibility(View.GONE);
//            commentLayout.setVisibility(View.GONE);
//            setMaxSize(true);
//        }
//        else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Log.i("info", "portrait");
//            titleLayout.setVisibility(View.VISIBLE);
//            commentLayout.setVisibility(View.VISIBLE);
//            setMinSize(true);
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//        }

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

        CompleteTaskUtils utils;
        utils=new CompleteTaskUtils(this,"11");
        utils.completeMission();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("onConfigurationChanged","");
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i("info", "landscape");
            titleLayout.setVisibility(View.GONE);
            commentLayout.setVisibility(View.GONE);
            setMaxSize(true);
        }
        else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i("info", "portrait");
            titleLayout.setVisibility(View.VISIBLE);
            commentLayout.setVisibility(View.VISIBLE);
            setMinSize(true);

        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//            }
//        },2000);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("LastPosition", mLastPos);


    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey("LastPosition")) {

            mLastPos = savedInstanceState.getInt("LastPosition");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        try{
            webview.getClass().getMethod("onResume").invoke(webview,(Object[])null);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (mLastPos != 0) {
            startPlayAnimationFromNet(url,mLastPos);
            Log.e("---------------", mLastPos + "");
        }
        Log.e("+++++++++++++", mLastPos + "");
    }


    private void initView(){
        dbManager=new DBManager(this);
        audiomanage = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.UK);

        inflater=LayoutInflater.from(VideoPlayActivity.this);
        view=inflater.inflate(R.layout.video_play_head,null);
        titleLayout=(RelativeLayout)findViewById(R.id.video_play_title_layout);
        commentLayout=(RelativeLayout)findViewById(R.id.video_play_comment_layout);

        submitBtn=(Button)findViewById(R.id.play_sumbit);
        submitBtn.setOnClickListener(this);
        commentEdt=(EditText)findViewById(R.id.play_edt);



        initHeadView(view);
        backBtn=(ImageButton)findViewById(R.id.video_play_back);
        backBtn.setOnClickListener(this);
        transcribBtn=(ImageButton)findViewById(R.id.video_play_transcribe);
        transcribBtn.setOnClickListener(this);
        persionBtn=(ImageButton)findViewById(R.id.video_play_persion);
        persionBtn.setOnClickListener(this);

        refreshListView=(RefreshListView)findViewById(R.id.play_comment_list);
        refreshListView.setPullLoadEnable(true);
        refreshListView.setXListViewListener(this);
        refreshListView.setPullRefreshEnable(false);
        refreshListView.addHeaderView(view);
        list=new ArrayList<CommentEntity>();
        connList=new ArrayList<CommentEntity>();

//        for (int i=0;i<5;i++){
//            CommentEntity entity=new CommentEntity();
//            entity.setImgPath("http://img5.imgtn.bdimg.com/it/u=1726668589,450500620&fm=11&gp=0.jpg");
//            entity.setName("李卓南");
//            entity.setTime("2014-5-10");
//            entity.setContent("好难玩，好难玩");
//            list.add(entity);
//        }

        adapter=new CommentAdapter(VideoPlayActivity.this,list);
        refreshListView.setAdapter(adapter);
        webview=(WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        webview.setWebChromeClient(new WebChromeClient());

//        commentEdt.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                commentEdt.setFocusable(true);
//                commentEdt.setFocusableInTouchMode(true);
//                commentEdt.requestFocus();
//                InputMethodManager inputManager =
//
//                        (InputMethodManager)commentEdt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                inputManager.showSoftInput(commentEdt, 0);
//
//                return true;
//            }
//        });
    }

    /**
     * 定义头部
     * @param view
     */
    private void initHeadView(View view){
        mZoomButton=(Button)view.findViewById(R.id.zoom_btn);
        webZoomBtn=(Button)view.findViewById(R.id.web_zoom_btn);
        webZoomBtn.setOnClickListener(this);
        playBtn=(ImageView)view.findViewById(R.id.media_play_btn);
        mCurrentScape = PORTRAIT;
        mVV=(BVideoView)view.findViewById(R.id.video_view);
        mProgress=(SeekBar)view.findViewById(R.id.media_progress);
        mDuration=(TextView)view.findViewById(R.id.time_total);
        mCurPosition=(TextView)view.findViewById(R.id.time_current);
        mHeaderWrapper = (RelativeLayout)view.findViewById(R.id.header_wrapper);
        mPlayBtn=(Button)view.findViewById(R.id.play_btn);

        mController=(RelativeLayout)view.findViewById(R.id.controlbar);
        mViewHolder = (RelativeLayout)view.findViewById(R.id.view_holder);
        mViewHolder.setOnTouchListener(this);

        flowerTv=(TextView)view.findViewById(R.id.play_flower_txt);
        flowerTv.setOnClickListener(this);
        collectTv=(TextView)view.findViewById(R.id.play_collect_txt);
        collectTv.setOnClickListener(this);
        downloadTv=(TextView)view.findViewById(R.id.play_download_txt);
        downloadTv.setOnClickListener(this);
        shareBtn=(ImageView)view.findViewById(R.id.play_share_btn);
        shareBtn.setOnClickListener(this);

        installBtn=(Button)view.findViewById(R.id.play_head_install);
        installBtn.setOnClickListener(this);

        //小屏显示的布局
        sTitleLayout=(RelativeLayout)view.findViewById(R.id.control_s_layout);
        sheadImg=(CircularImage)view.findViewById(R.id.video_play_head_simg);
//        ExApplication.imageLoader.displayImage("http://img5.imgtn.bdimg.com/it/u=1726668589,450500620&fm=11&gp=0.jpg", sheadImg, ExApplication.getOptions());

        sIntroduceTv=(TextView)view.findViewById(R.id.video_play_head_sintroduce);
        sName=(TextView)view.findViewById(R.id.video_play_head_sname);
        sPlayLayout=(RelativeLayout)view.findViewById(R.id.video_play_head_play_s_layout);

        newHeadImg=(ImageView)view.findViewById(R.id.video_play_head_nimg);
        newIntroduceTv=(TextView)view.findViewById(R.id.video_play_nintroduce);
        //大部件
        bTitleLayout=(RelativeLayout)view.findViewById(R.id.control_b_layout) ;
        bheadImg=(CircularImage)view.findViewById(R.id.video_play_head_bimg) ;
//        ExApplication.imageLoader.displayImage("http://img5.imgtn.bdimg.com/it/u=1726668589,450500620&fm=11&gp=0.jpg", bheadImg, ExApplication.getOptions());
        bName=(TextView)view.findViewById(R.id.video_play_head_bname);

        bIntroduceTv=(TextView)view.findViewById(R.id.video_play_head_b_introduce);
//        private ImageView shareIv,downloadIv,collectIv,fllowerIv;

        bPlayLayout=(RelativeLayout)view.findViewById(R.id.video_play_head_play_b_layout) ;
        bPlayBtn=(ImageView)view.findViewById(R.id.b_btn_play);
        bPlayBtn.setOnClickListener(this);
        bUpBtn=(ImageView)view.findViewById(R.id.b_btn_up);
        bUpBtn.setOnClickListener(this);
        bDownBtn=(ImageView)view.findViewById(R.id.b_btn_down);
        bDownBtn.setOnClickListener(this);
        bZoomBtn=(Button)view.findViewById(R.id.b_zoom_btn);
        bZoomBtn.setOnClickListener(this);
        bCurrentTv=(TextView)view.findViewById(R.id.b_time_current);
        btotalTv=(TextView)view.findViewById(R.id.b_time_total);
        bProgress=(SeekBar)view.findViewById(R.id.big_media_progress);

        shareIv=(ImageView)view.findViewById(R.id.video_play_head_b_share);
        shareIv.setOnClickListener(this);
        downloadIv=(ImageView)view.findViewById(R.id.video_play_head_b_download);
        downloadIv.setOnClickListener(this);
        collectIv=(ImageView)view.findViewById(R.id.video_play_head_b_star);
        collectIv.setOnClickListener(this);
        fllowerIv=(ImageView)view.findViewById(R.id.video_play_head_b_flower);
        fllowerIv.setOnClickListener(this);

        soundSb=(VerticalSeekBar)view.findViewById(R.id.video_play_head_sound);
        maxVolume = audiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  //获取系统最大音量
        soundSb.setMax(maxVolume);   //拖动条最高值与系统最大声匹配
        currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值
        soundSb.setProgress(currentVolume);
        soundSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() //调音监听器
        {
            public void onProgressChanged(SeekBar arg0,int progress,boolean fromUser)
            {
                audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值
                soundSb.setProgress(currentVolume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub


            }
        });

    }

    private void initPlayer() {
        BVideoView.setAKSK(AK, SK);
        mZoomButton.setOnClickListener(this);
        mVV.setVideoScalingMode(BVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        mVV.showCacheInfo(false);
        mPlayBtn.setOnClickListener(this);
        mVV.setOnPreparedListener(this);
        mVV.setOnCompletionListener(this);
        mVV.setOnErrorListener(this);
        playBtn.setOnClickListener(this);
        registerCallbackForControls();
    }

    private void startPlayAnimationFromNet(final String url,final int start){
        mVV.setVideoPath(url);
        mVV.seekTo(start);
        mVV.start();
        mPlayBtn.setBackgroundResource(R.drawable.pause_btn_style);

        hideControls();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mVV.setLogLevel(5);
    }

    public void hideControls(){
        mController.setVisibility(View.INVISIBLE);
    }
    Handler mUIHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UI_EVENT_UPDATE_CURRPOSITION:
                    int currPosition = mVV.getCurrentPosition();
                    int duration = mVV.getDuration();
                    updateTextViewWithTimeFormat(mCurPosition, currPosition);
                    updateTextViewWithTimeFormat(mDuration, duration);
                    mProgress.setMax(duration);
                    mProgress.setProgress(currPosition);

                    updateTextViewWithTimeFormat(bCurrentTv,currPosition);
                    updateTextViewWithTimeFormat(btotalTv,duration);
                    bProgress.setMax(duration);
                    bProgress.setProgress(currPosition);

                    mUIHandler.sendEmptyMessageDelayed(
                            UI_EVENT_UPDATE_CURRPOSITION, 200);
                    break;
                default:
                    break;
            }
        }
    };
    private void registerCallbackForControls() {

        SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                updateTextViewWithTimeFormat(mCurPosition, progress);
                updateTextViewWithTimeFormat(bCurrentTv,progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int seekPosition = seekBar.getProgress();
                mVV.seekTo(seekPosition);
                mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
            }
        };
        mProgress.setOnSeekBarChangeListener(seekBarChangeListener);
        bProgress.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    private void updateTextViewWithTimeFormat(TextView view, int second) {
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String strTemp = null;
        if (0 != hh) {
            strTemp = String.format(Locale.CHINA, "%02d:%02d:%02d", hh, mm, ss);
        } else {
            strTemp = String.format(Locale.CHINA, "%02d:%02d", mm, ss);
        }
        view.setText(strTemp);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.play_btn:

                if(mVV.isPlaying()){
                    mPlayBtn.setBackgroundResource(R.drawable.play_btn_style);
                    mVV.pause();
                }else{
                    mPlayBtn.setBackgroundResource(R.drawable.pause_btn_style);
                    mVV.resume();
                }
                mController.setVisibility(View.VISIBLE);
                break;

            case R.id.b_btn_up:
                Log.e("currentPosition",mVV.getCurrentPosition()+"");
                if (mVV.getCurrentPosition()+5<mVV.getDuration()){
                    mVV.seekTo(mVV.getCurrentPosition()+5);
                }else{
                    mVV.seekTo(mVV.getDuration());
                }

                break;
            case R.id.b_btn_down:
                if (mVV.getCurrentPosition()-5<=0){
                    mVV.seekTo(0);
                }else{
                    mVV.seekTo(mVV.getDuration()-5);
                }
                break;

            case R.id.media_play_btn:
            case R.id.b_btn_play:
                if (isBeginning){
                    startPlayAnimationFromNet(url,0);

                    mController.setVisibility(View.VISIBLE);
                    playBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_pause));
                    bPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_pause));
                    isBeginning=false;



                }else{
                    if(mVV.isPlaying()){
                        playBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_play));
                        bPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_ig_play));
                        mVV.pause();
                    }else {
                        playBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_pause));
                        bPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_pause));
                        mVV.resume();
                    }
                    mController.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.web_zoom_btn:
                if (mCurrentScape == LANDSCAPE) {
                    titleLayout.setVisibility(View.VISIBLE);
                    commentLayout.setVisibility(View.VISIBLE);
                    setMinSize(false);


                } else {
                    titleLayout.setVisibility(View.GONE);
                    commentLayout.setVisibility(View.GONE);
                    setMaxSize(false);
                    flowerTv.clearFocus();
                    collectTv.clearFocus();
                    downloadTv.clearFocus();
                    installBtn.clearFocus();
                    shareIv.clearFocus();

                }



                break;
            case R.id.b_zoom_btn:
                titleLayout.setVisibility(View.VISIBLE);
                commentLayout.setVisibility(View.VISIBLE);
                setMinSize(false);

                break;
            case R.id.video_play_back:
                stopPlay();
                VideoPlayActivity.this.finish();
                break;
            case R.id.video_play_persion:
                Intent intent=new Intent(VideoPlayActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.video_play_transcribe:
//                intent=new Intent(VideoPlayActivity.this,TranscribeActivity.class);
//                startActivity(intent);
                intent = new Intent();
                intent.setClassName(this, "com.fmscreenrecorder.LoadingActivity");
                startActivity(intent);
                break;

            case R.id.play_share_btn:
            case R.id.video_play_head_b_share:
                showShare();
                break;
            case R.id.play_flower_txt:
            case R.id.video_play_head_b_flower:

                if (ExApplication.MEMBER_ID.equals("")){
                    ToastUtils.showToast(this,"登陆之后才可进行");
                    return;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new FlowerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    new FlowerTask().execute();
                }
                break;

            case R.id.play_collect_txt:

                if (ExApplication.MEMBER_ID.equals("")){
                    ToastUtils.showToast(this,"登陆之后才可进行");
                    return;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new CollectTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    new CollectTask().execute();
                }
                break;

            case R.id.play_sumbit:

                if (canSumbit){
                    if (ExApplication.MEMBER_ID.equals("")){
                        ToastUtils.showToast(this,"请先登录");
                        return;
                    }

                    if (TextUtils.isEmpty(commentEdt.getText().toString().trim())){
                        ToastUtils.showToast(this,"评论不能为空");
                        return ;
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new SubmitTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }else{
                        new SubmitTask().execute();
                    }
                    Message msg = new Message();
                    msg.what = 0;
                    mWaitHandler.sendMessage(msg);
                }else{
                    Toast.makeText(this, "一分钟之内只能评论一次哦", Toast.LENGTH_LONG).show();
                }


                break;

            case R.id.play_download_txt:
            case R.id.video_play_head_b_download:

                if (dbManager.isDownloadVideoExist(id)){
                    ToastUtils.showToast(this,"你已经下载过该视频了");
                    return;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new DownloadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    new DownloadTask().execute();
                }

                if (youkuDetail!=null){
                    String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA))+"";
                    Intent updateIntent = new Intent(
                            VideoPlayActivity.this,
                            DownLoadService.class);
                        updateIntent.putExtra("id",id);
                        updateIntent.putExtra("name",name);
                        updateIntent.putExtra("url", youkuDetail.getUrl());
                        updateIntent.putExtra("img",youkuDetail.getFlagPath());
                        updateIntent.putExtra("title",youkuDetail.getName());
                        startService(updateIntent);
                }



                break;
            case R.id.play_head_install:
                if (detail!=null){
                    Uri uri = Uri.parse(detail.getGameDownloadUrl());
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(it);
                }

                break;
        }
    }

    private static boolean canSumbit=true;
    private Handler mWaitHandler = new Handler() {

        private static final int SECOND = 1000;
        private static final int MINUTE = 60 * SECOND;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.arg1 <= MINUTE) {
                canSumbit=false;
                Message newMsg = obtainMessage();
                newMsg.arg1 = msg.arg1 + SECOND;
                sendMessageDelayed(newMsg, SECOND);
            } else {
                canSumbit=true;
            }
        }
    };

    private void setMaxSize(boolean isAuto) {
//        if (!isAuto){
            if (Build.VERSION.SDK_INT >= 9) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
//        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        if(Build.MODEL.equals("M040")){
//            if(!mSharedPreferences.getBoolean("Meizu",false)){
//                SuperToast superToast = new SuperToast(this);
//                superToast.setDuration(12000);
//                superToast.setText("魅族某些版本固件可能存在兼容性问题，建议您升级到最新固件");
//                superToast.setIcon(SuperToast.Icon.Dark.INFO, SuperToast.IconPosition.LEFT);
//                superToast.show();
//                mSharedPreferences.edit().putBoolean("Meizu",true).commit();
//            }
//        }
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                Screen.getScreenWidth(getWindowManager()),
                Screen.getScreenHeight(getWindowManager()));
        mHeaderWrapper.setLayoutParams(param);
        webview.setLayoutParams(param);
//        mVV.setLayoutParams(param);
//        mZoomButton.setBackgroundResource(R.drawable.btn_samll);
        mCurrentScape = LANDSCAPE;
//        sTitleLayout.setVisibility(View.GONE);
//        sPlayLayout.setVisibility(View.GONE);
//        bTitleLayout.setVisibility(View.VISIBLE);
//        bPlayLayout.setVisibility(View.VISIBLE);
//        soundSb.setVisibility(View.VISIBLE);

    }

    private void setMinSize(boolean isAuto) {
//        if (!isAuto){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                Screen.getScreenWidth(getWindowManager()),getResources()
                .getDimensionPixelSize(R.dimen.player_height));
        mHeaderWrapper.setLayoutParams(param);
        webview.setLayoutParams(param);
//        mVV.setLayoutParams(param);
//        mZoomButton.setBackgroundResource(R.drawable.btn_big);
        mCurrentScape = PORTRAIT;

//        bTitleLayout.setVisibility(View.GONE);
//        bPlayLayout.setVisibility(View.GONE);
//        soundSb.setVisibility(View.GONE);
//        sTitleLayout.setVisibility(View.VISIBLE);
//        sPlayLayout.setVisibility(View.VISIBLE);

    }

    private  Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    titleLayout.setVisibility(View.VISIBLE);
                    commentLayout.setVisibility(View.VISIBLE);
                    mController.setVisibility(View.VISIBLE);
                    setMinSize(false);

//                    startPlayAnimationFromNet(url,0);
                    break;
                case 1:
                    playBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_play));
                    bPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_ig_play));
                    mController.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    @Override
    public void onCompletion() {
        mLastPos = 0;
        if (mCurrentScape == LANDSCAPE) {

            mhandler.sendEmptyMessage(0);
//            VideoPlayActivity.this.runOnUiThread(new Runnable() {
//
//                @Override
//                public void run() {
//
//                }
//            });
        }
        isBeginning=true;
        mhandler.sendEmptyMessage(1);

    }



    private void stopPlay() {
        if (mVV.isPlaying() == false)
            return;
        mLastPos = mVV.getCurrentPosition();
        mVV.stopPlayback();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlay();
        if (webview != null) {
            webview.clearHistory();
            webview.removeAllViewsInLayout();
            webview.clearDisappearingChildren();
            webview.clearFocus();
            webview.clearView();
            webview.destroy();
        }
    }

    @Override
    protected void onPause() {
        Log.e("onpause","onpause");
//        webview.onPause();
        try{
            webview.getClass().getMethod("onPause").invoke(webview,(Object[])null);
        }catch (Exception e){
            e.printStackTrace();
        }


        super.onPause();
//        pausePlay();

    }

    private void pausePlay() {
        if (mVV.isPlaying() == false)
            return;
        mLastPos = mVV.getCurrentPosition();
        mVV.pause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public boolean onError(int i, int i2) {
        return false;
    }

    @Override
    public void onPrepared() {
        mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
//        touchControlBar();
        return false;
    }

    private Timer mt;
    public void touchControlBar(){
        if(mController.getVisibility() == View.INVISIBLE){
            mController.setVisibility(View.VISIBLE);
            mt = new Timer();
            mt.schedule(new TimerTask() {
                @Override
                public void run() {
                    VideoPlayActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mController.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            },6000);
        }else{
            if(mt != null){
                mt.cancel();
            }
            mController.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        pageId=1;

        asyncType=REFRESH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new GetCommentAsync(VideoPlayActivity.this,id+"",pageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new GetCommentAsync(VideoPlayActivity.this,id+"",pageId+"").execute();
        }
    }

    @Override
    public void onLoadMore() {
        pageId+=1;

        asyncType=LOADMORE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new GetCommentAsync(VideoPlayActivity.this,id+"",pageId+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new GetCommentAsync(VideoPlayActivity.this,id+"",pageId+"").execute();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (mCurrentScape==LANDSCAPE){
                titleLayout.setVisibility(View.VISIBLE);
                commentLayout.setVisibility(View.VISIBLE);
                setMinSize(false);
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }

        return super.onKeyDown(keyCode, event);
    }

    public class GetVedioDetail extends AsyncTask<Void,Void,String>{
        String id="";
        public GetVedioDetail(String id){
            this.id=id;
        }
        @Override
        protected String doInBackground(Void... params) {

            detail= JsonHelper.getVedioDetail(VideoPlayActivity.this,id);
            if (detail!=null){
                return "s";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("")){
                ToastUtils.showToast(VideoPlayActivity.this,"获取视频数据失败");
                return;
            }

            url=detail.getUrl();
            handler.sendEmptyMessage(0);

            webview.loadUrl(file_url+"vid="+url);//通过传入的视频ID即可播放视频
            webview.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    view.loadUrl(url);
                    return super.shouldOverrideUrlLoading(view, url);
                }
            });
//            onClick(playBtn);

            if (detail!=null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new GetVideoInfoTask(detail.getUrl()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    new GetVideoInfoTask(detail.getUrl()).execute();
                }

            }
        }
    }


    /**
     * 获取评论列表
     */
    private class GetCommentAsync extends AsyncTask<Void,Void,String>{
        String id="";
        String page="";
        Context context;
        public GetCommentAsync(Context context,String id,String page){
            this.id=id;
            this.page=page;
            this.context=context;
        }

        @Override
        protected String doInBackground(Void... params) {
            connList=JsonHelper.getCommentList(context,id,page);
            Log.e("conList",connList+"1");
            if (connList!=null){
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
                    list.addAll(connList);
                }else{
                    ToastUtils.showToast(VideoPlayActivity.this,"连接服务器失败");
                }
            }else{
                if (s.equals("s")) {
                    if (connList.size()==0){
                        ToastUtils.showToast(VideoPlayActivity.this,"已经加载全部数据");
                    }else{
                        list.addAll(connList);
                    }

                }else{
                    ToastUtils.showToast(VideoPlayActivity.this,"连接服务器失败");
                }
            }
            adapter.notifyDataSetChanged();
            refreshListView.stopRefresh();
            refreshListView.stopLoadMore();
        }
    }

    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    sName.setText("本视频由"+detail.getUserName()+"上传");
                    sIntroduceTv.setText(detail.getContent());
                    flowerTv.setText(detail.getFlower_count());
                    collectTv.setText(detail.getCollection_count());
                    downloadTv.setText(detail.getDownload_count());
                    exApplication.imageLoader.displayImage(detail.getAvatar(),sheadImg,exApplication.getOptions());
                    bName.setText(detail.getUserName());
                    bIntroduceTv.setText(detail.getContent());
                    exApplication.imageLoader.displayImage(detail.getAvatar(),bheadImg,exApplication.getOptions());

                    newIntroduceTv.setText("本视频由"+detail.getUserName()+"上传");
                    exApplication.imageLoader.displayImage(detail.getAvatar(),newHeadImg,exApplication.getOptions());

                    break;
            }
        }
    };


    private void showShare() {

        if (detail!=null&&youkuDetail!=null){
            ShareSDK.initSDK(this);
            OnekeyShare oks = new OnekeyShare();
            //关闭sso授权
            oks.disableSSOWhenAuthorize();

            // 分享时Notification的图标和文字
            oks.setNotification(R.drawable.tubiao_top, getString(R.string.app_name));
            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
            oks.setTitle(getString(R.string.share));
            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
            oks.setTitleUrl(youkuDetail.getLink());
            // text是分享文本，所有平台都需要这个字段
            oks.setText("快来看看"+detail.getName()+youkuDetail.getLink());
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl(youkuDetail.getLink());
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite(getString(R.string.app_name));
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl(youkuDetail.getLink());

            // 启动分享GUI
            oks.show(this);
        }


    }

    /**
     * 点赞
     */
    private class FlowerTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            return JsonHelper.giveflower(id,ExApplication.MEMBER_ID);
        }

        @Override
        protected void onPostExecute(String b) {
            super.onPostExecute(b);

            if (b.equals("c")) {
                ToastUtils.showToast(VideoPlayActivity.this,"你已经赞过了该视频");
            }else if(b.equals("s")){
                flowerTv.setText(Integer.parseInt(flowerTv.getText().toString().trim().equals("")?"0":flowerTv.getText().toString().trim())+1+"");
            }else{
            }
        }
    }

    /**
     * 下载
     */
    private class DownloadTask extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected Boolean doInBackground(Void... voids) {
            return JsonHelper.addDownLoadCount(id);
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            if (b){
                downloadTv.setText(Integer.parseInt(downloadTv.getText().toString().trim().equals("")?"0":downloadTv.getText().toString().trim())+1+"");
            }else{
                ToastUtils.showToast(VideoPlayActivity.this,"网络错误");
            }
        }
    }

    /**
     * 提交评论
     */
    private class SubmitTask extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected Boolean doInBackground(Void... voids) {
            return JsonHelper.submitComment(id,ExApplication.MEMBER_ID,commentEdt.getText().toString().trim());
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            if (b){
                ToastUtils.showToast(VideoPlayActivity.this,"评论成功");
                commentEdt.setText("");
                onRefresh();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(commentEdt.getWindowToken(), 0) ;

                CompleteTaskUtils utils;
                utils=new CompleteTaskUtils(VideoPlayActivity.this,"12");
                utils.completeMission();

            }else{
                ToastUtils.showToast(VideoPlayActivity.this,"评论失败,请重试");
            }
        }
    }

    /**
     * 收藏
     */
    private class CollectTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            return JsonHelper.getCollectVideo(ExApplication.MEMBER_ID, id);
        }

        @Override
        protected void onPostExecute(String b) {
            super.onPostExecute(b);
            if (b.equals("c")) {
                ToastUtils.showToast(VideoPlayActivity.this,"你已经收藏过了该视频");
            }else if(b.equals("s")){


                collectTv.setText(Integer.parseInt(collectTv.getText().toString().trim().equals("")?"0":collectTv.getText().toString().trim())+1+"");
                ToastUtils.showToast(VideoPlayActivity.this,"收藏视频成功");

                CompleteTaskUtils utils;
                utils=new CompleteTaskUtils(VideoPlayActivity.this,"13");
                utils.completeMission();
            }else{
                ToastUtils.showToast(VideoPlayActivity.this,"收藏视频失败");
            }
        }
    }

    private VedioDetail youkuDetail;
    /**
     * 获取视频信息
     */
    private class GetVideoInfoTask extends AsyncTask<Void,Void,String>{
        String id="";
        public GetVideoInfoTask(String id){
            this.id=id;
        }
        @Override
        protected String doInBackground(Void... voids) {
            youkuDetail=JsonHelper.getYouKuDetail(id);
            if (youkuDetail!=null){
                return "s";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("s")){
                if (detail!=null){
                    VideoEntity video=new VideoEntity();
                    video.setId(recId);

                    String aa=detail.getTime_length();
//                    Log.d("aa",aa);
//                    int b=aa.indexOf(".");
//                    Log.d("b",b+"");
//                    if (b!=-1){
//                        String c=aa.substring(0, b);
//                        Log.d("c",c);
//                        video.setTime(TimeUtils.secToTime(Integer.parseInt(c)));
//                    }else{
                        video.setTime(aa);
//                    }

//                    video.setTime(TimeUtils.secToTime(Integer.parseInt(c)));
                    video.setSimg_url(detail.getFlagPath());
                    video.setFlower(detail.getFlower_count());
                    video.setComment(detail.getComment_count());
                    video.setViewCount(detail.getView_count());
                    video.setTitle_content(detail.getName());
                    dbManager.addRecordVideo(video);
                }
            }
        }
    }


}
