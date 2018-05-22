package com.android.android.database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseHelper extends SQLiteAssetHelper{


    private static DatabaseHelper mInstance = null;
    /**
     * name file of database
     */
    public static final String DATABASE_NAME = "news.db";
    /**
     * Database version
     */
    public static final int DATABASE_VERSION = 1;
    public final String LOG_TAG = getClass().getSimpleName();

    public static DatabaseHelper getInstance(Context ctx) {

        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }
    /**
     * Constructor
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

   /*@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }*/
}
