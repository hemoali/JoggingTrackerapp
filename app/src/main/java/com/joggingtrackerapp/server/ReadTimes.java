package com.joggingtrackerapp.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.joggingtrackerapp.Objects.Time;
import com.joggingtrackerapp.ui.MainActivityForManagers;
import com.joggingtrackerapp.ui.MainActivityForUsers;
import com.joggingtrackerapp.ui.MainActivityForUsers_AdminsView;
import com.joggingtrackerapp.ui.TimesFragment;
import com.joggingtrackerapp.utils.Checks;
import com.joggingtrackerapp.utils.Constants;
import com.joggingtrackerapp.utils.InternetConnectionsTimeout;
import com.joggingtrackerapp.utils.MyPreferences;
import com.joggingtrackerapp.utils.Parse;
import com.joggingtrackerapp.utils.Session;
import com.joggingtrackerapp.utils.Utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import static com.joggingtrackerapp.utils.Constants.API_URL;
import static com.joggingtrackerapp.utils.Constants.TAG_DEBUG;
import static com.joggingtrackerapp.utils.Constants.TAG_ERROR;

/**
 * Created by ibrahimradwan on 9/3/15.
 */
public class ReadTimes extends AsyncTask<String, Void, String> {
    private ProgressDialog pd;
    private Context context;
    private TimesFragment timesFragment;
    private String userID = "0";

    public ReadTimes (Context context) {
        this.context = context;
    }

    public ReadTimes (Context context, TimesFragment timesFragment) {
        this.context = context;
        this.timesFragment = timesFragment;
    }

    public ReadTimes (Context context, String userID) {
        this.context = context;
        this.userID = userID;
    }

    @Override
    protected void onPreExecute () {
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setIndeterminate(true);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();

        InternetConnectionsTimeout.startTimesStopWatch(this, 10000, context);

    }

    @Override
    protected String doInBackground (String... params) {
        if (!Checks.isNetworkAvailable(context)) return null;
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Authorization", MyPreferences.getString(context, Constants.PREF_SESSION_ID) + ":" + MyPreferences.getString(context, Constants.PREF_API_KEY));
//          Set Cookie
            if (Session.getsCookie(context) != null && Session.getsCookie(context).length() > 0) {
                conn.setRequestProperty("Cookie", Session.getsCookie(context));
            }

            Uri.Builder builder;
            if (userID.trim().equals("0")) {
                builder = new Uri.Builder()
                        .appendQueryParameter("task", "getTimes");
            } else {
                builder = new Uri.Builder()
                        .appendQueryParameter("task", "getTimes_ForAdmin")
                        .appendQueryParameter("user_id", userID);
            }
            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            conn.connect();

            InputStream in = conn.getInputStream();

            return Utils.convertStreamToString(in);

        } catch (MalformedURLException e) {
            Log.e(TAG_ERROR, "Error: " + e.getMessage());
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute (String result) {
        InternetConnectionsTimeout.stopTimesStopWatch();
        pd.dismiss();

        if (result == null || result.trim().length() <= 0) {
            Toast.makeText(context, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG_DEBUG, result);
            ArrayList<Time> allTimes = Parse.parseTimes(context, result);
            if (context instanceof MainActivityForUsers) {
                ((MainActivityForUsers) context).fillTimesListView(allTimes, false);
            } else if (context instanceof MainActivityForManagers) {
                timesFragment.fillTimesListView(allTimes, false);
            } else if (context instanceof MainActivityForUsers_AdminsView) {
                ((MainActivityForUsers_AdminsView) context).fillTimesListView(allTimes, false);
            }
        }

        super.onPostExecute(result);
    }

    public void stop () {
        onPostExecute(null);
        this.cancel(true);

    }
}
