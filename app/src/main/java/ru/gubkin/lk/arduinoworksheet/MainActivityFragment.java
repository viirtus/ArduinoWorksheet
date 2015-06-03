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
import ru.gubkin.lk.arduinoworksheet.connect.ConnectionHandler;
import ru.gubkin.lk.arduinoworksheet.connect.bt.BluetoothHandler;
import ru.gubkin.lk.arduinoworksheet.component.Controller;
import ru.gubkin.lk.arduinoworksheet.component.GroupItem;
import ru.gubkin.lk.arduinoworksheet.component.ListItem;
import ru.gubkin.lk.arduinoworksheet.component.led.LEDController;
import ru.gubkin.lk.arduinoworksheet.component.sensor.SensorController;
import ru.gubkin.lk.arduinoworksheet.component.servo.ServoController;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private Context context;
    private ConnectionHandler handler;
    private ArrayList<Controller> controllers;
    public MainActivityFragment(ConnectionHandler handler) {
        this.handler = handler;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        ListView groupList = (ListView) v.findViewById(R.id.group_list);
        ArrayList<ListItem> items = new ArrayList<>();
        LEDController ledController = new LEDController(context, handler);

        items.add(new GroupItem(ledController));

        ServoController servoController = new ServoController(context, handler);

        items.add(new GroupItem(servoController));

        SensorController sensorController = new SensorController(context, handler);
        items.add(new GroupItem(sensorController));

        groupList.setWillNotDraw(false);

        GroupAdapter adapter = new GroupAdapter(context, items);
        groupList.setAdapter(adapter);

        controllers = new ArrayList<>();
        controllers.add(ledController);
        controllers.add(servoController);
        controllers.add(sensorController);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        //register all listeners
        for (Controller controller: controllers) {
            controller.registerListeners();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregister all listeners
        for (Controller controller: controllers) {
            controller.unregisterListeners();
        }
    }
}
