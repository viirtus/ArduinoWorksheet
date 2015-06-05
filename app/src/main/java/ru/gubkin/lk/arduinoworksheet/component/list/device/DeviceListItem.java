package ru.gubkin.lk.arduinoworksheet.component.list.device;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.gubkin.lk.arduinoworksheet.R;
import ru.gubkin.lk.arduinoworksheet.component.ListItem;

/**
 * Created by root on 03.06.15.
 */
public class DeviceListItem extends ListItem {

    private final String name;
    private final String info;
    private ItemType type;
    private DeviceItem deviceItem;

    public DeviceListItem(String name, String info, ItemType type) {
        this.name = name;
        this.info = info;
        this.type = type;
    }

    public DeviceListItem(DeviceItem deviceItem, ItemType type) {
        this.deviceItem = deviceItem;
        this.name = deviceItem.getName();
        this.info = deviceItem.getInfo();
        this.type = type;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public int getViewType() {
        return type.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.device_li, parent, false);
        TextView name_ = (TextView) v.findViewById(R.id.device_name_tv);
        name_.setText(name);

        TextView info_ = (TextView) v.findViewById(R.id.device_mac_tv);
        info_.setText(info);
        return v;
    }

    public DeviceItem getDeviceItem() {
        return deviceItem;
    }
}
