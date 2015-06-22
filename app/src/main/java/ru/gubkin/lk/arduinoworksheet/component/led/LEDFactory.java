package ru.gubkin.lk.arduinoworksheet.component.led;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Observer;

import ru.gubkin.lk.arduinoworksheet.connect.MessageHandler;
import ru.gubkin.lk.arduinoworksheet.db.LedDbHelper;

/**
 * Created by ROOOOOOOOOOOOOOOOOOOOOOOOOOOOT on 02.05.2015.
 */
public class LEDFactory {
    public static LED getNew(LedDbHelper db, Observer observer, int deviceId) {
        LED led = new LED(-1, deviceId, LED.LedColors.BLUE, false, "Новый LED", "1:1", "1:0");
        db.insert(led);
        led.addObserver(observer);
        return led;
    }

    public static ArrayList<LED> getSavedLed(LedDbHelper db, Observer observer, MessageHandler messageHandler, int deviceId) {
        ArrayList<LED> list = new ArrayList<>();
        Cursor cursor = db.getAll(deviceId);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(LedDbHelper.KEY_ID));
            int state = cursor.getInt(cursor.getColumnIndex(LedDbHelper.KEY_STATE));
            int color = cursor.getInt(cursor.getColumnIndex(LedDbHelper.KEY_COLOR));
            String title = cursor.getString(cursor.getColumnIndex(LedDbHelper.KEY_TITLE));
            String dataKeyOn = cursor.getString(cursor.getColumnIndex(LedDbHelper.KEY_DATA_KEY_ON));
            String dataKeyOff = cursor.getString(cursor.getColumnIndex(LedDbHelper.KEY_DATA_KEY_OFF));


            LED led = new LED(
                    id,
                    deviceId,
                    idToEnumColor(color),
                    state == 1,
                    title,
                    dataKeyOn,
                    dataKeyOff
            );
            led.addObserver(observer);
            messageHandler.registerListeners(led);
            list.add(led);
            cursor.moveToNext();
        }
        return list;
    }


    protected static LED.LedColors idToEnumColor(int id) {
        switch (id) {
            case 0:
                return LED.LedColors.RED;
            case 1:
                return LED.LedColors.GREEN;
            case 2:
                return LED.LedColors.BLUE;
            default:
                return LED.LedColors.RED;
        }
    }
}
