package com.joggingtrackerapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.joggingtrackerapp.Objects.User;
import com.joggingtrackerapp.R;
import com.joggingtrackerapp.adapters.UsersAdapter;
import com.joggingtrackerapp.server.ReadUsers;

import java.util.ArrayList;

/**
 * Created by ibrahimradwan on 9/4/15.
 */
public class UsersFragment extends Fragment {
    private View rootView;
    private static ListView listview_users;
    private static UsersAdapter adapter;
    private static Activity activity;
    private static ArrayList<User> allUsers;
    private static TextView no_users_to_update;

    public static ArrayList<User> getAllUsers () {
        return allUsers;
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

        listview_users = (ListView) rootView.findViewById(R.id.listview_times);
        no_users_to_update = (TextView) rootView.findViewById(R.id.no_users_to_update);
        new ReadUsers(activity, this).execute();

        super.onActivityCreated(savedInstanceState);
    }

    public static void fillUsersListView (ArrayList<User> allUsers) {
        if (allUsers.size() == 0) {
            no_users_to_update.setVisibility(View.VISIBLE);
            listview_users.setVisibility(View.GONE);
        } else {
            no_users_to_update.setVisibility(View.GONE);
            listview_users.setVisibility(View.VISIBLE);
        }
        UsersFragment.allUsers = allUsers;
        adapter = new UsersAdapter(activity, allUsers);
        listview_users.setAdapter(adapter);

    }

    public static void removeUserFromLV (int position) {
        allUsers.remove(position);
        adapter.notifyDataSetChanged();
    }

    public static void addRecordToLV (User user) {
        allUsers.add(0, user);
        adapter.notifyDataSetChanged();
    }

    public static void editRecordInLV (User u, String positionStr) {
        int position = Integer.parseInt(positionStr);
        User oldUser = allUsers.get(position);
        oldUser.setEmail(u.getEmail());
        if (!u.getPass().trim().equals("")) {
            oldUser.setPass(u.getPass());
        }
        oldUser.setLevel(u.getLevel());
        adapter.notifyDataSetChanged();
    }
}