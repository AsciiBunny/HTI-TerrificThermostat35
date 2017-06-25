package com.projectapi.thermometerapi;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AsciiBunny on 21-6-2017.
 */

public class ThermostatData {
    protected WeekDay weekDay;
    protected String time;
    protected boolean weekProgramState;
    protected double currentTemperature, targetTemperature;
    protected WeekProgram weekProgram;

    protected boolean isWeekDayDirty;
    protected boolean isCurrentTemperatureDirty, isTargetTemperatureDirty;
    protected boolean isDayTemperatureDirty, isNightTemperatureDirty;
    protected boolean isWeekProgramStateDirty;
    protected Map<WeekDay, Boolean> weekDayDirtySwitchMap = new HashMap<>();
    protected boolean isTimeDirty;

    public ThermostatData() {
        weekDayDirtySwitchMap.put(WeekDay.MONDAY, false);
        weekDayDirtySwitchMap.put(WeekDay.TUESDAY, false);
        weekDayDirtySwitchMap.put(WeekDay.WEDNESDAY, false);
        weekDayDirtySwitchMap.put(WeekDay.THURSDAY, false);
        weekDayDirtySwitchMap.put(WeekDay.FRIDAY, false);
        weekDayDirtySwitchMap.put(WeekDay.SATURDAY, false);
        weekDayDirtySwitchMap.put(WeekDay.SUNDAY, false);
        weekProgram = new WeekProgram();
    }

    public WeekDay getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(WeekDay newWeekDay) {
        if (weekDay != newWeekDay) {
            isWeekDayDirty = true;
            weekDay = newWeekDay;
        }
    }

    public void setTime(String newTime) {
        if (!time.equals(newTime)) {
            isTimeDirty = true;
            time = newTime;
        }
    }

    public String getTime() {
        return time;
    }

    public double getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(double newTemperature) {
        if(Math.abs(newTemperature - currentTemperature) > ThermostatClient.EPSILON) {
            isCurrentTemperatureDirty = true;
            currentTemperature = newTemperature;
        }
    }

    public double getTargetTemperature() {
        return targetTemperature;
    }

    public void setTargetTemperature(double newTemperature) {
        if(Math.abs(newTemperature - targetTemperature) > ThermostatClient.EPSILON) {
            isTargetTemperatureDirty = true;
            targetTemperature = newTemperature;
        }
    }

    public double getDayTemperature() {
        return weekProgram.dayTemperature;
    }

    public void setDayTemperature(double newTemperature) {
        if(Math.abs(newTemperature - weekProgram.dayTemperature) > ThermostatClient.EPSILON) {
            isDayTemperatureDirty = true;
            weekProgram.dayTemperature = newTemperature;
        }
    }

    public double getNightTemperature() {
        return weekProgram.nightTemperature;
    }

    public void setNightTemperature(double newTemperature) {
        if(Math.abs(newTemperature - weekProgram.nightTemperature) > ThermostatClient.EPSILON) {
            isNightTemperatureDirty = true;
            weekProgram.nightTemperature = newTemperature;
        }
    }

    public boolean getWeekProgramState() {
        return weekProgramState;
    }

    public void setWeekProgramState(boolean newWeekProgramState) {
        if (weekProgramState != newWeekProgramState) {
            isWeekProgramStateDirty = true;
            weekProgramState = newWeekProgramState;
        }
    }

    public Switch getWeekDaySwitch(WeekDay weekDay, int switchIndex) {

        if (switchIndex >= ThermostatClient.NR_SWITCHES_WEEK) {
            throw new IllegalArgumentException("Switch index is too great!");
        }

        return weekProgram.weekDaySwitchMap.get(weekDay)[switchIndex];
    }

    public void setWeekDaySwitch(WeekDay weekDay, int switchIndex, Switch newSwitch) {

        Switch[] switchesForDay = weekProgram.weekDaySwitchMap.get(weekDay);
        Switch oldSwitch = switchesForDay[switchIndex];

        if (!oldSwitch.equals(newSwitch)) {
            weekDayDirtySwitchMap.put(weekDay, true);
            switchesForDay[switchIndex] = newSwitch;
        }
    }

    public WeekProgram getCopyOfWeekProgram() {
        return weekProgram.getCopy();
    };

    public void setWeekProgram(WeekProgram weekProgram) {
        this.weekProgram = weekProgram;
        isDayTemperatureDirty = true;
        isNightTemperatureDirty = true;
        for(WeekDay day : weekDayDirtySwitchMap.keySet()) {
            weekDayDirtySwitchMap.put(day, true);
        }
    }

}

