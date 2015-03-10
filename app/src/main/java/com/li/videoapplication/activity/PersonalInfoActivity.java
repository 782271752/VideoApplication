package com.li.videoapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.li.videoapplication.Adapter.CityAreaAdapter;
import com.li.videoapplication.R;
import com.li.videoapplication.View.CircularImage;
import com.li.videoapplication.entity.GameType;
import com.li.videoapplication.entity.UserEntity;
import com.li.videoapplication.utils.CompleteTaskUtils;
import com.li.videoapplication.utils.ImgUtils;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.SharePreferenceUtil;
import com.li.videoapplication.utils.ToastUtils;
import android.os.Handler.Callback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;

public class PersonalInfoActivity extends Activity implements View.OnClickListener,PlatformActionListener,Callback{

    private CircularImage headImg;
    private List<GameType> glist=new ArrayList<GameType>();
    private ImageButton backBtn;
    private UserEntity u;
    private TextView nicknameTv,sexTv,addressTv,likeTypeTv;
    private EditText phoneTv;
    private static final int PHOTO_REQUEST_GALLERY = 0;
    private static final int PHOTO_REQUEST_CAREMA = 1;
    private static final int PHOTO_REQUEST_CUT = 3;
    private static String state = Environment.getExternalStorageState();
    private static File tempFile;
    private ExApplication application;
    private static final String filename = "/headimg.jpg";
    private Button disconBtn;
    private ExApplication exApplication;
    private ImageView qqAuthorize,weiboAuthorize;
    //授权类型
    private static int type=0;
    private static final int qq =1;
    private static final int sina_weibo =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_personal_info);
        ShareSDK.initSDK(this);
        initView();
        u= SharePreferenceUtil.getUserEntity(this);
        if (u==null){
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
            this.finish();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new GetGameTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new GetGameTask().execute();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new getInfoDetailTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new getInfoDetailTask().execute();
        }



    }

    private void initView(){
        exApplication=new ExApplication(this);
        headImg=(CircularImage)findViewById(R.id.person_info_head_img);
        headImg.setOnClickListener(this);
//        ExApplication.imageLoader.displayImage("http://img5.imgtn.bdimg.com/it/u=1726668589,450500620&fm=11&gp=0.jpg", headImg, ExApplication.getOptions());
        backBtn=(ImageButton)findViewById(R.id.person_info_back);
        backBtn.setOnClickListener(this);
        nicknameTv=(TextView)findViewById(R.id.personal_info_nickname);
        nicknameTv.setOnClickListener(this);
        sexTv=(TextView)findViewById(R.id.personal_info_sex);
        sexTv.setOnClickListener(this);
        phoneTv=(EditText)findViewById(R.id.info_num_discon_txt);
        phoneTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (phoneTv.hasFocus()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new UpdateInfoTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }else{
                        new UpdateInfoTask().execute();
                    }
                }

            }
        });
        addressTv=(TextView)findViewById(R.id.info_address);
        addressTv.setOnClickListener(this);
        qqAuthorize=(ImageView)findViewById(R.id.info_qq);
        qqAuthorize.setOnClickListener(this);
        weiboAuthorize=(ImageView)findViewById(R.id.info_weibo);
        weiboAuthorize.setOnClickListener(this);

        if (!SharePreferenceUtil.getPreference(this,"qq").equals("")){
            qqAuthorize.setImageDrawable(getResources().getDrawable(R.drawable.qq_yes));
        }

        if (!SharePreferenceUtil.getPreference(this,"weibo").equals("")){
            weiboAuthorize.setImageDrawable(getResources().getDrawable(R.drawable.weibo_yes));
        }
//        addressTv.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (addressTv.hasFocus()){
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                        new UpdateInfoTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                    }else{
//                        new UpdateInfoTask().execute();
//                    }
//                }
//
//            }
//        });
        likeTypeTv=(TextView)findViewById(R.id.personal_info_like_type);
//        likeTypeTv.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (likeTypeTv.hasFocus()){
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                        new UpdateInfoTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                    }else{
//                        new UpdateInfoTask().execute();
//                    }
//                }
//
//            }
//        });
        likeTypeTv.setOnClickListener(this);
        disconBtn=(Button)findViewById(R.id.info_discon_btn);
        disconBtn.setOnClickListener(this);
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
//                UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
//                login(plat.getName(), userId, null);
                return;
            }
        }
        plat.setPlatformActionListener(this);
        plat.SSOSetting(true);
        plat.showUser(null);
    }

    public boolean handleMessage(Message msg) {
        switch(msg.what) {
            case MSG_AUTH_CANCEL: {
                Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_ERROR: {
                Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_COMPLETE: {
                Toast.makeText(this,"授权成功", Toast.LENGTH_SHORT).show();
                if (type== qq){
                    SharePreferenceUtil.setPreference(PersonalInfoActivity.this,"qq","qq");
                    qqAuthorize.setImageDrawable(getResources().getDrawable(R.drawable.qq_yes));
                }else if (type== sina_weibo){
                    SharePreferenceUtil.setPreference(PersonalInfoActivity.this,"weibo","weibo");
                    weiboAuthorize.setImageDrawable(getResources().getDrawable(R.drawable.weibo_yes));
                }
            }
            break;
        }
        return false;
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.person_info_back:
                this.finish();
                break;
            case R.id.person_info_head_img:
                showDialog(0);
                break;
            case R.id.personal_info_sex:
                showDialog(1);
                break;
            case R.id.info_discon_btn:
                SharePreferenceUtil.setUserEntity(this,"");
                ExApplication.MEMBER_ID="";
                Platform qzone = ShareSDK.getPlatform(this, QZone.NAME);
                if (qzone.isValid ()) {
                    ShareSDK.removeCookieOnAuthorize(true);
                    qzone.removeAccount();
                }

                Platform qq = ShareSDK.getPlatform(this, QQ.NAME);
                if (qq.isValid ()) {
                    ShareSDK.removeCookieOnAuthorize(true);
                    qq.removeAccount();
                }

                Platform weibo = ShareSDK.getPlatform(this, SinaWeibo.NAME);
                if (weibo.isValid ()) {
                    ShareSDK.removeCookieOnAuthorize(true);
                    weibo.removeAccount();
                }
                this.finish();
                break;
            case R.id.personal_info_nickname:
                Intent intent=new Intent(this,SetNameActivity.class);
                intent.putExtra("name",nicknameTv.getText().toString());
                startActivity(intent);
                break;
            case R.id.personal_info_like_type:
                areaSelectDialog(this);

                break;
            case R.id.info_address:
                intent=new Intent(this,SetAreaActivity.class);
                intent.putExtra("area",addressTv.getText().toString());
                startActivity(intent);
                break;

            case R.id.info_qq:
                authorize(new QQ(this));
                type= PersonalInfoActivity.qq;
                break;
            case R.id.info_weibo:
                authorize(new SinaWeibo(this));
                type= sina_weibo;
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher);

        switch (id){
            case 0:
                builder.setItems(R.array.image, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                camera();
                                break;
                            case 1:
                                gallery();
                                break;
                        }

                    }
                });

                break;

            case 1:
                builder.setItems(R.array.sex, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                sexTv.setText("男");
                                break;
                            case 1:
                                sexTv.setText("女");
                                break;
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new UpdateInfoTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }else{
                            new UpdateInfoTask().execute();
                        }

                    }
                });
                break;
        }


        dialog = builder.create();
        return dialog;
    }

    /**
     * 从相册获取
     */
    public void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /**
     * 从相机获取
     */
    public void camera() {
        // 激活相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            tempFile = new File(Environment.getExternalStorageDirectory(),
                    "/head.jpg");
            // 从文件中创建uri
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                ImgUtils.crop(PersonalInfoActivity.this, uri, PHOTO_REQUEST_CUT);
            }

        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            // 从相机返回的数据
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                ImgUtils.crop(PersonalInfoActivity.this,Uri.fromFile(tempFile),PHOTO_REQUEST_CUT);
            } else {
                Toast.makeText(PersonalInfoActivity.this, "未找到存储卡，无法存储照片！",
                        Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                ExApplication.headImg=bitmap;
                headImg.setImageBitmap(bitmap);
                if (bitmap != null) {
                    if (ImgUtils.saveBitmap2file(bitmap, filename)) {
                        ExApplication.headImgUrl=Environment.getExternalStorageDirectory() + "/yc/img"
                                + filename;
                        Log.e("----",ExApplication.headImgUrl);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new UploadImgTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }else{
                            new UploadImgTask().execute();
                        }
                    }
                } else {
                    Log.e("saveFile_False", "");
                }

            }
            try {
                // 将临时文件删除
                tempFile.delete();
            } catch (Exception e) {
                Log.e("tempFile_clear_fail", "临时文件删除失败");
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> stringObjectHashMap) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
        }
    }

    @Override
    public void onError(Platform platform, int action, Throwable throwable) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }


    public class UploadImgTask extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected Boolean doInBackground(Void... voids) {
            return JsonHelper.uploadFile(PersonalInfoActivity.this,ExApplication.MEMBER_ID,ExApplication.headImgUrl);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }

    private class UpdateInfoTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            return JsonHelper.getUpdateInfo(ExApplication.MEMBER_ID,"",phoneTv.getText().toString().trim(),nicknameTv.getText().toString().trim(),
                   sexTv.getText().toString().equals("男")?"1":"0",addressTv.getText().toString().trim(),areaId);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("s")){
                ToastUtils.showToast(PersonalInfoActivity.this, "修改成功");

                CompleteTaskUtils utils;
                utils=new CompleteTaskUtils(PersonalInfoActivity.this,"16");
                utils.completeMission();

            }else{
                ToastUtils.showToast(PersonalInfoActivity.this,"修改失败");
            }
        }

    }

    private static String areaId="";
    /**
     * 地区选择窗口
     * @param context
     */
    private void areaSelectDialog(final Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog, null);
        ListView list = (ListView) view.findViewById(R.id.dialog_list);
        CityAreaAdapter adapter = new CityAreaAdapter(context, glist);
        list.setAdapter(adapter);
        final AlertDialog dialog = new AlertDialog.Builder(context).setView(view)
//                setNegativeButton("返回", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        citySelectDialog(context, provinceList.get(proviceItemPosition).getValue(),proviceItemPosition);
//                    }
//                })
                .create();
        dialog.show();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                areaId=areaList.get(i).getCode();
//                area=area+"-"+areaList.get(i).getValue();
//                areaEt.setText(area);
//                Log.e("areaId", areaId);

                likeTypeTv.setText(glist.get(i).getName());
                areaId=glist.get(i).getId();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new UpdateInfoTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    new UpdateInfoTask().execute();
                }
                dialog.dismiss();
            }
        });

    }
    /**
     * 获取视频分类列表
     */
    private class GetGameTask extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... params) {
            glist= JsonHelper.getGameTypeList();
            if (glist!=null){
                return "s";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("")){
                ToastUtils.showToast(PersonalInfoActivity.this, "连接服务器失败");
                return;
            }

            if (s.equals("s")){
//                gAdapter=new GameTypeAdapter(getActivity(),glist);
//                gameGv.setAdapter(gAdapter);
            }
        }
    }

    private static UserEntity user=new UserEntity();
    private class getInfoDetailTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            user=JsonHelper.getUserDetailInfo(PersonalInfoActivity.this,ExApplication.MEMBER_ID);
//            Log.e("name",user.getTitle());
            if (user!=null){
                return "s";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("s")){
             exApplication.imageLoader.displayImage(user.getImgPath(), headImg, exApplication.getOptions());
                if (ExApplication.headImg!=null){
                    headImg.setImageBitmap(ExApplication.headImg);
                }else{
                    exApplication.imageLoader.displayImage(user.getImgPath(), headImg,exApplication.getOptions());
                }

                nicknameTv.setText(user.getTitle());

                sexTv.setText(user.getSex().equals("1")?"男":"女");
                sexTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog(1);
                    }
                });
                phoneTv.setText(user.getMobile());
                addressTv.setText(user.getAddress());
                String type="";
//                switch (Integer.parseInt(user.getLike_gametype())){
                    for (int i = 0; i < glist.size(); i++) {
                        if (user.getLike_gametype().equals(glist.get(i).getId())){
                            likeTypeTv.setText(glist.get(i).getName());
                    }
//
                }

            }

        }
    }
}
