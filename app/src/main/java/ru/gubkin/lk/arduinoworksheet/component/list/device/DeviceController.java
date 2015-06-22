package ru.gubkin.lk.arduinoworksheet.component.list.device;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;

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

    public DeviceController(Context context) {
        super(context, "", 0);
        db = new DeviceDbHelper(context);
        ArrayList<DeviceItem> rawItems = DeviceFactory.getAll(db, observer);
        items = prepareList(rawItems);
        adapter = new DevicesAdapter(context, items);
    }

    @Override
    public BaseAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void registerListeners() {

    }

    @Override
    public void unregisterListeners() {

    }

    @Override
    public void notifyChange() {

    }

    @Override
    public void updateComponent(DeviceItem o) {

    }

    @Override
    public void deleteComponent(DeviceItem o) {

    }

    @Override
    public void processComponent(DeviceItem o) {

    }

    public void add(DeviceItem item) {
        db.insert(item);
        if (item.getType() == DeviceType.BLUETOOTH) {
            items.add(1, new DeviceListItem(item));
        } else {
            items.add(tcpPointer, new DeviceListItem(item));
        }
        adapter.notifyDataSetChanged();
    }

    private ArrayList<ListItem> prepareList(ArrayList<DeviceItem> items) {
//        Collections.sort(items, new Comparator<DeviceItem>() {
//            @Override
//            public int compare(DeviceItem lhs, DeviceItem rhs) {
//                return lhs.getType();
//            }
//        });

        ArrayList<ListItem> prepared = new ArrayList<>();

        prepared.add(new HeaderListItem("Bluetooth"));

        for (DeviceItem item : items) {
            if (item.getType() == DeviceType.BLUETOOTH) prepared.add(new DeviceListItem(item));
        }
        prepared.add(new HeaderListItem("TCP/IP"));

        tcpPointer = items.size();

        for (DeviceItem item : items) {
            prepared.add(new DeviceListItem(item));
        }

        return prepared;
    }
}
