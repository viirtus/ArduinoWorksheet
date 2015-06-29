package ru.gubkin.lk.arduinoworksheet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.R;
import ru.gubkin.lk.arduinoworksheet.component.led.LED;

/**
 * Created by Андрей on 01.05.2015.
 */
public class LedGridAdapter extends BaseAdapter {
    private final ArrayList<LED> items;
    private final LayoutInflater inflater;
    public LedGridAdapter(Context context, ArrayList<LED> items) {
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView image;
        TextView title;
        RelativeLayout layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.led_li, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.led_image);
            holder.title = (TextView) convertView.findViewById(R.id.led_title);
            holder.layout = (RelativeLayout) convertView.findViewById(R.id.led_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LED led = items.get(position);
        holder.image.setImageResource(led.getImageResource());
        holder.title.setText(led.getTitle());
        holder.layout.setBackgroundColor(led.getBackground());
        return convertView;
    }
}
