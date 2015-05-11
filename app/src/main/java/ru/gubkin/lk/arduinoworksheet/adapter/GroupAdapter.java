package ru.gubkin.lk.arduinoworksheet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.component.ListItem;

/**
 * Created by root on 05.05.15.
 */
public class GroupAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private final ArrayList<ListItem> items;

    public GroupAdapter(Context context, ArrayList<ListItem> items) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
    }

    @Override
    public boolean isEnabled(int position) {
        return position % 2 == 0;
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
