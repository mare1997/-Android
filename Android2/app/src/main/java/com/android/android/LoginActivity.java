package com.android.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.android.database.HelperDatabaseRead;
import com.android.android.model.Post;
import com.android.android.model.User;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private TextView textView;
    private HelperDatabaseRead helperDatabaseRead;
    private ArrayList<User> users = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


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
    public void btnPostsActivity(View view){
        textView=(TextView)findViewById(R.id.user);
        String username=textView.getText().toString();
        textView=(TextView)findViewById(R.id.pass);
        String password=textView.getText().toString();
        if(login(username,password) == true){
            Intent startPosts = new Intent(this,PostsActivity.class);


            startActivity(startPosts);
            finish();
        }else{
            Toast.makeText(this,"Sjebao si se!  " ,Toast.LENGTH_LONG).show();

        }

    }
    public boolean login(String user,String pass){

        helperDatabaseRead = new HelperDatabaseRead();
        users = helperDatabaseRead.loadUsersFromDatabase(this);
        for(User u:users){
            if (u.getUsername().equals(user))
                if (u.getPassword().equals(pass)) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("mPref",0);
                    SharedPreferences.Editor editor=pref.edit();
                    editor.putInt("id",u.getId());
                    editor.apply();
                    return true;
                }
        }
        return false;
    }
}
