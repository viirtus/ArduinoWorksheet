package ru.gubkin.lk.arduinoworksheet.component.list.device;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by root on 04.05.15.
 */
public abstract class ListItem {

    public abstract String getInfo();

    public abstract int getViewType();

    public abstract View getView(LayoutInflater inflater, View convertView, ViewGroup parent);

    public enum ItemType {
        HEADER, ITEM_BLUETOOTH, ITEM_TCP
    }
}
