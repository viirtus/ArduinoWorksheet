package ru.gubkin.lk.arduinoworksheet.component.list.device;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Observer;

import ru.gubkin.lk.arduinoworksheet.db.DeviceDbHelper;

/**
 * Created by root on 22.06.15.
 */
public class DeviceFactory {



    public static ArrayList<DeviceItem> getAll(DeviceDbHelper db, Observer observer) {
        ArrayList<DeviceItem> list = new ArrayList<>();
        Cursor cursor = db.getAll(0);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(DeviceDbHelper.KEY_ID));
            int type = cursor.getInt(cursor.getColumnIndex(DeviceDbHelper.KEY_TYPE));
            String name = cursor.getString(cursor.getColumnIndex(DeviceDbHelper.KEY_NAME));
            String info = cursor.getString(cursor.getColumnIndex(DeviceDbHelper.KEY_INFO));
            DeviceType deviceType;
            switch (type) {
                case 0:
                    deviceType = DeviceType.BLUETOOTH;
                    break;
                case 1:
                    deviceType = DeviceType.TCP;
                    break;
                default:
                    deviceType = DeviceType.BLUETOOTH;
                    break;
            }
            DeviceItem item = new DeviceItem(id, deviceType, name, info);
            item.addObserver(observer);
            list.add(item);
            cursor.moveToNext();
        }
        return list;
    }
}
