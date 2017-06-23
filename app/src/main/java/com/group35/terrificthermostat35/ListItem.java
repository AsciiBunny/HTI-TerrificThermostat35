package com.group35.terrificthermostat35;

/**
 * Created by s163390 on 23-6-2017.
 */

public class ListItem {
    String ProgramName;
    String ProgramName1;

    public ListItem(String ProgramName1, String ProgramName)
    {
        this.ProgramName=ProgramName;
        this.ProgramName1=ProgramName1;
    }
    public String getProgramName()
    {
        return ProgramName;
    }
    public String getProgramName1()
    {
        return ProgramName1;
    }
}
