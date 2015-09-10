package com.joggingtrackerapp.server;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
public class EditTime extends AsyncTask<String, Void, String> {
    private ProgressDialog pd;
    private Context context;
    private String dateStr, timeStr, distanceStr, timeIDStr, position;
    private AlertDialog editTimeDialog;

    public EditTime (Context context, AlertDialog editTimeDialog) {
        this.context = context;
        this.editTimeDialog = editTimeDialog;
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
        timeIDStr = params[3];
        position = params[4];
        try {
            URL url = new URL(API_URL + "times/" + timeIDStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PATCH");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", MyPreferences.getString(context, Constants.PREF_API_KEY));

            String str = " { \"date\": \"" + dateStr + "\", \"time\": \"" + timeStr + "\", \"distance\": \"" + distanceStr + "\" }";
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
        InternetConnectionsTimeout.stopTimesStopWatch();
        pd.dismiss();
        if (result == null || result.trim().length() <= 0) {
            Toast.makeText(context, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        } else {

            String[] editData = Parse.parseUpdateTimeData(result);
            if (editData[0].trim().equals("200")) {
                Toast.makeText(context, "Record Updated Successfully", Toast.LENGTH_SHORT).show();

                Time t = new Time();
                t.setDate(dateStr);
                t.setTime(timeStr);
                t.setDistance(distanceStr);

                if (context instanceof MainActivityForUsers) {
                    ((MainActivityForUsers) context).editRecordInLV(t, position);
                } else if (context instanceof MainActivityForManagers) {
                    TimesFragment.editRecordInLV(t, position);
                } else if (context instanceof MainActivityForUsers_AdminsView) {
                    ((MainActivityForUsers_AdminsView) context).editRecordInLV(t, position);
                }
                editTimeDialog.dismiss();


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
