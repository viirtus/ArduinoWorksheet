package ru.gubkin.lk.arduinoworksheet.component.list.device;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.gubkin.lk.arduinoworksheet.R;

/**
 * Created by root on 03.06.15.
 */
public class DeviceListItem extends ListItem {
    public static final int NO_IMAGE = 0;
    private final String name;
    private final String info;
    private DeviceItem deviceItem;

    public DeviceListItem(String name, String info) {
        this.name = name;
        this.info = info;
    }

    public DeviceListItem(DeviceItem deviceItem) {
        this.deviceItem = deviceItem;
        this.name = deviceItem.getName();
        this.info = deviceItem.getInfo();
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public int getViewType() {
        return ItemType.DEVICE.ordinal();
    }


    @Override
    public View getView(LayoutInflater inflater, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.device_li, parent, false);
        TextView name_ = (TextView) v.findViewById(R.id.device_name_tv);
        name_.setText(name);
        ImageView imageView = (ImageView) v.findViewById(R.id.image);
        imageView.setImageResource(deviceItem.getImageResource());
        TextView info_ = (TextView) v.findViewById(R.id.device_mac_tv);
        info_.setText(info);
        return v;
    }

    public DeviceItem getDeviceItem() {
        return deviceItem;
    }
}
