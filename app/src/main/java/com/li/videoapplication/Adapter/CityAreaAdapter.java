package com.li.videoapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.entity.GameType;

import java.util.List;

/**选择省份，城市，地区
 * Created by li on 2014/9/26.
 */
public class CityAreaAdapter extends BaseAdapter{
    Context context;
    List<GameType> list;
    public CityAreaAdapter(Context context, List<GameType> list) {
        this.context = context;
        this.list = list;
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dialog_item, null);
            holder.item=(TextView)convertView.findViewById(R.id.dialog_item_title);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.item.setText(list.get(i).getName());
        return convertView;
    }

    private class ViewHolder {
        TextView item;
    }
}
