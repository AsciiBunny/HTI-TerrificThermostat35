package com.group35.terrificthermostat35;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.design.widget.BottomNavigationView;

import com.projectapi.thermometerapi.ThermostatClient;
import com.projectapi.thermometerapi.ThermostatData;
import com.projectapi.thermometerapi.ThermostatDataHandler;

public class MainActivity extends BasicActivity {

    double vtemp = 21.0;
    TextView cTemp;
    TextView sTemp;

    //private BottomNavigationView mBottomNav;

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_main:
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    return true;
                case R.id.nav_weekprog:
                    startActivity(new Intent(MainActivity.this, ProgramListActivity.class));
                    return true;
                case R.id.nav_settings:
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    return true;
            }
            return false;
        }

    };

    public static final String EXTRA_MESSAGE = "com.group35.terrificthermostat35.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Bottom Navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Gather references to UI objects
        cTemp = (TextView) findViewById(R.id.cTemp);
        sTemp = (TextView) findViewById(R.id.sTemp);
        ImageButton plus = (ImageButton) findViewById(R.id.plus);
        ImageButton minus = (ImageButton) findViewById(R.id.minus);


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data != null) {
                    data.setTargetTemperature(data.getTargetTemperature() + 0.1);
                    sTemp.setText(ThermostatClient.parseTempToString(data.getTargetTemperature()) + " \u2103");
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data != null) {
                    data.setTargetTemperature(data.getTargetTemperature() - 0.1);
                    sTemp.setText(ThermostatClient.parseTempToString(data.getTargetTemperature()) + " \u2103");
                }
            }
        });

    }

    @Override
    public void onFinish(ThermostatData thermostatData) {
        if (thermostatData != null) {
            data = thermostatData;
            cTemp.post(new TextViewUpdate(cTemp, ThermostatClient.parseTempToString(thermostatData.getCurrentTemperature()) + " \u2103"));
            sTemp.post(new TextViewUpdate(sTemp, ThermostatClient.parseTempToString(thermostatData.getTargetTemperature()) + " \u2103"));
        } else {
            Log.e("MainActivityDataHandler", "ThermostatData is null");
        }
    }

    private class TextViewUpdate implements Runnable {

        private TextView target;
        private String string;

        public TextViewUpdate(TextView target, String text) {
            this.target = target;
            this.string = text;
        }

        @Override
        public void run() {
            target.setText(string);
        }
    }
}
