package com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.ApiMethod;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;



public class SignIn_WebHit_Post_signIn {

    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseObject = null;
    Context mContext;


    public void requestSignIn(Context _mContext, final IWebCallbacks iWebCallback,
                              final String _emailId, final String _pin, final String _fcmToken) {

        this.mContext = _mContext;
        String myUrl = AppConstt.BASE_URL_MOBILE + ApiMethod.POST.signIn;
        Log.e("login_url",myUrl);
        String deviceInfo = "Android|" + android.os.Build.VERSION.RELEASE + "|" + android.os.Build.BRAND + "|" + android.os.Build.MODEL;
        RequestParams params = new RequestParams();
        params.put("email", _emailId);
        params.put("password", _pin);
        params.put("deviceType", AppConstt.DeviceType);
        params.put("device_info", deviceInfo);
        params.put("token", _fcmToken);
        Log.e("params",params+"");
        Log.e("OldDataIs", "FCM :" + AppConfig.getInstance().loadFCMToken());


        mClient.addHeader(ApiMethod.HEADER.Authorization, AppConstt.HeadersValue.Authorization);
        mClient.addHeader("app_id", AppConstt.HeadersValue.app_id);
        Log.e("header",AppConstt.HeadersValue.Authorization);
        mClient.setMaxRetriesAndTimeout(AppConstt.LIMIT_API_RETRY, AppConstt.LIMIT_TIMOUT_MILLIS);
        mClient.post(myUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strResponse;
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");

                            responseObject = gson.fromJson(strResponse, ResponseModel.class);

                            Log.e("RESPONSE_MESSAGE", strResponse+"");

                            switch (statusCode) {

                                case AppConstt.ServerStatus.OK:

                                    Log.e("zone_valuee",responseObject.getData().getZone()+" vvvv");

                                    AppConfig.getInstance().mUser.setmName(responseObject.getData().getName());
                                    if (responseObject.getData().getId()>0){

                                    }
                                    AppConfig.getInstance().mUser.setmUserId(responseObject.getData().getId() + "");
                                    AppConfig.getInstance().mUser.setmEmail(responseObject.getData().getEmail());
                                    if (responseObject.getData().getPhone() != null) {
                                        AppConfig.getInstance().mUser.setmPhoneNumber(responseObject.getData().getPhone());
                                    }
                                    AppConfig.getInstance().mUser.setmGender(responseObject.getData().getGender());
                                    AppConfig.getInstance().mUser.setmDob(responseObject.getData().getDOB());
                                    AppConfig.getInstance().mUser.setmNetworkType(responseObject.getData().getNetwork());
                                    AppConfig.getInstance().mUser.setmNationality(responseObject.getData().getNationality());
                                    AppConfig.getInstance().mUser.setZone(responseObject.getData().getZone());
                                    AppConfig.getInstance().mUser.setmAuthorizationToken(responseObject.getData().getAuthorization());
                                   // AppConfig.getInstance().mUser.setLoggedIn(true);
                                    Log.e("referral",responseObject.getData().getReferralCode()+"ref_code");
                                    AppConfig.getInstance().mUser.setmReferralCode(responseObject.getData().getReferralCode());
                                    int pinCode = Integer.parseInt(_pin);
                                    int num4 = pinCode % 10;
                                    int num1 = pinCode / 1000 % 10;
                                    AppConfig.getInstance().mUser.setmPinCode(num1 + "**" + num4);

                                    AppConfig.getInstance().saveUserData();
//                                    if(responseObject.getMessage().equalsIgnoreCase("Phone no. verification pending."))
//                                    {
                                    iWebCallback.onWebResult(true, responseObject.getMessage());

                                    break;

                                case AppConstt.ServerStatus.NO_CONTENT:
                                    iWebCallback.onWebResult(true, responseObject.getMessage());
                                    break;

                                default:
                                    iWebCallback.onWebResult(false, responseObject.getMessage());
                                    break;
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            iWebCallback.onWebException(ex);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                            error) {
                        Log.e("err",statusCode+","+error.getMessage());
                        switch (statusCode) {
                            case AppConstt.ServerStatus.NETWORK_ERROR:
                                iWebCallback.onWebResult(false, mContext.getResources().getString(R.string.MSG_ERROR_NETWORK));
                                break;

                            case AppConstt.ServerStatus.CONFLICT:
//                                AppConfig.getInstance().navtoLogin();
                                iWebCallback.onWebLogout();
                                break;

                            case AppConstt.ServerStatus.INTERNAL_SERVER_ERROR:
                                iWebCallback.onWebResult(false, mContext.getResources().getString(R.string.MSG_ERROR_PREFIX)
                                        + error.getMessage());
                                break;

                            case AppConstt.ServerStatus.DATABASE_NOT_FOUND:
                                iWebCallback.onWebResult(false, mContext.getResources().getString(R.string.MSG_ERROR_PREFIX)
                                        + error.getMessage());
                                break;

                            case AppConstt.ServerStatus.BAD_REQUEST:
                                iWebCallback.onWebResult(false, mContext.getResources().getString(R.string.MSG_ERROR_PREFIX)
                                        + error.getMessage());
                                break;

                            default:
                                try {
                                    Gson gson = new Gson();
                                    String strResponse = new String(responseBody, "UTF-8");
                                    responseObject = gson.fromJson(strResponse, ResponseModel.class);
                                    iWebCallback.onWebResult(false, responseObject.getMessage());
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                    iWebCallback.onWebException(e);
                                }
                                break;
                        }
                    }
                }
        );
    }

    public class ResponseModel {
        private int status;

        public int getStatus() {
            return this.status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        private String message;

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        private Data data;

        public Data getData() {
            return this.data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public class Data {
            private int id;

            public int getId() {
                return this.id;
            }

            public void setId(int id) {
                this.id = id;
            }

            private String name;

            public String getName() {
                return this.name;
            }

            public void setName(String name) {
                this.name = name;
            }

            private String email;

            public String getEmail() {
                return this.email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            private String phone;

            public String getPhone() {
                return this.phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            private String gender;

            public String getGender() {
                return this.gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            private String DOB;

            public String getDOB() {
                return this.DOB;
            }

            public void setDOB(String DOB) {
                this.DOB = DOB;
            }

            private String network;

            public String getNetwork() {
                return this.network;
            }

            public void setNetwork(String network) {
                this.network = network;
            }
            private String Zone;

            public String getZone() {
                return Zone;
            }

            public void setZone(String zone) {
                Zone = zone;
            }

            private String nationality;

            public String getNationality() {
                return this.nationality;
            }

            public void setNationality(String nationality) {
                this.nationality = nationality;
            }

            private String Authorization;

            public String getAuthorization() {
                return this.Authorization;
            }

            public void setAuthorization(String Authorization) {
                this.Authorization = Authorization;
            }
            private String refferelcode;

            public String getReferralCode() {
                return refferelcode;
            }

            public void setReferralCode(String referralCode) {
                refferelcode = referralCode;
            }
        }

    }

}
