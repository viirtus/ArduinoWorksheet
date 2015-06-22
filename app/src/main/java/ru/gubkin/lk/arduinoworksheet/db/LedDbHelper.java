package ru.gubkin.lk.arduinoworksheet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ru.gubkin.lk.arduinoworksheet.component.led.LED;

/**
 * Created by Андрей on 02.05.2015.
 *
 */
public class LedDbHelper extends DbHelper<LED> {



    // Leds Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_COLOR = "color";
    public static final String KEY_STATE = "state";
    public static final String KEY_DEVICE_ID = "device_id";
    public static final String KEY_DATA_KEY_ON = "data_key_on";
    public static final String KEY_DATA_KEY_OFF = "data_key_off";
    protected static final String TABLE = "led_list";
    protected static final String CREATE_STATEMENT =
            "CREATE TABLE " + TABLE + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_TITLE + " TEXT,"
                    + KEY_COLOR + " INTEGER,"
                    + KEY_DEVICE_ID + " INTEGER,"
                    + KEY_STATE + " INTEGER,"
                    + KEY_DATA_KEY_OFF + " TEXT,"
                    + KEY_DATA_KEY_ON + " TEXT" + ")";


    public LedDbHelper(Context context) {
        super(context);
    }


    public void insert(LED l) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, l.getTitle());
        values.put(KEY_DEVICE_ID, l.getDeviceId());
        values.put(KEY_COLOR, l.getColor().getColorId());
        values.put(KEY_STATE, l.isActive() ? 1 : 0);
        values.put(KEY_DATA_KEY_ON, l.getCommandOn());
        values.put(KEY_DATA_KEY_OFF, l.getCommandOff());
        long id = db.insert(TABLE, null, values);
        l.setId((int) id);
    }

    public void update(LED l) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, l.getTitle());
        values.put(KEY_DEVICE_ID, l.getDeviceId());
        values.put(KEY_COLOR, l.getColor().getColorId());
        values.put(KEY_STATE, l.isActive() ? 1 : 0);
        values.put(KEY_DATA_KEY_ON, l.getCommandOn());
        values.put(KEY_DATA_KEY_OFF, l.getCommandOff());
        db.update(TABLE, values, KEY_ID + " = " + l.getId(), null);
    }

    public void delete(LED l) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, KEY_ID + " = " + l.getId(), null);
    }

    public Cursor getAll(int deviceId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "select " + KEY_ID + ", " + KEY_TITLE + ", " + KEY_DEVICE_ID + ", " +
                KEY_COLOR + ", " + KEY_STATE + ", " + KEY_DATA_KEY_ON + ", " + KEY_DATA_KEY_OFF + " from " +
                TABLE + " where device_id = " + deviceId;
        return db.rawQuery(query, new String[]{});
    }

}
