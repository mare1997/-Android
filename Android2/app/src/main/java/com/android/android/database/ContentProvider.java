package com.android.android.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class ContentProvider extends android.content.ContentProvider {

    public static final String LOG_TAG = ContentProvider.class.getSimpleName();
    /**
     * URI matcher code for the whole Users table
     */
    private static final int USERS = 100;
    /**
     * URI matcher code for a single User
     */
    private static final int USER_ID = 101;

    /**
     * URI matcher for the whole Comments table
     */
    private static final int COMMENTS = 200;

    /**
     * URI matcher for a single COMMENT
     */
    private static final int COMMENT_ID = 201;

    /**
     * URI matcher for the whole Tags table
     */
    private static final int TAGS = 300;

    /**
     * URI matcher for a single TAG
     */
    private static final int TAG_ID = 301;

    /**
     * URI matcher for the whole Posts table
     */
    private static final int POSTS = 400;

    /**
     * URI matcher for a single POST
     */
    private static final int POST_ID = 401;

    /**
     * Root URI
     */
    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(UserContract.CONTENT_AUTHORITY, UserContract.PATH_USER, USERS);
        sUriMatcher.addURI(UserContract.CONTENT_AUTHORITY, UserContract.PATH_USER + "/#", USER_ID);

        sUriMatcher.addURI(CommentContract.CONTENT_AUTHORITY, CommentContract.PATH_COMMENT, COMMENTS);
        sUriMatcher.addURI(CommentContract.CONTENT_AUTHORITY, CommentContract.PATH_COMMENT + "/#", COMMENT_ID);


        sUriMatcher.addURI(TagContract.CONTENT_AUTHORITY, TagContract.PATH_TAG, TAGS);
        sUriMatcher.addURI(TagContract.CONTENT_AUTHORITY, TagContract.PATH_TAG + "/#", TAG_ID);

        sUriMatcher.addURI(PostContract.CONTENT_AUTHORITY, PostContract.PATH_POST, POSTS);
        sUriMatcher.addURI(PostContract.CONTENT_AUTHORITY, PostContract.PATH_POST + "/#", POST_ID);
    }

    Context context = getContext();
    private DatabaseHelper mDbHelper;
    public SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;

    /**
     * Open the database connection
     *
     * @return
     */
    public void open() {

        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection
     *
     * @return
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    @Override
    public boolean onCreate() {

        mDbHelper = DatabaseHelper.getInstance(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        this.openHelper =  DatabaseHelper.getInstance(getContext());
        open();
        Cursor cursor = null;
        int match = sUriMatcher.match(uri);
        switch (match) {
           case COMMENTS:
               cursor = database.query(CommentContract.CommentEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;

            case USERS:
                cursor = database.query(UserContract.UserEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;

            case TAGS:
                cursor = database.query(TagContract.TagEntry.TABLE_NAME, projection, selection,selectionArgs, null, null, sortOrder);
                break;

            case POSTS:
                cursor = database.query(PostContract.PostEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;

           default:
                   throw new IllegalArgumentException("Cannot query unknown URI" + uri);
        }
        //close();
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case USERS:
                return insertUser(uri, values);
           case COMMENTS:
                return insertComment(uri, values);
            case POSTS:
                return insertPost(uri, values);

           /* case TAGS:
                return insertTag(uri, values);*/

            default:
                throw new IllegalArgumentException("Insertion is not supported for: " + uri);

        }
    }

    private Uri insertUser(Uri uri, ContentValues values){
        //DATA VALIDATION
        String name = values.getAsString(UserContract.UserEntry.COLUMN_NAME);
        String photo = values.getAsString(UserContract.UserEntry.COLUMN_PHOTO);
        String username = values.getAsString(UserContract.UserEntry.COLUMN_USERNAME);
        String password = values.getAsString(UserContract.UserEntry.COLUMN_PASSWORD);
        if( name == null || username == null || password == null){
            throw new IllegalArgumentException("User arguments cannot be empty");
        }
        // Gain write access to the database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Insert a new user
        long id = database.insert(UserContract.UserEntry.TABLE_NAME, null, values);
        // Check if insertion is successful, -1 = failed;
        if(id == -1){
            Toast.makeText(getContext(), "Failed to add a new user", Toast.LENGTH_SHORT).show();
            return null;
        }
        return ContentUris.withAppendedId(uri, id);
    }
    private Uri insertComment(Uri uri, ContentValues values) {
        //DATA VALIDATION
        String title = values.getAsString(CommentContract.CommentEntry.COLUMN_TITLE);
        String description = values.getAsString(CommentContract.CommentEntry.COLUMN_DESCRIPTION);
        String postId = values.getAsString(CommentContract.CommentEntry.COLUMN_POST_ID);
        String authorId = values.getAsString(CommentContract.CommentEntry.COLUMN_AUTHOR_ID);
        String data = values.getAsString(CommentContract.CommentEntry.COLUMN_DATE);

        String likes = values.getAsString(CommentContract.CommentEntry.COLUMN_LIKES);
        String dislikes = values.getAsString(CommentContract.CommentEntry.COLUMN_DISLIKES);


        //Gain write access to the database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        //Insert a new post
        Long id = database.insert(CommentContract.CommentEntry.TABLE_NAME, null, values);
        //check if insertion is successful, -1 = failed
        if (id == -1) {
            Toast.makeText(getContext(), "Failed to add new comment", Toast.LENGTH_SHORT).show();
            return null;
        }
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertPost(Uri uri, ContentValues values) {
        //DATA VALIDATION
        String title = values.getAsString(PostContract.PostEntry.COLUMN_TITLE);
        String description = values.getAsString(PostContract.PostEntry.COLUMN_DESCRIPTION);
        String photo = values.getAsString(PostContract.PostEntry.COLUMN_PHOTO);
        String authorId = values.getAsString(PostContract.PostEntry.COLUMN_AUTHOR_ID);
        String data = values.getAsString(PostContract.PostEntry.COLUMN_DATE);
        String location = values.getAsString(PostContract.PostEntry.COLUMN_LOCATION);
        String likes = values.getAsString(PostContract.PostEntry.COLUMN_LIKES);
        String dislikes = values.getAsString(PostContract.PostEntry.COLUMN_DISLIKES);


        //Gain write access to the database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        //Insert a new post
        Long id = database.insert(PostContract.PostEntry.TABLE_NAME, null, values);
        //check if insertion is successful, -1 = failed
        if (id == -1) {
            Toast.makeText(getContext(), "Failed to add new post", Toast.LENGTH_SHORT).show();
            return null;
        }
        return ContentUris.withAppendedId(uri, id);
    }
    // insertTag
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (match){
            case POST_ID:
                String idPost = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = database.delete(PostContract.PostEntry.TABLE_NAME,
                            PostContract.PostEntry._ID + "=" + idPost,
                            null);
                } else {
                    rowsDeleted = database.delete(PostContract.PostEntry._ID,
                            PostContract.PostEntry._ID + "=" + idPost
                                    + " and "
                                    + selection,
                            selectionArgs);
                }break;
            case COMMENT_ID:
                String idComm = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = database.delete(CommentContract.CommentEntry.TABLE_NAME,
                            CommentContract.CommentEntry._ID + "=" + idComm,
                            null);
                } else {
                    rowsDeleted = database.delete(CommentContract.CommentEntry._ID,
                            CommentContract.CommentEntry._ID + "=" + idComm
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }


        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = 0;
        switch (match){
            case POST_ID:
                String idPOst = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = database.update(PostContract.PostEntry.TABLE_NAME,
                            values,
                            PostContract.PostEntry._ID + "=" + idPOst,
                            null);
                } else {
                    rowsUpdated = database.update(PostContract.PostEntry.TABLE_NAME,
                            values,
                            PostContract.PostEntry._ID + "=" + idPOst
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            case COMMENT_ID:
                String idComm = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = database.update(CommentContract.CommentEntry.TABLE_NAME,
                            values,
                            CommentContract.CommentEntry._ID + "=" + idComm,
                            null);
                } else {
                    rowsUpdated = database.update(CommentContract.CommentEntry.TABLE_NAME,
                            values,
                            CommentContract.CommentEntry._ID + "=" + idComm
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
