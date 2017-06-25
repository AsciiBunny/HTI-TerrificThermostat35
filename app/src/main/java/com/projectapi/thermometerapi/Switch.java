package com.projectapi.thermometerapi;

import java.io.Serializable;

/**
 * Created by AsciiBunny on 21-6-2017.
 */

public class Switch implements Serializable{
    public SwitchType type;
    public boolean state;
    public String time;

    public Switch(SwitchType type, boolean state, String time) {
        this.type = type;
        this.state = state;
        this.time = time;
    }

    public SwitchType getSwitchType() {
        return type;
    }

    public boolean getState() {
        return state;
    }

    public String getTime() {
        return time;
    }

    public Switch getCopy() {
        return new Switch(type, state, time);
    }
}
