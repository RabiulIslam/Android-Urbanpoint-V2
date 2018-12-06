package com.urbanpoint.UrbanPoint.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.urbanpoint.UrbanPoint.HomeAuxiliries.DModel_Badges;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.DModel_User;

/**
 * Created by Danish on 1/25/2018.
 */

public class AppConfig {
    public DModel_User mUser;
    public DModel_Badges mUserBadges;
    private static AppConfig ourInstance;// = new AppConfig(null);

    private Context mContext;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    public boolean isComingFromLogout;
    public boolean isComingFromHome;
    public int marginToast;
    public int mNotificationStatus;
    public boolean isArabic;
    public boolean isEnglish;
    public String mFCMToken;
    public boolean isCommingFromSplash;
    public boolean shouldNavToReview;
    public boolean isComingFromForgetPassword;
    public boolean langChangeFrmSettings;
    public boolean isComingFromLabTest;
    public boolean isLangChangeFromSignin;
    public boolean isComingFromOrderSummary;
    public boolean isComingFromEditOrder; // most important. using it in most of fragments if user wants to edit the order
    public boolean isComingFromReschedule;
    public boolean isCommingFromOfferDetail;
    public boolean isAppRunning;

    public boolean isLanguageClicked;
    public boolean isPlaceNewOrderClicked;
    public boolean isLanguageClickedFrmHome;
    public boolean isEditOrderFromViewResults;

    public String deviceToken;
    public int badgeCounter;
    public boolean isSpaceRequiredToShow;
    public boolean isEligible;

    private AppConfig(Context _mContext) {
        if (_mContext != null) {

            this.mContext = _mContext;

            this.sharedPref = mContext.getSharedPreferences(
                    _mContext.getApplicationInfo().packageName + "_preferences", Context.MODE_PRIVATE);
            this.editor = sharedPref.edit();

            this.marginToast = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10
                    , mContext.getResources().getDisplayMetrics()));

            initUserSessionData();

        }
    }

    private void initUserSessionData() {
        mNotificationStatus = -1;
        mUser = new DModel_User();
        mUserBadges = new DModel_Badges();
        //deviceToken ="";
        isComingFromOrderSummary = false;

        isEligible = false;
        isSpaceRequiredToShow = false;
        isCommingFromSplash = false;
        shouldNavToReview = false;
        mFCMToken = "";
        isAppRunning = true;
        langChangeFrmSettings = false;
        isLangChangeFromSignin = false;
        isComingFromForgetPassword = false;
        isPlaceNewOrderClicked = false;
        isComingFromForgetPassword = false;
        isLanguageClickedFrmHome = false;
        isComingFromLabTest = false;
        isComingFromReschedule = false;
        isEditOrderFromViewResults = false;
        isLanguageClicked = false;
        isComingFromEditOrder = false;
        isCommingFromOfferDetail = false;
        isEnglish = true;
        isArabic = false;
        badgeCounter = 0;
        loadUserData();
    }

    public static void initInstance(Context _mContext) {
        if (ourInstance == null) {
            // Create the instance
            ourInstance = new AppConfig(_mContext);
        }
    }

    public static AppConfig getInstance() {

        return ourInstance;
    }

    public void loadOldPrefrnceData() {
        mUser.mUserId = sharedPref.getString("CUSTOMER_ID", "");
        mUser.mName = sharedPref.getString("user_name", "");
        mUser.mEmail = sharedPref.getString("emailid", "");
        mUser.mAge = sharedPref.getString("age", "");
        mUser.mDob = sharedPref.getString("dob", "");
        mUser.mNationality = sharedPref.getString("nationality", "");
        mUser.mPinCode = sharedPref.getString("pinCode", "");
        mUser.mPhoneNumber = sharedPref.getString("msisdn_id", "");

        String isSubscribed = "";
        isSubscribed = sharedPref.getString("key_user_subscribe_status", "");
        if (isSubscribed.equalsIgnoreCase("true")) {
            mUser.isSubscribed = true;
        } else {
            mUser.isSubscribed = false;
        }
        String gender = sharedPref.getString("gender", "");
        if (gender.equalsIgnoreCase("1")) {
            mUser.mGender ="male";
         } else {
            mUser.mGender ="female";
        }
        String ooredoo = "", voda = "";
        ooredoo = sharedPref.getString("OPERATOR_TYPE_OOREDOO", "");
        voda = sharedPref.getString("OPERATOR_TYPE_VODAFONE", "");
        if (ooredoo.equalsIgnoreCase("1")) {
            mUser.mNetworkType = AppConstt.Networks.OOREDOO;
        } else if (voda.equalsIgnoreCase("1")) {
            mUser.mNetworkType = AppConstt.Networks.VODA;
        }
    }

    public void loadUserData() {
        mUser.setmUserId(sharedPref.getString("key_user_id", ""));
        mUser.setmName(sharedPref.getString("key_name", ""));
        mUser.setmEmail(sharedPref.getString("key_email", ""));
        mUser.setmAge(sharedPref.getString("key_age", ""));
        mUser.setmDob(sharedPref.getString("key_dob", ""));
        mUser.setmGender(sharedPref.getString("key_gender", ""));
        mUser.setmNationality(sharedPref.getString("key_nationality", ""));
        mUser.setmPinCode(sharedPref.getString("key_pin_code", ""));
        mUser.setmNetworkType(sharedPref.getString("key_network_type", ""));
        mUser.setmPhoneNumber(sharedPref.getString("key_phone_number", ""));
        mUser.setMasterMerchant(sharedPref.getString("key_merchint_pin", ""));
        mUser.setSubscribed(sharedPref.getBoolean("key_is_subscribe", false));
//        mUser.setmAppVersion(sharedPref.getFloat("key_app_version", 0));
        mUser.setmAuthorizationToken(sharedPref.getString("key_authorization", ""));
        mUser.setmFCMToken(sharedPref.getString("key_fcm_token", ""));
        mUser.setLoggedIn(sharedPref.getBoolean("key_is_loggedin", false));
        mUser.setNationalityLstDisplyd(sharedPref.getBoolean("key_is_nationality_lst_displayed", false));
        mUser.setForcefullyUpdateActive(sharedPref.getBoolean("key_is_forecfully_update_active", false));
        mUser.setPremierUser(sharedPref.getBoolean("key_is_premier_user", false));
        mUser.setUberRequired(sharedPref.getBoolean("key_is_uber_required", false));
        mUserBadges.setFavoriteCount(sharedPref.getInt("key_favorites_count", 0));
    }

    public void saveUserData() {
        editor.putString("key_user_id", mUser.getmUserId());
        editor.putString("key_name", mUser.getmName());
        editor.putString("key_email", mUser.getmEmail());
        editor.putString("key_age", mUser.getmAge());
        editor.putString("key_dob", mUser.getmDob());
        editor.putString("key_gender", mUser.getmGender());
        editor.putString("key_nationality", mUser.getmNationality());
        editor.putString("key_pin_code", mUser.getmPinCode());
        editor.putString("key_network_type", mUser.getmNetworkType());
        editor.putString("key_phone_number", mUser.getmPhoneNumber());
        editor.putString("key_merchint_pin", mUser.getMasterMerchant());
        editor.putBoolean("key_is_subscribe", mUser.isSubscribed());
//        editor.putFloat("key_app_version", mUser.getmAppVersion());
        editor.putString("key_authorization", mUser.getmAuthorizationToken());
        editor.putString("key_fcm_token", mUser.getmFCMToken());
        editor.putBoolean("key_is_loggedin", mUser.isLoggedIn());
        editor.putBoolean("key_is_nationality_lst_displayed", mUser.isNationalityLstDisplyd());
        editor.putBoolean("key_is_forecfully_update_active", mUser.isForcefullyUpdateActive());
        editor.putBoolean("key_is_premier_user", mUser.isPremierUser());
        editor.putBoolean("key_is_uber_required", mUser.isUberRequired());
        editor.putInt("key_favorites_count", mUserBadges.getFavoriteCount());
        editor.commit();
    }

    public void deleteUserData(String _token) {
        editor.clear();
        editor.commit();
        initUserSessionData();
        saveFCMToken(_token);
        if (mContext != null) {
            // Clear all notifications
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
        }
    }

    public void saveFCMToken(String _token) {
        editor.putString("key_urban_point_firebase_token", _token);
        editor.commit();
    }

    public void clearSharedPreferance() {
        editor.clear();
        editor.commit();
    }

    public String loadFCMToken() {
        mFCMToken = sharedPref.getString("key_urban_point_firebase_token", "");
        return mFCMToken;
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

    public String arabicToDecimal(String number) {
//        number = arabicToDecimal(number); // number = 42;

        final String arabic = "\u06f0\u06f1\u06f2\u06f3\u06f4\u06f5\u06f6\u06f7\u06f8\u06f9";
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }

    public String numberEngToArabic(String strNumber) {
        try {


            char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < strNumber.length(); i++) {
                if (Character.isDigit(strNumber.charAt(i))) {
                    builder.append(arabicChars[(int) (strNumber.charAt(i)) - 48]);
                } else {
                    builder.append(strNumber.charAt(i));
                }
            }
            return builder.toString();
        } catch (Exception e) {

            e.printStackTrace();
            return strNumber;
        }
    }

    public void saveDefLanguage(String lang) {
        editor.putString("upsa_def_lang", lang);
        editor.commit();
    }

    public String loadDefLanguage() {
        return sharedPref.getString("upsa_def_lang", "");
    }

    public void openKeyboard(Context _context) {
        InputMethodManager inputMethodManager = (InputMethodManager) _context.getSystemService(_context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public void closeKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window User_AccessToken from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window User_AccessToken from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
