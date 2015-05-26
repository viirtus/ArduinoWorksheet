package ru.gubkin.lk.arduinoworksheet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.R;
import ru.gubkin.lk.arduinoworksheet.component.sensor.Sensor;
import ru.gubkin.lk.arduinoworksheet.component.sensor.SensorDisplayView;

/**
 * Created by Андрей on 23.05.2015.
 */
public class SensorGridAdapter extends BaseAdapter {
    private Context context;
    private final ArrayList<Sensor> items;
    private final LayoutInflater inflater;

    public SensorGridAdapter(Context context, ArrayList<Sensor> items) {
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        TextView label;
        SensorDisplayView view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.sensor_item_display, parent, false);
            holder.label = (TextView) convertView.findViewById(R.id.sensor_display_label);
            holder.view = (SensorDisplayView) convertView.findViewById(R.id.sensor_display_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Sensor sensor = items.get(position);
        holder.label.setText(sensor.getName());
        sensor.setSensorView(holder.view);

        return convertView;
    }
}
