package ru.gubkin.lk.arduinoworksheet.component.led;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.io.IOException;
import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.R;
import ru.gubkin.lk.arduinoworksheet.adapter.LedGridAdapter;
import ru.gubkin.lk.arduinoworksheet.bt.BluetoothHandler;
import ru.gubkin.lk.arduinoworksheet.component.Controller;
import ru.gubkin.lk.arduinoworksheet.db.LedDBHandler;
import ru.gubkin.lk.arduinoworksheet.util.Util;

/**
 * Created by root on 05.05.15.
 */
public class LEDController extends Controller {
    private View wrapper;
    private GridView ledGrid;
    private LedGridAdapter ledGridAdapter;
    private ArrayList<LED> leds;
    private LedDBHandler ledDb;
    private LEDObserver ledObserver;
    private BluetoothHandler bluetoothHandler;
    private Button addButton;

    public LEDController(Context context, BluetoothHandler handler) {
        super(context);
        ledDb = new LedDBHandler(context);
        ledObserver = new LEDObserver(ledDb, this);
        leds = LEDFactory.getSavedLed(ledDb, ledObserver);
        ledGridAdapter = new LedGridAdapter(context, leds);
        bluetoothHandler = handler;
    }

    protected void processLed(LED l) {

        String command;
        if (l.isActive()) command = l.getCommandOn();
        else command = l.getCommandOff();
        try {
            bluetoothHandler.sendData(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leds.add(LEDFactory.getNew(ledDb, ledObserver));
                notifyChange();
            }
        });
        ledGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LED led = (LED) ledGridAdapter.getItem(position);
                led.toggleState(true);
            }
        });
        ledGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LED led = (LED) ledGridAdapter.getItem(position);
                Dialog dialog = new LEDDialog(context, led);
                dialog.show();
                return true;
            }
        });
    }

    @Override
    public View getViewItem(LayoutInflater inflater, View convertView, ViewGroup parent) {
        if (wrapper == null) {
            wrapper = inflater.inflate(R.layout.led_grid, parent, false);
            addButton = (Button) wrapper.findViewById(R.id.led_add_button);

            ledGrid = (GridView) wrapper.findViewById(R.id.led_grid);
            ledGrid.getLayoutParams().height = (int) ((Math.ceil(leds.size() / 4.0) ) * Util.convertDpToPixel(75, context));
            ledGrid.setAdapter(ledGridAdapter);
            ledObserver.setAdapter(ledGridAdapter);
            ledObserver.setDisplayedLeds(leds);
            registerListeners();
        }

        return wrapper;
    }

    @Override
    public void notifyChange() {
        ledGridAdapter.notifyDataSetChanged();
        ledGrid.getLayoutParams().height = (int) ((Math.ceil(leds.size() / 4.0) ) * Util.convertDpToPixel(75, context));
    }


}
