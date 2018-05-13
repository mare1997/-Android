package com.android.android;

import android.support.v4.app.LoaderManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.android.adapters.PostAdapter;
import com.android.android.database.PostContentProvider;
import com.android.android.database.PostSQLiteHelper;
import com.android.android.model.Post;
import com.android.android.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class PostsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private DrawerLayout drawerLayout;
    private ListView listView;
    private String[] lista;
    private boolean sortPostbyDate;
    private boolean sortPostbyPppularity;
    private SharedPreferences sharedPreferences;
    private ActionBarDrawerToggle toggle;
    private ArrayList<Post> posts = new ArrayList<Post>();
    public static String USER_KEY = "rs.android2.USER_KEY";
    private SimpleCursorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        Toast.makeText(this,"CreatePostActivity",Toast.LENGTH_SHORT).show();
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbarPost);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.toolbar_);
            actionBar.setHomeButtonEnabled(true);

        }
        drawerLayout =(DrawerLayout) findViewById(R.id.post_DrawerLayout);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.open,R.string.close){
            @Override
            public void onDrawerOpened(View drawerView) {
                Toast.makeText(PostsActivity.this,"Drawer Opened",Toast.LENGTH_SHORT).show();
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Toast.makeText(PostsActivity.this,"Drawer Closed",Toast.LENGTH_SHORT).show();
                invalidateOptionsMenu();
            }

        };

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        lista=getResources().getStringArray(R.array.nav_drawer);
        listView = (ListView) findViewById(R.id.create_post_list);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,lista));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(position == 0){
                    startActivity(new Intent(view.getContext(), PostsActivity.class));
                }
                if(position == 1){
                    startActivity(new Intent(view.getContext(), SettingsActivity.class));
                }

            }
        });

        /*
        User newUser = new User();
        newUser.setUsername("newUsername");
        Date date=new Date(2018,05,1);
        Date date2=new Date(2018,03,1);
        Date currentDate = Calendar.getInstance().getTime();
        Post post2 = new Post();
        post2.setAuthor(newUser);
        post2.setTitle("Ne popularan");
        post2.setDate(currentDate);
        post2.setLikes(50);
        post2.setDislikes(1232);
        posts.add(post2);
        Post post = new Post();
        post.setAuthor(newUser);
        post.setTitle("Popularan");
        post.setDate(date);
        post.setLikes(100);
        post.setDislikes(1);

        Post post1 = new Post();
        post1.setAuthor(newUser);
        post1.setTitle("Mare");
        post1.setDate(date2);
        post1.setLikes(50);
        post1.setDislikes(2);
        posts.add(post1);
        posts.add(post);




        sortPosts();
        PostAdapter postListAdapter = new PostAdapter(this, posts);
        ListView listView = findViewById(R.id.post_list_view);
        listView.setAdapter(postListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PostsActivity.this,ReadPostActivity.class);
                startActivity(intent);
            }
        });
        */

    }
    @Override
    protected  void onStart() {
        super.onStart();
    }
    @Override
    protected  void onRestart() {
        super.onRestart();
    }
    @Override
    protected  void onResume() {
        super.onResume();
    }
    @Override
    protected  void onPause() {
        super.onPause();
    }
    @Override
    protected  void onStop() {
        super.onStop();
    }
    @Override
    protected  void onDestroy() {
        super.onDestroy();
    }

    public void init(){
        ListView listView = findViewById(R.id.post_list_view);
        getLoaderManager().initLoader(0, null, (android.app.LoaderManager.LoaderCallbacks<Cursor>)this);
        String[] from = new String[] { PostSQLiteHelper.COLUMN_TITLE, PostSQLiteHelper.COLUMN_DATE };
        int[] to = new int[] {R.id.title_post_list, R.id.date_post_list};
        adapter = new SimpleCursorAdapter(this, R.layout.post_layout_list_items, null, from,
                to, 0);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent = new Intent(PostsActivity.this, ReadPostActivity.class);
                Uri todoUri = Uri.parse(PostContentProvider.CONTENT_URI_POST + "/" + id);
                intent.putExtra("id", todoUri);
                startActivity(intent);
            }
        });




    }

    public void sortPosts(){
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        sortPostbyDate =sharedPreferences.getBoolean(getString(R.string.sort_posts_date_key),false);
        sortPostbyPppularity=sharedPreferences.getBoolean(getString(R.string.sort_posts_popu),false);
        if(sortPostbyDate == true){
            sortByDate();
        }
        if(sortPostbyPppularity == true){
            sortPostsByPopularity();
        }


    }
    public void sortPostsByPopularity(){

        Collections.sort(posts, new Comparator< Post >() {
            @Override
            public int compare(Post post, Post t1) {
                return   post.getPopularity() - t1.getPopularity();
            }


        });

    }
    public void sortByDate(){
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post, Post t1) {
                return post.getDate().compareTo(t1.getDate()) ;
            }
        });
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(this,lista[position] + " was selected",Toast.LENGTH_SHORT).show();
        listView.setItemChecked(position,true);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_post, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
        {
            return  true;
        }
        if(item.getItemId() == R.id.settings){
            startActivity(new Intent(this, SettingsActivity.class));
        }
        if(item.getItemId() == R.id.createPost){
            startActivity(new Intent(this, CreatePostActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    public void setTitle(String title){
        getSupportActionBar().setTitle(title);

    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] allColumns = { PostSQLiteHelper.COLUMN_ID,
                PostSQLiteHelper.COLUMN_TITLE, PostSQLiteHelper.COLUMN_DATE, PostSQLiteHelper.COLUMN_DESCRIPTION, PostSQLiteHelper.COLUMN_LIKES, PostSQLiteHelper.COLUMN_DISLIKES,
                 PostSQLiteHelper.COLUMN_LOCATION, PostSQLiteHelper.COLUMN_PHOTO
        };

        CursorLoader cursor = new CursorLoader(this, PostContentProvider.CONTENT_URI_POST,
                allColumns, null, null, null);

        return cursor;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }






}
