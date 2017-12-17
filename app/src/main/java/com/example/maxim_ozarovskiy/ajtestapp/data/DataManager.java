package com.example.maxim_ozarovskiy.ajtestapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.maxim_ozarovskiy.ajtestapp.model.DbModelEx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */

public class DataManager {
    private static final String TAG = "RestManagerContract";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;


    public class MyFieldDB {

        private static final String DATABASE_NAME = "CurrencyResponseDB";

        private static final String CURRENCY_TABLE = "CurencyResponce";
        public static final String RESPONSE_ID = "id";
        public static final String TARGET_CURRENCY_NAME = "target_currency_name";
        public static final String TARGET_CURRENCY_COURSE = "target_currency_course";
        public static final String BASE_CURRENCY_VOLUME = "base_currency_volume";
        public static final String BASE_CURRENCY_NAME = "base_currency_name";
        public static final String TARGET_CURRENCY_VALUE = "target_currency_volume";
        public static final String TARGET_CURRENCY_CODE = "target_currency_code";
        public static final String BASE_CURRENCY_CODE = "base_currency_code";
        public static final String DAY_TIME_REQUEST = "day_time_request";

        private static final int DATABASE_VERSION = 1;

        private static final String CREATE_PRODUCT_TABLE =
                "CREATE TABLE IF NOT EXISTS " + CURRENCY_TABLE + " (" +
                        RESPONSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        TARGET_CURRENCY_NAME + "," +
                        TARGET_CURRENCY_COURSE + "," +
                        BASE_CURRENCY_VOLUME + "," +
                        BASE_CURRENCY_NAME + "," +
                        BASE_CURRENCY_CODE + "," +
                        TARGET_CURRENCY_VALUE + "," +
                        TARGET_CURRENCY_CODE + "," +
                        DAY_TIME_REQUEST + "," +
                        " UNIQUE (" + RESPONSE_ID + "));";
    }

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, MyFieldDB.DATABASE_NAME, null, MyFieldDB.DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, MyFieldDB.CREATE_PRODUCT_TABLE);
            db.execSQL(MyFieldDB.CREATE_PRODUCT_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + MyFieldDB.CURRENCY_TABLE);
            onCreate(db);
        }
    }

    public DataManager(Context ctx) {
        this.mCtx = ctx;
    }

    public DataManager open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long saveNewConverterRequest(String targetName,
                                 String targetCourse,
                                 String baseVolume,
                                 String baseName,
                                 String baseCode,
                                 String targetVolume,
                                 String targetCode,
                                 String dayTimeRequest) {

        ContentValues initialValues = new ContentValues();

        initialValues.put(MyFieldDB.TARGET_CURRENCY_NAME, targetName);
        initialValues.put(MyFieldDB.TARGET_CURRENCY_COURSE, targetCourse);
        initialValues.put(MyFieldDB.BASE_CURRENCY_VOLUME, baseVolume);
        initialValues.put(MyFieldDB.BASE_CURRENCY_NAME, baseName);
        initialValues.put(MyFieldDB.BASE_CURRENCY_CODE, baseCode);
        initialValues.put(MyFieldDB.TARGET_CURRENCY_VALUE, targetVolume);
        initialValues.put(MyFieldDB.TARGET_CURRENCY_CODE, targetCode);
        initialValues.put(MyFieldDB.DAY_TIME_REQUEST, dayTimeRequest);

        return mDb.insert(MyFieldDB.CURRENCY_TABLE, null, initialValues);
    }

    public void delConverterRequestRecord(long id) {
        mDb.delete(MyFieldDB.CURRENCY_TABLE, MyFieldDB.RESPONSE_ID + " = " + id, null);
    }

    public List<DbModelEx> getHistory() {
        List<DbModelEx> newModelList = new ArrayList<DbModelEx>();
        String query = "select * from " + MyFieldDB.CURRENCY_TABLE;
        mDb = mDbHelper.getWritableDatabase();
        Cursor cursor = mDb.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                DbModelEx newModelEx = new DbModelEx();
                newModelEx.setId(cursor.getInt(0));
                newModelEx.setTargetCurrencyName(cursor.getString(1));
                newModelEx.setTargetCyrrencyCourse(cursor.getString(2));
                newModelEx.setBaseCurrencyValue(cursor.getString(3));
                newModelEx.setBaseCurrencyName(cursor.getString(4));
                newModelEx.setBaseCurrencyCode(cursor.getString(5));
                newModelEx.setTargetCurrencyValue(cursor.getString(6));
                newModelEx.setTargetCurrencyCode(cursor.getString(7));
                newModelEx.setDayTimeRequest(cursor.getString(8));
                newModelList.add(newModelEx);
            } while (cursor.moveToNext());
        }

        Log.d("history_menu data", newModelList.toString());
        return newModelList;
    }
}
