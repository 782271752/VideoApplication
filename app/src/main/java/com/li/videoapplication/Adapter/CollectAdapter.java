package com.li.videoapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.activity.ExApplication;
import com.li.videoapplication.activity.VideoPlayActivity;
import com.li.videoapplication.entity.VideoEntity;
import com.li.videoapplication.utils.CollectCheckUtil;

import java.util.List;

/**
 * Created by li on 2014/8/15.
 */
public class CollectAdapter extends BaseAdapter {

    private List<VideoEntity> list;
    private Context context;
    private LayoutInflater inflater;
    private ExApplication exApplication;

    public CollectAdapter(Context context, List<VideoEntity> list){
        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);
        exApplication=new ExApplication(context);
    }

    @Override
    public int getCount() {

        if (list!=null){
            if (list.size()%2==1){
                return list.size()/2+1;
            }
            return list.size()/2;
        }else{
            return 0;
        }

    }

    @Override
    public Object getItem(int i) {
        if (list.size()%2==1){
            return list.get(list.size()/2+1);
        }
        return list.get(list.size()/2);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.collect_item, null);
            holder=new ViewHolder();
            holder.leftLayout=(RelativeLayout)convertView.findViewById(R.id.left_layout_l);
            holder.imgeIv1=(ImageView)convertView.findViewById(R.id.home_item_img1);
            holder.flowerTv1=(TextView)convertView.findViewById(R.id.home_item_flower1);
            holder.commentTv1=(TextView)convertView.findViewById(R.id.home_item_comment1);
            holder.timeTv1=(TextView)convertView.findViewById(R.id.home_item_time1);
            holder.contentTv1=(TextView)convertView.findViewById(R.id.home_item_introduce1);
            holder.playTimesTv1=(TextView)convertView.findViewById(R.id.home_item_playcount1);
            holder.leftCheckTv=(RelativeLayout)convertView.findViewById(R.id.check_img_left_layout);
            holder.leftCheckTv.setTag(position);

            holder.rightLayout =(RelativeLayout)convertView.findViewById(R.id.right_layout_r);
            holder.imgeIv2=(ImageView)convertView.findViewById(R.id.home_item_img2);
            holder.flowerTv2=(TextView)convertView.findViewById(R.id.home_item_flower2);
            holder.commentTv2=(TextView)convertView.findViewById(R.id.home_item_comment2);
            holder.timeTv2=(TextView)convertView.findViewById(R.id.home_item_time2);
            holder.contentTv2=(TextView)convertView.findViewById(R.id.home_item_introduce2);
            holder.playTimesTv2=(TextView)convertView.findViewById(R.id.home_item_playcount2);
            holder.rightCheckTv=(RelativeLayout)convertView.findViewById(R.id.check_img_right_layout);
            holder.rightCheckTv.setTag(position);

            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
            holder.leftCheckTv.setTag(position);
            holder.rightCheckTv.setTag(position);
        }


        if (!list.get(position*2).getSimg_url().equals("")){
//            ExApplication.imageLoader.displayImage(list.get(position*2).getSimg_url(),holder.imgeIv1,
//                    ExApplication.getOptions());
            exApplication.imageLoader.displayImage(list.get(position*2).getSimg_url(),holder.imgeIv1,exApplication.getOptions());
        }else{
            holder.imgeIv1.setImageDrawable(context.getResources().getDrawable(R.drawable.radio_fra_bottom_bg));
        }
        holder.flowerTv1.setText(list.get(position*2).getFlower());
        holder.commentTv1.setText(list.get(position*2).getComment());
        holder.timeTv1.setText(list.get(position*2).getTime());
        holder.contentTv1.setText(list.get(position*2).getTitle_content());
        holder.playTimesTv1.setText(list.get(position*2).getViewCount());


        if (!list.get(position*2).getSimg_url().equals("")){
//            ExApplication.imageLoader.displayImage(list.get(position*2).getSimg_url(),holder.imgeIv1,
//                    ExApplication.getOptions());
            exApplication.imageLoader.displayImage(list.get(position*2).getSimg_url(),holder.imgeIv1,exApplication.getOptions());
            holder.imgeIv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CollectCheckUtil.isCheck){
                        if (holder.leftCheckTv.getVisibility()==View.GONE) {
                            holder.leftCheckTv.setVisibility(View.VISIBLE);
                            list.get(position * 2).setIsCheck("true");
                            CollectCheckUtil.addCollectProduct(list.get(position*2));
                        }else{
                            holder.leftCheckTv.setVisibility(View.GONE);
                            list.get(position * 2).setIsCheck("false");
                            CollectCheckUtil.removeCollectProduce(list.get(position * 2));
                        }
                    }else{
                        Intent intent=new Intent(context, VideoPlayActivity.class);
                        intent.putExtra("id",list.get(position*2).getId());
                        intent.putExtra("title",list.get(position*2).getTitle_content());
                        context.startActivity(intent);
                    }

                }
            });
        }else{
            holder.imgeIv1.setImageDrawable(context.getResources().getDrawable(R.drawable.bg));
        }
        holder.flowerTv1.setText(list.get(position*2).getFlower());
        holder.commentTv1.setText(list.get(position*2).getComment());
        holder.timeTv1.setText(list.get(position*2).getTime());
        holder.contentTv1.setText(list.get(position*2).getTitle_content());




        if (position*2+1<list.size()){
            holder.rightLayout.setVisibility(View.VISIBLE);
            if (!list.get(position*2+1).getSimg_url().equals("")){
//                ExApplication.imageLoader.displayImage(list.get(position*2+1).getSimg_url(),holder.imgeIv2,
//                        ExApplication.getOptions());
                exApplication.imageLoader.displayImage(list.get(position*2+1).getSimg_url(),holder.imgeIv2,exApplication.getOptions());
                holder.imgeIv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (CollectCheckUtil.isCheck){
                            if (holder.rightCheckTv.getVisibility()==View.GONE) {
                                holder.rightCheckTv.setVisibility(View.VISIBLE);
                                list.get(position * 2+1).setIsCheck("true");
                                CollectCheckUtil.addCollectProduct(list.get(position*2+1));
                            }else{
                                holder.rightCheckTv.setVisibility(View.GONE);
                                list.get(position * 2+1).setIsCheck("false");
                                CollectCheckUtil.removeCollectProduce(list.get(position*2+1));
                            }
                        }else {
                            Intent intent = new Intent(context, VideoPlayActivity.class);
                            intent.putExtra("id", list.get(position * 2 + 1).getId());
                            intent.putExtra("title", list.get(position * 2 + 1).getTitle_content());
                            context.startActivity(intent);
                        }
                    }
                });

            }else{
                holder.imgeIv2.setImageDrawable(context.getResources().getDrawable(R.drawable.bg));
            }
            holder.flowerTv2.setText(list.get(position*2+1).getFlower());
            holder.commentTv2.setText(list.get(position*2+1).getComment());
            holder.timeTv2.setText(list.get(position*2+1).getTime());
            holder.contentTv2.setText(list.get(position*2+1).getTitle_content());
            holder.playTimesTv2.setText(list.get(position*2+1).getViewCount());


        }else{
            holder.rightLayout.setVisibility(View.INVISIBLE);
        }

        Log.e("CollectCheckUtil.isCheck",CollectCheckUtil.isCheck+"");
        if (CollectCheckUtil.isCheck==false){
            holder.leftCheckTv.setVisibility(View.GONE);
            int i=(Integer)holder.leftCheckTv.getTag();
            list.get(i).setIsCheck("false");

            holder.rightCheckTv.setVisibility(View.GONE);
            int j=(Integer)holder.rightCheckTv.getTag();
            list.get(j).setIsCheck("false");
        }

        String value=list.get(position*2).getIsCheck();
        if (value != null && "true".equals(value) &&!value.equals("")) {
            holder.leftCheckTv.setVisibility(View.VISIBLE);
        } else {
            holder.leftCheckTv.setVisibility(View.GONE);
        }


        if (position*2+1<list.size()){
            String va=list.get(position*2+1).getIsCheck();
            if (va != null && "true".equals(va) &&!va.equals("")) {
                holder.rightCheckTv.setVisibility(View.VISIBLE);
            } else {
                holder.rightCheckTv.setVisibility(View.GONE);
            }
        }
        return convertView;

    }



    private class ViewHolder{
        RelativeLayout leftLayout;
        TextView playTimesTv1;
        ImageView imgeIv1;
        TextView flowerTv1;
        TextView commentTv1;
        TextView timeTv1;
        TextView contentTv1;
        RelativeLayout leftCheckTv;

        RelativeLayout rightLayout;
        TextView playTimesTv2;
        ImageView imgeIv2;
        TextView flowerTv2;
        TextView commentTv2;
        TextView timeTv2;
        TextView contentTv2;
        RelativeLayout rightCheckTv;

    }
}
