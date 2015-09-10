package com.joggingtrackerapp.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.joggingtrackerapp.Objects.User;
import com.joggingtrackerapp.ui.MainActivityForManagers;
import com.joggingtrackerapp.ui.UsersFragment;
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

public class AddUser extends AsyncTask<String, Void, String> {
    private ProgressDialog pd;
    private Context context;
    private String emailStr, passStr, levelStr;

    public AddUser (Context context) {
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

        InternetConnectionsTimeout.startUsersStopWatch(this, 10000);
    }

    @Override
    protected String doInBackground (String... params) {
        if (!Checks.isNetworkAvailable(context)) return null;
        emailStr = params[0];
        passStr = params[1];
        levelStr = params[2];
        try {
            URL url = new URL(API_URL+"users");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Authorization", MyPreferences.getString(context, Constants.PREF_API_KEY));

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("email", emailStr)
                    .appendQueryParameter("pass", passStr)
                    .appendQueryParameter("level", levelStr);

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
        InternetConnectionsTimeout.stopUsersStopWatch();
        pd.dismiss();
        if (result == null || result.trim().length() <= 0) {
            Toast.makeText(context, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        } else {
            String[] addData = Parse.parseAddUserData(result);
            if (addData[0].trim().equals("200") && addData[1].trim().equals("User Add Succeeded")) {
                Toast.makeText(context, "User Added Successfully", Toast.LENGTH_SHORT).show();

                User u = new User();
                u.setId(addData[2]);
                u.setEmail(emailStr);
                u.setLevel(levelStr);
                
                if (context instanceof MainActivityForManagers) {

                    UsersFragment.addRecordToLV(u);
                    MainActivityForManagers.dismissAddUserDialog();
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
