package com.li.videoapplication.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler.Callback;
import com.li.videoapplication.Adapter.HomeAdapter;
import com.li.videoapplication.DB.DBManager;
import com.li.videoapplication.R;
import com.li.videoapplication.View.MyListView;
import com.li.videoapplication.View.RefreshListView;
import com.li.videoapplication.entity.UserEntity;
import com.li.videoapplication.entity.VideoEntity;
import com.li.videoapplication.utils.CompleteTaskUtils;
import com.li.videoapplication.utils.DialogUtils;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.SharePreferenceUtil;
import com.li.videoapplication.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class LoginActivity extends Activity implements View.OnClickListener,RefreshListView.IXListViewListener,Callback,
        PlatformActionListener {

    private ImageView phone_focus_img,other_focus_img;
    private LinearLayout phone_layout,other_layout;
    private TextView phoneLoginTv,otherLoginTv;
    private ImageButton backBtn;
    private MyListView refreshListView;
    private List<VideoEntity> imgList;
    private List<VideoEntity> homeList;
    private List<VideoEntity> reponseList;
    private Context context;
    private SimpleDateFormat dateFormat = null;
    private HomeAdapter homeAdapter;
    private Button loginbtn;
    private TextView settingTv;
    private Button getCodeBtn;
    private EditText phoneEdt,codeEdt;
    private String phoneNum;
    private Button loginBtn;
    private LinearLayout sinaLogin,txLogin,qqLogin;
    private boolean canGetCode=true;
    private TextView downloadTv,collectTv,giftTv;
    private DBManager dbManager;
    private List<VideoEntity> dblist;
    private TextView moreTv,uploadTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        dbManager=new DBManager(context);
        if(SharePreferenceUtil.getUserEntity(this)!=null){
            Intent intent=new Intent(this,PersonalActivity.class);
            startActivity(intent);
            finish();
        }

        SMSSDK.initSDK(this, "3407e70513bc", "ec08272e03b38c375d68f2ecd6849a77");
        init();
        initView();

        EventHandler eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        // 注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }


    @Override
    protected void onResume() {
        super.onResume();
        homeList.clear();
        dblist=dbManager.getRecordVideo();
        Log.e("size",dblist.size()+"");
        if (dblist.size()>5){
            for (int i=0;i<4;i++){
                homeList.add(dblist.get(i));
                Log.e("-",i+"");
            }
        }else{
            homeList.addAll(dblist);
        }
        homeAdapter=new HomeAdapter(context,homeList);
        refreshListView.setAdapter(homeAdapter);
    }


    private Handler mWaitHandler = new Handler() {
        private static final int SECOND = 1000;
        private static final int MINUTE = 60 * SECOND;
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.arg1 <= MINUTE) {
                canGetCode=false;
                getCodeBtn.setEnabled(false);
                getCodeBtn.setText(String.format("(%s)重试", (MINUTE - msg.arg1) / SECOND));
                getCodeBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.getcode_gray));
                Message newMsg = obtainMessage();
                newMsg.arg1 = msg.arg1 + SECOND;
                sendMessageDelayed(newMsg, SECOND);

            } else {
                canGetCode=true;
                getCodeBtn.setEnabled(true);
                getCodeBtn.setText("获取验证码");
                getCodeBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.install));
            }
        }
    };


    private void init(){
        context=LoginActivity.this;
        ShareSDK.initSDK(this);
        dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.UK);
        dblist=new ArrayList<VideoEntity>();
        homeList=new ArrayList<VideoEntity>();
        reponseList=new ArrayList<VideoEntity>();
    }

    private  void  initView(){
        sinaLogin=(LinearLayout)findViewById(R.id.login_sina);
        sinaLogin.setFocusable(true);
        sinaLogin.setFocusableInTouchMode(true);
        sinaLogin.requestFocus();
        sinaLogin.setOnClickListener(this);
        txLogin=(LinearLayout)findViewById(R.id.login_tx);
        txLogin.setOnClickListener(this);
        qqLogin=(LinearLayout)findViewById(R.id.login_qq);
        qqLogin.setOnClickListener(this);
        loginbtn=(Button)findViewById(R.id.login_login_btn);
        loginbtn.setOnClickListener(this);
        phone_focus_img=(ImageView)findViewById(R.id.phone_foucs_tab_img);
        other_focus_img=(ImageView)findViewById(R.id.other_foucs_tab_img);
        phone_layout=(LinearLayout)findViewById(R.id.phone_layout);
        other_layout=(LinearLayout)findViewById(R.id.other_layout);
        phoneLoginTv=(TextView)findViewById(R.id.phone_login);
        phoneLoginTv.setOnClickListener(this);
        otherLoginTv=(TextView)findViewById(R.id.other_login);
        otherLoginTv.setOnClickListener(this);
        otherLoginTv.setFocusable(true);
        otherLoginTv.setFocusableInTouchMode(true);
        otherLoginTv.requestFocus();
        backBtn=(ImageButton)findViewById(R.id.login_back);
        backBtn.setOnClickListener(this);
        refreshListView=(MyListView)findViewById(R.id.login_recent_list);
//        refreshListView.setAdapter(homeAdapter);

        moreTv=(TextView)findViewById(R.id.personal_read_more);
        moreTv.setOnClickListener(this);
//        refreshListView.setPullLoadEnable(true);
//        refreshListView.setXListViewListener(this);
//        refreshListView.setPullRefreshEnable(false);

        settingTv=(TextView)findViewById(R.id.login_setting);
        settingTv.setOnClickListener(this);

        giftTv=(TextView)findViewById(R.id.login_gift);
        giftTv.setOnClickListener(this);

        getCodeBtn=(Button)findViewById(R.id.login_getCode);
        getCodeBtn.setOnClickListener(this);

        phoneEdt=(EditText)findViewById(R.id.login_username_edt);
        codeEdt=(EditText)findViewById(R.id.login_code_edt);
        loginbtn=(Button)findViewById(R.id.login_login_btn);
        loginbtn.setOnClickListener(this);

        downloadTv=(TextView)findViewById(R.id.login_download);
        downloadTv.setOnClickListener(this);

        collectTv=(TextView)findViewById(R.id.login_collect);
        collectTv.setOnClickListener(this);
        uploadTv=(TextView)findViewById(R.id.login_upload);
        uploadTv.setOnClickListener(this);

        dbManager=new DBManager(LoginActivity.this);
        dblist=dbManager.getRecordVideo();
        Log.e("size",dblist.size()+"");
        if (dblist.size()>5){
            for (int i=0;i<4;i++){
                homeList.add(dblist.get(i));
                Log.e("-",i+"");
            }
        }else{
            homeList.addAll(dblist);
        }
        homeAdapter=new HomeAdapter(context,homeList);
        refreshListView.setAdapter(homeAdapter);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.phone_login:
                phone_focus_img.setVisibility(View.VISIBLE);
                other_focus_img.setVisibility(View.INVISIBLE);
                phone_layout.setVisibility(View.VISIBLE);
                other_layout.setVisibility(View.GONE);
                break;
            case R.id.other_login:
                phone_focus_img.setVisibility(View.INVISIBLE);
                other_focus_img.setVisibility(View.VISIBLE);
                phone_layout.setVisibility(View.GONE);
                other_layout.setVisibility(View.VISIBLE);
                break;
            case R.id.login_download:
                Intent intent=new Intent(this,DownLoadActivity.class);
                startActivity(intent);
                break;
            case R.id.login_back:
                this.finish();
                break;
            case R.id.login_login_btn:


                    DialogUtils.showWaitDialog(this);
                    if(!TextUtils.isEmpty(codeEdt.getText().toString())){
                        SMSSDK.submitVerificationCode("86", phoneNum, codeEdt.getText().toString());

                    }else {
                        Toast.makeText(this, "验证码不能为空",Toast.LENGTH_LONG).show();
                    }

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                    new LoginTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                }else{
//                    new LoginTask().execute();
//                }
                break;
            case R.id.login_setting:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.login_gift:
                ToastUtils.showToast(this,"请先登录");
                break;

            case R.id.login_getCode:

            if (canGetCode){


                if (!TextUtils.isEmpty(phoneEdt.getText().toString())) {
                    SMSSDK.getVerificationCode("86", phoneEdt.getText().toString());
                    phoneNum = phoneEdt.getText().toString();

                    Message msg = new Message();
                    msg.what = 0;
                    mWaitHandler.sendMessage(msg);
                } else {
                    Toast.makeText(this, "电话不能为空", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this, "操作太频繁，请稍候重试",Toast.LENGTH_LONG).show();
            }
                break;

            case R.id.login_sina:
                authorize(new SinaWeibo(this));
                break;
            case R.id.login_qq:
                authorize(new QQ(this));
                break;
            case R.id.login_tx:
                authorize(new QZone(this));
                break;
            case R.id.login_collect:
                intent=new Intent(this,CollectActivity.class);
                startActivity(intent);
                break;
            case R.id.personal_read_more:
                intent=new Intent(this,RecordActivity.class);
                startActivity(intent);
                break;
            case R.id.login_upload:
                intent=new Intent(this,UploadActivity.class);
                startActivity(intent);
                break;
        }
    }

    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR= 4;
    private static final int MSG_AUTH_COMPLETE = 5;

    private void authorize(Platform plat) {
        if (plat == null) {
            popupOthers();
            return;
        }

        if(plat.isValid()) {
            String userId = plat.getDb().getUserId();
            if (!TextUtils.isEmpty(userId)) {
                UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
                login(plat.getName(), userId, null);
                return;
            }
        }
        plat.setPlatformActionListener(this);
        plat.SSOSetting(true);
        plat.showUser(null);
    }
    private void popupOthers() {
        Dialog dlg = new Dialog(this);
        View dlgView = View.inflate(this, R.layout.other_plat_dialog, null);
        View tvFacebook = dlgView.findViewById(R.id.tvFacebook);
        tvFacebook.setTag(dlg);
        tvFacebook.setOnClickListener(this);
        View tvTwitter = dlgView.findViewById(R.id.tvTwitter);
        tvTwitter.setTag(dlg);
        tvTwitter.setOnClickListener(this);

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(dlgView);
        dlg.show();
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();

    }

    Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event="+event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new LoginTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }else{
                        new LoginTask().execute();
                    }
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//返回支持发送验证码的国家列表
                    Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
                }
            } else {
                DialogUtils.cancelWaitDialog();
                try {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;

                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    if (!TextUtils.isEmpty(des)) {
                        Toast.makeText(context, des, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //#if def{lang} == cn
                // 如果木有找到资源，默认提示
                //#elif def{lang} == en
                // show default error when can't find the resource of string
                //#endif

                Toast.makeText(context,"请求错误", Toast.LENGTH_SHORT).show();

            }

        }

    };
    private static UserEntity user;

    public void onComplete(Platform platform, int action,
                           HashMap<String, Object> res) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
            login(platform.getName(), platform.getDb().getUserId(), res);
        }
//        System.out.println(res);
        String openId = platform.getDb().getUserId(); // 获取用户在此平台的ID
        String nickname=platform.getDb().get("nickname");
//        String city=platform.getDb().get("city");
//        String gender=platform.getDb().get("gender");
        Log.e("-----",openId+"--"+nickname);
        new OtherLoginTask(openId,nickname).execute();
    }

    public void onError(Platform platform, int action, Throwable t) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }
        t.printStackTrace();
    }

    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }

    private void login(String plat, String userId, HashMap<String, Object> userInfo) {

//        if (user!=null){
//            Map map = new HashMap();
//            Iterator iter = map.entrySet().iterator();
//            while (iter.hasNext()) {
//                Map.Entry entry = (Map.Entry) iter.next();
//                Object key = entry.getKey();
//                Object val = entry.getValue();
//                Log.e("key_val",key.toString()+"---"+val.toString());
//            }
//        }
        Message msg = new Message();
        msg.what = MSG_LOGIN;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }

    public boolean handleMessage(Message msg) {
        switch(msg.what) {
            case MSG_USERID_FOUND: {
                Toast.makeText(this, R.string.userid_found, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_LOGIN: {

                String text = getString(R.string.logining, msg.obj);
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

//				Builder builder = new Builder(this);
//				builder.setTitle(R.string.if_register_needed);
//				builder.setMessage(R.string.after_auth);
//				builder.setPositiveButton(R.string.ok, null);
//				builder.create().show();
            }
            break;
            case MSG_AUTH_CANCEL: {
                Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_ERROR: {
                Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_COMPLETE: {
                Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT).show();
            }
            break;
        }
        return false;
    }

    private class LoginTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            user= JsonHelper.getUserInfo(LoginActivity.this,phoneNum);

            if (user!=null){
                ExApplication.MEMBER_ID=user.getId();
                return "s";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            DialogUtils.cancelWaitDialog();
            if (s.equals("s")){
                Intent intent = new Intent(LoginActivity.this, PersonalActivity.class);
                startActivity(intent);
                finish();

                CompleteTaskUtils utils;
                utils=new CompleteTaskUtils(LoginActivity.this,"15");
                utils.completeMission();
            }else{
                ToastUtils.showToast(LoginActivity.this,"还未填写资料");
            }

        }
    }
    private String reponse="";
    private UserEntity userEntity;
    private class OtherLoginTask extends AsyncTask<Void,Void,String>{
        String openId="";
        String name="";
        public OtherLoginTask(String openId,String name){
            this.openId=openId;
            this.name=name;
        }
        @Override
        protected String doInBackground(Void... voids) {
            userEntity=JsonHelper.getOtherUser(LoginActivity.this,openId,name);
            if (userEntity!=null){
                return "s";
            }

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("s")){
                Intent intent = new Intent(LoginActivity.this, PersonalActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

}
