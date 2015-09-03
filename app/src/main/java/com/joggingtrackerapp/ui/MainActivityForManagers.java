package com.joggingtrackerapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.joggingtrackerapp.R;
import com.joggingtrackerapp.adapters.SamplePagerAdapter;
import com.joggingtrackerapp.utils.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ibrahimradwan on 9/3/15.
 */
public class MainActivityForManagers extends AppCompatActivity {
    private ViewPager viewPager;
    //private static SlidingTabLayout slidingTabLayout;
    private SamplePagerAdapter samplePagerAdapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        setContentView(R.layout.activity_main_for_managers);

        List<Fragment> fragments = getFragments();
        samplePagerAdapter = new SamplePagerAdapter(getSupportFragmentManager(), fragments);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(samplePagerAdapter);


        SlidingTabLayout sliding_tabs = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        sliding_tabs.setDistributeEvenly(true);
        sliding_tabs.setViewPager(viewPager);
    }

    private List<Fragment> getFragments () {

        List<Fragment> fList = new ArrayList<>();

        fList.add(new TimesFragment());
        fList.add(new TimesFragment());

        return fList;

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
            case R.id.menu_filter_by_date:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
