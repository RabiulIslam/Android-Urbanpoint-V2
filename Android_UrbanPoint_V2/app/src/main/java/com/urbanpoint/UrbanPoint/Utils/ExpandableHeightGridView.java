package com.urbanpoint.UrbanPoint.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

/**
 * Created by Ibrar on 1/30/2017.
 */

    public class ExpandableHeightGridView extends ListView {

        boolean expanded = false;

        public ExpandableHeightGridView(Context context) {
            super(context);
        }

        public ExpandableHeightGridView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ExpandableHeightGridView(Context context, AttributeSet attrs,
                                        int defStyle) {
            super(context, attrs, defStyle);
        }

        public boolean isExpanded() {
            return expanded;
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // HACK! TAKE THAT ANDROID!
            if (isExpanded()) {
                // Calculate entire height by providing a very large height hint.
                // View.MEASURED_SIZE_MASK represents the largest height possible.
                int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
                        MeasureSpec.AT_MOST);
                super.onMeasure(widthMeasureSpec, expandSpec);

                ViewGroup.LayoutParams params = getLayoutParams();
                params.height = getMeasuredHeight();


//                params.width = 400;//working
//                params.width = LayoutParams.WRAP_CONTENT;
//                params.width = ViewGroup.LayoutParams.WRAP_CONTENT;

//                LayoutParams tempParams=  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//               params.width =  tempParams.width;
//              params.width=  widthMeasureSpec;

//              params.width=  getMeasuredWidth();
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }

        public void setExpanded(boolean expanded) {
            this.expanded = expanded;
        }
    }

