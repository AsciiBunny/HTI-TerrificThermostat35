package com.group35.terrificthermostat35;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.util.Log;

import com.projectapi.thermometerapi.ThermostatClient;
import com.projectapi.thermometerapi.ThermostatData;
import com.projectapi.thermometerapi.ThermostatDataHandler;

/**
 * Created by AsciiBunny on 23-6-2017.
 */

public class TerrificApplication extends Application {

    private static final int THERMOSTAT_CLIENT_UPDATE_TIME = 1000;
    private static final String THERMOSTAT_SERVER_WEEKPROGRAM_NAME = "SERVER WEEKPROGRAM";
    private FileManager fileManager;
    private BasicActivity currentActivity;
    private ThermostatData thermostatData;

    Handler timerHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        timerHandler = new Handler();
        ThermostatClient.init(35, new ThermostatDataHandler() {
            @Override
            public void onFinish(ThermostatData _thermostatData) {
                thermostatData = _thermostatData;
                timerHandler.postDelayed(new ThermostatClientPusher(), THERMOSTAT_CLIENT_UPDATE_TIME);
            }
        });
        fileManager = new FileManager(this);
    }

    public void setCurrentActivity(BasicActivity activity) {
        this.currentActivity = activity;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public ThermostatData getThermostatData() {
        return thermostatData;
    }

    public FileManager getFileManager() {
        return fileManager;
    }


    private class ThermostatClientPusher implements Runnable {

        @Override
        public void run() {
            ThermostatClient.updateThermometerData(new BasicActivityThermostatDataHandler());
            timerHandler.postDelayed(new ThermostatClientPusher(), THERMOSTAT_CLIENT_UPDATE_TIME);
        }
    }

    private class BasicActivityThermostatDataHandler implements ThermostatDataHandler {

        @Override
        public void onFinish(final ThermostatData thermostatData) {
            if (currentActivity != null) {
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentActivity != null) {
                            currentActivity.onThermostatDataUpdate(thermostatData);
                        }
                    }
                });
            }
        }
    }
}
