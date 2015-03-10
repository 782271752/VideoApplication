package com.li.videoapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.entity.GiftEntity;
import com.li.videoapplication.utils.DialogUtils;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.ToastUtils;

public class GiftAtuoDetailActivity extends Activity implements View.OnClickListener{

    private ImageView headImg;
    private ImageButton backBtn,persionBtn;
    private TextView titleTv,endTimeTv,countTv,changTv,contentTv;
    private ExApplication exApplication;
    private TextView codeTitleTv,codeTv;
    private LinearLayout codeLayoutTv;
    private String id;
    private Button copy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gift_detail);
        exApplication=new ExApplication(this);
        initView();

        if (this.getIntent().getExtras()!=null){
            id=this.getIntent().getExtras().getString("id");
            DialogUtils.showWaitDialog(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                new GetDetailTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }else{
                new GetDetailTask().execute();
            }
//            GiftEntity giftEntity=(GiftEntity)this.getIntent().getExtras().get("gift");
////            ExApplication.imageLoader.displayImage(giftEntity.getImgPath(),headImg,
////                    ExApplication.getOptions());
//            exApplication.imageLoader.displayImage(giftEntity.getImgPath(),headImg,exApplication.getOptions());
//            titleTv.setText(giftEntity.getTitle());
//            endTimeTv.setText("截止到"+giftEntity.getEndtime()+"结束");
//            countTv.setText(giftEntity.getCount()+"个");
//            changTv.setText(giftEntity.getTrade_type());
//            contentTv.setText(giftEntity.getContent());
//
//            if (!giftEntity.getActivity_code().equals("")){
//                codeTv.setText(giftEntity.getActivity_code());
//                codeTitleTv.setVisibility(View.VISIBLE);
//                codeLayoutTv.setVisibility(View.VISIBLE);
//            }
        }
    }

    private void initView(){
//        Log.e("member_id",ExApplication.MEMBER_ID);
        headImg=(ImageView)findViewById(R.id.gift_detail_img);
        backBtn=(ImageButton)findViewById(R.id.gift_detail_back);
        backBtn.setOnClickListener(this);
        persionBtn=(ImageButton)findViewById(R.id.gift_detail_persion);
        persionBtn.setOnClickListener(this);
        titleTv=(TextView)findViewById(R.id.gift_detail_title);
        endTimeTv=(TextView)findViewById(R.id.gift_detail_endtime);
        countTv=(TextView)findViewById(R.id.gift_detail_count);
        changTv=(TextView)findViewById(R.id.gift_detail_change);
        contentTv=(TextView)findViewById(R.id.gift_detail_content);

        codeTitleTv=(TextView)findViewById(R.id.gift_code_title);
        codeTv=(TextView)findViewById(R.id.gift_detail_code);
        codeLayoutTv=(LinearLayout)findViewById(R.id.gift_code_layout);

        copy=(Button)findViewById(R.id.gift_detail_copy);
        copy.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.gift_detail_back:
                this.finish();
                break;
            case R.id.gift_detail_persion:
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.gift_detail_copy:
                ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                cmb.setText(codeTv.getText().toString());
                ToastUtils.showToast(this,"已复制到剪贴板");
                break;
        }
    }
    private GiftEntity giftEntity;
    private class GetDetailTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            giftEntity= JsonHelper.getAutoGift(id);
            if (giftEntity!=null){
                return "s";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            DialogUtils.cancelWaitDialog();
            if (s.equals("")){
                ToastUtils.showToast(GiftAtuoDetailActivity.this,"获取数据失败");
                return;
            }
            if (s.equals("s")){
                exApplication.imageLoader.displayImage(giftEntity.getImgPath(),headImg,exApplication.getOptions());
            titleTv.setText(giftEntity.getTitle());
            endTimeTv.setText("截止到"+giftEntity.getEndtime()+"结束");
            countTv.setText(giftEntity.getCount()+"个");
            changTv.setText(giftEntity.getTrade_type());
            contentTv.setText(giftEntity.getContent());

            if (!giftEntity.getActivity_code().equals("")){
                codeTv.setText(giftEntity.getActivity_code());
                codeTitleTv.setVisibility(View.VISIBLE);
                codeLayoutTv.setVisibility(View.VISIBLE);
            }
            }
        }
    }

}
