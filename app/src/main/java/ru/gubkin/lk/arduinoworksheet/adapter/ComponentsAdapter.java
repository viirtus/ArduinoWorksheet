package ru.gubkin.lk.arduinoworksheet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.R;
import ru.gubkin.lk.arduinoworksheet.component.Controller;

/**
 * Created by ������ on 19.06.2015.
 */
public class ComponentsAdapter extends BaseAdapter {

    private final ArrayList<Controller> list;
    private LayoutInflater inflater;

    public ComponentsAdapter(Context context, ArrayList<Controller> list) {
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.common_li, parent, false);
            holder.grid = (GridView) convertView.findViewById(R.id.grid);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.addButton = (Button) convertView.findViewById(R.id.add_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Controller controller = list.get(position);

        //by default
        holder.grid.setNumColumns(2);

        //by default
        holder.addButton.setVisibility(View.VISIBLE);

        holder.grid.setAdapter(controller.getAdapter());
        holder.title.setText(controller.getTitle());

        controller.setGridView(holder.grid);
        controller.setAddButton(holder.addButton);
        controller.registerListeners();
        return null;
    }

    class ViewHolder {
        GridView grid;
        TextView title;
        Button addButton;
    }
}
