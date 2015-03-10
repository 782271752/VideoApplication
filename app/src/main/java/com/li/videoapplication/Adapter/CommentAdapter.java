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
import com.li.videoapplication.entity.CommentEntity;

import java.util.List;

/**
 * Created by li on 2014/8/18.
 */
public class CommentAdapter extends BaseAdapter{

    private List<CommentEntity> list;
    private Context context;
    private LayoutInflater inflater;
    private ExApplication exApplication;
    public CommentAdapter(Context context,List<CommentEntity> list){
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
            convertView = inflater.inflate(R.layout.play_comment_item, null);
            holder=new ViewHolder();
            holder.img=(ImageView)convertView.findViewById(R.id.comment_item_img);
            holder.nameTv=(TextView)convertView.findViewById(R.id.comment_item_name);
            holder.timeTv=(TextView)convertView.findViewById(R.id.comment_item_time);
            holder.contentTv=(TextView)convertView.findViewById(R.id.comment_item_content);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        if (list.get(position).getImgPath().equals("")||list.get(position).getImgPath()==null) {
            holder.img.setVisibility(View.GONE);
        }else{
            holder.img.setVisibility(View.VISIBLE);
//            ExApplication.imageLoader.displayImage(list.get(position).getImgPath(),holder.img,
//                    ExApplication.getOptions());
            exApplication.imageLoader.displayImage(list.get(position).getImgPath(),holder.img,exApplication.getOptions());

        }

        holder.nameTv.setText(list.get(position).getName());
        holder.timeTv.setText(list.get(position).getTime());
        holder.contentTv.setText(list.get(position).getContent());

        return convertView;
    }

    private class ViewHolder {

        ImageView img;
        TextView nameTv;
        TextView timeTv;
        TextView contentTv;
    }

}
