package com.android.android;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Build;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.DatePicker;

import java.util.Calendar;

public class SettingsActivity extends PreferenceActivity implements DatePickerDialog.OnDateSetListener {
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setRules();


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Log.i("dasd", "year " + i + " month " + i1 + " day " + i2);
    }



    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setStatus(Preference preference, boolean stats) {
        preference.setEnabled(stats);
    }

    public void setRules() {
        CheckBoxPreference cbPostDate = (CheckBoxPreference) findPreference(getString(R.string.sort_posts_date_key));
        cbPostDate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
                setStatus(findPreference(getString(R.string.sort_posts_popu_key)), !checkBoxPreference.isChecked());
                return true;

            }
        });

        CheckBoxPreference cbPostPop = (CheckBoxPreference) findPreference(getString(R.string.sort_posts_popu_key));
        cbPostPop.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
                setStatus(findPreference(getString(R.string.sort_posts_date_key)), !checkBoxPreference.isChecked());
                return true;

            }
        });

        CheckBoxPreference cbCommDate = (CheckBoxPreference) findPreference(getString(R.string.sort_comm_date_key));
        cbCommDate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
                setStatus(findPreference(getString(R.string.sort_comm_popu_key)), !checkBoxPreference.isChecked());
                return true;

            }
        });

        CheckBoxPreference cbCommPop = (CheckBoxPreference) findPreference(getString(R.string.sort_comm_popu_key));
        cbCommPop.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
                setStatus(findPreference(getString(R.string.sort_comm_date_key)), !checkBoxPreference.isChecked());
                return true;

            }
        });

        if (cbPostDate.isChecked() & cbPostDate.isEnabled()) {
            setStatus(findPreference(getString(R.string.sort_posts_popu_key)), false);
        }

        if (cbPostPop.isChecked() & cbPostPop.isEnabled()) {
            setStatus(findPreference(getString(R.string.sort_posts_date_key)), false);
        }

        if (cbCommDate.isChecked() & cbCommDate.isEnabled()) {
            setStatus(findPreference(getString(R.string.sort_comm_popu_key)), false);
        }

        if (cbCommPop.isChecked() & cbCommPop.isEnabled()) {
            setStatus(findPreference(getString(R.string.sort_comm_date_key)), false);
        }

    }
}
