package com.urbanpoint.UrbanPoint.IntroAuxiliries.FCMAuxiliries;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;



public class FirebaseInstanceId extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = com.google.firebase.iid.FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken); // If you want to send messages to this application instance or // manage this apps subscriptions on the server side, send the // Instance ID token to your app server.
        AppConfig.getInstance().saveFCMToken(refreshedToken);

    }
}