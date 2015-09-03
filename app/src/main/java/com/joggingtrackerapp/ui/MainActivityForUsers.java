package com.joggingtrackerapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.joggingtrackerapp.Objects.Time;
import com.joggingtrackerapp.R;
import com.joggingtrackerapp.adapters.TimesAdapter;
import com.joggingtrackerapp.server.ReadTimes;

import java.util.ArrayList;

/**
 * Created by ibrahimradwan on 9/3/15.
 */
public class MainActivityForUsers extends AppCompatActivity {
    private static ListView listview_times;
    private static TimesAdapter adapter;
    private static Activity activity;

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

    public static void fillTimesListView (ArrayList<Time> allTimes) {
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

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}