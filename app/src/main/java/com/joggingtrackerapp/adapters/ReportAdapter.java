package com.joggingtrackerapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joggingtrackerapp.Objects.Report;
import com.joggingtrackerapp.R;

import java.util.ArrayList;

/**
 * Created by ibrahimradwan on 9/3/15.
 */
public class ReportAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Report> allReports;

    public ReportAdapter (Context c, ArrayList<Report> allReports) {
        context = c;
        this.allReports = allReports;
    }


    @Override
    public int getCount () {
        return allReports.size();
    }

    @Override
    public Object getItem (int position) {
        return allReports.get(position);
    }

    @Override
    public long getItemId (int position) {
        return 0;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View holder = mInflater.inflate(R.layout.listview_report, null);

        final TextView weekNo = (TextView) holder.findViewById(R.id.weekNo), distance = (TextView) holder.findViewById(R.id.distance), speed = (TextView) holder.findViewById(R.id.speed);

        final Report currentReport = allReports.get(position);

        // Fill Fields
        weekNo.append(currentReport.getNo() + "");
        distance.setText(currentReport.getDistance());
        speed.setText(currentReport.getAvgSpeed());


        return holder;
    }

}
