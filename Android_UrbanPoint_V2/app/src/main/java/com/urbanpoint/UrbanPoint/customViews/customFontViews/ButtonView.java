package com.urbanpoint.UrbanPoint.customViews.customFontViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.urbanpoint.UrbanPoint.MyApplication;

@SuppressLint("AppCompatCustomView")
public class ButtonView extends Button {
    MyApplication myApplication;
    public ButtonView(Context context, AttributeSet attrs) {

        super(context, attrs);
        myApplication=new MyApplication();
//        Typeface font = myApplication.getFont(context);
//        if (font != null) {
//          this.setTypeface(font);
//        }
    }

}
