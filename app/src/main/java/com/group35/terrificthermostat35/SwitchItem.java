package com.group35.terrificthermostat35;

/**
 * Created by s163390 on 25-6-2017.
 */

public class SwitchItem {
    String time;
    int Image;

    public SwitchItem(String time,int Image)
    {
        this.time=time;
        this.Image=Image;
    }
    public String getTime()
    {
        return time;
    }
    public int getImage()
    {
        return Image;
    }
}
