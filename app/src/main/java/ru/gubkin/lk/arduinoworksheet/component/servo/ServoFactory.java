package ru.gubkin.lk.arduinoworksheet.component.servo;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Observer;

import ru.gubkin.lk.arduinoworksheet.component.led.LED;
import ru.gubkin.lk.arduinoworksheet.db.ServoDBHandler;

/**
 * Created by Андрей on 09.05.2015.
 */
public class ServoFactory  {
    public static Servo getNew(ServoDBHandler db, Observer observer){
        Servo servo = new Servo("Сервопривод", "14:", 180, 0, 20);
        db.insertNew(servo);
        servo.addObserver(observer);
        return servo;
    }

    public static ArrayList<Servo> getSavedServo(ServoDBHandler db, Observer observer) {
        ArrayList<Servo> list = new ArrayList<>();
        Cursor cursor = db.getAllSavedLed();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(db.getIdIndex());
            int state = cursor.getInt(db.getStateIndex());
            int maxValue = cursor.getInt(db.getMaxValueIndex());
            int delay = cursor.getInt(db.getDelayIndex());
            String title = cursor.getString(db.getTitleIndex());
            String dataKey = cursor.getString(db.getDataKeyIndex());

            Servo led = new Servo(
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
