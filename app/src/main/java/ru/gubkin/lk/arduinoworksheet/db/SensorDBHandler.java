package ru.gubkin.lk.arduinoworksheet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.gubkin.lk.arduinoworksheet.component.sensor.Sensor;

/**
 * Created by root on 11.05.15.
 */
public class SensorDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "arduino_inner_db_";

    private static final String TABLE_SENSOR = "sensor_list";

    // Leds Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    //    private static final String KEY_COLOR = "color";
    private static final String KEY_MAX_VALUE = "max_value";
    private static final String KEY_MIN_VALUE = "min_value";
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_START_PATTERN = "start_pattern";
    private static final String KEY_END_PATTERN = "end_pattern";

    private static final String CREATE_STATEMENT =
            "CREATE TABLE " + TABLE_SENSOR + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_TITLE + " TEXT,"
                    + KEY_MAX_VALUE + " INTEGER,"
                    + KEY_MIN_VALUE + " INTEGER,"
                    + KEY_START_PATTERN + " TEXT,"
                    + KEY_END_PATTERN + " TEXT" + ")";

    public SensorDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENSOR);
        onCreate(db);
    }

    public void insertNew(Sensor s) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, s.getName());
        values.put(KEY_MAX_VALUE, s.getMaxValue());
        values.put(KEY_MIN_VALUE, s.getMinValue());
        values.put(KEY_START_PATTERN, s.getStartPattern());
        values.put(KEY_END_PATTERN, s.getEndPattern());
        long id = db.insert(TABLE_SENSOR, null, values);
        s.setId((int) id);
    }

    public void updateSensor (Sensor s) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, s.getName());
        values.put(KEY_MAX_VALUE, s.getMaxValue());
        values.put(KEY_MIN_VALUE, s.getMinValue());
        values.put(KEY_START_PATTERN, s.getStartPattern());
        values.put(KEY_END_PATTERN, s.getEndPattern());
        db.update(TABLE_SENSOR, values, KEY_ID + " = " + s.getId(), null);
    }
    public void deleteSensor (Sensor s) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SENSOR, KEY_ID + " = " + s.getId(), null);
    }
    public Cursor getAllSavedLed () {
        SQLiteDatabase db = getReadableDatabase();
        String query = "select " +
                KEY_ID + ", " +
                KEY_TITLE + ", " +
                KEY_MAX_VALUE + ", " +
                KEY_MIN_VALUE + ", " +
                KEY_START_PATTERN + ", " +
                KEY_END_PATTERN + " from " +
                TABLE_SENSOR;
        try {
            return db.rawQuery(query, new String[]{});
        } catch (Exception e) {
            onCreate(getWritableDatabase());
            return getAllSavedLed();
        }
    }
    public int getIdIndex () {
        return 0;
    }

    public int getTitleIndex() {
        return 1;
    }

    public int getMaxValueIndex() {
        return 2;
    }
    public int getMinValueIndex() {
        return 3;
    }
    public int getStartPatternIndex() {
        return 4;
    }
    public int getEndPatternIndex() {
        return 5;
    }

}
