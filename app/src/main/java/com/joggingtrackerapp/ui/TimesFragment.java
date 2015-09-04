package com.joggingtrackerapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.joggingtrackerapp.Objects.Time;
import com.joggingtrackerapp.R;
import com.joggingtrackerapp.adapters.TimesAdapter;
import com.joggingtrackerapp.server.ReadTimes;

import java.util.ArrayList;

/**
 * Created by ibrahimradwan on 9/3/15.
 */
public class TimesFragment extends Fragment {
    private View rootView;
    private static ListView listview_times;
    private static TimesAdapter adapter;
    private static Activity activity;
    private static ArrayList<Time> allTimes;

    public static ArrayList<Time> getAllTimes () {
        return allTimes;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(
                R.layout.activity_main_for_users, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        activity = getActivity();

        listview_times = (ListView) rootView.findViewById(R.id.listview_times);
        new ReadTimes(activity, this).execute();

        super.onActivityCreated(savedInstanceState);
    }

    public static void removeRecordFromLV (int position) {
        allTimes.remove(position);
        adapter.notifyDataSetChanged();
    }

    public static void addRecordToLV (Time time) {
        allTimes.add(0, time);
        adapter.notifyDataSetChanged();
    }

    public static void editRecordInLV (Time t, String positionStr) {
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
            TimesFragment.allTimes = allTimes;

        if (allTimes.size() == 0 && !filterEnabled){
            Toast.makeText(activity, "No Jogging Times Recorded Yet",Toast.LENGTH_SHORT).show();
        }
        adapter = new TimesAdapter(activity, allTimes);
        listview_times.setAdapter(adapter);
    }


}
