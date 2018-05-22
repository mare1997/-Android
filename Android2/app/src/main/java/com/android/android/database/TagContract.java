package com.android.android.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class TagContract {

    // Content authority
    public static final String CONTENT_AUTHORITY = "com.example.mare.mareapp";
    // URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    // Table name
    public static final String PATH_TAG = "tag";

    private TagContract(){

    }

    public static class TagEntry implements BaseColumns {
        // Complete URI for the Tag table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TAG);

        // Name of the database table for tag
        public static final String TABLE_NAME = "tag";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_POST_ID = "post_id";
    }

}
