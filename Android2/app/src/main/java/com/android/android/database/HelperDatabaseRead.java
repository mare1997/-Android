package com.android.android.database;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.android.android.model.Comment;
import com.android.android.model.Post;
import com.android.android.model.Tag;
import com.android.android.model.User;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HelperDatabaseRead {
    private ArrayList<Post> postsss = new ArrayList<>();

    public ArrayList<Post> loadPostsFromDatabase(Activity activity){
        //Definisi projekciju pomocu koje ces da ucitas podatke iz tabele post
        String[] projection = {
                PostContract.PostEntry._ID,
                PostContract.PostEntry.COLUMN_TITLE,
                PostContract.PostEntry.COLUMN_DESCRIPTION,
                PostContract.PostEntry.COLUMN_PHOTO,
                PostContract.PostEntry.COLUMN_AUTHOR_ID,
                PostContract.PostEntry.COLUMN_DATE,
                PostContract.PostEntry.COLUMN_LOCATION,
                PostContract.PostEntry.COLUMN_LIKES,
                PostContract.PostEntry.COLUMN_DISLIKES
        };
        String sortOrder = PostContract.PostEntry._ID + " DESC";
        Cursor cursor = activity.getContentResolver().query(PostContract.PostEntry.CONTENT_URI, projection, null, null, null);

        ArrayList<Post> posts = new ArrayList<>();
        //Uzmi relevantne podatke za svaki post
        try{
            //Prvo nadji indeks svake kolone
            int idColumnIndex = cursor.getColumnIndex(PostContract.PostEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(PostContract.PostEntry.COLUMN_TITLE);
            int descriptionColumnIndex = cursor.getColumnIndex(PostContract.PostEntry.COLUMN_DESCRIPTION);
            int photoColumnIndex = cursor.getColumnIndex(PostContract.PostEntry.COLUMN_PHOTO);
            int authorIdColumnIndex = cursor.getColumnIndex(PostContract.PostEntry.COLUMN_AUTHOR_ID);
            int dateColumnIndex = cursor.getColumnIndex(PostContract.PostEntry.COLUMN_DATE);
            int locationColumnIndex = cursor.getColumnIndex(PostContract.PostEntry.COLUMN_LOCATION);
            int likesColumnIndex = cursor.getColumnIndex(PostContract.PostEntry.COLUMN_LIKES);
            int dislikesColumnIndex = cursor.getColumnIndex(PostContract.PostEntry.COLUMN_DISLIKES);

            while (cursor.moveToNext()){
                int currentId = cursor.getInt(idColumnIndex);
                String title = cursor.getString(titleColumnIndex);
                String description = cursor.getString(descriptionColumnIndex);
                String photo = cursor.getString(photoColumnIndex);
                int authorId = Integer.parseInt(cursor.getString(authorIdColumnIndex));
                String date = cursor.getString(dateColumnIndex);
                Date cDate= convertStringToDate(date);
                String location = cursor.getString(locationColumnIndex);
                int likes = Integer.parseInt(cursor.getString(likesColumnIndex));
                int dislikes = Integer.parseInt(cursor.getString(dislikesColumnIndex));
                User author = null;
                for(User u:loadUsersFromDatabase(activity)){
                    if(u.getId() == authorId){
                        author=u;
                    }
                }
                ArrayList<Comment> commentsAll = loadCommentsFromDatabase(activity);
                ArrayList<Comment> commentsFiltered = new ArrayList<>();
                for(Comment comment : commentsAll) {

                    if (comment != null) {

                        if (comment.getPost() == currentId) {
                            commentsFiltered.add(comment);

                        }

                    }
                }/*
                ArrayList<Tag> tagsAll = loadTagsFromDatabase(activity);
                ArrayList<Tag> tagsFiltered = new ArrayList<>();
                for(Tag tag : tagsAll) {

                    if (tag != null) {
                        for(Post p:tag.getPosts()) {
                            if (p.getId() == currentId) {
                                tagsFiltered.add(tag);

                            }
                        }
                    }
                }*/

                    posts.add(new Post(currentId,title,description,null,author,cDate,null,null,commentsFiltered,likes,dislikes));
                }



        } finally {
            cursor.close();
        }
        return posts;
    }
    public Date convertStringToDate(String dtStart){

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = format.parse(dtStart);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
  //  private ArrayList<Comment> comments = new ArrayList<>();

    public ArrayList<Comment> loadCommentsFromDatabase(Activity activity){
        //Definisi projekciju pomocu koje ces da ucitas podatke iz tabele comment
        String[] projection = {
                CommentContract.CommentEntry._ID,
                CommentContract.CommentEntry.COLUMN_TITLE,
                CommentContract.CommentEntry.COLUMN_DESCRIPTION,
                CommentContract.CommentEntry.COLUMN_AUTHOR_ID,
                CommentContract.CommentEntry.COLUMN_DATE,
                CommentContract.CommentEntry.COLUMN_POST_ID,
                CommentContract.CommentEntry.COLUMN_LIKES,
                CommentContract.CommentEntry.COLUMN_DISLIKES
        };
        String sortOrder = CommentContract.CommentEntry._ID + "DESC";
        Cursor cursor = activity.getContentResolver().query(CommentContract.CommentEntry.CONTENT_URI, projection, null,null, null);

        ArrayList<Comment> comments = new ArrayList<>();
        try {
            int idColumnIndex = cursor.getColumnIndex(CommentContract.CommentEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(CommentContract.CommentEntry.COLUMN_TITLE);
            int descriptionColumnIndex = cursor.getColumnIndex(CommentContract.CommentEntry.COLUMN_DESCRIPTION);
            int authorIdColumnIndex = cursor.getColumnIndex(CommentContract.CommentEntry.COLUMN_AUTHOR_ID);
            int dateColumnIndex = cursor.getColumnIndex(CommentContract.CommentEntry.COLUMN_DATE);
            int postIdColumnIndex = cursor.getColumnIndex(CommentContract.CommentEntry.COLUMN_POST_ID);
            int likesColumnIndex = cursor.getColumnIndex(CommentContract.CommentEntry.COLUMN_LIKES);
            int dislikesColumnIndex = cursor.getColumnIndex(CommentContract.CommentEntry.COLUMN_DISLIKES);

            while(cursor.moveToNext()){
                int currentId = cursor.getInt(idColumnIndex);
                String title = cursor.getString(titleColumnIndex);
                String description = cursor.getString(descriptionColumnIndex);
                int authorId = Integer.parseInt(cursor.getString(authorIdColumnIndex));
                String date = cursor.getString(dateColumnIndex);
                Date cDate=convertStringToDate(date);
                int post = Integer.parseInt(cursor.getString(postIdColumnIndex));
                int likes = Integer.parseInt(cursor.getString(likesColumnIndex));
                int dislikes = Integer.parseInt(cursor.getString(dislikesColumnIndex));
                User author = null;
                for(User u:loadUsersFromDatabase(activity)){
                    if(u.getId() == authorId){
                        author=u;
                    }
                }


                comments.add(new Comment(currentId, title, description, author, cDate ,post, likes,dislikes, null));
            }
        }finally {
            cursor.close();
        }
        return comments;

    }
    public ArrayList<User> loadUsersFromDatabase(Activity activity){
        //Definisi projekciju pomocu koje ces da ucitas podatke iz tabele user
        String[] projection = {
                UserContract.UserEntry._ID,
                UserContract.UserEntry.COLUMN_NAME,
                UserContract.UserEntry.COLUMN_PHOTO,
                UserContract.UserEntry.COLUMN_USERNAME,
                UserContract.UserEntry.COLUMN_PASSWORD

        };
        String sortOrder = UserContract.UserEntry._ID + "DESC";
        Cursor cursor = activity.getContentResolver().query(UserContract.UserEntry.CONTENT_URI, projection, null,null, null);

        ArrayList<User> users = new ArrayList<>();
        try {
            int idColumnIndex = cursor.getColumnIndex(UserContract.UserEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_NAME);
            int photoColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_PHOTO);
            int usernameColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_USERNAME);
            int passwordColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_PASSWORD);

            while(cursor.moveToNext()){
                int currentId = cursor.getInt(idColumnIndex);
                String name = cursor.getString(nameColumnIndex);
                String photo = cursor.getString(photoColumnIndex);
                String username = cursor.getString(usernameColumnIndex);
                String password = cursor.getString(passwordColumnIndex);

                users.add(new User(currentId, name, null, username, password));

            }
        }finally {
            cursor.close();
        }
        return users;

    }

    public ArrayList<Tag> loadTagsFromDatabase(Activity activity){
        //Definisi projekciju pomocu koje ces da ucitas podatke iz tabele tag
        String[] projection = {
                TagContract.TagEntry._ID,
                TagContract.TagEntry.COLUMN_NAME,
                TagContract.TagEntry.COLUMN_POST_ID

        };
        String sortOrder = TagContract.TagEntry._ID + "DESC";
        Cursor cursor = activity.getContentResolver().query(TagContract.TagEntry.CONTENT_URI, projection, null,null, null);

        ArrayList<Tag> tags = new ArrayList<>();
        try {
            int idColumnIndex = cursor.getColumnIndex(TagContract.TagEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(TagContract.TagEntry.COLUMN_NAME);
            int postColumnIndex = cursor.getColumnIndex(TagContract.TagEntry.COLUMN_POST_ID);

            while(cursor.moveToNext()){
                int currentId = cursor.getInt(idColumnIndex);
                String name = cursor.getString(nameColumnIndex);
                int post= cursor.getInt(postColumnIndex);
                tags.add(new Tag(currentId,name,post));
            }
        }finally {
            cursor.close();
        }
        return tags;

    }




    public User findUserById(int userId, Activity activity){
        User user = null;

        String[] projection={
                UserContract.UserEntry._ID,
                UserContract.UserEntry.COLUMN_NAME,
                UserContract.UserEntry.COLUMN_PHOTO,
                UserContract.UserEntry.COLUMN_USERNAME,
                UserContract.UserEntry.COLUMN_PASSWORD
        };
        String sortOrder = UserContract.UserEntry._ID + "DESC";
        Cursor cursor = activity.getContentResolver().query(UserContract.UserEntry.CONTENT_URI, projection, null, null, null);

        try{
            int idColumnIndex = cursor.getColumnIndex(UserContract.UserEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_NAME);
            int photoColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_PHOTO);
            int usernameColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_USERNAME);
            int passwordColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_PASSWORD);

        while (cursor.moveToNext()){
            int currentId = cursor.getInt(idColumnIndex);
            String name = cursor.getString(nameColumnIndex);
            String photo = cursor.getString(photoColumnIndex);
            String username = cursor.getString(usernameColumnIndex);
            String password = cursor.getString(passwordColumnIndex);
           if(currentId == userId){
                user.setId(currentId);
            }


            user = new User(userId, name, null,username, password);

        }
        }
        finally {
            cursor.close();
        }
    return user;
    }

    public Post findPostById(int postId, Activity activity){
        Post post = null;
        String[] projection = {
                PostContract.PostEntry._ID,
                PostContract.PostEntry.COLUMN_TITLE,
                PostContract.PostEntry.COLUMN_DESCRIPTION,
                PostContract.PostEntry.COLUMN_PHOTO,
                PostContract.PostEntry.COLUMN_AUTHOR_ID,
                PostContract.PostEntry.COLUMN_DATE,
                PostContract.PostEntry.COLUMN_LOCATION,
                PostContract.PostEntry.COLUMN_LIKES,
                PostContract.PostEntry.COLUMN_DISLIKES
        };
        String sortOrder = PostContract.PostEntry._ID + "DESC";
        Cursor cursor = activity.getContentResolver().query(PostContract.PostEntry.CONTENT_URI, projection, null, null, null);

        try {
            int idColumnIndex = cursor.getColumnIndex(PostContract.PostEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(PostContract.PostEntry.COLUMN_TITLE);
            int descriptionColumnIndex = cursor.getColumnIndex(PostContract.PostEntry.COLUMN_DESCRIPTION);
            int photoColumnIndex = cursor.getColumnIndex(PostContract.PostEntry.COLUMN_PHOTO);
            int authorIdColumnIndex = cursor.getColumnIndex(PostContract.PostEntry.COLUMN_AUTHOR_ID);
            int dateColumnIndex = cursor.getColumnIndex(PostContract.PostEntry.COLUMN_DATE);
            int locationColumnIndex = cursor.getColumnIndex(PostContract.PostEntry.COLUMN_LOCATION);
            int likesColumnIndex = cursor.getColumnIndex(PostContract.PostEntry.COLUMN_LIKES);
            int dislikesColumnIndex = cursor.getColumnIndex(PostContract.PostEntry.COLUMN_DISLIKES);

            while(cursor.moveToNext()) {
                int currentId = cursor.getInt(idColumnIndex);
                String title = cursor.getString(titleColumnIndex);
                String description = cursor.getString(descriptionColumnIndex);
                String photo = cursor.getString(photoColumnIndex);
                int authorId = Integer.parseInt(cursor.getString(authorIdColumnIndex));
                String date = cursor.getString(dateColumnIndex);
                String location = cursor.getString(locationColumnIndex);
                int likes = Integer.parseInt(cursor.getString(likesColumnIndex));
                int dislikes = Integer.parseInt(cursor.getString(dislikesColumnIndex));
                User author = findUserById(authorId, activity);
                ArrayList<Comment> commentsAll = loadCommentsFromDatabase(activity);
                ArrayList<Comment> commentsFiltered = new ArrayList<>();
                for(Comment comment : commentsAll) {

                    if (comment != null) {

                        if (comment.getPost() == currentId) {
                            commentsFiltered.add(comment);

                        }

                    }
                }

                post = new Post(postId, title, description, null,author, null, null, null, commentsFiltered, likes,dislikes);


            }

        }finally {
            cursor.close();
        } return post;

    }


    public Tag findTagById(int postId, Activity activity){
        Tag tag = null;
        String[] projection = {
                TagContract.TagEntry._ID,
                TagContract.TagEntry.COLUMN_NAME,
                TagContract.TagEntry.COLUMN_POST_ID
        };
        String sortOrder = TagContract.TagEntry._ID + "DESC";
        Cursor cursor = activity.getContentResolver().query(TagContract.TagEntry.CONTENT_URI, projection, null,null, null);

        try {
            int idColumnIndex = cursor.getColumnIndex(TagContract.TagEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(TagContract.TagEntry.COLUMN_NAME);
            int postColumnIndex = cursor.getColumnIndex(TagContract.TagEntry.COLUMN_POST_ID);

            while(cursor.moveToNext()) {
                int currentId = cursor.getInt(idColumnIndex);
                String name = cursor.getString(nameColumnIndex);
                int post = cursor.getInt(postColumnIndex);
                tag = new Tag(currentId,name,post);

            }

        }finally {
            cursor.close();
        } return tag;

    }

    public void insertPost(Post post,Activity activity){
        Uri mNewUri;
        String date = new SimpleDateFormat("dd.MM.yyyy").format(post.getDate());
        ContentValues values = new ContentValues();
        values.put(PostContract.PostEntry.COLUMN_TITLE, post.getTitle());
        values.put(PostContract.PostEntry.COLUMN_DESCRIPTION, post.getDescription());
        values.put(PostContract.PostEntry.COLUMN_DATE, date);
        values.put(PostContract.PostEntry.COLUMN_AUTHOR_ID, post.getAuthor().getId());
        values.put(PostContract.PostEntry.COLUMN_LIKES, post.getLikes());
        values.put(PostContract.PostEntry.COLUMN_DISLIKES, post.getDislikes());
        values.put(PostContract.PostEntry.COLUMN_LOCATION, post.getLocation().toString());
        mNewUri= activity.getContentResolver().insert(PostContract.PostEntry.CONTENT_URI,values);

    }
    public void insertComment(Comment comment,Activity activity){
        Uri mNewUri;
        String date = new SimpleDateFormat("dd.MM.yyyy").format(comment.getDate());
        ContentValues values = new ContentValues();
        values.put(CommentContract.CommentEntry.COLUMN_TITLE, comment.getTitle());
        values.put(CommentContract.CommentEntry.COLUMN_DESCRIPTION, comment.getDescription());
        values.put(CommentContract.CommentEntry.COLUMN_DATE, date);
        values.put(CommentContract.CommentEntry.COLUMN_AUTHOR_ID, comment.getAuthor().getId());
        values.put(CommentContract.CommentEntry.COLUMN_LIKES, comment.getLikes());
        values.put(CommentContract.CommentEntry.COLUMN_DISLIKES, comment.getDislikes());
        values.put(CommentContract.CommentEntry.COLUMN_POST_ID, comment.getPost());
        mNewUri= activity.getContentResolver().insert(CommentContract.CommentEntry.CONTENT_URI,values);

    }
    public void insertTag(Tag tag,Activity activity){
        Uri mNewUri;

        ContentValues values = new ContentValues();
        values.put(TagContract.TagEntry.COLUMN_NAME, tag.getName());
        values.put(TagContract.TagEntry.COLUMN_POST_ID, tag.getPosts());

        mNewUri= activity.getContentResolver().insert(TagContract.TagEntry.CONTENT_URI,values);

    }


}

