package com.group35.terrificthermostat35;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.projectapi.thermometerapi.ThermostatData;
import com.projectapi.thermometerapi.ThermostatDataHandler;

/**
 * Created by AsciiBunny on 23-6-2017.
 */

public abstract class BasicActivity extends AppCompatActivity implements ThermostatDataHandler{

    protected TerrificApplication app;
    protected ThermostatData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (TerrificApplication) getApplication();
        app.setCurrentActivity(this);
        if (app.getThermostatData() != null)
            data = app.getThermostatData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        app.setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        clearReferences();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences(){
        Activity currActivity = app.getCurrentActivity();
        if (this.equals(currActivity))
            app.setCurrentActivity(null);
    }

    @Override
    public abstract void onFinish(ThermostatData thermostatData);
}
