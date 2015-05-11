package ru.gubkin.lk.arduinoworksheet.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by root on 04.05.15.
 */
public abstract class ListItem {

    enum ItemType {
        HEADER, ITEM;
    }

    public abstract int getViewType();
    public abstract View getView(LayoutInflater inflater, View convertView, ViewGroup parent);
}
