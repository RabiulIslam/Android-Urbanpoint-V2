package com.urbanpoint.UrbanPoint.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.R;

/**
 * Created by Danish on 2/14/2018.
 */

public class ProgressDilogue {
    CustomIOSTransparentProgressDialog iOSProgressDialogObj;
    String textColorOfMessage = null;

    public void setTextColorOfMessage(String textColorOfMessage) {
        this.textColorOfMessage = textColorOfMessage;
    }

    public void startiOSLoader(Context context, int image_for_rotation_resource_id, String text, boolean isRequiredTransparent) {


        this.iOSProgressDialogObj = new CustomIOSTransparentProgressDialog(context, image_for_rotation_resource_id);
        TextView viewById = (TextView) this.iOSProgressDialogObj.findViewById(R.id.progressDialogMessageText);
        viewById.setText("" + text);

        if (textColorOfMessage != null) {
            viewById.setTextColor(Integer.parseInt(textColorOfMessage));
        }

        if (isRequiredTransparent) {

            LinearLayout mainLayout = (LinearLayout) this.iOSProgressDialogObj.findViewById(R.id.progressDialogMainLayout);
            mainLayout.setBackgroundColor(Color.TRANSPARENT);
            // RelativeLayout layout = (RelativeLayout) iOSProgressDialogObj.findViewById(R.id.progressDialogParentLayout);
            //Drawable drawable = ContextCompat.getDrawable(context, R.drawable.test_gradient_effect);
            //layout.setBackground(drawable);
        }
        this.iOSProgressDialogObj.show();

        //-------------

        /*LayoutInflater factory = LayoutInflater.from(context);
        View myView = factory.inflate(R.layout.custome_progresslayout, null);
        myView.setBackgroundResource(R.drawable.custom_progressbar_background);

        TextView viewById = (TextView) myView.findViewById(R.id.progressDialogMessageText);
        viewById.setText("" + text);
        if (isRequiredTransparent) {

            LinearLayout mainLayout = (LinearLayout) myView.findViewById(R.id.progressDialogMainLayout);
            mainLayout.setBackgroundColor(Color.TRANSPARENT);
            // RelativeLayout layout = (RelativeLayout) iOSProgressDialogObj.findViewById(R.id.progressDialogParentLayout);
            //Drawable drawable = ContextCompat.getDrawable(context, R.drawable.test_gradient_effect);
            //layout.setBackground(drawable);
        }
        kProgressHUDProgress = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCustomView(myView)
                .setDimAmount(0.5f)
                .show();*/
        //---------
    }
    public void stopiOSLoader() {
       /* if (iOSProgressDialogObj != null) {
            if (iOSProgressDialogObj.isShowing()) {
                iOSProgressDialogObj.dismiss();
                iOSProgressDialogObj = null;
            }
        }*/

        try {
            if ((this.iOSProgressDialogObj != null)) {
                if (this.iOSProgressDialogObj.isShowing()) {
                    this.iOSProgressDialogObj.dismiss();
                }
            }
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
            // Handle or log or ignore
        } catch (final Exception e) {
            e.printStackTrace();
            // Handle or log or ignore
        } finally {
            this.iOSProgressDialogObj = null;
        }
        // kProgressHUDProgress.dismiss();
    }
    public class CustomIOSTransparentProgressDialog extends Dialog {

        @SuppressWarnings("unused")
        private ImageView iv;

        /**
         * Constructor
         *
         * @param context           The Context from which the dialog is called
         * @param resourceIdOfImage the ID for the waiting image to be shown
         * @return none
         * @since 2014-07-28
         */
        public CustomIOSTransparentProgressDialog(Context context, int resourceIdOfImage) {
            //super(context, R.style.TransparentProgressDialog);
            super(context, R.style.CustomIOSTransparentProgressDialog);
            WindowManager.LayoutParams wlmp = getWindow().getAttributes();
            wlmp.gravity = Gravity.CENTER_HORIZONTAL;
            getWindow().setAttributes(wlmp);
            setTitle(null);
            setCancelable(true);
            setOnCancelListener(null);
            setContentView(R.layout.custome_progresslayout);
        }

        /**
         * Start the dialog and display it on screen.
         *
         * @return none
         * @since 2014-07-28
         */
        @Override
        public void show() {
            super.show();

        }

        /**
         * Dismiss the dialog
         *
         * @return none
         * @since 2014-07-28
         */
        @Override
        public void dismiss() {
            super.dismiss();
        }
    }
}
