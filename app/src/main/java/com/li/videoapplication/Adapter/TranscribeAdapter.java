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
import com.li.videoapplication.entity.TranscribeEntity;

import java.util.List;

/**
 * Created by li on 2014/8/20.
 */
public class TranscribeAdapter extends BaseAdapter{

    private List<TranscribeEntity> list;
    private Context context;
    private LayoutInflater inflater;
    private ExApplication exApplication;

    public TranscribeAdapter(Context context,List<TranscribeEntity> list){
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
            convertView = inflater.inflate(R.layout.transcribe_item, null);
            holder=new ViewHolder();
            holder.imgeIv=(ImageView)convertView.findViewById(R.id.transcribe_item_img);
            holder.titleTv=(TextView)convertView.findViewById(R.id.transcribe_item_title);
            holder.recordIv=(ImageView)convertView.findViewById(R.id.transcribe_item_record);

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
        holder.recordIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    private class ViewHolder {
        ImageView imgeIv;
        TextView titleTv;
        ImageView recordIv;
    }

}
