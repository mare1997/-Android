package com.android.android.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class CommentContract {
    // Content authority
    public static final String CONTENT_AUTHORITY = "com.example.mare.mareapp";
    // URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    // Table name
    public static final String PATH_COMMENT = "comment";

    private CommentContract(){

    }

    public static class CommentEntry implements BaseColumns {
        // Complete URI for the Comment table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_COMMENT);
        // Name of the database table for comment
        public static final String TABLE_NAME = "Comment";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_AUTHOR_ID = "author_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_POST_ID = "post_id";
        public static final String COLUMN_LIKES = "likes";
        public static final String COLUMN_DISLIKES = "dislikes";

    }


}
