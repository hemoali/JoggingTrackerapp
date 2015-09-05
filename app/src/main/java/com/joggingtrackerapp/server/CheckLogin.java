package com.joggingtrackerapp.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.joggingtrackerapp.ui.MainActivityForManagers;
import com.joggingtrackerapp.ui.MainActivityForUsers;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.joggingtrackerapp.utils.Constants.API_URL;
import static com.joggingtrackerapp.utils.Constants.TAG_DEBUG;
import static com.joggingtrackerapp.utils.Constants.TAG_ERROR;

/**
 * Created by ibrahimradwan on 9/2/15.
 */
public class CheckLogin extends AsyncTask<String, Void, String> {
    private ProgressDialog pd;
    private Context context;
    private String emailStr;

    public CheckLogin (Context context) {
        this.context = context;
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
        emailStr = params[0];
        String passStr = params[1];
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "keep-alive");

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("task", "login")
                    .appendQueryParameter("email", emailStr)
                    .appendQueryParameter("pass", passStr);

            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            conn.connect();

            String cookie = conn.getHeaderField("set-cookie");
            //Get Cookie from connection
            if (cookie != null && cookie.length() > 0) {
                Session.setsCookie(context, cookie);
            }

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
            String[] loginData = Parse.parseLoginData(result);
            if (loginData[0].equals("200")) {
                if (loginData[1].equals("Login Succeeded")) {
                    if (!loginData[2].trim().equals("") && loginData[2] != null) {
                        Toast.makeText(context, "Welcome!", Toast.LENGTH_SHORT).show();
                        MyPreferences.add(context, Constants.PREF_EMAIL, emailStr, "string");
                        MyPreferences.add(context, Constants.PREF_SESSION_ID, loginData[2], "string");
                        MyPreferences.add(context, Constants.PREF_LEVEL, loginData[3], "string");
                        MyPreferences.add(context, Constants.PREF_API_KEY, loginData[4], "string");

                        addToWeeklyReportList();

                        Utils.moveAfterLoginOrSignup(context, (loginData[3].equals("2")) ? MainActivityForUsers.class : MainActivityForManagers.class, true);

                    }
                } else {
                    if (loginData[1] != null)
                        Toast.makeText(context, loginData[1], Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Please Check Your Email/Password Again", Toast.LENGTH_SHORT).show();
            }
        }

        super.onPostExecute(result);
    }

    private void addToWeeklyReportList () { // for signup too
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        String currentDate = dateFormat.format(cal.getTime());

        boolean add = true;

        if (MyPreferences.getString(context, Constants.PREF_REPORT_LIST) != null && !MyPreferences.getString(context, Constants.PREF_REPORT_LIST).trim().equals("")) {
            String oldReportList = MyPreferences.getString(context, Constants.PREF_REPORT_LIST);

            for (String s : oldReportList.split(";")) {
                Log.e(TAG_ERROR, s.split(":")[0]);
                if (s.split(":")[0].equalsIgnoreCase(emailStr)) {
                    add = false;
                }
            }
            if (add) {
                oldReportList = oldReportList.concat(emailStr + ":" + currentDate + ";");
                MyPreferences.add(context, Constants.PREF_REPORT_LIST, oldReportList, "string");
            }
        } else {
            String ReportList = (emailStr + ":" + currentDate + ";");
            MyPreferences.add(context, Constants.PREF_REPORT_LIST, ReportList, "string");
        }
        if (add){
            //Register notification
        }
        Log.d(TAG_DEBUG, MyPreferences.getString(context, Constants.PREF_REPORT_LIST));
    }

    public void stop () {
        onPostExecute(null);
        this.cancel(true);

    }
}
