package ru.gubkin.lk.arduinoworksheet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ru.gubkin.lk.arduinoworksheet.component.servo.Servo;

/**
 * Created by Андрей on 09.05.2015.
 */
public class ServoDbHelper extends DbHelper<Servo> {

    // Leds Table Columns names
    public static final String KEY_ID = "id";

    public static final String KEY_TITLE = "title";
    public static final String KEY_STATE = "state";
    public static final String KEY_MAX_VALUE = "max_value";
    public static final String KEY_DEVICE_ID = "device_id";
    public static final String KEY_DELAY = "delay";
    public static final String KEY_DATA_KEY = "data_key";

    protected static final String TABLE = "linear_list";
    protected static final String CREATE_STATEMENT =
            "CREATE TABLE " + TABLE + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_TITLE + " TEXT,"
                    + KEY_STATE + " INTEGER,"
                    + KEY_DEVICE_ID + " INTEGER,"
                    + KEY_MAX_VALUE + " INTEGER,"
                    + KEY_DELAY + " INTEGER,"
                    + KEY_DATA_KEY + " TEXT" + ")";

    public ServoDbHelper(Context context) {
        super(context);
    }

    public void insert(Servo s) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, s.getName());
        values.put(KEY_DEVICE_ID, s.getDeviceId());
        values.put(KEY_STATE, s.getValue());
        values.put(KEY_MAX_VALUE, s.getMaxValue());
        values.put(KEY_DATA_KEY, s.getCommand());
        values.put(KEY_DELAY, s.getDelay());
        long id = db.insert(TABLE, null, values);
        s.setId((int) id);
    }

    public void update(Servo s) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, s.getName());
        values.put(KEY_DEVICE_ID, s.getDeviceId());
        values.put(KEY_STATE, s.getValue());
        values.put(KEY_MAX_VALUE, s.getMaxValue());
        values.put(KEY_DATA_KEY, s.getCommand());
        values.put(KEY_DELAY, s.getDelay());
        db.update(TABLE, values, KEY_ID + " = " + s.getId(), null);
    }

    public void delete(Servo s) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, KEY_ID + " = " + s.getId(), null);
    }

    public Cursor getAll(int deviceId) {

        SQLiteDatabase db = getReadableDatabase();
        String query = "select " + KEY_ID + ", " + KEY_TITLE + ", " + KEY_DEVICE_ID + ", " +
                KEY_DELAY + ", " + KEY_STATE + ", " + KEY_MAX_VALUE + ", " + KEY_DATA_KEY + " from " +
                TABLE + " where device_id = " + deviceId;
            return db.rawQuery(query, new String[]{});
    }

}
