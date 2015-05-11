package ru.gubkin.lk.arduinoworksheet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.gubkin.lk.arduinoworksheet.component.led.LED;

/**
 * Created by Андрей on 02.05.2015.
 *
 */
public class LedDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_NAME = "arduino_inner_db";

    private static final String TABLE_LED = "led_list";

    // Leds Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_COLOR = "color";
    private static final String KEY_STATE = "state";
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_DATA_KEY_ON = "data_key_on";
    private static final String KEY_DATA_KEY_OFF = "data_key_off";

    private static final String CREATE_STATEMENT =
            "CREATE TABLE " + TABLE_LED + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_TITLE + " TEXT,"
                    + KEY_COLOR + " INTEGER,"
                    + KEY_STATE + " INTEGER,"
                    + KEY_DATA_KEY_OFF + " TEXT,"
                    + KEY_DATA_KEY_ON + " TEXT" + ")";


    public LedDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LED);
        onCreate(db);
    }

    public void insertNew(LED l) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, l.getTitle());
        values.put(KEY_COLOR, l.getColor().getEngName());
        values.put(KEY_STATE, l.isActive() ? 1 : 0);
        values.put(KEY_DATA_KEY_ON, l.getCommandOn());
        values.put(KEY_DATA_KEY_OFF, l.getCommandOff());
        long id = db.insert(TABLE_LED, null, values);
        l.setId((int) id);
    }

    public void updateLed(LED l) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, l.getTitle());
        values.put(KEY_COLOR, l.getColor().getColorId());
        values.put(KEY_STATE, l.isActive() ? 1 : 0);
        values.put(KEY_DATA_KEY_ON, l.getCommandOn());
        values.put(KEY_DATA_KEY_OFF, l.getCommandOff());
        db.update(TABLE_LED, values, KEY_ID + " = " + l.getId(), null);
    }

    public void deleteLed (LED l) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_LED, KEY_ID + " = " + l.getId(), null);
    }

    public Cursor getAllSavedLed () {
        SQLiteDatabase db = getReadableDatabase();
        String query = "select " + KEY_ID + ", " + KEY_TITLE + ", " +
                KEY_COLOR + ", " + KEY_STATE + ", " + KEY_DATA_KEY_ON + ", " + KEY_DATA_KEY_OFF + " from " +
                TABLE_LED;
        Cursor c = db.rawQuery(query, new String[]{});
        return c;
    }


    public int getIdIndex () {
        return 0;
    }

    public int getTitleIndex() {
        return 1;
    }

    public int getColorIndex() {
        return 2;
    }

    public int getStateIndex() {
        return 3;
    }

    public int getDataKeyOffIndex() {
        return 4;
    }
    public int getDataKeyOnIndex() {
        return 5;
    }
}
