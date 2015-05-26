package ru.gubkin.lk.arduinoworksheet.component.sensor;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.MainActivity;
import ru.gubkin.lk.arduinoworksheet.R;
import ru.gubkin.lk.arduinoworksheet.adapter.SensorGridAdapter;
import ru.gubkin.lk.arduinoworksheet.bt.BluetoothHandler;
import ru.gubkin.lk.arduinoworksheet.component.Controller;
import ru.gubkin.lk.arduinoworksheet.db.SensorDBHandler;
import ru.gubkin.lk.arduinoworksheet.util.MessageHandler;
import ru.gubkin.lk.arduinoworksheet.util.Util;

/**
 * Created by root on 11.05.15.
 */
public class SensorController extends Controller {
    private final BluetoothHandler bluetoothHandler;
    private ArrayList<Sensor> items;
    private View wrapper;
    private GridView gridView;
    private Button addButton;
    private LayoutInflater inflater;
    private SensorDBHandler dbHandler;
    private SensorObserver observer;
    private MessageHandler messageHandler;
    private SensorGridAdapter adapter;
    private Button button;
    private LinearLayout layout;

    public SensorController(Context context, BluetoothHandler bluetoothHandler) {
        super(context);
        this.bluetoothHandler = bluetoothHandler;
        dbHandler = new SensorDBHandler(context);
        observer = new SensorObserver(dbHandler, this);
        messageHandler = new MessageHandler();

        if (!MainActivity.debug)
        bluetoothHandler.registerMessageHandler(messageHandler);

        items = SensorFactory.getSavedSensor(dbHandler, messageHandler, observer);

        adapter = new SensorGridAdapter(context, items);

    }

    @Override
    public void registerListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public View getViewItem(LayoutInflater inflater, View convertView, ViewGroup parent) {
        if (wrapper == null) {
            this.inflater = inflater;
            wrapper = inflater.inflate(R.layout.sensor_grid, parent, false);
            addButton = (Button) wrapper.findViewById(R.id.sensor_add_button);
            gridView = (GridView) wrapper.findViewById(R.id.sensor_grid);
            gridView.getLayoutParams().height = (int) (Math.ceil(items.size() / 2.0) * Util.convertDpToPixel(110, context));
            gridView.setAdapter(adapter);
            registerListeners();
        }
        return wrapper;
    }

    public void notifyChange() {
        adapter.notifyDataSetChanged();
        gridView.getLayoutParams().height = (int) (Math.ceil(items.size() / 2.0) * Util.convertDpToPixel(110, context));
    }

    public void deleteSensor(Sensor sensor) {
        items.remove(sensor);
    }


}
