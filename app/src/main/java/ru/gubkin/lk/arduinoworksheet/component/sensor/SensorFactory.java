package ru.gubkin.lk.arduinoworksheet.component.sensor;

import android.database.Cursor;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.db.SensorDBHandler;
import ru.gubkin.lk.arduinoworksheet.util.MessageHandler;

/**
 * Created by root on 11.05.15.
 */
public class SensorFactory {
    public static Sensor getNew(SensorDBHandler db, MessageHandler handler, SensorObserver observer){
        Sensor sensor = new Sensor("Сенсор", "", "", 0, 1024, 0);
        db.insertNew(sensor);
        sensor.addObserver(observer);
        handler.registerListeners(sensor);

        return sensor;
    }

    public static ArrayList<Sensor> getSavedServo(SensorDBHandler db, MessageHandler handler, SensorObserver observer) {
        ArrayList<Sensor> list = new ArrayList<>();
        Cursor cursor = db.getAllSavedLed();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(db.getIdIndex());
            int maxValue = cursor.getInt(db.getMaxValueIndex());
            int minValue = cursor.getInt(db.getMinValueIndex());
            String title = cursor.getString(db.getTitleIndex());
            String startPattern = cursor.getString((db.getStartPatternIndex()));
            String endPattern = cursor.getString((db.getEndPatternIndex()));

            Sensor sensor = new Sensor(
                    title,
                    startPattern,
                    endPattern,
                    id,
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
