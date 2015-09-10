package com.joggingtrackerapp.server;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
public class EditUser extends AsyncTask<String, Void, String> {
    private ProgressDialog pd;
    private Context context;
    private String emailStr, passStr, userIDStr, position, levelStr = "";
    private AlertDialog editUserDialog;

    public EditUser (Context context, AlertDialog editUserDialog) {
        this.context = context;
        this.editUserDialog = editUserDialog;
    }

    public EditUser (Context context, AlertDialog editUserDialog, String levelStr) {
        this.context = context;
        this.editUserDialog = editUserDialog;
        this.levelStr = levelStr;
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
        userIDStr = params[2];
        position = params[3];
        try {
            URL url = new URL(API_URL + "users/" + userIDStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PATCH");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", MyPreferences.getString(context, Constants.PREF_API_KEY));
            String str = " { \"email\": \"" + emailStr + "\", \"pass\": \"" + passStr + "\", \"level\": \"" + levelStr + "\" }";
            byte[] outputInBytes = str.getBytes("UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write(outputInBytes);
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

            String[] editData = Parse.parseUpdateUserData(result);
            if (editData[0].trim().equals("200")) {
                Toast.makeText(context, "User Updated Successfully", Toast.LENGTH_SHORT).show();

                User u = new User();
                u.setEmail(emailStr);
                u.setId(userIDStr);
                u.setLevel(levelStr);

                if (context instanceof MainActivityForManagers) {
                    UsersFragment.editRecordInLV(u, position);
                }
                editUserDialog.dismiss();


            } else {
                Toast.makeText(context, editData[1], Toast.LENGTH_SHORT).show();
            }
        }
        super.onPostExecute(result);
    }

    public void stop () {
        onPostExecute(null);
    }
}
