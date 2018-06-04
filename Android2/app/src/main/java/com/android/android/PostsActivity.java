package com.android.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
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
import android.widget.TextView;
import android.widget.Toast;


import com.android.android.adapters.PostAdapter;
import com.android.android.database.DatabaseHelper;
import com.android.android.database.CRUD;
import com.android.android.model.Post;
import com.android.android.model.User;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;


public class PostsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private DrawerLayout drawerLayout;
    private ListView listView;
    private String[] lista;
    private boolean sortPostbyDate;
    private boolean sortPostbyPppularity;
    private SharedPreferences sharedPreferences;
    private ActionBarDrawerToggle toggle;
    private CRUD CRUD;
    private ArrayList<Post> posts = new ArrayList<Post>();
    private DatabaseHelper mDbHelper;
    private TextView textView;
    private SimpleCursorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        Toast.makeText(this,"PostActivity",Toast.LENGTH_SHORT).show();
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
                CRUD = new CRUD();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("mPref",0);
                int idUser = pref.getInt("id",-1);
                User u=null;
                for(User user: CRUD.loadUsersFromDatabase(PostsActivity.this)){
                    if(user.getId() == idUser){
                        u=user;
                    }
                }
                textView=(TextView) findViewById(R.id.nameNav);
                textView.setText(u.getName());
                textView=(TextView) findViewById(R.id.usernameNav);
                textView.setText(u.getUsername());

                invalidateOptionsMenu();

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Toast.makeText(PostsActivity.this,"Drawer Closed",Toast.LENGTH_SHORT).show();
                invalidateOptionsMenu();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("mPref",0);
                int idUser = pref.getInt("id",-1);
                drawerLayout.addDrawerListener(toggle);
                toggle.syncState();


            }

        };




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
                    startActivity(new Intent(view.getContext(), CreatePostActivity.class));
                }
                if(position == 2){
                    startActivity(new Intent(view.getContext(), SettingsActivity.class));
                }
                if(position == 3){
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("mPref",0);
                    SharedPreferences.Editor editor=pref.edit();
                    editor.clear();
                    editor.apply();
                    Intent start = new Intent(PostsActivity.this,LoginActivity.class);
                    startActivity(start);
                    PostsActivity.this.finish();
                }

            }
        });
        CRUD = new CRUD();
        posts = CRUD.loadPostsFromDatabase(this);

        PostAdapter postListAdapter = new PostAdapter(this, posts);
        ListView listView = findViewById(R.id.post_list_view);
        listView.setAdapter(postListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Post clickedObj = (Post) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(PostsActivity.this,ReadPostActivity.class);
                intent.putExtra("id",clickedObj.getId());
                startActivity(intent);
            }
        }); }
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
        sortPosts();
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

    public void sortPosts(){
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        sortPostbyDate =sharedPreferences.getBoolean(getString(R.string.sort_posts_date_key),false);
        sortPostbyPppularity=sharedPreferences.getBoolean(getString(R.string.sort_posts_popu_key),false);
        if(sortPostbyDate == true){
            sortByDate();
        }
        if(sortPostbyPppularity == true){
            sortPostsByPopularity();
        }


    }
    public void sortPostsByPopularity(){

        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post, Post t1) {
                int first;
                int second ;
                first = post.getLikes() - post.getDislikes();
                second = t1.getLikes() - t1.getDislikes();
                return Integer.valueOf(second).compareTo(first);
            }
        });

    }
    public void sortByDate(){
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post, Post t1) {
                return t1.getDate().compareTo(post.getDate()) ;
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







}
