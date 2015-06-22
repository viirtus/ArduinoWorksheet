package ru.gubkin.lk.arduinoworksheet.component.servo;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.adapter.ServoGridAdapter;
import ru.gubkin.lk.arduinoworksheet.component.Controller;
import ru.gubkin.lk.arduinoworksheet.connect.ConnectionHandler;
import ru.gubkin.lk.arduinoworksheet.db.ServoDBHandler;

/**
 * Created by Андрей on 07.05.2015.
 */
public class ServoController extends Controller<Servo> {
    private static final String TITLE = "Аналоговые устройства";
    private ArrayList<Servo> items;
    private ConnectionHandler handler;
    private ServoDBHandler dbHandler;
    private ServoGridAdapter adapter;

    private static final int HEIGHT = 155;

    public ServoController(Context context, ConnectionHandler handler) {
        super(context, TITLE);
        this.handler = handler;
        items = new ArrayList<>();
        dbHandler = new ServoDBHandler(context);
        items = ServoFactory.getSavedServo(dbHandler, observer);
        adapter = new ServoGridAdapter(context, items);
    }

    @Override
    public BaseAdapter getGridAdapter() {
        return adapter;
    }

    @Override
    public void registerListeners() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Servo newOne = ServoFactory.getNew(dbHandler, observer);
                items.add(newOne);
                notifyChange();
            }
        });
    }

    @Override
    public void unregisterListeners() {
        button.setOnClickListener(null);
    }


    public void notifyChange() {
        adapter.notifyDataSetChanged();
        initHeight(HEIGHT, 2, items.size());
    }

    @Override
    public void updateComponent(Servo servo) {
        dbHandler.updateServo(servo);
        notifyChange();
    }

    @Override
    public void deleteComponent(Servo servo) {
        dbHandler.deleteServo(servo);
        items.remove(servo);
    }

    @Override
    public void processComponent(Servo servo) {
        String command = servo.getCommand() + servo.getValue();
        handler.sendData(command);
    }


}
