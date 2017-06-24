package com.projectapi.thermometerapi;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by s161473 on 22-6-2017.
 */

public class WeekProgram implements Serializable {

    public static final String DEFAULT_NAME = "Thermostat Weekprogram";

    public WeekProgram() {
        this.name = DEFAULT_NAME;
    }

    public WeekProgram(String name) {
        this.name = name;
    }

    public String name;
    public double dayTemperature, nightTemperature;
    public Map<WeekDay, Switch[]> weekDaySwitchMap = new HashMap<>();

public String getName(){return name;}

    public WeekProgram getCopy() {
        WeekProgram copy = new WeekProgram(name);

        copy.dayTemperature = dayTemperature;
        copy.nightTemperature = nightTemperature;
        copy.weekDaySwitchMap = new HashMap<>();
        for(WeekDay day : weekDaySwitchMap.keySet()) {
            Switch[] oldSwitches = weekDaySwitchMap.get(day);
            Switch[] newSwitches = new Switch[oldSwitches.length];
            for(int i = 0; i < oldSwitches.length; i++) {
                newSwitches[i] = oldSwitches[i].getCopy();
            }
            copy.weekDaySwitchMap.put(day, newSwitches);
        }


        return copy;
    }


}
