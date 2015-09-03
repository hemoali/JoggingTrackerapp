package com.joggingtrackerapp.Objects;

import com.joggingtrackerapp.utils.Utils;

import java.io.Serializable;

/**
 * Created by ibrahimradwan on 9/3/15.
 */
public class Time implements Serializable {
    String id, user_id, date, time, distance;
    boolean optionsVisible = false;

    public boolean isOptionsVisible () {
        return optionsVisible;
    }

    public void setOptionsVisible (boolean optionsVisible) {
        this.optionsVisible = optionsVisible;
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getUser_id () {
        return user_id;
    }

    public void setUser_id (String user_id) {
        this.user_id = user_id;
    }

    public String getDate () {
        return date;
    }

    public void setDate (String date) {
        this.date = date;
    }

    public String getTime () {
        return time;
    }

    public void setTime (String time) {
        this.time = time;
    }

    public String getDistance () {
        return distance;
    }

    public void setDistance (String distance) {
        this.distance = distance;
    }

    public String getSpeed () {
        int dis = Integer.parseInt(getDistance());
        int time = Integer.parseInt(getTime());
        float speed = (float) dis / time;

        return String.valueOf(Utils.round(speed, 2));
    }
}
