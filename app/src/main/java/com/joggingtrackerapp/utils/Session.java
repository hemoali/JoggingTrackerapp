package com.joggingtrackerapp.utils;

import android.content.Context;

/**
 * Created by ibrahimradwan on 9/2/15.
 */
public class Session {

    public static String getsCookie (Context context) {
        return MyPreferences.getString(context, Constants.PREF_COOKIE);
    }

    public static void setsCookie (Context context, String sCookie) {
        MyPreferences.add(context, Constants.PREF_COOKIE, sCookie, "string");
    }
}
