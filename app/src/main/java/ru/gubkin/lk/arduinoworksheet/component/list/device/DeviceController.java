package ru.gubkin.lk.arduinoworksheet.component.list.device;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.MainActivity;
import ru.gubkin.lk.arduinoworksheet.adapter.DevicesAdapter;
import ru.gubkin.lk.arduinoworksheet.component.Controller;
import ru.gubkin.lk.arduinoworksheet.db.DeviceDbHelper;

/**
 * Created by root on 22.06.15.
 */
public class DeviceController extends Controller<DeviceItem> {
    private DeviceDbHelper db;
    private DevicesAdapter adapter;
    private ArrayList<ListItem> items;
    private int tcpPointer;
    private ListView list;

    public DeviceController(Context context) {
        super(context, "", 0);
        db = new DeviceDbHelper(context);
        ArrayList<DeviceItem> rawItems = DeviceFactory.getAll(db, observer);
        items = prepareList(rawItems);
        adapter = new DevicesAdapter(context, items);
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
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem item = items.get(position);
                if (item.getDeviceItem().getType() == DeviceType.BLUETOOTH) {
                    add(item.getDeviceItem(), true);
                    ((MainActivity) context).tryToConnectBluetooth(item.getInfo());
                } else {
                    ((MainActivity) context).tryToConnectTcp(item.getInfo());
                }
            }
        });
        final DeviceController me = this;
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DeviceItem item = items.get(position).getDeviceItem();
                if (item.getType() == DeviceType.TCP) {
                    me.updateDialog(item);
                }
                return true;
            }
        });
    }

    @Override
    public void unregisterListeners() {

    }

    @Override
    public void notifyChange() {
        ArrayList<DeviceItem> rawItems = DeviceFactory.getAll(db, observer);
        ArrayList<ListItem> items_ = prepareList(rawItems);
        items.clear();
        items.addAll(items_);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateComponent(DeviceItem o) {
        db.update(o);
        notifyChange();
    }

    @Override
    public void deleteComponent(DeviceItem o) {
        db.delete(o);
        notifyChange();
    }

    @Override
    public void processComponent(DeviceItem o) {

    }

    public void setList(ListView list) {
        this.list = list;
        list.setAdapter(adapter);
    }

    public void add(DeviceItem item, boolean store) {
        if (store) db.insert(item);

        //already have
        for (ListItem listItem : items) {
            DeviceItem deviceItem = listItem.getDeviceItem();
            if (deviceItem != null) {
                if (deviceItem.equals(item)) return;
            }
        }
        item.addObserver(observer);
        if (item.getType() == DeviceType.BLUETOOTH) {
            items.add(1, new DeviceListItem(item));
            tcpPointer++;
        } else {
            items.add(tcpPointer, new DeviceListItem(item));
        }
        adapter.notifyDataSetChanged();
    }

    public void updateDialog(DeviceItem item) {
        Dialog dialog = new DeviceDialog(context, item);
        dialog.show();
    }

    private ArrayList<ListItem> prepareList(ArrayList<DeviceItem> items) {

        ArrayList<ListItem> prepared = new ArrayList<>();

        prepared.add(new HeaderListItem("Bluetooth"));

        for (DeviceItem item : items) {
            if (item.getType() == DeviceType.BLUETOOTH) prepared.add(new DeviceListItem(item));
        }
        prepared.add(new HeaderListItem("TCP/IP"));

        tcpPointer = prepared.size();

        for (DeviceItem item : items) {
            if (item.getType() == DeviceType.TCP) prepared.add(new DeviceListItem(item));
        }

        return prepared;
    }
}
