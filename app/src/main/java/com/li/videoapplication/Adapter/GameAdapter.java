package com.li.videoapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.activity.ExApplication;
import com.li.videoapplication.entity.Game;
import com.li.videoapplication.entity.VideoEntity;

import java.util.List;

/**
 * Created by li on 2014/8/18.
 */
public class GameAdapter extends BaseAdapter{

    private Context context;
    private List<Game> list;
    private LayoutInflater inflater;
    private ExApplication exApplication;

    public GameAdapter(Context context, List<Game> list){
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
            convertView = inflater.inflate(R.layout.download_item, null);
            holder=new ViewHolder();
            holder.imgeIv=(ImageView)convertView.findViewById(R.id.download_video_item_img);
            holder.titleTv=(TextView)convertView.findViewById(R.id.download_video_item_name);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

//        ExApplication.imageLoader.displayImage(list.get(position).getFlagPath(),holder.imgeIv,
//                ExApplication.getOptions());

        if (list.size()>0){
            exApplication.imageLoader.displayImage(list.get(position).getFlagPath(),
                    holder.imgeIv,exApplication.getOptions());
        }

        holder.titleTv.setText(list.get(position).getName());
        return convertView;
    }

    private class ViewHolder {
        ImageView imgeIv;

        TextView titleTv;
    }

}
