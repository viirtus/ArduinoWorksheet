package ru.gubkin.lk.arduinoworksheet.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by root on 05.05.15.
 */
public class GroupItem extends ListItem  {
    private static ItemType type = ItemType.HEADER;
    private Controller controller;

    public GroupItem(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public int getViewType() {
        return type.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView, ViewGroup parent) {
        return controller.getViewItem(inflater, convertView, parent);
    }
}
