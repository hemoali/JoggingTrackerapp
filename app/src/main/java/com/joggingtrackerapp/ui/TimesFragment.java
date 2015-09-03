package com.joggingtrackerapp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.joggingtrackerapp.Objects.Time;
import com.joggingtrackerapp.R;
import com.joggingtrackerapp.adapters.TimesAdapter;

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
    private static AlertDialog addTimeDialog, filterTimesDialog;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(
                R.layout.activity_main_for_users, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }
}
