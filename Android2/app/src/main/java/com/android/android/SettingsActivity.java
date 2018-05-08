package com.android.android;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

public class SettingsActivity extends PreferenceActivity implements DatePickerDialog.OnDateSetListener {
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragment()).commit();
        Preference btnDateFilter = (Preference) findPreference("btnDateFilter");
        /*btnDateFilter.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showDateDialog();
                return false;
            }
        });*/


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
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Log.i("dasd","year "+i+" month "+i1+" day "+i2);
    }
    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }
    private void showDateDialog(){
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this,this, year, month, day).show();

    }
}
