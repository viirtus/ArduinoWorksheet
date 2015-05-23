package ru.gubkin.lk.arduinoworksheet.component.sensor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.MainActivity;
import ru.gubkin.lk.arduinoworksheet.R;
import ru.gubkin.lk.arduinoworksheet.bt.BluetoothHandler;
import ru.gubkin.lk.arduinoworksheet.component.Controller;
import ru.gubkin.lk.arduinoworksheet.component.servo.RadialScaleView;
import ru.gubkin.lk.arduinoworksheet.db.SensorDBHandler;
import ru.gubkin.lk.arduinoworksheet.util.MessageHandler;

/**
 * Created by root on 11.05.15.
 */
public class SensorController extends Controller {
    private final BluetoothHandler bluetoothHandler;
    private ArrayList<Sensor> items;
    private View wrapper;
    private LayoutInflater inflater;
    private SensorDBHandler dbHandler;
    private SensorObserver observer;
    private MessageHandler messageHandler;
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

        items = SensorFactory.getSavedServo(dbHandler, messageHandler, observer);
    }

    @Override
    public void registerListeners() {

    }

    @Override
    public View getViewItem(LayoutInflater inflater, View convertView, ViewGroup parent) {
        if (wrapper == null) {
            this.inflater = inflater;
            wrapper = inflater.inflate(R.layout.linear_list, parent, false);
            registerListeners();
            renderList();
        }
        return wrapper;
    }

    private void renderList() {
        layout = (LinearLayout) wrapper.findViewById(R.id.wrapper_lv);
        layout.removeAllViews();
        for(final Sensor sensor : items) {
            View row = inflater.inflate(R.layout.sensor_item_radiate, layout, false);
            RadialScaleView view = (RadialScaleView) row.findViewById(R.id.sensor_one);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    SensorDialog dialog = new SensorDialog(context, sensor);
                    dialog.show();
                    return false;
                }
            });
            sensor.setSensorView(view);
            layout.addView(row);
        }
    }

    public void notifyChange() {
        renderList();
    }

    public void deleteSensor(Sensor sensor) {
        items.remove(sensor);
    }

    public void registerAddButton(Button button) {
        this.button = button;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.add(SensorFactory.getNew(dbHandler, messageHandler, observer));
                notifyChange();
            }
        });
    }
}
