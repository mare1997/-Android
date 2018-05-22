package com.android.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.android.adapters.CommentAdapter;
import com.android.android.database.ContentProvider;
import com.android.android.database.DatabaseHelper;
import com.android.android.database.HelperDatabaseRead;
import com.android.android.database.PostContract;
import com.android.android.model.Comment;
import com.android.android.model.Post;
import com.android.android.model.Tag;
import com.android.android.model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReadPostActivity extends AppCompatActivity implements AdapterView.OnItemClickListener  {
    private DrawerLayout drawerLayout;
    private ListView listView;
    private String[] lista;
    private ActionBarDrawerToggle toggle;
    private HelperDatabaseRead helperDatabaseRead;
    private ArrayList<Post> posts = new ArrayList<Post>();
    private Post post=null;
    private TextView textView;
    private int idUser;
    private int id;
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
                helperDatabaseRead = new HelperDatabaseRead();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("mPref",0);
                int idUser = pref.getInt("id",-1);
                User u=null;
                for(User user: helperDatabaseRead.loadUsersFromDatabase(ReadPostActivity.this)){
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
                    startActivity(new Intent(view.getContext(), SettingsActivity.class));
                }
                if(position == 2){
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("mPref",0);
                    SharedPreferences.Editor editor=pref.edit();
                    editor.clear();
                    editor.apply();
                    Intent start = new Intent(ReadPostActivity.this,LoginActivity.class);
                    startActivity(start);
                    finish();
                }

            }
        });
        Post p=null;
        Intent myIntent = getIntent();
        id= myIntent.getIntExtra("id",-1);
        helperDatabaseRead = new HelperDatabaseRead();
        posts=helperDatabaseRead.loadPostsFromDatabase(this);
        if(id != -1){
            for(Post pp: posts){
                if(pp.getId() == id){
                    post = pp;
                }
            }
        }
        textView=(TextView)findViewById(R.id.titleRead);
        textView.setText(post.getTitle());
        textView=(TextView)findViewById(R.id.ReadAuthor);
        textView.setText(post.getAuthor().getName());
        textView=(TextView)findViewById(R.id.desc);
        textView.setText(post.getDescription());
        textView=(TextView)findViewById(R.id.tag);
        List<Tag> tags=post.getTags();
        String tag="";
        int size= -1;
        if(post.getTags() == null){
            size=0;
        }else{
            size=post.getTags().size();
        }
        for(int i =0 ;i< size;i++){
            tag=tag+ ", "+tags.get(i).getName();
            textView.setText(tag);
        }
        textView=(TextView)findViewById(R.id.likes);
        String like= String.valueOf(post.getLikes());
        textView.setText(like);
        textView=(TextView)findViewById(R.id.dislikes);
        String disslike= String.valueOf(post.getDislikes());
        textView.setText(disslike);
        textView=(TextView)findViewById(R.id.date);
        String date = new SimpleDateFormat("dd.MM.yyyy").format(post.getDate());
        textView.setText(date);
        CommentAdapter commentAdapter=new CommentAdapter(this,post.getComments());
        ListView listView = findViewById(R.id.readpost_list_view_comment);
        listView.setAdapter(commentAdapter);




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
            Toast.makeText(this,"Deleted",Toast.LENGTH_SHORT).show();


            startActivity(new Intent(this, PostsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void btnAddComment(View view) {
        helperDatabaseRead = new HelperDatabaseRead();
        textView=(TextView)findViewById(R.id.titleComm);
        String title=textView.getText().toString();
        textView=(TextView)findViewById(R.id.descComm);
        String desc=textView.getText().toString();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("mPref",0);
        idUser = pref.getInt("id",-1);
        User u=null;
        for(User uu:helperDatabaseRead.loadUsersFromDatabase(this)){
            if(uu.getId() == idUser){
                u=uu;
            }
        }
        Comment c= new Comment(title,desc,u,getDateTime(),id,0,0);
        helperDatabaseRead.insertComment(c,this);
        Intent intent = new Intent(ReadPostActivity.this,ReadPostActivity.class);

        startActivity(intent);
    }
    private Date getDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        String sDate= dateFormat.format(date);
        Toast.makeText(this,sDate,Toast.LENGTH_SHORT).show();
        return  convertStringToDate(sDate);
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

}
