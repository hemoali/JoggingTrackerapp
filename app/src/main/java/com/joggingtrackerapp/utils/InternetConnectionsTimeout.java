package com.joggingtrackerapp.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.joggingtrackerapp.server.CheckLogin;
import com.joggingtrackerapp.server.SignUp;

/**
 * Created by ibrahimradwan on 9/3/15.
 */
public class InternetConnectionsTimeout {
    private static Handler handler;
    private static Runnable runnable;

    public static void startStopWatch (final AsyncTask asyncTask, int timeout, Context context) {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run () {
                if (asyncTask instanceof SignUp) {
                    ((SignUp) asyncTask).stop();
                } else if (asyncTask instanceof CheckLogin) {
                    ((CheckLogin) asyncTask).stop();
                }
            }
        };
        handler.postDelayed(runnable, timeout);
    }

    public static void stopStopWatch () {
        handler.removeCallbacks(runnable);
    }
}
