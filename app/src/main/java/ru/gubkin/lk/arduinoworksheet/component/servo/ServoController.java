package ru.gubkin.lk.arduinoworksheet.component.servo;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.adapter.ServoGridAdapter;
import ru.gubkin.lk.arduinoworksheet.component.Controller;
import ru.gubkin.lk.arduinoworksheet.connect.ConnectionHandler;
import ru.gubkin.lk.arduinoworksheet.db.ServoDbHelper;

/**
 * Created by Андрей on 07.05.2015.
 */
public class ServoController extends Controller<Servo> {
    private static final String TITLE = "Аналоговые устройства";
    private static final int HEIGHT = 155;
    private ArrayList<Servo> items;
    private ConnectionHandler handler;
    private ServoDbHelper dbHandler;
    private ServoGridAdapter adapter;

    public ServoController(Context context, ConnectionHandler handler, int deviceId) {
        super(context, TITLE, deviceId);
        this.handler = handler;
        items = new ArrayList<>();
        dbHandler = new ServoDbHelper(context);
        items = ServoFactory.getSavedServo(dbHandler, observer, deviceId);
        adapter = new ServoGridAdapter(context, items);
    }
    @Override
    public boolean isEmpty() {
        return items.size() == 0;
    }
    @Override
    public BaseAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void registerListeners() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Servo newOne = ServoFactory.getNew(dbHandler, observer, deviceId);
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
        validateView(HEIGHT, 2, items.size());
    }

    @Override
    public void updateComponent(Servo servo) {
        dbHandler.update(servo);
        notifyChange();
    }

    @Override
    public void deleteComponent(Servo servo) {
        dbHandler.delete(servo);
        items.remove(servo);
        notifyChange();
    }

    @Override
    public void processComponent(Servo servo) {
        String command = servo.getCommand() + servo.getValue();
        handler.sendData(command);
    }


}
