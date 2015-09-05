package com.joggingtrackerapp.Objects;

import com.joggingtrackerapp.utils.Utils;

import java.io.Serializable;

/**
 * Created by ibrahimradwan on 9/5/15.
 */
public class Report implements Serializable {
    String distance, time, avgSpeed, timesCount;
    int no;

    public int getNo () {
        return no;
    }

    public void setNo (int no) {
        this.no = no;
    }

    public String getDistance () {
        return distance;
    }

    public void setDistance (String distance) {
        this.distance = distance;
    }

    public String getTime () {
        return time;
    }

    public void setTime (String time) {
        this.time = time;
    }

    public String getAvgSpeed () {
        int dis = Integer.parseInt(getDistance());
        int time = Integer.parseInt(getTime());
        float speed = (float) dis / time;

        return String.valueOf(Utils.round(speed, 2));
    }

    public void setAvgSpeed (String avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public String getTimesCount () {
        return timesCount;
    }

    public void setTimesCount (String timesCount) {
        this.timesCount = timesCount;
    }
}
