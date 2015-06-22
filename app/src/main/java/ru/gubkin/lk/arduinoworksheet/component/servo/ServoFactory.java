package ru.gubkin.lk.arduinoworksheet.component.servo;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Observer;

import ru.gubkin.lk.arduinoworksheet.db.ServoDbHelper;

/**
 * Created by Андрей on 09.05.2015.
 */
public class ServoFactory  {
    public static Servo getNew(ServoDbHelper db, Observer observer, int deviceId) {
        Servo servo = new Servo(0, deviceId, "Сервопривод", "14:", 180, 0, 20);
        db.insert(servo);
        servo.addObserver(observer);
        return servo;
    }

    public static ArrayList<Servo> getSavedServo(ServoDbHelper db, Observer observer, int deviceId) {
        ArrayList<Servo> list = new ArrayList<>();
        Cursor cursor = db.getAll(deviceId);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(ServoDbHelper.KEY_ID));
            int state = cursor.getInt(cursor.getColumnIndex(ServoDbHelper.KEY_STATE));
            int maxValue = cursor.getInt(cursor.getColumnIndex(ServoDbHelper.KEY_MAX_VALUE));
            int delay = cursor.getInt(cursor.getColumnIndex(ServoDbHelper.KEY_DELAY));
            String title = cursor.getString(cursor.getColumnIndex(ServoDbHelper.KEY_TITLE));
            String dataKey = cursor.getString(cursor.getColumnIndex(ServoDbHelper.KEY_DATA_KEY));

            Servo led = new Servo(
                    id,
                    deviceId,
                    title,
                    dataKey,
                    maxValue,
                    state,
                    delay
            );
            led.setId(id);

            led.addObserver(observer);
            list.add(led);
            cursor.moveToNext();
        }

        return list;
    }

}
