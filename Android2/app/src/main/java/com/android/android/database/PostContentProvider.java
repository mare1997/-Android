package com.android.android.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class PostContentProvider extends ContentProvider {
    private PostSQLiteHelper database;

    private static final int POST = 10;
    private static final int POST_ID = 20;

    private static final String AUTHORITY = "rs.android2";

    private static final String POST_PATH = "post";

    public static final Uri CONTENT_URI_POST = Uri.parse("content://" + AUTHORITY + "/" + POST_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, POST_PATH, POST);
        sURIMatcher.addURI(AUTHORITY, POST_PATH + "/#", POST_ID);
    }

    public PostContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        int rowsDeleted = 0;
        switch (uriType) {
            case POST:
                rowsDeleted = sqlDB.delete(PostSQLiteHelper.TABLE_POST,
                        selection,
                        selectionArgs);
                break;
            case POST_ID:
                String idCinema = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(PostSQLiteHelper.TABLE_POST,
                            PostSQLiteHelper.COLUMN_ID + "=" + idCinema,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(PostSQLiteHelper.TABLE_POST,
                            PostSQLiteHelper.COLUMN_ID + "=" + idCinema
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri retVal = null;
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case POST:
                id = sqlDB.insert(PostSQLiteHelper.TABLE_POST, null, values);
                retVal = Uri.parse(POST_PATH + "/" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
    }

    @Override
    public boolean onCreate() {
        database = new PostSQLiteHelper(getContext());
        return true;
    }
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case POST_ID:
                // Adding the ID to the original query
                queryBuilder.appendWhere(PostSQLiteHelper.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                //$FALL-THROUGH$
            case POST:
                // Set the table
                queryBuilder.setTables(PostSQLiteHelper.TABLE_POST);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        int rowsUpdated = 0;
        switch (uriType) {
            case POST:
                rowsUpdated = sqlDB.update(PostSQLiteHelper.TABLE_POST,
                        values,
                        selection,
                        selectionArgs);
                break;
            case POST_ID:
                String idCinema = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(PostSQLiteHelper.TABLE_POST,
                            values,
                            PostSQLiteHelper.COLUMN_ID + "=" + idCinema,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(PostSQLiteHelper.TABLE_POST,
                            values,
                            PostSQLiteHelper.COLUMN_ID + "=" + idCinema
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }


}
