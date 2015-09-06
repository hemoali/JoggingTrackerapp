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
import android.widget.ImageView;
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
public class MainActivityForUsers_AdminsView extends AppCompatActivity {
    private static ListView listview_times;
    private static TimesAdapter adapter;
    private static Activity activity;
    private static ArrayList<Time> allTimes;
    private static AlertDialog addTimeDialog, filterTimesDialog;
    private static String userID;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activity = this;
        setContentView(R.layout.activity_main_for_users);

        listview_times = (ListView) findViewById(R.id.listview_times);

        userID = getIntent().getStringExtra("userID");

        new ReadTimes(MainActivityForUsers_AdminsView.this, userID).execute();


    }

    public static void removeRecordFromLV (int position) {
        allTimes.remove(position);
        adapter.notifyDataSetChanged();
    }

    public static void addRecordToLV (Time time) {
        allTimes.add(0, time);
        adapter.notifyDataSetChanged();
    }

    public void editRecordInLV (Time t, String positionStr) {
        int position = Integer.parseInt(positionStr);
        Time oldTime = allTimes.get(position);
        oldTime.setDate(t.getDate());
        oldTime.setTime(t.getTime());
        oldTime.setDistance(t.getDistance());
        allTimes.set(position, oldTime);
        adapter.notifyDataSetChanged();

    }

    public static void fillTimesListView (ArrayList<Time> allTimes, boolean filterEnabled) {
        if (!filterEnabled)
            MainActivityForUsers_AdminsView.allTimes = allTimes;

        if (allTimes.size() == 0 && !filterEnabled) {
            Toast.makeText(activity, "No Jogging Times Recorded Yet", Toast.LENGTH_SHORT).show();
        }
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
            case android.R.id.home:
                this.finish();
                return true;
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
                        String dateStr = date.getYear() + "-" + String.format("%02d", date.getMonth() + 1) + "-" + String.format("%02d", date.getDayOfMonth());
                        if (timeStr.equals("") || timeStr == null || distanceStr.equals("") || distanceStr == null ||
                                dateStr.trim().equals("") || dateStr == null) {
                            Toast.makeText(activity, "All Fields Are Required", Toast.LENGTH_SHORT).show();

                        }else if (Integer.parseInt(timeStr) == 0 || Integer.parseInt(distanceStr) == 0) {
                            Toast.makeText(activity, "Invalid Values", Toast.LENGTH_SHORT).show();
                        }  else {
                            new AddTime(activity, userID).execute(dateStr, timeStr, distanceStr);
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
            case R.id.menu_filter_by_date:
                View filterView = getLayoutInflater().inflate(R.layout.dialog_filter_times, null);

                AlertDialog.Builder filterTimesDialogBuilder = new AlertDialog.Builder(this);
                filterTimesDialogBuilder.setView(filterView);
                filterTimesDialogBuilder.setCancelable(true);

                // Listeners
                Button filterItems = (Button) filterView.findViewById(R.id.filter);
                Button cancelFilter = (Button) filterView.findViewById(R.id.cancel);
                Button resetFilter = (Button) filterView.findViewById(R.id.reset);
                final ImageView switchDatePicker = (ImageView) filterView.findViewById(R.id.switchDatePicker);
                final DatePicker fromDate = (DatePicker) filterView.findViewById(R.id.fromDate);
                final DatePicker toDate = (DatePicker) filterView.findViewById(R.id.toDate);


                switchDatePicker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        if (fromDate.getVisibility() == View.VISIBLE) {
                            switchDatePicker.setImageResource(R.drawable.to_enabled_check);
                            fromDate.setVisibility(View.GONE);
                            toDate.setVisibility(View.VISIBLE);
                        } else {
                            switchDatePicker.setImageResource(R.drawable.from_enabled_check);
                            fromDate.setVisibility(View.VISIBLE);
                            toDate.setVisibility(View.GONE);
                        }
                    }
                });
                filterItems.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        String fromDateStr = fromDate.getYear() + "-" + String.format("%02d", fromDate.getMonth() + 1) + "-" + String.format("%02d", fromDate.getDayOfMonth());
                        String toDateStr = toDate.getYear() + "-" + String.format("%02d", toDate.getMonth() + 1) + "-" + String.format("%02d", toDate.getDayOfMonth());
                        if (toDateStr.equals("") || toDateStr == null ||
                                fromDateStr.trim().equals("") || fromDateStr == null) {
                            Toast.makeText(activity, "All Fields Are Required", Toast.LENGTH_SHORT).show();

                        } else if (fromDateStr.compareTo(toDateStr) > 0) {
                            Toast.makeText(activity, "'From' Date Must Be Before 'To' Date", Toast.LENGTH_SHORT).show();
                        } else {
                            ArrayList<Time> filteredItems = new ArrayList<Time>();
                            for (Time t : allTimes) {
                                String date = t.getDate();
                                if (date.compareTo(fromDateStr) >= 0 && date.compareTo(toDateStr) <= 0) {
                                    filteredItems.add(t);
                                }
                            }
                            fillTimesListView(filteredItems, true);
                            filterTimesDialog.dismiss();
                        }
                    }
                });
                cancelFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        filterTimesDialog.dismiss();
                    }
                });
                resetFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        fillTimesListView(allTimes, false);
                        filterTimesDialog.dismiss();
                    }
                });
                filterTimesDialog = filterTimesDialogBuilder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void dismissAddTimeDialog () {
        addTimeDialog.dismiss();
    }


}