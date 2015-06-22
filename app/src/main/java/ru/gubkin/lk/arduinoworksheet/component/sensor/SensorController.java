package ru.gubkin.lk.arduinoworksheet.component.sensor;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.MainActivity;
import ru.gubkin.lk.arduinoworksheet.adapter.SensorGridAdapter;
import ru.gubkin.lk.arduinoworksheet.component.Controller;
import ru.gubkin.lk.arduinoworksheet.connect.ConnectionHandler;
import ru.gubkin.lk.arduinoworksheet.db.SensorDBHandler;
import ru.gubkin.lk.arduinoworksheet.connect.MessageHandler;

/**
 * Created by root on 11.05.15.
 */
public class SensorController extends Controller<Sensor> {
    private static final String TITLE = "Сенсоры";
    private ArrayList<Sensor> items;
    private SensorDBHandler dbHandler;
    private MessageHandler messageHandler;
    private SensorGridAdapter adapter;

    private static final int HEIGHT = 125;

    public SensorController(Context context, ConnectionHandler connectionHandler) {
        super(context, TITLE);
        dbHandler = new SensorDBHandler(context);
        messageHandler = new MessageHandler();

        if (!MainActivity.debug) connectionHandler.registerMessageHandler(messageHandler);

        items = SensorFactory.getSavedSensor(dbHandler, messageHandler, observer);

        adapter = new SensorGridAdapter(context, items);

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
                Sensor sensor = SensorFactory.getNew(dbHandler, messageHandler, observer);
                items.add(sensor);
                notifyChange();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog dialog = new SensorDetailDialog(context, items.get(position));
                dialog.show();
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog dialog = new SensorDialog(context, items.get(position));
                dialog.show();
                return true;
            }
        });
    }

    @Override
    public void unregisterListeners() {
        button.setOnClickListener(null);
        gridView.setOnItemClickListener(null);
        gridView.setOnItemLongClickListener(null);
    }


    public void notifyChange() {
        adapter.notifyDataSetChanged();
        initHeight(HEIGHT, 2, items.size());
    }

    @Override
    public void updateComponent(Sensor sensor) {
        dbHandler.updateSensor(sensor);
        notifyChange();
    }

    @Override
    public void deleteComponent(Sensor sensor) {
        dbHandler.deleteSensor(sensor);
        items.remove(sensor);
        notifyChange();
    }

    @Override
    public void processComponent(Sensor sensor) {

    }


}
