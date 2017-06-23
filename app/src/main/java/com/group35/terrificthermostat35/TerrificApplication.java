package com.group35.terrificthermostat35;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;

import com.projectapi.thermometerapi.ThermostatClient;
import com.projectapi.thermometerapi.ThermostatData;
import com.projectapi.thermometerapi.ThermostatDataHandler;

/**
 * Created by AsciiBunny on 23-6-2017.
 */

public class TerrificApplication extends Application {

    private static final int THERMOSTAT_CLIENT_UPDATE_TIME = 1000;
    private Activity currentActivity;
    private ThermostatDataHandler dataHandler;
    private ThermostatData thermostatData;

    Handler timerHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        timerHandler = new Handler();
        ThermostatClient.init(35, new ThermostatDataHandler() {
            @Override
            protected void onFinish(ThermostatData _thermostatData) {
                thermostatData = _thermostatData;
                timerHandler.postDelayed(new ThermostatClientPusher(), THERMOSTAT_CLIENT_UPDATE_TIME);
            }
        });
    }

    public void setCurrentActivity(Activity activity, ThermostatDataHandler dataHandler) {
        this.currentActivity = activity;
        this.dataHandler = dataHandler;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public ThermostatData getThermostatData() {
        return thermostatData;
    }


    private class ThermostatClientPusher implements Runnable {

        @Override
        public void run() {
            ThermostatClient.updateThermometerData(dataHandler);
            timerHandler.postDelayed(new ThermostatClientPusher(), THERMOSTAT_CLIENT_UPDATE_TIME);
        }
    }
}
