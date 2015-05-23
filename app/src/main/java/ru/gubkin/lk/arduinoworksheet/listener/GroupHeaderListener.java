package ru.gubkin.lk.arduinoworksheet.listener;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import ru.gubkin.lk.arduinoworksheet.adapter.GroupAdapter;
import ru.gubkin.lk.arduinoworksheet.component.Controller;
import ru.gubkin.lk.arduinoworksheet.component.GroupItem;

/**
 * Created by Андрей on 08.05.2015.
 */
public class GroupHeaderListener implements AdapterView.OnItemClickListener {
    private final static String TAG = "GROUP_LISTENER";
    private GroupAdapter adapter;
    public GroupHeaderListener(GroupAdapter adapter){
        this.adapter = adapter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupItem nextItem = (GroupItem) adapter.getItem(position + 1);
        Controller controller = nextItem.getController();
    }
}
