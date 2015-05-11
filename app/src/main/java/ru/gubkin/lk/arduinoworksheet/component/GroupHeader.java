package ru.gubkin.lk.arduinoworksheet.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import ru.gubkin.lk.arduinoworksheet.R;

/**
 * Created by root on 04.05.15.
 */
public class GroupHeader extends ListItem {
    private static ItemType type = ItemType.HEADER;
    private String name;
    private Controller controller;

    public GroupHeader (String name, Controller controller) {
        this.name = name;
        this.controller = controller;
    }
    @Override
    public int getViewType() {
        return type.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.header_li, parent, false);
        TextView tv = (TextView) v.findViewById(R.id.header_tv);
        Button addButton = (Button) v.findViewById(R.id.header_add_button);
        controller.registerAddButton(addButton);
//        controller.registerListeners();
        tv.setText(name);
        return v;
    }

}
