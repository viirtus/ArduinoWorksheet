package ru.gubkin.lk.arduinoworksheet.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.R;

/**
 * Created by Андрей on 07.05.2015.
 */
public class DevicesAdapter extends BaseAdapter {
    private LayoutInflater inflater;

    private ArrayList<BluetoothDevice> items;

    public DevicesAdapter(Context context, ArrayList<BluetoothDevice> items) {
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void add(BluetoothDevice device) {
        items.add(device);
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
        return 0;
    }

    class ViewHolder {
        TextView name;
        TextView mac;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.device_li, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.device_name_tv);
            holder.mac = (TextView) convertView.findViewById(R.id.device_mac_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(items.get(position).getName());
        holder.mac.setText(items.get(position).getAddress());

        return convertView;
    }
}
