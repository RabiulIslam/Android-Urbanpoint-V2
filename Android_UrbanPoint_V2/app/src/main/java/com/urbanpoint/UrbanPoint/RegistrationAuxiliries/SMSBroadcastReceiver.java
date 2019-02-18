package com.urbanpoint.UrbanPoint.RegistrationAuxiliries;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch(status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get SMS message contents
                    String message = (String) intent.getExtras().get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    // Extract one-time code from the message and complete verification
                    // by sending the code back to your server.
                    Pattern pattern = Pattern.compile("(\\d{4})");

                    Matcher matcher = pattern.matcher(message);
                    String val = "";
                    if (matcher.find()) {
                        val = matcher.group(0);  // 4 digit number
                    }
                    Intent intent1 = new Intent(AppConstt.ACTIONS.SMS_RECEIVED);
                    intent1.putExtra(AppConstt.EXTRAS.OTP_CODE, val);
                    context.sendBroadcast(intent1);
                    break;
                case CommonStatusCodes.TIMEOUT:
                    // Waiting for SMS timed out (5 minutes)
                    // Handle the error ...
                    break;
            }
        }
    }
}
