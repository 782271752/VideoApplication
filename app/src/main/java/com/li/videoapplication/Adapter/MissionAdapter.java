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
import com.li.videoapplication.entity.MissionEntity;

import java.util.List;

/**
 * Created by li on 2014/8/15.
 */
public class MissionAdapter extends BaseAdapter{


    private Context context;
    private LayoutInflater inflater;
    private List<MissionEntity> list;
    private ExApplication exApplication;

    public MissionAdapter(Context context,List<MissionEntity> list){
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
            convertView = inflater.inflate(R.layout.mission_item, null);
            holder=new ViewHolder();
            holder.imgeIv=(ImageView)convertView.findViewById(R.id.mission_item_img);

            holder.titleTv=(TextView)convertView.findViewById(R.id.mission_item_title);
            holder.contentTv=(TextView)convertView.findViewById(R.id.mission_item_content);
            holder.giftTv=(TextView)convertView.findViewById(R.id.mission_item_gfit);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        if (list.get(position).getImgPath().equals("")||list.get(position).getImgPath()==null) {
            holder.imgeIv.setVisibility(View.GONE);
        }else{
            holder.imgeIv.setVisibility(View.VISIBLE);
//            ExApplication.imageLoader.displayImage(list.get(position).getImgPath(),holder.imgeIv,
//                    ExApplication.getOptions());
            exApplication.imageLoader.displayImage(list.get(position).getImgPath(),holder.imgeIv,exApplication.getOptions());
        }

        holder.titleTv.setText(list.get(position).getTitle());
        holder.contentTv.setText(list.get(position).getContent());
        holder.giftTv.setText(list.get(position).getReward());
        return convertView;
    }

    private class ViewHolder {
        ImageView imgeIv;
        TextView titleTv;
        TextView contentTv;
        TextView giftTv;
    }
}
