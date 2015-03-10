package com.li.videoapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.activity.ExApplication;
import com.li.videoapplication.R;
import com.li.videoapplication.entity.SearchVideo;
import com.li.videoapplication.entity.VideoEntity;
import com.li.videoapplication.utils.TimeUtils;

import java.util.List;

/**
 * Created by li on 2014/8/18.
 */
public class VideoAdapter extends BaseAdapter{

    private Context context;
    private List<VideoEntity> list;
    private LayoutInflater inflater;
    private ExApplication exApplication;

    public VideoAdapter(Context context,List<VideoEntity> list){
        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);
        exApplication=new ExApplication(context);
    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_video_item, null);
            holder=new ViewHolder();
            holder.imgeIv=(ImageView)convertView.findViewById(R.id.video_item_img);
            holder.playTimesTv=(TextView)convertView.findViewById(R.id.video_item_playcount);
            holder.flowerTv=(TextView)convertView.findViewById(R.id.video_item_flower);
            holder.commentTv=(TextView)convertView.findViewById(R.id.video_item_comment);
            holder.timeTv=(TextView)convertView.findViewById(R.id.video_item_time);
            holder.contentTv=(TextView)convertView.findViewById(R.id.video_item_content);
            holder.titleTv=(TextView)convertView.findViewById(R.id.video_item_title);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
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
    }

}
