package com.urbanpoint.UrbanPoint;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.SubscriptionAuxiliries.ActivityOrderDetail;
import com.urbanpoint.UrbanPoint.SubscriptionAuxiliries.WebServices.Subscribe_WebHit_Post_subscribe;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;
import com.walletmix.walletmixopglibrary.WalletmixOnlinePaymentGateway;

import org.json.JSONException;
import org.json.JSONObject;

public class GatewayCallbackActivity extends AppCompatActivity {
    TextView TransactionDetails;
    CustomAlert customAlert;
    String type;
    private ProgressDilogue progressDilogue;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatewayactivity);
        progressDilogue = new ProgressDilogue();
        TransactionDetails=(TextView)findViewById(R.id.tv_transctndetails);
        customAlert=new CustomAlert();
        Log.e("check", "transaction");
        if (getIntent() != null) {



                String response = getIntent().getStringExtra("response");
                if (response.equalsIgnoreCase("false")) {
                    Log.e("transaction", "failed");
                } else {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.e("response", jsonObject + " response");
                        String wmxid = jsonObject.getString("wmx_id");
                        String txnstatus = jsonObject.getString("txn_status");
                        if (txnstatus == null) {
                            TransactionDetails.setText("Transaction Attempted");
                        } else {
                            switch (txnstatus) {
                                case "1000":
//                                    TransactionDetails.setText("Transaction Success");
                                    //        finish();

                                    if (jsonObject.getString("merchant_req_amount").equalsIgnoreCase("11.48")) {
                                        type = "1";

                                    }
                                    else if (jsonObject.getString("merchant_req_amount").equalsIgnoreCase("57.48")) {
                                        type = "2";
                                    }
                                    else

                                    {
                                        type = "3";

                                    }

                                    String date = jsonObject.getString("txn_time");
                                     String order_id=jsonObject.getString("merchant_order_id");
                                    requestSubscribe(type, date,order_id);
                                    break;
                                case "1001":
//                                    TransactionDetails.setText("Transaction Rejected");
                                    customAlert.showCustomAlertDialog(GatewayCallbackActivity.this, null, "Transaction Rejected", null, null, false, null);
                                  finish();

                                    break;
                                case "1009":
//                                    TransactionDetails.setText("Transaction Cancelled");
                                    customAlert.showCustomAlertDialog(GatewayCallbackActivity.this, null, "Transaction Cancelled", null, null, false, null);
                                    finish();
                                    break;


                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
//                        TransactionDetails.setText("exception:" + e.getMessage());
                    }

                }

            }

    }



    private void requestSubscribe(final String type,final String subscription_date,String order_id) {
        Subscribe_WebHit_Post_subscribe subscribe_webHit_post_subscribe = new Subscribe_WebHit_Post_subscribe();
        subscribe_webHit_post_subscribe.requestsubscribeUser(GatewayCallbackActivity.this, new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                  progressDilogue.stopiOSLoader();
                  Log.e("subsribe responseee",isSuccess+","+strMsg);
                if (isSuccess) {
//                    if (AppConfig.getInstance().mUser.isEligible()) {
//                        navToSubscriptionEligibleSuccessFragment();
//                    } else {
                 Log.e("successss",isSuccess+" resbool");
                    AppConfig.getInstance().isCommingFromSplash = true;
                    Intent i = new Intent(GatewayCallbackActivity.this, PaymentSuccess.class);
                    i.putExtra("subscribe","true");
                    i.putExtra("type",type);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(i);
                    finish();
                    Log.e("success","success");
//                        navToSubscriptionSuccessFragment();
//                   // }
                } else {
                    customAlert.showCustomAlertDialog(GatewayCallbackActivity.this, null, strMsg, null, null, false, null);
                }

            }

            @Override
            public void onWebException(Exception ex) {
//                progressDilogue.stopiOSLoader();
                Log.e("exccc","ex",ex);
                customAlert.showCustomAlertDialog(GatewayCallbackActivity.this, null, ex.getMessage(), null, null, false, null);

            }

            @Override
            public void onWebLogout() {
                Log.e("logout","logout");
//                progressDilogue.stopiOSLoader();

            }
        }, type,subscription_date,order_id);
    }




}



