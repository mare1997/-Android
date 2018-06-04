package com.android.android;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.android.database.CRUD;
import com.android.android.dialogs.LocationDialog;
import com.android.android.model.Post;
import com.android.android.model.Tag;
import com.android.android.model.User;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CreatePostActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,LocationListener {
    private DrawerLayout drawerLayout;
    private ListView listView;
    private String[] lista;
    private ActionBarDrawerToggle toggle;
    int idUser = -1;
    private TextView textView;
    private CRUD CRUD;
    private double longitude;
    private double latitude;

    private LocationManager locationManager;
    private AlertDialog dialog;
    private String provider;
    private Location location;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        Toast.makeText(this, "CreatePostActivity", Toast.LENGTH_SHORT).show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCreate);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.create_post);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                CRUD = new CRUD();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("mPref", 0);
                int idUser = pref.getInt("id", -1);
                User u = null;
                for (User user : CRUD.loadUsersFromDatabase(CreatePostActivity.this)) {
                    if (user.getId() == idUser) {
                        u = user;
                    }
                }
                textView = (TextView) findViewById(R.id.nameNav);
                textView.setText(u.getName());
                textView = (TextView) findViewById(R.id.usernameNav);
                textView.setText(u.getUsername());
                Toast.makeText(CreatePostActivity.this, "Drawer Opened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Toast.makeText(CreatePostActivity.this, "Drawer Closed", Toast.LENGTH_SHORT).show();
            }

        };
        //noinspection deprecation
        drawerLayout.setDrawerListener(toggle);
        if (getActionBar() != null) {

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        lista = getResources().getStringArray(R.array.nav_drawer);
        listView = (ListView) findViewById(R.id.create_post_list);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(view.getContext(), PostsActivity.class));
                }
                if (position == 1) {
                    startActivity(new Intent(view.getContext(), CreatePostActivity.class));
                }
                if (position == 2) {
                    startActivity(new Intent(view.getContext(), SettingsActivity.class));
                }
                if (position == 3) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("mPref", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.apply();
                    Intent start = new Intent(CreatePostActivity.this, LoginActivity.class);
                    startActivity(start);
                    CreatePostActivity.this.finish();
                }

            }
        });
        SharedPreferences pref = getApplicationContext().getSharedPreferences("mPref", 0);
        idUser = pref.getInt("id", -1);







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
        getProvider();

        if (location == null) {
            Toast.makeText(getApplicationContext(), "Location not found", Toast.LENGTH_SHORT).show();
        }
        if (location != null) {
            getAddress(location.getLatitude(), location.getLongitude());
            onLocationChanged(location);
            locationManager.removeUpdates(CreatePostActivity.this);
        }
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
        getMenuInflater().inflate(R.menu.toolbar_create_post, menu);
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
        if(item.getItemId() == R.id.yes){
            createPost();
            Toast.makeText(this,"Created",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, PostsActivity.class));
        }
        if(item.getItemId() == R.id.no){
            Toast.makeText(this,"Canceled",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, PostsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(this,lista[position] + " was selected",Toast.LENGTH_SHORT).show();
        listView.setItemChecked(position,true);
    }




    public void setTitle(String title){
        getSupportActionBar().setTitle(title);

    }
    public void createPost(){
        CRUD = new CRUD();
        textView=(TextView)findViewById(R.id.createTitle);
        String title=textView.getText().toString();
        textView=(TextView)findViewById(R.id.createDesc);
        String desc=textView.getText().toString();
        textView=(TextView)findViewById(R.id.createTags);
        String tag=textView.getText().toString();
        String[] tagss= tag.split(",");
        ArrayList<Tag> ttt=null;
        User u=null;
        for(User uu: CRUD.loadUsersFromDatabase(this)){
            if(uu.getId() == idUser){
                u=uu;
            }
        }
        Post p =new Post(desc, title,null,u,getDateTime(),getAddress(latitude,longitude),null,null,0,0);
        CRUD.insertPost(p,this);
        Post ppp= null;
        int ii= CRUD.loadPostsFromDatabase(this).size();
        ppp= CRUD.loadPostsFromDatabase(this).get(ii-1);
        for(int i=0;i<tagss.length;i++){
            Tag newTag=new Tag(tagss[i],ppp.getId());
            CRUD.insertTag(newTag,this);
        }

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
    public void getProvider(){
        Criteria criteria = new Criteria();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(locationManager!=null)
            provider = locationManager.getBestProvider(criteria, true);

        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean wifi = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.e("gps",gps+"");
        Log.e("wifi",wifi+"");
        if(!gps &&  !wifi){
            showLocatonDialog();
        }else{
            if(checkLocationPermission()){
                if(ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                    locationManager.requestLocationUpdates(provider,0,0,this);
                }else if(ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED){

                    locationManager.requestLocationUpdates(provider,0,0,this);
                }
            }
        }

        location = null;

        if(checkLocationPermission()){
            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                location = locationManager.getLastKnownLocation(provider);

            }else if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                location = locationManager.getLastKnownLocation(provider);

            }
        }

    }

    private void showLocatonDialog() {
        if (dialog == null) {
            dialog = new LocationDialog(this).prepareDialog();
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        dialog.show();
    }


    public boolean checkLocationPermission(){
       if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                new AlertDialog.Builder(this)
                        .setTitle("Allow user location")
                        .setMessage("To continue working we need your locations... Allow now?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(CreatePostActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        }else{
            return true;
        }
    }

    public String getAddress(double latitude,double longitude){
        String s="";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String city = addresses.get(0).getLocality();
            String country = addresses.get(0).getCountryName();
            s=city + ", " + country;
            return s;


        } catch (IOException e) {
            e.printStackTrace();
        }
        return latitude + ", " + longitude;
    }


    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
