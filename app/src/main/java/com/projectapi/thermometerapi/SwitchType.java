package com.projectapi.thermometerapi;

/**
 * Created by AsciiBunny on 21-6-2017.
 */

public enum SwitchType {
    DAY("Day"),
    NIGHT("Night");

    public final String name;

    SwitchType(String name) {
        this.name = name;
    }
}
