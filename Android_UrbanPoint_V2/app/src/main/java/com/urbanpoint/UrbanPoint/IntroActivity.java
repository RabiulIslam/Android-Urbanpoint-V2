package com.urbanpoint.UrbanPoint;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
//import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.SignUpFragment;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.SplashFragment;
import com.urbanpoint.UrbanPoint.MyApplication;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;

import java.util.Locale;



public class IntroActivity extends AppCompatActivity implements INavBarUpdateUpdateListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {
    private ImageView imvLoader;
    private LinearLayout llContainer;
    private VideoView videoView;
    private LocationRequest mLocationRequest;
    public GoogleApiClient mGoogleApiClient;
    final static int REQUEST_LOCATION = 199;
    final static int MY_PERMISSIONS_REQUEST_LOCATION = 1999;
    android.location.Location mLastLocation;
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
  boolean shouldNavigate;
    double lat ,lng;
    String Id = "";
    String title = "";
    String msg = "";
    String date = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        buildGoogleApiClient();
        // context=getApplicationContext();
        createLocationRequest();
        OnUpdateListener();
        AppConfig.getInstance().loadFCMToken();

        if (!AppConfig.getInstance().loadDefLanguage().equals("")) {
            Log.i("checkdef", "yes in if");
            if (AppConfig.getInstance().loadDefLanguage().equalsIgnoreCase(AppConstt.ENGLISH)) {
                AppConfig.getInstance().isArabic = false;
            } else if (AppConfig.getInstance().loadDefLanguage().equalsIgnoreCase(AppConstt.ARABIC)) {
                Log.i("checkdef", "yes in if");
                AppConfig.getInstance().isArabic = true;
            }
            setDefLang("en");
        } else {
            AppConfig.getInstance().saveDefLanguage(AppConstt.ENGLISH);
            AppConfig.getInstance().isArabic = false;
        }
        // setupDefaultFont();

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity

            Log.d("ExtraEntery", "Extra entry is handled");
            finish();
            return;
        }

        setContentView(R.layout.activity_intro);
        //playVideo();
        AppConfig.getInstance().clearSignupData();
        AppConfig.getInstance().isCommingFromSplash = true;

        AppConfig.getInstance().isComingFromHome =false;
        if (AppConfig.getInstance().isComingFromLogout) {
            navToSignUpFragment();
        } else {
            navToSplash();
        }
        String projectToken = AppConstt.mixPanel.MIXPANEL_TOKEN; // e.g.: "1ef7e30d2a58d27f4b90c42e31d6d7ad"
//        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);


        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)  {
            Log.e("checkloc", "1");

            if (AppConfig.getInstance().isLocationEnabled(IntroActivity.this))
            {
                  new Handler().postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          shouldNavigate=true;
                          if (shouldNavigate) {
                              if (AppConfig.getInstance().mUser.getmUserId().length() > 0) {
                                  navToHomeActivity();
                              } else {
                                  navToGetStartedFragment();
                              }
                          }
                      }
                  },2000);

            }
        }

    }
    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    void navToSplash() {
        Intent intent = getIntent();

        if (intent != null) {
            Id = intent.getStringExtra(AppConstt.Notifications.PUSH_NTIFCN_ID);
            title = intent.getStringExtra(AppConstt.Notifications.PUSH_NTIFCN_TITLE);
            msg = intent.getStringExtra(AppConstt.Notifications.PUSH_NTIFCN_MSG);
            date = intent.getStringExtra(AppConstt.Notifications.PUSH_NTIFCN_DATE);
        }
        Bundle b = new Bundle();
        b.putString(AppConstt.Notifications.PUSH_NTIFCN_ID, Id);
        b.putString(AppConstt.Notifications.PUSH_NTIFCN_TITLE, title);
        b.putString(AppConstt.Notifications.PUSH_NTIFCN_MSG, msg);
        b.putString(AppConstt.Notifications.PUSH_NTIFCN_DATE, date);

        Fragment fragment = new SplashFragment();
        fragment.setArguments(b);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.activity_intro_frm, fragment, AppConstt.FRGTAG.SplashFragment);
        ft.commit();

    }

    void navToSignUpFragment() {
        Intent intent=new Intent(IntroActivity.this,SignupActivity.class);
        startActivity(intent);
        finish();
      //  Fragment fragment = new SignUpFragment();
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.activity_intro_frm, fragment, AppConstt.FRGTAG.FN_SignUpFragment);
//        ft.commit();

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        ExitMessageDialog();
    }

    private void ExitMessageDialog() {
        AlertDialog alertDialog;
        alertDialog =   new AlertDialog.Builder(IntroActivity.this)
                .setMessage(getResources().getString(R.string.exit_app))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }})
                .setNegativeButton(getResources().getString(R.string.No), null)
                .show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);

    }
    public void setDefLang(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        ((MyApplication) getApplication()).setLanguageSpecificFonts(true);
    }

    @Override
    public void setNavBarTitle(String strTitle)
    {

    }

    @Override
    public void setMenuBadgeVisibility(boolean _shouldBageVisible) {

    }

    @Override
    public void setBackBtnVisibility(int _visibility) {

    }

    @Override
    public void setCancelBtnVisibility(int _visibility) {

    }

    @Override
    public void setToolBarbackgroudVisibility(int _visibility) {

    }

    @Override
    public void setReviewCount(int _count) {

    }

    @Override
    public void setProfileCountVisibility(int _visibility) {

    }

    @Override
    public void setUnSubscribeVisibility(int _visibility) {

    }

    @Override
    public void navToLogin() {

    }

    @Override
    public void setProfileCount(String Count) {

    }




    @Override
    protected void onStart()
    {
        super.onStart();
        if (mGoogleApiClient != null)
        {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }
    protected void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }


    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
            }
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);
    }

    private void navToHomeActivity() {
        Log.e("home","home");
//        Bundle b = getArguments();
//        String id = "";
//        String title = "";
//        String msg = "";
//        String date = "";
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        if (getIntent() != null) {
            Log.e("home","home11");
//            id = b.getString(AppConstt.Notifications.PUSH_NTIFCN_ID);
//            title = b.getString(AppConstt.Notifications.PUSH_NTIFCN_TITLE);
//            msg = b.getString(AppConstt.Notifications.PUSH_NTIFCN_MSG);
//            date = b.getString(AppConstt.Notifications.PUSH_NTIFCN_DATE);
            intent.putExtra(AppConstt.Notifications.PUSH_NTIFCN_ID, Id);
            intent.putExtra(AppConstt.Notifications.PUSH_NTIFCN_TITLE, title);
            intent.putExtra(AppConstt.Notifications.PUSH_NTIFCN_MSG, msg);
            intent.putExtra(AppConstt.Notifications.PUSH_NTIFCN_DATE, date);
        }

        startActivity(intent);
        finish();
    }

    void navToGetStartedFragment()
    {
        Log.e("home","home23");
        Intent intent= new Intent(IntroActivity.this, SignupActivity.class);
        startActivity(intent);
        finish();
//        Fragment fragment = new SignUpFragment();
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.activity_intro_frm, fragment, AppConstt.FRGTAG.IntroMainFragment);
//        ft.commitAllowingStateLoss();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         Log.e("requestcode",requestCode+","+resultCode);
        switch (requestCode)
        {

            case REQUEST_LOCATION:

                switch (resultCode) {
                    case Activity.RESULT_OK:
                        displayLocation();
                       shouldNavigate=true;
                        if (shouldNavigate) {
                            if (AppConfig.getInstance().mUser.getmUserId().length() > 0) {
                                navToHomeActivity();
                            } else {
                                navToGetStartedFragment();
                            }
                        }
                        break;
                    case Activity.RESULT_CANCELED:
//                        turnGPSOn();
                        shouldNavigate=true;

                        if (shouldNavigate) {
                            if (AppConfig.getInstance().mUser.getmUserId().length() > 0) {

                                navToHomeActivity();
                            } else {
                                navToGetStartedFragment();
                            }
                        }

                        break;
                }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.e("check", "permissionresult" + requestCode);

        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkGPSStatus(IntroActivity.this)) {
                        turnGPSOn();
                    } else {
                        displayLocation();
                        shouldNavigate = true;

                        if (shouldNavigate) {
                            if (AppConfig.getInstance().mUser.getmUserId().length() > 0) {

                                navToHomeActivity();
                            } else {
                                navToGetStartedFragment();
                            }
                        }
                    }
                    // do your work here


                } else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(permissions[0])) {
//                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);

                    shouldNavigate = true;

                    if (shouldNavigate) {
                        if (AppConfig.getInstance().mUser.getmUserId().length() > 0) {

                            navToHomeActivity();
                        } else {
                            navToGetStartedFragment();
                        }
                    }

                    // User selected the Never Ask Again Option
                } else if (grantResults.length == 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Log.e("inside", "insideagain");

                    shouldNavigate = true;

                    if (shouldNavigate) {
                        if (AppConfig.getInstance().mUser.getmUserId().length() > 0) {

                            navToHomeActivity();
                        } else {
                            navToGetStartedFragment();
                        }
                    }
                }
                else
                {
                    shouldNavigate = true;

                    if (shouldNavigate) {
                        if (AppConfig.getInstance().mUser.getmUserId().length() > 0) {

                            navToHomeActivity();
                        } else {
                            navToGetStartedFragment();
                        }
                    }
                }

                break;
        }


//


        }




    private void displayLocation()
    {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
            }
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null)
        {
//            double latitude = mLastLocation.getLatitude();
//            double longitude = mLastLocation.getLongitude();

            lat= mLastLocation.getLatitude();
            lng= mLastLocation.getLongitude();
            Log.e("Location_gps",lat + ", " + lng);

           // findGirlfriendbydefaultlocation(lat,lng);
        } else {
            //Loge("check","location");
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        displayLocation();

    }
    public static boolean checkGPSPermission(Context mContext){
        boolean gps_enabled = true;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED )
            gps_enabled = true;
        else
            gps_enabled = false;
        return gps_enabled;
    }
    public void OnUpdateListener()
    {
        if(checkGPSPermission(getApplicationContext()))
        {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
//            {
//                ActivityCompat.requestPermissions(IntroActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            }
//            else
//            {
//                ActivityCompat.requestPermissions(IntroActivity.this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            }
        }
        else if(checkGPSStatus(IntroActivity.this))
            turnGPSOn();
        else if (mGoogleApiClient.isConnected())
        {
            startLocationUpdates();
        }

    }

    public void turnGPSOn()
    {
        mGoogleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>()
        {
            @Override
            public void onResult(LocationSettingsResult result)
            {
                final Status status = result.getStatus();
                switch (status.getStatusCode())
                {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try
                        {
                            status.startResolutionForResult(IntroActivity.this, REQUEST_LOCATION);
                        }
                        catch (@SuppressLint("NewApi") IntentSender.SendIntentException e)
                        {

                        }
                        break;
                }
            }
        });
    }

    public static boolean checkGPSStatus(Context mContext) {
        LocationManager locationManager = null;
        boolean gps_enabled = true;
        if (locationManager == null) {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        }
        if (!locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ))
            gps_enabled = true;
        else
            gps_enabled = false;
        Log.e("gps_enabled",":2:"+gps_enabled);
        return gps_enabled;
    }
}
