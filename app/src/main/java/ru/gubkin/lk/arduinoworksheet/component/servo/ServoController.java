package ru.gubkin.lk.arduinoworksheet.component.servo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.R;
import ru.gubkin.lk.arduinoworksheet.adapter.ServoGridAdapter;
import ru.gubkin.lk.arduinoworksheet.component.Controller;
import ru.gubkin.lk.arduinoworksheet.connect.ConnectionHandler;
import ru.gubkin.lk.arduinoworksheet.db.ServoDBHandler;
import ru.gubkin.lk.arduinoworksheet.util.Util;

/**
 * Created by Андрей on 07.05.2015.
 */
public class ServoController extends Controller {
    private View wrapper;
    private LinearLayout layout;
    private ArrayList<Servo> items;
    private ConnectionHandler handler;
    private ServoObserver observer;
    private ServoDBHandler dbHandler;
    private ServoGridAdapter adapter;
    private GridView gridView;
    private Button addButton;
    private LayoutInflater inflater;
    public ServoController(Context context, ConnectionHandler handler) {
        super(context);
        this.handler = handler;
        items = new ArrayList<>();
        dbHandler = new ServoDBHandler(context);
        observer = new ServoObserver(this, dbHandler);
        items = ServoFactory.getSavedServo(dbHandler, observer);
        adapter = new ServoGridAdapter(context, items);
    }

    public void processServo(Servo servo) {

        String command = servo.getCommand() + servo.getValue();
        try {
            handler.sendData(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
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
        addButton.setOnClickListener(null);
    }

    @Override
    public View getViewItem(LayoutInflater inflater, View convertView, ViewGroup parent) {
        this.inflater = inflater;
        if (wrapper == null) {
            wrapper = inflater.inflate(R.layout.servo_grid, parent, false);
            addButton = (Button) wrapper.findViewById(R.id.servo_add_button);
            gridView = (GridView) wrapper.findViewById(R.id.servo_grid);
            gridView.getLayoutParams().height = (int) (Math.ceil(items.size() / 2.0) * Util.convertDpToPixel(165, context));
            gridView.setAdapter(adapter);
            registerListeners();
        }
        return wrapper;
    }

    public void deleteServo(Servo servo) {
        items.remove(servo);
    }

    public void notifyChange() {
        adapter.notifyDataSetChanged();
        gridView.getLayoutParams().height = (int) (Math.ceil(items.size() / 2.0) * Util.convertDpToPixel(155, context));
    }



}
