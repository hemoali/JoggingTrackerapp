package com.joggingtrackerapp.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MyTypeFace {

	public static Typeface getFont(String fontName, Context context) {
		return Typeface.createFromAsset(context.getAssets(), "fonts/"
				+ fontName);
	}

	public static void setTextViewTypeFace(TextView textView, String fontName,
			Context context, int Style) {
		textView.setTypeface(getFont(fontName, context), Style);
	}

	public static void setEditTextTypeFace(EditText editText, String fontName,
			Context context) {
		editText.setTypeface(getFont(fontName, context));
	}

	public static void setButtonTypeFace(Button button, String fontName,
			Context context) {
		button.setTypeface(getFont(fontName, context));
	}

}
