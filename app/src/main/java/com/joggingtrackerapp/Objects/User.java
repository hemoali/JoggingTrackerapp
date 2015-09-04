package com.joggingtrackerapp.Objects;

import java.io.Serializable;

/**
 * Created by ibrahimradwan on 9/4/15.
 */
public class User implements Serializable {
    String id, email, pass = "";
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
