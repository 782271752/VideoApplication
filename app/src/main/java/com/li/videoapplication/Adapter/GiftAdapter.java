package com.li.videoapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.li.videoapplication.activity.ExApplication;
import com.li.videoapplication.R;
import com.li.videoapplication.activity.GiftAtuoDetailActivity;
import com.li.videoapplication.entity.GiftEntity;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.ToastUtils;

import java.util.List;

/**
 * Created by li on 2014/8/15.
 */
public class GiftAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater inflater;
    private List<GiftEntity> list;
    private ExApplication exApplication;
    private boolean isMygift;

    public GiftAdapter(Context context,List<GiftEntity> list,boolean isMygift){
        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);
        this.isMygift=isMygift;
        exApplication=new ExApplication(context);
    }


    @Override
    public int getCount() {
        if (list!=null){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gift_item, null);
            holder=new ViewHolder();
            holder.imgeIv=(ImageView)convertView.findViewById(R.id.gift_item_img);

            holder.titleTv=(TextView)convertView.findViewById(R.id.gift_item_title);
            holder.contentTv=(TextView)convertView.findViewById(R.id.gift_item_introduce);
            holder.countTv=(TextView)convertView.findViewById(R.id.gift_item_count);
            holder.getBtn=(Button)convertView.findViewById(R.id.gift_item_getBtn);
            holder.getBtn.setTag(position);
            holder.bar=(ProgressBar)convertView.findViewById(R.id.gift_item_progressbar);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
            holder.getBtn.setTag(position);
        }
        if (list.get(position).getImgPath().equals("")||list.get(position).getImgPath()==null) {
            holder.imgeIv.setVisibility(View.GONE);
        }else{
            holder.imgeIv.setVisibility(View.VISIBLE);
            Log.e("gift_img", list.get(position).getImgPath());
//            ExApplication.imageLoader.displayImage(list.get(position).getImgPath(), holder.imgeIv,
//                    ExApplication.getOptions());
            exApplication.imageLoader.displayImage(list.get(position).getImgPath(),holder.imgeIv,exApplication.getOptions());
        }

        holder.titleTv.setText(list.get(position).getTitle());
        holder.contentTv.setText(list.get(position).getContent());
        holder.countTv.setText(list.get(position).getCount());
        holder.bar.setProgress(30);

        if (isMygift){
            holder.getBtn.setVisibility(View.GONE);
        }

        holder.getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ExApplication.MEMBER_ID.equals("")){
                    ToastUtils.showToast(context,"请先登录");
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                    new GetGiftTask(holder,position).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    new GetGiftTask(holder,position).execute();
                }
            }
        });


        String value=list.get(position).getHasTake();
        if (value != null && "true".equals(value)) {
            holder.getBtn.setText("已领取");
        } else {
            holder.getBtn.setText("领取");
        }

        return convertView;
    }


    private class ViewHolder {
        ImageView imgeIv;
        TextView titleTv;
        TextView contentTv;
        TextView countTv;
        Button getBtn;
        ProgressBar bar;
    }

    /**
     * 领取礼包
     */
    private class GetGiftTask extends AsyncTask<Void,Void,Boolean>{
        int position=0;
        ViewHolder mholder;
        public GetGiftTask(ViewHolder mholder,int position){
            this.position=position;
            this.mholder=mholder;
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            return JsonHelper.getGiftResponse(ExApplication.MEMBER_ID,list.get(position).getId());
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            if (b){
                ToastUtils.showToast(context,"领取成功");
                mholder.getBtn.setText("已领取");
//                mholder.getBtn.setEnabled(false);

                int position=(Integer)mholder.getBtn.getTag();
                list.get(position).setHasTake("true");

                Intent intent=new Intent(context, GiftAtuoDetailActivity.class);
                intent.putExtra("id",list.get(position).getId());
                context.startActivity(intent);


            }else{
                ToastUtils.showToast(context,"领取失败");
            }
        }
    }
}
