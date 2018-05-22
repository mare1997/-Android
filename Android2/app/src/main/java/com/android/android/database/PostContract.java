package com.android.android.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class PostContract {

    // Content authority
    public static final String CONTENT_AUTHORITY = "com.example.mare.mareapp";
    // URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    // Table name
    public static final String PATH_POST = "post";

    private PostContract(){

    }

    public static class PostEntry implements BaseColumns {
        // Complete URI for the Post table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_POST);
        //Name of the database table
        public static final String TABLE_NAME= "Post";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PHOTO = "photo";
        public static final String COLUMN_AUTHOR_ID = "author_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_LIKES = "likes";
        public static final String COLUMN_DISLIKES = "dislikes";
     //   public static final String COLUMN_TAGS = "tags";
     //   public static final String COLUMN_COMMENTS = "comments";
    }

}
