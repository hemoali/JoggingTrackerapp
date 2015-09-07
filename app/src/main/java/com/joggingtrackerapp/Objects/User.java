package com.joggingtrackerapp.Objects;

import java.io.Serializable;

/**
 * Created by ibrahimradwan on 9/4/15.
 */
public class User implements Serializable {
    private String id, email, pass = "", level;
    private boolean optionsVisible = false;

    public String getLevel () {
        return level;
    }

    public void setLevel (String level) {
        this.level = level;
    }

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

    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getPass () {
        return pass;
    }

    public void setPass (String pass) {
        this.pass = pass;
    }
}
