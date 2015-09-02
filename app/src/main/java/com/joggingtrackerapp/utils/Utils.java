package com.joggingtrackerapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ibrahimradwan on 9/2/15.
 */
public class Utils {
    public static String convertStreamToString (java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static void moveAfterLoginOrSignup (Context context, Class<?> nextClass, boolean finish) {
        Intent intent = new Intent(context, nextClass);
        ((Activity) context).startActivity(intent);
        if (finish) ((Activity) context).finish();
    }
}
