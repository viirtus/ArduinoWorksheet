package ru.gubkin.lk.arduinoworksheet.component.sensor;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Observer;

import ru.gubkin.lk.arduinoworksheet.connect.MessageHandler;
import ru.gubkin.lk.arduinoworksheet.db.SensorDbHelper;

/**
 * Created by root on 11.05.15.
 */
public class SensorFactory {
    public static Sensor getNew(SensorDbHelper db, MessageHandler handler, Observer observer, int deviceId) {
        Sensor sensor = new Sensor(0, deviceId, "Сенсор", "", "", 1024, 0);
        db.insert(sensor);
        sensor.addObserver(observer);
        handler.registerListeners(sensor);

        return sensor;
    }

    public static ArrayList<Sensor> getSavedSensor(SensorDbHelper db, MessageHandler handler, Observer observer, int deviceId) {
        ArrayList<Sensor> list = new ArrayList<>();
        Cursor cursor = db.getAll(deviceId);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(SensorDbHelper.KEY_ID));
            int maxValue = cursor.getInt(cursor.getColumnIndex(SensorDbHelper.KEY_MAX_VALUE));
            int minValue = cursor.getInt(cursor.getColumnIndex(SensorDbHelper.KEY_MIN_VALUE));
            String title = cursor.getString(cursor.getColumnIndex(SensorDbHelper.KEY_TITLE));
            String startPattern = cursor.getString(cursor.getColumnIndex(SensorDbHelper.KEY_START_PATTERN));
            String endPattern = cursor.getString(cursor.getColumnIndex(SensorDbHelper.KEY_END_PATTERN));

            Sensor sensor = new Sensor(id,
                    deviceId,
                    title,
                    startPattern,
                    endPattern,
                    maxValue,
                    minValue
            );
            sensor.addObserver(observer);
            handler.registerListeners(sensor);
            list.add(sensor);
            cursor.moveToNext();
        }

        return list;
    }
}
