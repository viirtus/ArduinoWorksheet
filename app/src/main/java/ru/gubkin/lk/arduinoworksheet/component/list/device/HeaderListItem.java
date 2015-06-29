package ru.gubkin.lk.arduinoworksheet.component.list.device;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.gubkin.lk.arduinoworksheet.R;

/**
 * Created by root on 03.06.15.
 */
public class HeaderListItem extends ListItem {
    private String name;

    public HeaderListItem(String name) {
        this.name = name;
    }

    @Override
    public String getInfo() {
        return "";
    }

    @Override
    public int getViewType() {
        return ItemType.HEADER.ordinal();
    }

    @Override
    public DeviceItem getDeviceItem() {
        return null;
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.device_header_item, parent, false);
        TextView header = (TextView) v.findViewById(R.id.header_tv);
        header.setText(name);
        return v;
    }
}
