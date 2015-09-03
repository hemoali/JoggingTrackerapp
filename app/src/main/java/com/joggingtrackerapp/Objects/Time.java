package com.joggingtrackerapp.Objects;

import java.io.Serializable;

/**
 * Created by ibrahimradwan on 9/3/15.
 */
public class Time implements Serializable {
    String id, user_id, date, time, distance;

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
}
