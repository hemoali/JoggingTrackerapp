package com.joggingtrackerapp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.joggingtrackerapp.Objects.Time;
import com.joggingtrackerapp.R;
import com.joggingtrackerapp.adapters.TimesAdapter;
import com.joggingtrackerapp.server.AddTime;
import com.joggingtrackerapp.server.ReadTimes;

import java.util.ArrayList;

/**
 * Created by ibrahimradwan on 9/3/15.
 */
public class MainActivityForUsers extends AppCompatActivity {
    private static ListView listview_times;
    private static TimesAdapter adapter;
    private static Activity activity;
    private static ArrayList<Time> allTimes;
    private static AlertDialog addTimeDialog;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        activity = this;
        setContentView(R.layout.activity_main_for_users);

        listview_times = (ListView) findViewById(R.id.listview_times);

        new ReadTimes(MainActivityForUsers.this).execute();

    }

    public static void removeRecordFromLV (int position) {
        allTimes.remove(position);
        adapter.notifyDataSetChanged();
    }

    public static void addRecordToLV (Time time) {
        allTimes.add(0, time);
        adapter.notifyDataSetChanged();
    }

    public static void fillTimesListView (ArrayList<Time> allTimes) {
        MainActivityForUsers.allTimes = allTimes;
        adapter = new TimesAdapter(activity, allTimes);
        listview_times.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_times, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_add:
                View view = getLayoutInflater().inflate(R.layout.dialog_add_time, null);

                AlertDialog.Builder addTimeDialogBuilder = new AlertDialog.Builder(this);
                addTimeDialogBuilder.setView(view);
                addTimeDialogBuilder.setCancelable(true);

                // Listeners
                Button addItem = (Button) view.findViewById(R.id.addItem);
                Button cancel = (Button) view.findViewById(R.id.cancel);
                final EditText time = (EditText) view.findViewById(R.id.time);
                final EditText distance = (EditText) view.findViewById(R.id.distance);
                final DatePicker date = (DatePicker) view.findViewById(R.id.date);

                addItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        String timeStr = time.getText().toString().trim();
                        String distanceStr = distance.getText().toString().trim();
                        String dateStr = date.getYear() + "-" + String.format("%02d", date.getMonth()) + "-" + String.format("%02d", date.getDayOfMonth());
                        if (timeStr.equals("") || timeStr == null || distanceStr.equals("") || distanceStr == null ||
                                dateStr.trim().equals("") || dateStr == null) {
                            Toast.makeText(activity, "All Fields Are Required", Toast.LENGTH_SHORT).show();

                        } else {
                            new AddTime(activity).execute(dateStr, timeStr, distanceStr);
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        addTimeDialog.dismiss();
                    }
                });
                addTimeDialog = addTimeDialogBuilder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void dismissAddTimeDialog () {
        addTimeDialog.dismiss();
    }
}