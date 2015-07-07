package ru.gubkin.lk.arduinoworksheet;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.adapter.ComponentsAdapter;
import ru.gubkin.lk.arduinoworksheet.component.Controller;
import ru.gubkin.lk.arduinoworksheet.component.led.LEDController;
import ru.gubkin.lk.arduinoworksheet.component.sensor.SensorController;
import ru.gubkin.lk.arduinoworksheet.component.servo.ServoController;
import ru.gubkin.lk.arduinoworksheet.component.terminal.TerminalController;
import ru.gubkin.lk.arduinoworksheet.connect.ConnectionHandler;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    public static final String DEVICE_ID_EXTRA = "DEVICE_ID";
    private ConnectionHandler handler;
    private ArrayList<Controller> controllers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        handler = MainActivity.getHandler();
        Context context = getActivity();
        ListView v = (ListView) inflater.inflate(R.layout.fragment_test, container, false);

        int deviceId = getActivity().getIntent().getIntExtra(DEVICE_ID_EXTRA, 0);

        LEDController ledController = new LEDController(context, handler, deviceId);
        ServoController servoController = new ServoController(context, handler, deviceId);

        SensorController sensorController = new SensorController(context, handler, deviceId);
        TerminalController terminalController = new TerminalController(context, handler, deviceId);

        controllers = new ArrayList<>();
        controllers.add(ledController);
        controllers.add(servoController);
        controllers.add(sensorController);
        controllers.add(terminalController);

        ComponentsAdapter componentsAdapter = new ComponentsAdapter(context, controllers);
        v.setAdapter(componentsAdapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        handler = MainActivity.getHandler();
        //register all listeners
        for (Controller controller : controllers) {
            try {
                controller.registerListeners();
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregister all listeners
        for (Controller controller : controllers) {
            try {
                //controller.unregisterListeners();
            } catch (Exception ignored) {
            }
        }
    }
}
