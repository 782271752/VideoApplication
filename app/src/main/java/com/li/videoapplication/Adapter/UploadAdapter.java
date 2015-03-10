package com.li.videoapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.activity.ExApplication;
import com.li.videoapplication.entity.LocalFile;
import com.li.videoapplication.entity.TranscribeVideo;
import com.li.videoapplication.utils.TextTypeUtils;
import com.li.videoapplication.utils.UploadUtils;
import com.li.videoapplication.utils.VideoUtils;

import java.util.List;

/**最新的适配器
 * Created by li on 2014/10/13.
 */
public class UploadAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<LocalFile> files;
    private ExApplication exApplication;
//    private VideoUtils utils;
    private List<Bitmap> bitmaps;
    public UploadAdapter(Context context, List<LocalFile> files,List<Bitmap> bitmaplist) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.files = files;
        exApplication = new ExApplication(context);
//        utils=new VideoUtils();
        this.bitmaps=bitmaplist;

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
            convertView = inflater.inflate(R.layout.add_friends_item, null);
            holder = new ViewHolder();
            holder.ad_imge = (ImageView) convertView.findViewById(R.id.relation_focus_item_img);
            holder.titleTv = (TextView) convertView.findViewById(R.id.relation_focus_item_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (files.size() != 0) {
            holder.titleTv.setText(files.get(position).getName());
            if (!files.get(position).getPath().equals("")) {
//                holder.ad_imge.setImageBitmap(utils.getVideoThumbnail(files.get(position).getPath(),80,80, MediaStore.Video.Thumbnails.MICRO_KIND));
                holder.ad_imge.setImageBitmap(bitmaps.get(position));
            } else {
                holder.ad_imge.setImageDrawable(mContext.getResources().getDrawable(R.drawable.radio_fra_bottom_bg));
            }
        }

        return convertView;
    }



    private class ViewHolder{
        ImageView ad_imge;
        TextView titleTv;

    }

}
