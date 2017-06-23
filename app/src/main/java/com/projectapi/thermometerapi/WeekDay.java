package com.projectapi.thermometerapi;

/**
 * Created by AsciiBunny on 21-6-2017.
 */

public enum WeekDay {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    public final String name;

    WeekDay(String name) {
        this.name = name;
    }

    public static WeekDay parseWeekDay(String weekDay) {
        switch(weekDay.toLowerCase()) {
            case "monday":
                return MONDAY;
            case "tuesday":
                return TUESDAY;
            case "wednesday":
                return WEDNESDAY;
            case "thursday":
                return THURSDAY;
            case "friday":
                return FRIDAY;
            case "saturday":
                return SATURDAY;
            case "sunday":
                return SUNDAY;
        }

        return null;
    }
}
