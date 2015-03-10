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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.activity.ExApplication;
import com.li.videoapplication.activity.VideoPlayActivity;
import com.li.videoapplication.entity.Advertisement;
import com.li.videoapplication.entity.DownloadVideo;
import com.li.videoapplication.utils.ImgCheckUtils;
import com.li.videoapplication.utils.TextTypeUtils;

import java.util.List;

/**下载视频适配器
 * Created by li on 2014/10/13.
 */
public class
        DownloadVideoAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<DownloadVideo> files;
    private ExApplication exApplication;
    private Typeface typeface;
    //判断是否是第一次点击,onLongclck 会影响 onclick
    public boolean isFirstCheck=false;
    private OnCheckListener listener;

    public DownloadVideoAdapter(Context context, List<DownloadVideo> files,OnCheckListener listener) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.files = files;
        exApplication = new ExApplication(context);
        typeface= TextTypeUtils.getTypeface(context);
        this.listener=listener;

    }

    @Override
    public int getCount() {
        if (files != null) {
            return files.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.download_item, null);
            holder = new ViewHolder();
            holder.ad_imge = (ImageView) convertView.findViewById(R.id.download_video_item_img);
            holder.titleTv = (TextView) convertView.findViewById(R.id.download_video_item_name);
            holder.checkTv=(RelativeLayout)convertView.findViewById(R.id.collect_item_gv_item_check_img_layout);
            holder.checkTv.setTag(position);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.checkTv.setTag(position);
        }


        if (files.size() != 0) {
            holder.titleTv.setText(files.get(position).getName());
            if (!files.get(position).getImgUrl().equals("")) {
                exApplication.imageLoader.displayImage(files.get(position).getImgUrl(),
                        holder.ad_imge, exApplication.getOptions());
            } else {
                holder.ad_imge.setImageDrawable(mContext.getResources().getDrawable(R.drawable.radio_fra_bottom_bg));
            }

        }
        holder.ad_imge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("", ImgCheckUtils.isCheckState?"true":"false");
                if (ImgCheckUtils.isCheckState){
//                    if (isFirstCheck){
//                        isFirstCheck=false;//已经过了长按的第一次
//                    }else {
                    if (holder.checkTv.getVisibility()==View.VISIBLE) {
                        holder.checkTv.setVisibility(View.GONE);
                        ImgCheckUtils.removeCollectProduce(files.get(position));
                        int i=(Integer)holder.checkTv.getTag();
                        files.get(i).setIsCheck("false");
                    } else {
                        holder.checkTv.setVisibility(View.VISIBLE);
                        ImgCheckUtils.addCollectProduct(files.get(position));
                        int i=(Integer)holder.checkTv.getTag();
                        files.get(i).setIsCheck("true");
                    }
//                    }
                }else{
                    if (files.get(position).getPlayUrl().equals("")){
                        return;
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String type = "video/mp4";
                    Uri uri = Uri.parse(files.get(position).getPlayUrl());
                    Log.e("path",files.get(position).getPlayUrl());
                    intent.setDataAndType(uri, type);
                    mContext.startActivity(intent);
                }
            }
        });

//        holder.ad_imge.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                if (!ImgCheckUtils.isCheckState){
//                    ImgCheckUtils.isCheckState=true;
//                    isFirstCheck=true;
//                    holder.checkTv.setVisibility(View.VISIBLE);
//                    int i=(Integer)holder.checkTv.getTag();
//                    files.get(i).setIsCheck("true");
//                    ImgCheckUtils.addCollectProduct(files.get(position));
//                    listener.onCheck();
//                }
//                return false;
//            }
//        });

        if (ImgCheckUtils.isCheckState==false){
            holder.checkTv.setVisibility(View.GONE);
            int i=(Integer)holder.checkTv.getTag();
            files.get(i).setIsCheck("false");
        }

        String value=files.get(position).getIsCheck();
        if (value != null && "true".equals(value) &&!"".equals("")) {
            holder.checkTv.setVisibility(View.VISIBLE);
        } else {
            holder.checkTv.setVisibility(View.GONE);
        }

        return convertView;
    }



    private class ViewHolder{
        ImageView ad_imge;
        TextView titleTv;
        RelativeLayout checkTv;

    }
    /**
     * 选中接口
     */
    public interface OnCheckListener{
        public void onCheck();
    }
}
