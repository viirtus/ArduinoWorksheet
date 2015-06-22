package ru.gubkin.lk.arduinoworksheet.component.led;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.MainActivity;
import ru.gubkin.lk.arduinoworksheet.adapter.LedGridAdapter;
import ru.gubkin.lk.arduinoworksheet.component.Controller;
import ru.gubkin.lk.arduinoworksheet.connect.ConnectionHandler;
import ru.gubkin.lk.arduinoworksheet.db.LedDBHandler;
import ru.gubkin.lk.arduinoworksheet.connect.MessageHandler;

/**
 * Created by root on 05.05.15.
 */
public class LEDController extends Controller<LED> {
    private static final String TITLE = "Бинарные Устройства";
    private LedGridAdapter adapter;
    private ArrayList<LED> items;
    private LedDBHandler dbHandler;
    private ConnectionHandler connectionHandler;

    private static final int HEIGHT = 125;

    public LEDController(Context context, ConnectionHandler handler) {
        super(context, TITLE);
        dbHandler = new LedDBHandler(context);
        connectionHandler = handler;
        MessageHandler messageHandler = new MessageHandler();
        if (!MainActivity.debug)
            connectionHandler.registerMessageHandler(messageHandler);
        items = LEDFactory.getSavedLed(dbHandler, observer, messageHandler);
        adapter = new LedGridAdapter(context, items);
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
                items.add(LEDFactory.getNew(dbHandler, observer));
                notifyChange();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LED led = (LED) adapter.getItem(position);
                led.toggleState(true);
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LED led = (LED) adapter.getItem(position);
                Dialog dialog = new LEDDialog(context, led);
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


    @Override
    public void notifyChange() {
        adapter.notifyDataSetChanged();
        initHeight(HEIGHT, 2, items.size());
    }

    @Override
    public void updateComponent(LED led) {
        dbHandler.updateLed(led);
        notifyChange();
    }

    @Override
    public void deleteComponent(LED led) {
        dbHandler.deleteLed(led);
        items.remove(led);
        notifyChange();
    }

    @Override
    public void processComponent(LED led) {
        String command;
        if (led.isActive()) command = led.getCommandOn();
        else command = led.getCommandOff();
        connectionHandler.sendData(command);

        notifyChange();
    }


}
