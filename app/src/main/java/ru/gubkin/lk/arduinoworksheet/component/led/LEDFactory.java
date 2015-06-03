package ru.gubkin.lk.arduinoworksheet.component.led;

import android.database.Cursor;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.db.LedDBHandler;
import ru.gubkin.lk.arduinoworksheet.util.MessageHandler;

/**
 * Created by Андрей on 02.05.2015.
 */
public class LEDFactory {
    public static LED getNew(LedDBHandler db, LEDObserver observer) {
        LED led = new LED(LED.LedColors.BLUE, false, "Новый LED", -1, "1:1", "1:0");
        db.insertNew(led);
        led.addObserver(observer);
        return led;
    }

    public static ArrayList<LED> getSavedLed(LedDBHandler db, LEDObserver observer, MessageHandler messageHandler) {
        ArrayList<LED> list = new ArrayList<>();
        Cursor cursor = db.getAllSavedLed();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(db.getIdIndex());
            int state = cursor.getInt(db.getStateIndex());
            int color = cursor.getInt(db.getColorIndex());
            String title = cursor.getString(db.getTitleIndex());
            String dataKeyOn = cursor.getString(db.getDataKeyOnIndex());
            String dataKeyOff = cursor.getString(db.getDataKeyOffIndex());

            LED led = new LED(
                    idToEnumColor(color),
                    state == 1,
                    title,
                    id,
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

    protected static LED.LedColors stringToEnumColor(String color) {
        switch (color) {
            case "Red":
                return LED.LedColors.RED;
            case "Blue":
                return LED.LedColors.BLUE;
            case "Green":
                return LED.LedColors.GREEN;
            default:
                return LED.LedColors.UNDEFINED;
        }
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
                return LED.LedColors.UNDEFINED;
        }
    }
}
