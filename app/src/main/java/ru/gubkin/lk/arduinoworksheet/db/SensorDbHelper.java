package ru.gubkin.lk.arduinoworksheet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ru.gubkin.lk.arduinoworksheet.component.sensor.Sensor;

/**
 * Created by root on 11.05.15.
 */
public class SensorDbHelper extends DbHelper<Sensor> {

    // Leds Table Columns names
    public static final String KEY_ID = "id";

    public static final String KEY_TITLE = "title";
    public static final String KEY_MAX_VALUE = "max_value";
    public static final String KEY_MIN_VALUE = "min_value";
    public static final String KEY_DEVICE_ID = "device_id";
    public static final String KEY_START_PATTERN = "start_pattern";
    public static final String KEY_END_PATTERN = "end_pattern";

    protected static final String TABLE = "sensor_list";
    protected static final String CREATE_STATEMENT =
            "CREATE TABLE " + TABLE + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_TITLE + " TEXT,"
                    + KEY_MAX_VALUE + " INTEGER,"
                    + KEY_MIN_VALUE + " INTEGER,"
                    + KEY_DEVICE_ID + " INTEGER,"
                    + KEY_START_PATTERN + " TEXT,"
                    + KEY_END_PATTERN + " TEXT" + ")";

    public SensorDbHelper(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void insert(Sensor s) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, s.getName());
        values.put(KEY_MAX_VALUE, s.getMaxValue());
        values.put(KEY_DEVICE_ID, s.getDeviceId());
        values.put(KEY_MIN_VALUE, s.getMinValue());
        values.put(KEY_START_PATTERN, s.getStartPattern());
        values.put(KEY_END_PATTERN, s.getEndPattern());
        long id = db.insert(TABLE, null, values);
        s.setId((int) id);
    }

    public void update(Sensor s) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, s.getName());
        values.put(KEY_DEVICE_ID, s.getDeviceId());
        values.put(KEY_MAX_VALUE, s.getMaxValue());
        values.put(KEY_MIN_VALUE, s.getMinValue());
        values.put(KEY_START_PATTERN, s.getStartPattern());
        values.put(KEY_END_PATTERN, s.getEndPattern());
        db.update(TABLE, values, KEY_ID + " = " + s.getId(), null);
    }

    public void delete(Sensor s) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, KEY_ID + " = " + s.getId(), null);
    }

    public Cursor getAll(int deviceId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "select " +
                KEY_ID + ", " +
                KEY_TITLE + ", " +
                KEY_DEVICE_ID + ", " +
                KEY_MAX_VALUE + ", " +
                KEY_MIN_VALUE + ", " +
                KEY_START_PATTERN + ", " +
                KEY_END_PATTERN + " from " +
                TABLE + " where device_id = " + deviceId;
        return db.rawQuery(query, new String[]{});
    }

}
