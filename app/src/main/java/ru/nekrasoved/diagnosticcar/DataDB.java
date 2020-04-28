package ru.nekrasoved.diagnosticcar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class DataDB {
    public static final int DATABASE_VERSION = 2;
    public static final String  DATABASE_NAME = "dataDB";
    public static final String  TABLE_LOGS = "logs";

    public static final String  KEY_ID = "_id";
    public static final String  KEY_DATE = "date";
    public static final String  KEY_OBOROTY_DVS = "engine_speed";
    public static final String  KEY_PRESSURE_WHEELS = "pressure_wheels";
    public static final String  KEY_VOLTAGE = "voltage";
    public static final String  KEY_TEMPERATURE = "temperature";
    public static final String  KEY_GAS_CONSUMPTION = "gas_cons";
    public static final String  KEY_PRESSURE_OIL= "pressure_oil";
    public static final String  KEY_BIENIE_RULYA = "rudder";
    public static final String  KEY_CAR_SHOCKS = "car_shocks";

    private static final String DB_CREATE =
            "create table " + TABLE_LOGS + " (" + KEY_ID + " integer primary key autoincrement," +
                    KEY_DATE + " text," + KEY_OBOROTY_DVS + " integer," + KEY_PRESSURE_WHEELS + " real," +
                    KEY_VOLTAGE + " real," + KEY_TEMPERATURE + " integer," + KEY_GAS_CONSUMPTION + " real," +
                    KEY_PRESSURE_OIL + " real," + KEY_BIENIE_RULYA + " integer,"
                    + KEY_CAR_SHOCKS + " integer" + ");";


    private final Context cont;

    private DBHelper helper;
    private SQLiteDatabase database;


    public DataDB(Context context) {
        cont = context;
    }

    // открыть подключение
    public void open() {
        helper = new DBHelper(cont, DATABASE_NAME, null, DATABASE_VERSION);
        database = helper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (helper!=null) helper.close();
    }

    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData() {
        return database.query(TABLE_LOGS, null, null, null, null, null, null);
    }


    // добавить запись в DB_TABLE


    public void addRec(String date, Integer oboroty_dvs, float pressure_wheels,
                       float voltage, Integer temperature, float gas_consumption,
                       float pressure_oil, boolean bienie_rulya, boolean car_shocks) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_DATE, date);
        cv.put(KEY_OBOROTY_DVS, oboroty_dvs);
        cv.put(KEY_PRESSURE_WHEELS, pressure_wheels);
        cv.put(KEY_VOLTAGE, voltage);
        cv.put(KEY_TEMPERATURE, temperature);
        cv.put(KEY_GAS_CONSUMPTION, gas_consumption);
        cv.put(KEY_PRESSURE_OIL, pressure_oil);
        cv.put(KEY_BIENIE_RULYA, bienie_rulya);
        cv.put(KEY_CAR_SHOCKS, car_shocks);

        database.insert(TABLE_LOGS, null, cv);
    }


    // удалить запись из DB_TABLE
    public void delRec(long id) {
        database.delete(TABLE_LOGS, KEY_ID + " = " + id, null);
    }


    // запрос в БД
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy,
                        String having, String orderBy) {
        return database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    // класс по созданию и управлению БД
    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + TABLE_LOGS);
            onCreate(db);
        }
    }
}
