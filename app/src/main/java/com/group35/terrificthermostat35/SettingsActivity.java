package com.group35.terrificthermostat35;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.projectapi.thermometerapi.ThermostatData;

public class SettingsActivity extends BasicActivity {

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_main:
                    startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                    return true;
                case R.id.nav_weekprog:
                    startActivity(new Intent(SettingsActivity.this, ProgramListActivity.class));
                    return true;
                case R.id.nav_settings:
                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onFinish(ThermostatData thermostatData) {

    }
}
