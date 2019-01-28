package com.urbanpoint.UrbanPoint.Utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.JobIntentService;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.facebook.share.Share;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.text.DecimalFormat;


public class GPSTracker  extends JobIntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private static final String LOGSERVICE = "#######";
    private static final String TAG = "MyLocationService";
    public  static  double lat,lng;
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    SharedPreferences preferences = null;
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            //Loge("LocationListener ","" + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            //Loge( "onLocationChanged: ","lati= " + location.getLatitude()+" Longi= "+location.getLongitude());
            lat =location.getLatitude();
            lng =location.getLongitude();
//            preferences.setlng(location.getLongitude()+"");
//            preferences.setlng(location.getLatitude()+"");
//            preferences.setLatitude(location.getLatitude()+"");
//            preferences.setLongitude(location.getLongitude()+"");

            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            //Loge(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            //Loge("onProviderEnabled: ","" + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //Loge( "onStatusChanged: " ,""+ provider);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildGoogleApiClient();
//        preferences=new SharedPreferences(getApplicationContext());

        initializeLocationManager();

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[0]
            );
        } catch (SecurityException ex) {
            //Loge(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            //Loge(TAG, "network provider does not exist, " + ex.getMessage());
        }
        //Loge(LOGSERVICE, "onCreate");

    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//       //Loge(LOGSERVICE, "onStartCommand");
//
//        if (!mGoogleApiClient.isConnected())
//            mGoogleApiClient.connect();
//        return START_STICKY;
//    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.PASSIVE_PROVIDER)
    };

    @Override
    public void onConnected(Bundle bundle) {
        //Loge(LOGSERVICE, "onConnected" + bundle);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (l != null) {
            //Loge(LOGSERVICE, "lat " + l.getLatitude());
            //Loge(LOGSERVICE, "lng " + l.getLongitude());

        }

        startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Loge(LOGSERVICE, "onConnectionSuspended " + i);

    }

    @Override
    public void onLocationChanged(Location location) {
        //Loge(LOGSERVICE, "lat " + location.getLatitude());
        //Loge(LOGSERVICE, "lng " + location.getLongitude());
//        LatLng mLocation = (new LatLng(location.getLatitude(), location.getLongitude()));

        //Loge( "gps ",mLocation+"" );
        lat=location.getLatitude();
        lng=location.getLongitude();

        if (String.valueOf(location.getLatitude()).equalsIgnoreCase("0.0")
                && String.valueOf(location.getLongitude()).equalsIgnoreCase("0.0") )
        {
            //Loge( "onLocationChanged: ",location.getLatitude()+"" );
            //Loge( "onLocationChanged: ",location.getLongitude()+"" );

        }else {


            Intent intent = new Intent("YourAction");
            Bundle bundle = new Bundle();
//            bundle.put... // put extras you want to pass with broadcast. This is optional

            bundle.putDouble("Longitude", location.getLongitude());
            bundle.putDouble("Latitude", location.getLatitude());
            intent.putExtras(bundle);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

//            preferences.setLongitude(location.getLongitude() + "");
//            preferences.setLatitude(location.getLatitude() + "");

        }

//        EventBus.getDefault().post(mLocation);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    //Loge(TAG, "fail to remove location listener, ignore", ex);
                }
            }
        }
        //Loge(LOGSERVICE, "onDestroy - Estou sendo destruido ");

    }
    public static int JOB_ID=2;
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, GPSTracker.class, JOB_ID, work);
    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Loge(LOGSERVICE, "onConnectionFailed ");

    }

    private void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    private void startLocationUpdate() {
        initLocationRequest();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }


    private void stopLocationUpdate() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }


    private void initializeLocationManager() {
        //Loge( TAG,"initializeLocationManager - LOCATION_INTERVAL: "+ LOCATION_INTERVAL + " LOCATION_DISTANCE: " + LOCATION_DISTANCE);
        if (mLocationManager == null) {
//            preferences=new SharedPreferences(getApplicationContext());
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
//        repeat();
    }



}
