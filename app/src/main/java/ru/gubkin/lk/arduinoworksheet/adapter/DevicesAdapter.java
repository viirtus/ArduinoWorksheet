package ru.gubkin.lk.arduinoworksheet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.component.ListItem;

/**
 * Created by Андрей on 07.05.2015.
 */
public class DevicesAdapter extends BaseAdapter {
    private LayoutInflater inflater;

    private ArrayList<ListItem> items;

    public DevicesAdapter(Context context, ArrayList<ListItem> items) {
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public boolean isEnabled(int position) {
        return items.get(position).getViewType() != ListItem.ItemType.HEADER.ordinal();
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


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return items.get(position).getView(inflater, convertView, parent);
    }
}
