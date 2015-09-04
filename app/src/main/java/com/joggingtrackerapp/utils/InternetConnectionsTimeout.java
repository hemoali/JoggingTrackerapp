package com.joggingtrackerapp.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.joggingtrackerapp.server.AddTime;
import com.joggingtrackerapp.server.CheckLogin;
import com.joggingtrackerapp.server.DeleteTime;
import com.joggingtrackerapp.server.DeleteUser;
import com.joggingtrackerapp.server.EditUser;
import com.joggingtrackerapp.server.ReadTimes;
import com.joggingtrackerapp.server.ReadUsers;
import com.joggingtrackerapp.server.SignUp;

/**
 * Created by ibrahimradwan on 9/3/15.
 */
public class InternetConnectionsTimeout {
    private static Handler timesHandler, usersHandler;
    private static Runnable timesRunnable, usersRunnable;

    public static void startTimesStopWatch (final AsyncTask asyncTask, int timeout, Context context) {
        timesHandler = new Handler();
        timesRunnable = new Runnable() {
            @Override
            public void run () {
                if (asyncTask instanceof SignUp) {
                    ((SignUp) asyncTask).stop();
                } else if (asyncTask instanceof CheckLogin) {
                    ((CheckLogin) asyncTask).stop();
                } else if (asyncTask instanceof ReadTimes) {
                    ((ReadTimes) asyncTask).stop();
                } else if (asyncTask instanceof DeleteTime) {
                    ((DeleteTime) asyncTask).stop();
                } else if (asyncTask instanceof AddTime) {
                    ((AddTime) asyncTask).stop();
                }
            }
        };
        timesHandler.postDelayed(timesRunnable, timeout);
    }

    public static void stopTimesStopWatch () {
        timesHandler.removeCallbacks(timesRunnable);
    }

    public static void startUsersStopWatch (final AsyncTask asyncTask, int timeout, Context context) {
        usersHandler = new Handler();
        usersRunnable = new Runnable() {
            @Override
            public void run () {
                if (asyncTask instanceof ReadUsers) {
                    ((ReadUsers) asyncTask).stop();
                } else if(asyncTask instanceof DeleteUser){
                    ((DeleteUser) asyncTask).stop();
                }else if(asyncTask instanceof EditUser){
                    ((EditUser) asyncTask).stop();
                }
            }
        };
        usersHandler.postDelayed(usersRunnable, timeout);
    }

    public static void stopUsersStopWatch () {
        usersHandler.removeCallbacks(usersRunnable);
    }
}
