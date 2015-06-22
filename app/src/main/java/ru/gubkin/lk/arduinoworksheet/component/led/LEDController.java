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
import ru.gubkin.lk.arduinoworksheet.connect.MessageHandler;
import ru.gubkin.lk.arduinoworksheet.db.LedDbHelper;

/**
 * Created by root on 05.05.15.
 */
public class LEDController extends Controller<LED> {
    private static final String TITLE = "�������� ����������";
    private static final int HEIGHT = 125;
    private LedGridAdapter adapter;
    private ArrayList<LED> items;
    private LedDbHelper dbHandler;
    private ConnectionHandler connectionHandler;

    public LEDController(Context context, ConnectionHandler handler, int deviceId) {
        super(context, TITLE, deviceId);
        dbHandler = new LedDbHelper(context);
        connectionHandler = handler;
        MessageHandler messageHandler = new MessageHandler();
        if (!MainActivity.debug)
            connectionHandler.registerMessageHandler(messageHandler);
        items = LEDFactory.getSavedLed(dbHandler, observer, messageHandler, deviceId);
        adapter = new LedGridAdapter(context, items);
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
                items.add(LEDFactory.getNew(dbHandler, observer, deviceId));
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
        dbHandler.update(led);
        notifyChange();
    }

    @Override
    public void deleteComponent(LED led) {
        dbHandler.delete(led);
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
