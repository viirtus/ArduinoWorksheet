package ru.gubkin.lk.arduinoworksheet.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 22.06.15.
 */
public abstract class DbHelper<T> extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "arduino_inner_db_";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LedDbHelper.CREATE_STATEMENT);
        db.execSQL(SensorDbHelper.CREATE_STATEMENT);
        db.execSQL(ServoDbHelper.CREATE_STATEMENT);
        db.execSQL(DeviceDbHelper.CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LedDbHelper.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SensorDbHelper.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ServoDbHelper.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DeviceDbHelper.TABLE);
        onCreate(db);
    }

    /**
     * When necessary add component to db
     *
     * @param o object for add
     */
    public abstract void insert(T o);

    /**
     * When necessary update component to db
     *
     * @param o object for update
     */
    public abstract void update(T o);

    /**
     * When necessary delete component from table
     *
     * @param o object for delete
     */
    public abstract void delete(T o);

    /**
     * Fetching all records from table
     *
     * @return Cursor for all records
     */
    public abstract Cursor getAll(int deviceId);

}
