package com.urbanpoint.UrbanPoint.IntroAuxiliries.FCMAuxiliries;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.urbanpoint.UrbanPoint.MainActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;

import org.json.JSONObject;

/**
 * Created by indus on 5/3/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCMService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.e(TAG, remoteMessage.getData()+"");
        String mPushTitle = "Biyog";
        String mTitle = "";
        String mMessage = "";
        String mId = "";
        String mDate = "";
        try {
            //Event

            JSONObject jsonObjectA = new JSONObject(remoteMessage.getData());
            Log.d(TAG, "onMessageReceived" + jsonObjectA);
             mPushTitle = "Biyog";
             mTitle = (String) jsonObjectA.get("title");
             mMessage = (String) jsonObjectA.get("message");
             mId = (String) jsonObjectA.get("id");
             mDate = (String) jsonObjectA.get("date");

            if (AppConfig.getInstance().mUser.isLoggedIn()) {
                sendNotification(mPushTitle, mTitle, mMessage, mId, mDate);
                Log.e(TAG, "onMessageNotificationSend" );
            }else {
                Log.e(TAG, "onMessageNotificationNotSend" );
            }

        } catch (Exception e) {
            Log.e(TAG, "onException" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendNotification(String _pushTitle, String _title, String _message, String _notificationId, String _date) {
//        if (AppConfig.getInstance().isAppRunning) {
//            AppConfig.getInstance().mUserBadges.setNotificationCount(AppConfig.getInstance().mUserBadges.getNotificationCount() + 1);
//            sendBCupdateBadges(true);
//        }


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String tempChannelId = "";
        int tempCategory = 0;

        //Set channles for Oreo+ (Pre-Req for NotificationCompat)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = null;

            //Create the channel object with a unique ID
            channel = new NotificationChannel(
                    AppConstt.Notifications.CHNL_ID_ANNOUNCEMENT,
                    AppConstt.Notifications.CHNL_NAME_ANNOUNCEMENT,
                    NotificationManager.IMPORTANCE_HIGH);//Makes a sound and appears as a heads-up notification
            channel.setLightColor(Color.GREEN);
            tempCategory = AppConstt.Notifications.PUSH_CATG_ANNOUNCEMENTS;
// Creating an Audio Attribute
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            //Configure Channel's initial settings
            channel.setDescription(_message);
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setSound(defaultSoundUri,audioAttributes);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            //Submit notification channel object to the notificaiton manager
            notificationManager.createNotificationChannel(channel);

            tempChannelId = channel.getId();
        } else {
            //Group Notificaitons for older devices
            tempCategory = AppConstt.Notifications.PUSH_CATG_ANNOUNCEMENTS;
        }


        //Set Intent (Pre-Req for NotificationCompat)
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setClass(this, MainActivity.class);
        intent.putExtra(AppConstt.Notifications.PUSH_NTIFCN_ID, _notificationId);
        intent.putExtra(AppConstt.Notifications.PUSH_NTIFCN_TITLE, _title);
        intent.putExtra(AppConstt.Notifications.PUSH_NTIFCN_MSG, _message);
        intent.putExtra(AppConstt.Notifications.PUSH_NTIFCN_DATE, _date);
        PendingIntent notifyPIntent = PendingIntent.getActivity(
                this, tempCategory /* Request code */,
                intent, PendingIntent.FLAG_ONE_SHOT);//


        //Build push Notification
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, tempChannelId)
                        .setSmallIcon(getNotificationIcon())
                        .setContentTitle(_pushTitle)
                        .setTicker(_pushTitle)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(Html.fromHtml(_message)))
                        .setContentText(Html.fromHtml(_message))
                        .setSound(defaultSoundUri)
                        .setContentIntent(notifyPIntent)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setAutoCancel(true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            notificationBuilder.setColor(
                    getResources().
                            getColor(R.color.orange_notification));

        //Show notification to user
        notificationManager.notify(tempCategory, notificationBuilder.build());
    }

    private void sendBCupdateBadges(boolean _shouldUpdateBadge) {
        //Send a local broadcast for Ride End
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Intent localBCTIntent = new Intent(AppConstt.Notifications.LBCT_INTENT_NOTFICTN_UPDATED);
        localBCTIntent.putExtra(AppConstt.Notifications.PUSH_NTIFCN_BAGDE, _shouldUpdateBadge);
        localBroadcastManager.sendBroadcast(localBCTIntent);
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_launcher : R.mipmap.ic_launcher;
    }
}