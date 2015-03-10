package com.li.videoapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.activity.ExApplication;
import com.li.videoapplication.activity.VideoPlayActivity;
import com.li.videoapplication.entity.VideoEntity;
import com.li.videoapplication.utils.RecordCheckUtils;
import com.li.videoapplication.utils.TextTypeUtils;

import java.util.List;

/** 浏览视频
 * Created by li on 2014/8/18.
 */
public class RecordAdapter extends BaseAdapter{

    private Context context;
    private List<VideoEntity> list;
    private LayoutInflater inflater;
    private ExApplication exApplication;
    private Typeface typeface;
    //判断是否是第一次点击,onLongclck 会影响 onclick
    public boolean isFirstCheck=false;
    private OnCheckListener listener;


    public RecordAdapter(Context context, List<VideoEntity> list,OnCheckListener listener){
        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);
        exApplication=new ExApplication(context);
        typeface= TextTypeUtils.getTypeface(context);
        this.listener=listener;
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
            convertView = inflater.inflate(R.layout.record_item, null);
            holder=new ViewHolder();
            holder.imgeIv=(ImageView)convertView.findViewById(R.id.video_item_img);
            holder.playTimesTv=(TextView)convertView.findViewById(R.id.video_item_playcount);
            holder.flowerTv=(TextView)convertView.findViewById(R.id.video_item_flower);
            holder.commentTv=(TextView)convertView.findViewById(R.id.video_item_comment);
            holder.timeTv=(TextView)convertView.findViewById(R.id.video_item_time);
            holder.contentTv=(TextView)convertView.findViewById(R.id.video_item_content);
            holder.titleTv=(TextView)convertView.findViewById(R.id.video_item_title);
            holder.checkTv=(TextView)convertView.findViewById(R.id.collect_item_gv_item_check_img);
            holder.checkTv.setTypeface(typeface);
            holder.checkTv.setTag(position);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
            holder.checkTv.setTag(position);
        }

//        ExApplication.imageLoader.displayImage(list.get(position).getFlagPath(),holder.imgeIv,
//                ExApplication.getOptions());

        if (list.size()>0){
            exApplication.imageLoader.displayImage(list.get(position).getSimg_url(),
                    holder.imgeIv,exApplication.getOptions());
        }

        holder.flowerTv.setText(list.get(position).getFlower());
        holder.commentTv.setText(list.get(position).getComment());
        holder.timeTv.setText(list.get(position).getTime());
        holder.contentTv.setText(list.get(position).getTitle_content());
        holder.titleTv.setText(list.get(position).getTitle());
        holder.playTimesTv.setText(list.get(position).getViewCount());

        holder.imgeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("", RecordCheckUtils.isCheckState ? "true" : "false");
                if (RecordCheckUtils.isCheckState){
                    if (isFirstCheck){
                        isFirstCheck=false;//已经过了长按的第一次
                    }else {
                        if (holder.checkTv.getVisibility()==View.VISIBLE) {
                            holder.checkTv.setVisibility(View.GONE);
                            RecordCheckUtils.removeCollectProduce(list.get(position));
                            int i=(Integer)holder.checkTv.getTag();
                            list.get(i).setIsCheck("false");
                        } else {
                            holder.checkTv.setVisibility(View.VISIBLE);
                            RecordCheckUtils.addCollectProduct(list.get(position));
                            int i=(Integer)holder.checkTv.getTag();
                            list.get(i).setIsCheck("true");
                        }
                    }
                }else{
                    if (list.get(position).getId().equals("")){
                        return;
                    }
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    String type = "video/mp4";
//                    Uri uri = Uri.parse(list.get(position).getPlayUrl());
//                    Log.e("path",list.get(position).getPlayUrl());
//                    intent.setDataAndType(uri, type);
//                    mContext.startActivity(intent);
                    Intent intent=new Intent(context, VideoPlayActivity.class);
                    intent.putExtra("id",list.get(position).getId());
                    intent.putExtra("title",list.get(position).getTitle_content());
                    context.startActivity(intent);
                }
            }
        });

        holder.imgeIv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!RecordCheckUtils.isCheckState){
                    RecordCheckUtils.isCheckState=true;
                    isFirstCheck=true;
                    holder.checkTv.setVisibility(View.VISIBLE);
                    int i=(Integer)holder.checkTv.getTag();
                    list.get(i).setIsCheck("true");
                    RecordCheckUtils.addCollectProduct(list.get(position));
                    listener.onCheck();
                }
                return false;
            }
        });

        if (RecordCheckUtils.isCheckState==false){
            holder.checkTv.setVisibility(View.GONE);
            int i=(Integer)holder.checkTv.getTag();
            list.get(i).setIsCheck("false");
        }

        String value=list.get(position).getIsCheck();
        if (value != null && !"".equals(value)&&"true".equals(value)) {
            holder.checkTv.setVisibility(View.VISIBLE);
        } else {
            holder.checkTv.setVisibility(View.GONE);
        }
        
        return convertView;
    }

    private class ViewHolder {
        TextView playTimesTv;
        ImageView imgeIv;
        TextView flowerTv;
        TextView commentTv;
        TextView timeTv;
        TextView contentTv;
        TextView titleTv;
        TextView checkTv;
    }

    /**
     * 选中接口
     */
    public interface OnCheckListener{
        public void onCheck();
    }

}
