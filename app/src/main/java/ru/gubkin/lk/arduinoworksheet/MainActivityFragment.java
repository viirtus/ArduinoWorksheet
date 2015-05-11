package ru.gubkin.lk.arduinoworksheet;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.adapter.GroupAdapter;
import ru.gubkin.lk.arduinoworksheet.bt.BluetoothHandler;
import ru.gubkin.lk.arduinoworksheet.component.Controller;
import ru.gubkin.lk.arduinoworksheet.component.GroupHeader;
import ru.gubkin.lk.arduinoworksheet.component.GroupItem;
import ru.gubkin.lk.arduinoworksheet.component.ListItem;
import ru.gubkin.lk.arduinoworksheet.component.led.LEDController;
import ru.gubkin.lk.arduinoworksheet.component.servo.ServoController;
import ru.gubkin.lk.arduinoworksheet.listener.GroupHeaderListener;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private Context context;
    private BluetoothHandler handler;
    private boolean debug = true;
    private ArrayList<Controller> controllers;
    public MainActivityFragment() {
        handler = MainActivity.getBluetoothHandler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        ListView groupList = (ListView) v.findViewById(R.id.group_list);
        ArrayList<ListItem> items = new ArrayList<>();
        LEDController ledController = new LEDController(context, handler);
        GroupHeader ledHeader = new GroupHeader("Светодиоды", ledController);
        items.add(ledHeader);
        items.add(new GroupItem(ledController));

        ServoController servoController = new ServoController(context, handler);
        GroupHeader servoHeader = new GroupHeader("Сервоприводы", servoController);

        items.add(servoHeader);

        items.add(new GroupItem(servoController));
        groupList.setWillNotDraw(false);

        GroupAdapter adapter = new GroupAdapter(context, items);
        groupList.setAdapter(adapter);
        groupList.setOnItemClickListener(new GroupHeaderListener(adapter));

        controllers = new ArrayList<>();
        controllers.add(ledController);
        controllers.add(servoController);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
//        for (Controller controller: controllers) {
//            controller.registerListeners();
//        }
        if (!debug) {
            try {
                handler.tryToConnect(((MainActivity) context).getConnectedDevice().getAddress());
            } catch (Exception e) {
                e.printStackTrace();
                ((MainActivity) context).connectFallback();
            }
        }
    }
}
