package com.android.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PostSQLiteHelper   extends SQLiteOpenHelper {

    public static final String TABLE_POST = "post";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_LIKES = "likes";
    public static final String COLUMN_DISLIKES = "dislikes";

    private static final String DATABASE_NAME = "post.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DB_CREATE = "create table "
            + TABLE_POST + "("
            + COLUMN_ID  + " integer primary key autoincrement , "
            + COLUMN_TITLE + " text, "
            + COLUMN_DESCRIPTION + " text, "
            + COLUMN_PHOTO + " integer"
            + COLUMN_DATE + "date"
            + COLUMN_LOCATION + "text"
            + COLUMN_LIKES + "integer"
            +COLUMN_LIKES + "integer"
            + COLUMN_DISLIKES + "integer"

            + ")";





    public PostSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POST);
        onCreate(db);
    }

}
