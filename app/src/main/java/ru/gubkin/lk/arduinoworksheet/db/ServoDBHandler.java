package ru.gubkin.lk.arduinoworksheet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.gubkin.lk.arduinoworksheet.component.led.LED;
import ru.gubkin.lk.arduinoworksheet.component.servo.Servo;

/**
 * Created by Андрей on 09.05.2015.
 */
public class ServoDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "arduino_inner_db";

    private static final String TABLE_SERVO = "servo_list";

    // Leds Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
//    private static final String KEY_COLOR = "color";
    private static final String KEY_STATE = "state";
    private static final String KEY_MAX_VALUE = "max_value";
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_DELAY = "delay";
    private static final String KEY_DATA_KEY = "data_key";

    private static final String CREATE_STATEMENT =
            "CREATE TABLE " + TABLE_SERVO + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_TITLE + " TEXT,"
                    + KEY_STATE + " INTEGER,"
                    + KEY_MAX_VALUE + " INTEGER,"
                    + KEY_DELAY + " INTEGER,"
                    + KEY_DATA_KEY + " TEXT" + ")";

    public ServoDBHandler(Context context) {
        super(context, TABLE_SERVO, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVO);
        onCreate(db);
    }

    public void insertNew(Servo s) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, s.getName());
        values.put(KEY_STATE, s.getValue());
        values.put(KEY_MAX_VALUE, s.getMaxValue());
        values.put(KEY_DATA_KEY, s.getCommand());
        values.put(KEY_DELAY, s.getDelay());
        long id = db.insert(TABLE_SERVO, null, values);
        s.setId((int) id);
    }

    public void updateServo(Servo s) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, s.getName());
        values.put(KEY_STATE, s.getValue());
        values.put(KEY_MAX_VALUE, s.getMaxValue());
        values.put(KEY_DATA_KEY, s.getCommand());
        values.put(KEY_DELAY, s.getDelay());
        db.update(TABLE_SERVO, values, KEY_ID + " = " + s.getId(), null);
    }
    public void deleteServo (Servo s) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SERVO, KEY_ID + " = " + s.getId(), null);
    }
    public Cursor getAllSavedLed () {
        SQLiteDatabase db = getReadableDatabase();
        String query = "select " + KEY_ID + ", " + KEY_TITLE + ", " +
                KEY_DELAY + ", " + KEY_STATE + ", " + KEY_MAX_VALUE + ", " + KEY_DATA_KEY + " from " +
                TABLE_SERVO;
        return db.rawQuery(query, new String[]{});
    }
    public int getIdIndex () {
        return 0;
    }

    public int getTitleIndex() {
        return 1;
    }

    public int getStateIndex() {
        return 3;
    }

    public int getMaxValueIndex() {
        return 4;
    }

    public int getDelayIndex() {
        return 2;
    }

    public int getDataKeyIndex() {
        return 5;
    }

}
