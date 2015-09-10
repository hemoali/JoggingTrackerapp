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

import static com.joggingtrackerapp.utils.Constants.API_URL;
import static com.joggingtrackerapp.utils.Constants.TAG_ERROR;

public class AddTime extends AsyncTask<String, Void, String> {
    private ProgressDialog pd;
    private Context context;
    private String dateStr, timeStr, distanceStr, userID = "0";

    public AddTime (Context context) {
        this.context = context;
    }

    public AddTime (Context context, String userID) {
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

        InternetConnectionsTimeout.startTimesStopWatch(this, 10000);
    }

    @Override
    protected String doInBackground (String... params) {
        if (!Checks.isNetworkAvailable(context)) return null;
        dateStr = params[0];
        timeStr = params[1];
        distanceStr = params[2];
        try {
            URL url = new URL(API_URL+"times");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Authorization", MyPreferences.getString(context, Constants.PREF_API_KEY));

            Uri.Builder builder;
            if (userID.trim().equals("0")) {
                builder = new Uri.Builder()
                        .appendQueryParameter("date", dateStr)
                        .appendQueryParameter("time", timeStr)
                        .appendQueryParameter("distance", distanceStr);
            } else {
                builder = new Uri.Builder()
                        .appendQueryParameter("user_id", userID)
                        .appendQueryParameter("date", dateStr)
                        .appendQueryParameter("time", timeStr)
                        .appendQueryParameter("distance", distanceStr);
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
            Toast.makeText(context, "Please Check Your Internet Connections", Toast.LENGTH_SHORT).show();
        } else {
            String[] addData = Parse.parseAddTimeData(result);
            if (addData[0].trim().equals("200")) {
                Toast.makeText(context, "Record Added Successfully", Toast.LENGTH_SHORT).show();

                Time t = new Time();
                t.setDate(dateStr);
                t.setTime(timeStr);
                t.setDistance(distanceStr);
                t.setUser_id(addData[2]);
                t.setId(addData[3]);

                if (context instanceof MainActivityForUsers) {

                    MainActivityForUsers.addRecordToLV(t);
                    MainActivityForUsers.dismissAddTimeDialog();
                } else if (context instanceof MainActivityForManagers) {
                    TimesFragment.addRecordToLV(t);
                    MainActivityForManagers.dismissAddTimeDialog();
                } else if (context instanceof MainActivityForUsers_AdminsView) {
                    MainActivityForUsers_AdminsView.addRecordToLV(t);
                    MainActivityForUsers_AdminsView.dismissAddTimeDialog();
                }


            } else {
                Toast.makeText(context, addData[1], Toast.LENGTH_SHORT).show();
            }
        }
        super.onPostExecute(result);
    }

    public void stop () {
        onPostExecute(null);
        this.cancel(true);
    }
}
