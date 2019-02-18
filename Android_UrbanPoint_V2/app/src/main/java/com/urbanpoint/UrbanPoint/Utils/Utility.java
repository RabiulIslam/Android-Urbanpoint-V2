package com.urbanpoint.UrbanPoint.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
//import com.kaopiz.kprogresshud.KProgressHUD;
//import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
//import com.urbanpoint.UrbanPoint.helpers.CustomIOSTransparentProgressDialog;
//import com.urbanpoint.UrbanPoint.helpers.TransparentProgressDialog;
//import com.urbanpoint.UrbanPoint.interfaces.CustomDialogConfirmationInterfaces;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.view.inputmethod.InputMethodManager;

import java.text.DecimalFormat;

import com.urbanpoint.UrbanPoint.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.urbanpoint.UrbanPoint.MyApplication;
import com.urbanpoint.UrbanPoint.helpers.CustomIOSTransparentProgressDialog;
import com.urbanpoint.UrbanPoint.helpers.TransparentProgressDialog;
//import com.urbanpoint.UrbanPoint.interfaces.CustomDialogConfirmationInterfaces;

import static android.content.Context.LOCATION_SERVICE;


/**
 * Created by Anurag Sethi
 * This class is used to define the common functions to be used in the application
 */
public class Utility {

    Context context;
    TransparentProgressDialog progressDialogObj;
    CustomIOSTransparentProgressDialog iOSProgressDialogObj;
    String textColorOfMessage = null;
    private Typeface novaThin, novaRegular;

//    private KProgressHUD kProgressHUDProgress;

    private boolean isCustomAlertDialogCancelable = true;

    /**
     * Constructor
     *
     * @param contextObj The Context from where the method is called
     * @return none
     */

    public Utility(Context contextObj) {
        context = contextObj;
    }


    /**
     * The method validates email address
     *
     * @param email email address to validate
     * @return true if the email entered is valid
     */
    public boolean checkEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * This method will convert object to Json String
     *
     * @param obj Object whose data needs to be converted into JSON String
     * @return object data in JSONString
     */
    public String convertObjectToJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }


    /**
     * The method will start the loader till the AsynTask completes the assigned task
     *
     * @param context                        The Context from where the method is called
     * @param image_for_rotation_resource_id resourceID of the image to be displayed as loader
     * @return none
     * @since 2014-08-20
     */
    public void startLoader(Context context, int image_for_rotation_resource_id) {
        progressDialogObj = new TransparentProgressDialog(context, image_for_rotation_resource_id);
       Log.e("startLoader=", ""+ progressDialogObj);
        progressDialogObj.show();
    }

    /**
     * The method will start the loader till the AsynTask completes the assigned task
     *
     * @return none
     * @since 2014-08-20
     */
    public void stopLoader() {
       Log.e("stopLoader=" ,""+ progressDialogObj);
        progressDialogObj.dismiss();
    }


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

    /**
     * The method will create an alertDialog box
     *
     * @param context         The Context from where the method is called
     * @param title           of the dialog box
     * @param msgToShow       message to be shown in the dialog box
     * @param positiveBtnText text to be shown in the positive button
     * @param negativeBtnText text to be shown in the negative button
     * @param btnTagName      to differentiate the action on the displayed activity
     * @param data            to be posted on click of a button
     * @return none
     */
    public void showAlertDialog(final Context context, String title, String msgToShow, String positiveBtnText, String negativeBtnText, final String btnTagName, final int data) {

        AlertDialog.Builder dialogObj = new AlertDialog.Builder(context);
        dialogObj.setIcon(android.R.drawable.ic_dialog_alert);
        dialogObj.setTitle(title);
        dialogObj.setMessage(msgToShow);
        dialogObj.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // TODO Auto-generated method stub
                // Show location settings when the user acknowledges the alert dialog
                if (btnTagName.equalsIgnoreCase("network services")) {
                    Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                    context.startActivity(intent);
                }

                if (btnTagName.equalsIgnoreCase("location services")) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                }

            }

        });

        if (negativeBtnText != null) {
            dialogObj.setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                }
            });
        }

        dialogObj.show();

    }

    /**
     * The method will return the date and time in requested format
     *
     * @param selectedDateTime to be converted to requested format
     * @param requestedFormat  the format in which the provided datetime needs to be changed
     * @param formatString     differentiate parameter to format date or time
     * @return formated date or time
     */
    public String formatDateTime(String selectedDateTime, String requestedFormat, String formatString) {

        if (formatString.equalsIgnoreCase("time")) {
            SimpleDateFormat ft = new SimpleDateFormat("hh:mm");
            Date dateObj = null;

            try {
                dateObj = ft.parse(selectedDateTime);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            long millis = dateObj.getTime();
            SimpleDateFormat simpleDateFormatObj = new SimpleDateFormat(requestedFormat);
            return simpleDateFormatObj.format(millis);

        }//if
        else if (formatString.equalsIgnoreCase("date")) {
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-mm-dd");
            Date dateObj = null;

            try {
                dateObj = ft.parse(selectedDateTime);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            SimpleDateFormat simpleDateFormatObj = new SimpleDateFormat(requestedFormat);
            return simpleDateFormatObj.format(dateObj);


        }
        return null;

    }

    /**
     * The method will return current time
     *
     * @return current time
     */
    public String getCurrentTime() {
        String am_pm = "";
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        if (minutes < 12) {
            String modifiedMinutes = "0" + minutes;
            minutes = Integer.parseInt(modifiedMinutes);
        }
      Log.e("minutes=","" + minutes);

        if (c.get(Calendar.AM_PM) == Calendar.AM) {
            am_pm = "AM";
        } else if (c.get(Calendar.AM_PM) == Calendar.PM) {
            am_pm = "PM";
        }

        String currentTime = Integer.toString(hour) + ":" + Integer.toString(minutes) + " " + am_pm;
        return currentTime;
    }

    /**
     * The method will return current date
     *
     * @return current date
     */
    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        // String currentDate = Integer.toString(day) + "-" + Integer.toString(month) + "-" + Integer.toString(year);
        String currentDate = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
        return currentDate;
    }

    /**
     * The method show the error
     *
     * @param contextObj  context of the activity from where the method is called
     * @param message     message to be displayed
     * @param textViewObj object of the textView where the error message will be shown
     * @param editTextObj object of the editText containing the error
     */
    public void showError(Context contextObj, String message, TextView textViewObj, EditText editTextObj) {
        if (message == null || message.equalsIgnoreCase("")) {
            textViewObj.setVisibility(View.GONE);
        } else {
            textViewObj.setVisibility(View.VISIBLE);
            textViewObj.setText(message);
            textViewObj.setTextColor(contextObj.getResources().getColor(R.color.error_text_color));
        }

    }

    /**
     * This method will convert json String to ArrayList
     *
     * @param jsonString string which needs to be converted to ArrayList
     * @return ArrayList of type String
     * @throws JSONException
     * @since 2014-08-13
     */
    private ArrayList<String> convertJsonStringToArray(String jsonString) throws JSONException {

        ArrayList<String> stringArray = new ArrayList<String>();

        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            stringArray.add(jsonArray.getString(i));
        }

        return stringArray;
    }


    /**
     * The method will save the data in shared preferences defined by "sharedPrefName" and the key provided by "key" parameter
     *
     * @param sharedPrefName name of the container
     * @param mode           private
     * @param key            name of the key in which values are saved
     * @param value          data to be saved associated to the particular key
     * @return none
     * @since 2014-08-13
     */

    public void saveDataInSharedPreferences(String sharedPrefName, int mode, String key, String value) {
        SharedPreferences prefsObj = context.getSharedPreferences(sharedPrefName, mode);
        SharedPreferences.Editor editorObj = prefsObj.edit();
        editorObj.putString(key, value);
        editorObj.commit();
    }

    /**
     * The method will read the data in shared preferences defined by "sharedPrefName" and the key provided by "key" parameter
     *
     * @param sharedPrefName name of the container
     * @param mode           private
     * @param key            name of the key in which values are saved
     * @return String
     */

    public String readDataInSharedPreferences(String sharedPrefName, int mode, String key) {
        SharedPreferences prefsObj = context.getSharedPreferences(sharedPrefName, mode);
        return prefsObj.getString(key, "");
    }

    /**
     * The method will remove the data stored in shared preferences defined by "sharedPrefName" and the key provided by "key" parameter
     *
     * @param sharedPrefName name of the container
     * @param mode           private
     * @param key            name of the key in which values are saved
     * @param removeAll      if true will remove all the values stored in shared preferences else remove as specified by key
     */
    public void removeKeyFromSharedPreferences(String sharedPrefName, int mode, String key, boolean removeAll) {

        SharedPreferences prefsObj = context.getSharedPreferences(sharedPrefName, mode);
        SharedPreferences.Editor editorObj = prefsObj.edit();
        if (removeAll) {
            editorObj.clear();
        } else {
            editorObj.remove(key);
        }
        editorObj.commit();
    }

    /**
     * show message to user using showToast
     *
     * @param mContext                   contains context of application
     * @param message                    contains text/message to show
     * @param durationForMessageToAppear 1 will show the message for short duration else long duration
     */
    public void showToast(Context mContext, String message, int durationForMessageToAppear) {
        if (durationForMessageToAppear == 1) {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * This method will hide virtual keyboard if opened
     *
     * @param mContext contains context of application
     */
    public static void hideVirtualKeyboard(Context mContext) {

        try {

            IBinder binder = ((Activity) mContext).getWindow().getCurrentFocus()
                    .getWindowToken();

            if (binder != null) {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binder, 0);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    /**
     * This method will show virtual keyboard where ever required
     *
     * @param mContext contains context of application
     */
    public void showVirtualKeyboard(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Format value up to 2 decimal places
     *
     * @param num - number to be formatted
     */
    public float formatValueUpTo2Decimals(double num) {

        try {

            DecimalFormat df = new DecimalFormat("#.##");

            String value = df.format(num);
            String decimalPlace = ",";

            if (value.contains(decimalPlace)) {
                value = value.replace(decimalPlace, ".");
            }

            return Float.parseFloat(value);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return (float) num;

    }

    /**
     * The method validates if GooglePlayServices are available or not
     *
     * @param context - contains the context of the activity from where it is called
     * @return true if GooglePlayServices exists else false
     */

//    public boolean checkPlayServices(Activity context) {
//        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
//        int result = googleAPI.isGooglePlayServicesAvailable(context);
//        if (result != ConnectionResult.SUCCESS) {
//            if (googleAPI.isUserResolvableError(result)) {
//                googleAPI.getErrorDialog(context, result,
//                        Constants.GCM.PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            }
//
//            return false;
//        }
//
//        return true;
//    }

//    public void showAppUpdateAlertDialog(final Context context, boolean _isCancellAble, final CustomDialogConfirmationInterfaces customDialogConfirmationListener) {
//
//        // custom dialog
//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setContentView(R.layout.app_update_dialogue);
//
//
//        this.novaThin = Typeface.createFromAsset(context.getAssets(), "fonts/proxima_nova_alt_thin.ttf");
//        this.novaRegular = Typeface.createFromAsset(context.getAssets(), "fonts/proxima_nova_alt_regular.ttf");
//
//        // set the custom dialog components - text
//        TextView messageText = (TextView) dialog.findViewById(R.id.app_update_txv_1);
//        Button cancelButton = (Button) dialog.findViewById(R.id.app_update_btn_no);
//        Button okButton = (Button) dialog.findViewById(R.id.app_update_btn_yes);
//
//
//
//        messageText.setTypeface(novaRegular);
//        cancelButton.setTypeface(novaRegular);
//        okButton.setTypeface(novaRegular);
//
//
//        // if button is clicked, close the custom dialog
//        okButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//
//                if (customDialogConfirmationListener != null)
//                    customDialogConfirmationListener.callConfirmationDialogPositive();
//            }
//        });
//
//        if (_isCancellAble) {
//            cancelButton.setVisibility(View.VISIBLE);
//            dialog.setCancelable(_isCancellAble);
//        } else {
//            cancelButton.setVisibility(View.GONE);
//            dialog.setCancelable(_isCancellAble);
//        }
//
//
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (customDialogConfirmationListener != null)
//                    customDialogConfirmationListener.callConfirmationDialogNegative();
//
//            }
//        });
//
//        dialog.show();
//    }
//
//    public void showUberAlertDialog(final Context context, final CustomDialogConfirmationInterfaces customDialogConfirmationListener) {
//
//        // custom dialog
//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setContentView(R.layout.contextual_permision_dialogue);
//
//        dialog.setCancelable(isCustomAlertDialogCancelable);
//
//        this.novaThin = Typeface.createFromAsset(context.getAssets(), "fonts/proxima_nova_alt_thin.ttf");
//        this.novaRegular = Typeface.createFromAsset(context.getAssets(), "fonts/proxima_nova_alt_regular.ttf");
//
//        // set the custom dialog components - text
//        TextView messageTitle = (TextView) dialog.findViewById(R.id.contextual_txv_1);
//        TextView messageText = (TextView) dialog.findViewById(R.id.contextual_txv_2);
//        Button cancelButton = (Button) dialog.findViewById(R.id.contextual_btn_no);
//        Button okButton = (Button) dialog.findViewById(R.id.contextual_btn_yes);
//
//
//        messageText.setText(R.string.contextual_uber_messages);
//        okButton.setText(R.string.contextual_uber_btn);
//
//        messageTitle.setTypeface(novaRegular);
//        messageText.setTypeface(novaRegular);
//        cancelButton.setTypeface(novaRegular);
//        okButton.setTypeface(novaRegular);
//
//
//        // if button is clicked, close the custom dialog
//        okButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//
//                if (customDialogConfirmationListener != null)
//                    customDialogConfirmationListener.callConfirmationDialogPositive();
//            }
//        });
//
//        cancelButton.setVisibility(View.VISIBLE);
//
//
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (customDialogConfirmationListener != null)
//                    customDialogConfirmationListener.callConfirmationDialogNegative();
//
//            }
//        });
//
//        dialog.show();
//    }
//
//    public void showContextualAlertDialog(final Context context, final CustomDialogConfirmationInterfaces customDialogConfirmationListener) {
//
//        // custom dialog
//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setContentView(R.layout.contextual_permision_dialogue);
//
//        dialog.setCancelable(isCustomAlertDialogCancelable);
//
//        this.novaThin = Typeface.createFromAsset(context.getAssets(), "fonts/proxima_nova_alt_thin.ttf");
//        this.novaRegular = Typeface.createFromAsset(context.getAssets(), "fonts/proxima_nova_alt_regular.ttf");
//
//        // set the custom dialog components - text
//        TextView messageTitle = (TextView) dialog.findViewById(R.id.contextual_txv_1);
//        TextView messageText = (TextView) dialog.findViewById(R.id.contextual_txv_2);
//        Button cancelButton = (Button) dialog.findViewById(R.id.contextual_btn_no);
//        Button okButton = (Button) dialog.findViewById(R.id.contextual_btn_yes);
//
//        messageTitle.setTypeface(novaRegular);
//        messageText.setTypeface(novaRegular);
//        cancelButton.setTypeface(novaRegular);
//        okButton.setTypeface(novaRegular);
//
//
//        // if button is clicked, close the custom dialog
//        okButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//
//                if (customDialogConfirmationListener != null)
//                    customDialogConfirmationListener.callConfirmationDialogPositive();
//            }
//        });
//
//        cancelButton.setVisibility(View.VISIBLE);
//
//
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (customDialogConfirmationListener != null)
//                    customDialogConfirmationListener.callConfirmationDialogNegative();
//
//            }
//        });
//
//        dialog.show();
//    }
//
//    public void showCustomAlertDialog(final Context context, final String title, String msgToShow, String positiveBtnText, String negativeBtnText, boolean isNegativeButtonRequired, final CustomDialogConfirmationInterfaces customDialogConfirmationListener) {
//
//        // custom dialog
//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setContentView(R.layout.custom_mesage_box);
//
//        dialog.setCancelable(isCustomAlertDialogCancelable);
//
//        // set the custom dialog components - text
//        TextView messageTitle = (TextView) dialog.findViewById(R.id.messageTitle);
//        TextView messageText = (TextView) dialog.findViewById(R.id.dialogMessageText);
//        Button cancelButton = (Button) dialog.findViewById(R.id.dialogButtonCancel);
//        Button okButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
//
//
//        TextView dialogButtonSeparator = (TextView) dialog.findViewById(R.id.dialogButtonSeparator);
//        dialogButtonSeparator.setVisibility(View.GONE);
//        cancelButton.setVisibility(View.GONE);
//
//        if (msgToShow != null) {
//            messageText.setText(msgToShow);
//        }
//        if (positiveBtnText != null) {
//            okButton.setText(positiveBtnText);
//        }
//
//        if (title != null) {
//            messageTitle.setText(title);
//        }
//
//
//        // if button is clicked, close the custom dialog
//        okButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//
//                if (customDialogConfirmationListener != null)
//                    customDialogConfirmationListener.callConfirmationDialogPositive();
//
//                if (title != null && title.equalsIgnoreCase("Redeem")) {
//                    MyApplication.getInstance().trackEvent("Redemption: pop up", "button click", "Redemption: pop up - present at the outlet");
//                } else if (title != null && title.equalsIgnoreCase(context.getResources().getString(R.string.ga_login_unsuccess_popup))) {
//                    MyApplication.getInstance().trackEvent("Login unsuccessful: pop up", "button click", "Not Registered Email");
//                } else if (title != null && title.equalsIgnoreCase(context.getResources().getString(R.string.header_setup))) {
//                    MyApplication.getInstance().trackEvent("Signup unsuccessful: pop up", "button click", "Entered Existing Email");
//                } else if (title != null && title.equalsIgnoreCase(context.getResources().getString(R.string.ga_gain_access_voda_screen))) {
//                    MyApplication.getInstance().trackEvent(context.getResources().getString(R.string.ga_gain_access_voda_screen), "Subscribe  click", "Entered Existing phone number");
//                } else if (title != null && title.equalsIgnoreCase(context.getResources().getString(R.string.logout))) {
//                    MyApplication.getInstance().trackEvent(context.getResources().getString(R.string.logout), "Logout Yes Click", "Logout Successful");
//                }
//            }
//        });
//
//        if (isNegativeButtonRequired) {
//
//            dialogButtonSeparator.setVisibility(View.VISIBLE);
//            cancelButton.setVisibility(View.VISIBLE);
//
//            if (negativeBtnText != null) {
//                cancelButton.setText(negativeBtnText);
//            }
//
//            cancelButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                    if (customDialogConfirmationListener != null)
//                        customDialogConfirmationListener.callConfirmationDialogNegative();
//                    if (title != null && title.equalsIgnoreCase("Redeem")) {
//                        MyApplication.getInstance().trackEvent("Redemption: pop up", "button click", "Redemption: pop up - not present at the outlet");
//                    } else if (title != null && title.equalsIgnoreCase(context.getResources().getString(R.string.logout))) {
//                        MyApplication.getInstance().trackEvent(context.getResources().getString(R.string.logout), "Logout No Click", "Logout Cancel");
//                    }
//
//                }
//            });
//        }
//        dialog.show();
//    }
//
//    public void showCustomiOSMessageBox(final Context context, String message, int duration) {
//
//        // custom dialog
//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setContentView(R.layout.custom_mesage_box_without_buttons);
//        dialog.setCancelable(true);
//        // set the custom dialog components - text
//        TextView messageText = (TextView) dialog.findViewById(R.id.dialogMessageText);
//
//        messageText.setText("" + message);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dialog.dismiss();
//            }
//        }, duration);
//        dialog.show();
//    }


    /**
     * The method use for close keyboard
     *
     * @param context - contains the context of the activity from where it is called
     * @return null
     */


    public void keyboardClose(Context context, View view) {
        // View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static Date convertStringToDate(String dateString, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String dateInString = dateString;
        Date date = null;
        try {
            date = formatter.parse(dateInString);
            System.out.println(date);
            System.out.println(formatter.format(date));
            return date;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * The method is use for convert one date format to another format
     *
     * @param date,currentFormat,convertFormat -
     * @return date format in Month Date, year
     */
    public static String convertDate(String date, String currentFormat, String convertFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(currentFormat);
            Date d = format.parse(date);
            SimpleDateFormat serverFormat = new SimpleDateFormat(convertFormat);
            return serverFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public void generateEvent(Context context, String action, Bundle bundle, String bundleKeyName) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null && bundleKeyName != null) {
            intent.putExtra(bundleKeyName, bundle);
        }
        context.sendBroadcast(intent);
    }

    /**
     * Return ordinal suffix (e.g. 'st', 'nd', 'rd', or 'th') for a given number
     *
     * @param value a number
     * @return Ordinal suffix for the given number
     */
    public static String getOrdinalSuffix(int value) {
        int hunRem = value % 100;
        int tenRem = value % 10;

        if (hunRem - tenRem == 10) {
            return "th";
        }
        switch (tenRem) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public boolean isCustomAlertDialogCancelable() {
        return isCustomAlertDialogCancelable;
    }

    public void setIsCustomAlertDialogCancelable(boolean isCustomAlertDialogCancelable) {
        this.isCustomAlertDialogCancelable = isCustomAlertDialogCancelable;
    }

    public boolean checkPermission(Context mContext) {
        int result = -1;
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if ((ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                    result = 0;
                } else {
                    result = -1;
                }
            } else {
                result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result >= 0) {
//        if (result >= 0 && result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }
}
