package com.joggingtrackerapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.joggingtrackerapp.R;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import static com.joggingtrackerapp.utils.Constants.TAG_ERROR;

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
        context.startActivity(intent);
        if (finish) ((Activity) context).finish();
    }

    public static float round (float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static int[] getScreenDiem (Context context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        return new int[]{width, height};
    }

    public static float dpToPx (Context context, int dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static long getDaysInBetween (String timeDate, Context context) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String registerationDate = MyPreferences.getString(context, Constants.PREF_REG_DATE);
        try {
            Date date1 = dateFormat.parse(timeDate);
            Date date2 = dateFormat.parse(registerationDate);
            long diff = date1.getTime() - date2.getTime();
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            Log.e(TAG_ERROR, "Error: " + e.getMessage());
        }
        return 0;
    }

    public static String formattedDate (Context context, String date) {
        int Month = Integer.parseInt(date.substring(5, 7));

        String month = "";

        String Day = date.substring(8, 10);

        if (Day.startsWith("0")) {

            Day = date.substring(9, 10);

        }

        String Year = date.substring(0, 4);


        switch (Month) {
            case 1:
                month = context.getResources().getString(R.string.jan);
                break;
            case 2:
                month = context.getResources().getString(R.string.feb);
                break;
            case 3:
                month = context.getResources().getString(R.string.mar);
                break;
            case 4:
                month = context.getResources().getString(R.string.apr);
                break;
            case 5:
                month = context.getResources().getString(R.string.may);
                break;
            case 6:
                month = context.getResources().getString(R.string.jun);
                break;
            case 7:
                month = context.getResources().getString(R.string.jul);
                break;
            case 8:
                month = context.getResources().getString(R.string.aug);
                break;
            case 9:
                month = context.getResources().getString(R.string.sep);
                break;
            case 10:
                month = context.getResources().getString(R.string.oct);
                break;
            case 11:
                month = context.getResources().getString(R.string.nov);
                break;
            case 12:
                month = context.getResources().getString(R.string.dec);
                break;
        }
        String dayName = "";
        Calendar calendar = new GregorianCalendar(Integer.parseInt(Year),
                Month - 1, Integer.parseInt(Day));
        int result = calendar.get(Calendar.DAY_OF_WEEK);
        switch (result) {
            case Calendar.SATURDAY:

                dayName = (context.getResources().getString(R.string.sat));

                break;
            case Calendar.SUNDAY:

                dayName = (context.getResources().getString(R.string.sun));

                break;
            case Calendar.MONDAY:

                dayName = (context.getResources().getString(R.string.mon));

                break;
            case Calendar.TUESDAY:

                dayName = (context.getResources().getString(R.string.tues));

                break;
            case Calendar.WEDNESDAY:

                dayName = (context.getResources().getString(R.string.wed));

                break;
            case Calendar.THURSDAY:

                dayName = (context.getResources().getString(R.string.thurs));

                break;
            case Calendar.FRIDAY:

                dayName = (context.getResources().getString(R.string.fri));

                break;
        }

        return month + " " + Day + ", " + Year;

    }

    public static int checkLevel (Context context) {
        return Integer.parseInt(MyPreferences.getString(context, Constants.PREF_LEVEL));
    }

    public static void hideKeyboard (Context context) {
        // Check if no view has focus:
        View view = ((Activity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
