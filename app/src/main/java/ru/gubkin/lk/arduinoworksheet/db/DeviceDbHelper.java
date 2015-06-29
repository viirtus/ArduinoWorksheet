package ru.gubkin.lk.arduinoworksheet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ru.gubkin.lk.arduinoworksheet.component.list.device.DeviceItem;

/**
 * Created by ROOOOOOOT on 11.06.2015.
 */
public class DeviceDbHelper extends DbHelper<DeviceItem> {


    // Leds Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_INFO = "info";
    /**
     * Bluetooth - 0, TCP - 1
     */
    public static final String KEY_TYPE = "type";
    protected static final String TABLE = "device_list";
    protected static final String CREATE_STATEMENT =
            "CREATE TABLE " + TABLE + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_NAME + " TEXT,"
                    + KEY_TYPE + " TEXT,"
                    + KEY_INFO + " TEXT" + ")";

    public DeviceDbHelper(Context context) {
        super(context);
    }

    @Override
    public void insert(DeviceItem o) {
        //Not yet in store
        if (o.getId() == DeviceItem.DEFAULT_ID) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, o.getName());
            values.put(KEY_INFO, o.getInfo());
            values.put(KEY_TYPE, o.getType().getId());
            long id = db.insert(TABLE, null, values);
            o.setId((int) id);
        }
    }

    @Override
    public void update(DeviceItem o) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, o.getName());
        values.put(KEY_INFO, o.getInfo());
        values.put(KEY_TYPE, o.getType().getId());
        db.update(TABLE, values, KEY_ID + " = " + o.getId(), null);
    }

    @Override
    public void delete(DeviceItem o) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, KEY_ID + " = " + o.getId(), null);
    }

    @Override
    public Cursor getAll(int deviceId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "select " + KEY_ID + ", " + KEY_INFO + ", " + KEY_NAME + ", " +
                KEY_TYPE + " from " +
                TABLE;
        return db.rawQuery(query, new String[]{});
    }


}
