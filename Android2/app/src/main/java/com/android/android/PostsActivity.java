package com.android.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PostsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
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
    public void btnLoginActivity(View view){
        Intent start = new Intent(this,LoginActivity.class);
        startActivity(start);

    }
    public void btnCreatePostActivity(View view){
        Intent start = new Intent(this,CreatePostActivity.class);
        startActivity(start);

    }
    public void btnReadPostActivity(View view){
        Intent start = new Intent(this,ReadPostActivity.class);
        startActivity(start);

    }
    public void btnSettingsActivity(View view){
        Intent start = new Intent(this,SettingsActivity.class);
        startActivity(start);

    }
}