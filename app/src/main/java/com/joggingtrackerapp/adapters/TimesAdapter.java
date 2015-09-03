package com.joggingtrackerapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joggingtrackerapp.Objects.Time;
import com.joggingtrackerapp.R;

import java.util.ArrayList;

/**
 * Created by ibrahimradwan on 9/3/15.
 */
public class TimesAdapter extends BaseAdapter {
    private Context context;
    public static ArrayList<Time> allTimes;

    public TimesAdapter (Context c, ArrayList<Time> allTimes) {
        context = c;
        this.allTimes = allTimes;
    }


    @Override
    public int getCount () {
        return allTimes.size();
    }

    @Override
    public Object getItem (int position) {
        return allTimes.get(position);
    }

    @Override
    public long getItemId (int position) {
        return 0;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View holder = mInflater.inflate(R.layout.listview_time, null);

        TextView time = (TextView) holder.findViewById(R.id.time), date = (TextView) holder.findViewById(R.id.date), distance = (TextView) holder.findViewById(R.id.distance);

        Time currentTime = allTimes.get(position);

        time.setText(currentTime.getTime());
        date.setText(currentTime.getDate());
        distance.setText(currentTime.getDistance());

        return holder;
    }
}
