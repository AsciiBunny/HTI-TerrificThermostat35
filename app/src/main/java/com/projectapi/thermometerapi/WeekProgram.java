package com.projectapi.thermometerapi;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by s161473 on 22-6-2017.
 */

public class WeekProgram implements Serializable {

    public static final String DEFAULT_NAME = "New Weekprogram";

    public WeekProgram() {
        this.name = DEFAULT_NAME;
    }

    public WeekProgram(String name) {
        this.name = name;
    }

    public String name;
    public double dayTemperature, nightTemperature;
    public Map<WeekDay, Switch[]> weekDaySwitchMap = new HashMap<>();

    public void setName(String Name) { name = Name;}

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

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
