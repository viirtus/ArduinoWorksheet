package ru.gubkin.lk.arduinoworksheet.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Андрей on 11.06.2015.
 */
public class DeviceDbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "arduino_inner_db_";

    private static final String TABLE_LED = "device_list";

    // Leds Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PORT = "port";

    private static final String CREATE_STATEMENT =
            "CREATE TABLE " + TABLE_LED + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_NAME + " TEXT,"
                    + KEY_ADDRESS + " TEXT,"
                    + KEY_PORT + " TEXT" + ")";

    public DeviceDbHandler (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
