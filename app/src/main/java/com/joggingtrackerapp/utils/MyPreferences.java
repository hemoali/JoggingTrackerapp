package com.joggingtrackerapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyPreferences {
	private static SharedPreferences sharedPreferences;
	private static SharedPreferences.Editor prefsEditor;

	public static void add(Context context, String key, String value,
			String type) {
		InitPreferences(context);
		prefsEditor = sharedPreferences.edit();

		if (type.toLowerCase().equals("string")) {
			prefsEditor.putString(key, value);
			prefsEditor.commit();
		} else if (type.equals("int")) {
			prefsEditor.putInt(key, Integer.parseInt(value));
			prefsEditor.commit();
		}
	}

	public static String getString(Context context, String key) {
		InitPreferences(context);
		if (sharedPreferences.contains(key)) {
			return sharedPreferences.getString(key, "");
		}
		return null;
	}

	private static void InitPreferences(Context context) {
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
	}
}
