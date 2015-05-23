package ru.gubkin.lk.arduinoworksheet.component.servo;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.R;
import ru.gubkin.lk.arduinoworksheet.adapter.ServoGridAdapter;
import ru.gubkin.lk.arduinoworksheet.bt.BluetoothHandler;
import ru.gubkin.lk.arduinoworksheet.component.Controller;
import ru.gubkin.lk.arduinoworksheet.db.ServoDBHandler;
import ru.gubkin.lk.arduinoworksheet.util.Util;

/**
 * Created by Андрей on 07.05.2015.
 */
public class ServoController extends Controller {
    private View wrapper;
    private LinearLayout layout;
    private ArrayList<Servo> items;
    private BluetoothHandler handler;
    private ServoObserver observer;
    private ServoDBHandler dbHandler;
    private ServoGridAdapter adapter;
    private GridView gridView;
    private Button addButton;
    private LayoutInflater inflater;
    public ServoController(Context context, BluetoothHandler handler) {
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
    public View getViewItem(LayoutInflater inflater, View convertView, ViewGroup parent) {
        this.inflater = inflater;
        if (wrapper == null) {
            wrapper = inflater.inflate(R.layout.servo_grid, parent, false);
            addButton = (Button) wrapper.findViewById(R.id.servo_add_button);
            gridView = (GridView) wrapper.findViewById(R.id.servo_grid);
            gridView.getLayoutParams().height = (int) (Math.ceil(items.size() / 2.0) * Util.convertDpToPixel(160, context));
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
        gridView.getLayoutParams().height = (int) (Math.ceil(items.size() / 2.0) * Util.convertDpToPixel(150, context));
    }



}
