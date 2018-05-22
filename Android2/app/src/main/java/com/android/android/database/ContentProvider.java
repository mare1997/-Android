package com.android.android.database;

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
        Log.e("blabla", "blabla");
        mDbHelper = DatabaseHelper.getInstance(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.e("blabla", "blabla");
       // SQLiteOpenHelper openHelper = DatabaseHelper.getInstance(getContext());
       // SQLiteDatabase database = openHelper.getWritableDatabase();
        this.openHelper =  DatabaseHelper.getInstance(getContext());
       open();
        //Cursor
        Cursor cursor = null;
        // Match the correct URI
        int match = sUriMatcher.match(uri);
        switch (match) {
           case COMMENTS:
                // This will return all comments with the given projection, selection, selection arguments and sort order.
               cursor = database.query(CommentContract.CommentEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;

            case USERS:
                // This will return all users with the given projection, selection, selection arguments and sort order.
                cursor = database.query(UserContract.UserEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;

            case TAGS:
                // This will return all tags with the given projection, selection, selection arguments and sort order.
                cursor = database.query(TagContract.TagEntry.TABLE_NAME, projection, selection,selectionArgs, null, null, sortOrder);
                break;

            case POSTS:
                   Log.e("blabla", "blabla");
                // This will return all posts with the given projection, selection, selection arguments and sort order.
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
            // Since we're inserting a single user, there's no need to match USER_ID URI
            case USERS:
                return insertUser(uri, values);
            // Since we're inserting a single comment, there's no need to match COMMENT_ID URI
          /*  case COMMENTS:
                return insertComment(uri, values);*/
            // Since we're inserting a single post, there's no need to match POST_ID URI
            case POSTS:
                return insertPost(uri, values);
            // Since we're inserting a single tag, there's no need to match TAG_ID URI
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

        /*if (title == null || description == null) {
            throw new IllegalArgumentException("Posts arguments cannot be empty");
        }*/
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
    //insertComment, insertTag
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        database.delete(PostContract.PostEntry.TABLE_NAME, selection, selectionArgs);
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }


}
