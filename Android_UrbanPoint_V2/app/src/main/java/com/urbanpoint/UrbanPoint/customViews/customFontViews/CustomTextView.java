package com.urbanpoint.UrbanPoint.customViews.customFontViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.MyApplication;


@SuppressLint("AppCompatCustomView")
public class CustomTextView extends TextView {

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        Typeface font = MyApplication.getInstance().getFont(context);
//        if (font != null) {
//      this.setTypeface(font);
//        }
    }
}
