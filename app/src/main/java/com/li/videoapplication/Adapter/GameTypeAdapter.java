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
import com.li.videoapplication.entity.GameType;
import com.li.videoapplication.entity.VideoType;

import java.util.List;

/**
 * Created by li on 2014/10/10.
 */
public class GameTypeAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater inflater;
    private List<Game> list;
    private ExApplication exApplication;
//    private int[] icon={R.drawable.onee,R.drawable.twoo,R.drawable.threee,R.drawable.four,
//            R.drawable.five,R.drawable.six,R.drawable.seven,R.drawable.onee,R.drawable.twoo,R.drawable.threee,R.drawable.four,
//            R.drawable.five,R.drawable.six,R.drawable.seven,R.drawable.onee,R.drawable.twoo,R.drawable.threee,R.drawable.four,
//            R.drawable.five,R.drawable.six,R.drawable.seven,R.drawable.onee,R.drawable.twoo,R.drawable.threee,R.drawable.four,
//            R.drawable.five,R.drawable.six,R.drawable.seven};
    public GameTypeAdapter(Context context,List<Game> list){
        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);
        exApplication=new ExApplication(context);
    }

    @Override
    public int getCount() {
        if (list!=null) {
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.assort_item, null);
            holder=new ViewHolder();
            holder.img=(ImageView)convertView.findViewById(R.id.assort_item_img);
            holder.titleTv=(TextView)convertView.findViewById(R.id.assort_item_name);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        if (list.get(position).getFlagPath().equals("")||list.get(position).getFlagPath()==null) {
            holder.img.setVisibility(View.GONE);
        }else{
            holder.img.setVisibility(View.VISIBLE);
//            ExApplication.imageLoader.displayImage(list.get(position).getImgPath(),holder.img,
//                    ExApplication.getOptions());
            exApplication.imageLoader.displayImage(list.get(position).getFlagPath(),holder.img,exApplication.getOptions());
        }
//        holder.img.setImageDrawable(context.getResources().getDrawable(icon[position]));
        holder.titleTv.setText(list.get(position).getName());

        return convertView;
    }

    private class ViewHolder {
        ImageView img;
        TextView titleTv;
    }
}
