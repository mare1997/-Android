package com.android.android.tools;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.android.android.database.PostContentProvider;
import com.android.android.database.PostSQLiteHelper;

public class Utils {
    public static void initDB(Activity activity) {
        PostSQLiteHelper dbHelper = new PostSQLiteHelper(activity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        {
            ContentValues entry = new ContentValues();
            entry.put(PostSQLiteHelper.COLUMN_TITLE, "Marko");
            entry.put(PostSQLiteHelper.COLUMN_DESCRIPTION, "MArko je car!!!");
            entry.put(PostSQLiteHelper.COLUMN_DATE, "15.08.2017.");
            entry.put(PostSQLiteHelper.COLUMN_ID, 1);
            entry.put(PostSQLiteHelper.COLUMN_DISLIKES, 0);
            entry.put(PostSQLiteHelper.COLUMN_LIKES, 1255);


            activity.getContentResolver().insert(PostContentProvider.CONTENT_URI_POST, entry);

            entry = new ContentValues();
            entry.put(PostSQLiteHelper.COLUMN_TITLE, "Radojkovic");
            entry.put(PostSQLiteHelper.COLUMN_DESCRIPTION, "MArko je car!!!");
            entry.put(PostSQLiteHelper.COLUMN_DATE, "25.01.2018.");
            entry.put(PostSQLiteHelper.COLUMN_ID, 1);
            entry.put(PostSQLiteHelper.COLUMN_DISLIKES, 0);
            entry.put(PostSQLiteHelper.COLUMN_LIKES, 12);

            activity.getContentResolver().insert(PostContentProvider.CONTENT_URI_POST, entry);
        }

        db.close();
    }
}
