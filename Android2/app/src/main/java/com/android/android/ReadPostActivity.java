package com.android.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
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

import com.android.android.adapters.SectionsPagerAdapter;
import com.android.android.database.CRUD;
import com.android.android.database.PostContract;
import com.android.android.fragments.CommentFragment;
import com.android.android.fragments.PostFragment;
import com.android.android.model.Post;
import com.android.android.model.User;

import java.util.ArrayList;

public class ReadPostActivity extends AppCompatActivity implements AdapterView.OnItemClickListener  {
    private DrawerLayout drawerLayout;
    private ListView listView;
    private String[] lista;
    private ActionBarDrawerToggle toggle;
    private CRUD CRUD;
    private ArrayList<Post> posts = new ArrayList<Post>();
    private Post post=null;
    private TextView textView;
    private int idUser;
    private int id;
    private SectionsPagerAdapter mSPA;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_post);
        Toast.makeText(this,"ReadPostActivity",Toast.LENGTH_SHORT).show();
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbarRead);
        setSupportActionBar(toolbar);
        drawerLayout =(DrawerLayout) findViewById(R.id.read_DL);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.open,R.string.close){
            @Override
            public void onDrawerOpened(View drawerView) {
                CRUD = new CRUD();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("mPref",0);
                int idUser = pref.getInt("idUser",-1);
                User u=null;
                for(User user: CRUD.loadUsersFromDatabase(ReadPostActivity.this)){
                    if(user.getId() == idUser){
                        u=user;
                    }
                }
                textView=(TextView) findViewById(R.id.nameNav);
                textView.setText(u.getName());
                textView=(TextView) findViewById(R.id.usernameNav);
                textView.setText(u.getUsername());
                Toast.makeText(ReadPostActivity.this,"Drawer Opened",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Toast.makeText(ReadPostActivity.this,"Drawer Closed",Toast.LENGTH_SHORT).show();
            }

        };
        //noinspection deprecation
        drawerLayout.setDrawerListener(toggle);
        if(getActionBar() != null) {

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        lista=getResources().getStringArray(R.array.nav_drawer);
        listView = (ListView) findViewById(R.id.read_post_list);
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
                    Intent start = new Intent(ReadPostActivity.this,LoginActivity.class);
                    startActivity(start);
                    ReadPostActivity.this.finish();
                }

            }
        });

        mSPA = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);





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

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PostFragment(), "Post");
        adapter.addFragment(new CommentFragment(), "Comment");

        viewPager.setAdapter(adapter);
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
        getMenuInflater().inflate(R.menu.toolbar_read_post, menu);
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
        if(item.getItemId() == R.id.delete){
            Intent myIntent = getIntent();
            id= myIntent.getIntExtra("id",-1);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("mPref",0);
            int idUser = pref.getInt("id",-1);
            CRUD = new CRUD();
            posts = CRUD.loadPostsFromDatabase(this);
            if (id != -1) {
                for (Post pp : posts) {
                    if (pp.getId() == id) {
                        post = pp;
                    }
                }
            }
            if(post.getAuthor().getId() == idUser) {

                Uri uri = Uri.withAppendedPath(PostContract.PostEntry.CONTENT_URI, String.valueOf(post.getId()));
                this.getContentResolver().delete(uri, null, null);
                Intent startPosts = new Intent(this, PostsActivity.class);
                startActivity(startPosts);
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();


                startActivity(new Intent(this, PostsActivity.class));
            }else{
                Toast.makeText(this, "You can delete just your posts!!!", Toast.LENGTH_SHORT).show();

            }
        }
        return super.onOptionsItemSelected(item);
    }




}
