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

/**
 * Created by ibrahimradwan on 9/2/15.
 */
public class SignUp extends AsyncTask<String, Void, String> {
    private ProgressDialog pd;
    private Context context;
    private String emailStr, levelStr;

    public SignUp (Context context) {
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

        InternetConnectionsTimeout.startTimesStopWatch(this, 10000);
    }

    @Override
    protected String doInBackground (String... params) {
        if (!Checks.isNetworkAvailable(context)) return null;
        emailStr = params[0];
        String passStr = params[1];
        levelStr = params[2];
        try {
            URL url = new URL(API_URL+"register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "keep-alive");

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("email", emailStr)
                    .appendQueryParameter("level", levelStr)
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
            String[] signupData = Parse.parseSignupData(result);
            if (signupData != null) {
                if (signupData[0].equals("200")) {
                    if (signupData[1].equals("Signup Succeeded")) {
                        if (!signupData[2].trim().equals("") && signupData[2] != null) {
                            Toast.makeText(context, "Welcome!", Toast.LENGTH_SHORT).show();
                            MyPreferences.add(context, Constants.PREF_EMAIL, emailStr, "string");
                            MyPreferences.add(context, Constants.PREF_LEVEL, levelStr, "string");
                            MyPreferences.add(context, Constants.PREF_API_KEY, signupData[2], "string");
                            MyPreferences.add(context, Constants.PREF_REG_DATE, signupData[3], "string");

                            Utils.moveAfterLoginOrSignup(context, (levelStr.equals("2")) ? MainActivityForUsers.class : MainActivityForManagers.class, true);

                        }
                    } else {
                        if (signupData[1] != null)
                            Toast.makeText(context, signupData[1], Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();

            }
        }
        super.onPostExecute(result);
    }

    public void stop () {
        onPostExecute(null);
        this.cancel(true);

    }
}
