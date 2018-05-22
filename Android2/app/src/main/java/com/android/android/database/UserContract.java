package com.android.android.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class UserContract {
    // Content authority
    public static final String CONTENT_AUTHORITY = "com.example.mare.mareapp";
    // URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    // Table name
    public static final String PATH_USER = "user";

    private UserContract() {

    }

    public static class UserEntry implements BaseColumns {
        // Complete URI for the User table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USER);

        // Name of the database table for user
        public static final String TABLE_NAME = "User";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHOTO = "photo";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";

    }
}