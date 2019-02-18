package com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices;

import android.content.Context;
import android.provider.Settings;
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


/**
 * Created by lenovo on 09/08/2018.
 */

public class SignUp_WebHit_Post_addUser {

    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseObject = null;
    Context mContext;


    public void requestSignUp(Context _mContext, final IWebCallbacks iWebCallback, final String _name, final String _emailId,
                              final String _gender, final String _age,String _occupation,String _referral_code,
                              final String _pin, final String _fcmToken) {

        Log.e("pinnn",_pin);
        this.mContext = _mContext;
        String myUrl = AppConstt.BASE_URL_MOBILE + ApiMethod.POST.signUp;
        String deviceInfo = "Android|" + android.os.Build.VERSION.RELEASE + "|" + android.os.Build.BRAND + "|" + android.os.Build.MODEL;
        String android_id = Settings.Secure.getString(_mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        RequestParams params = new RequestParams();
        params.put("reffered_by",_referral_code);
        params.put("nationality",  "");
        params.put("occupation",_occupation);
        params.put("age", _age);
        params.put("name", _name);
        params.put("gender", _gender);
        params.put("email", _emailId);
        params.put("password", _pin);
        params.put("deviceType", AppConstt.DeviceType);
        params.put("device_info", deviceInfo);
        params.put("token", _fcmToken);
        params.put("device_token",android_id);
        Log.e("params",params+"");
        Log.e("header",ApiMethod.HeadersValue.Authorization);
        Log.e("register_url",myUrl);
        mClient.addHeader(ApiMethod.HEADER.Authorization, ApiMethod.HeadersValue.Authorization);
        mClient.addHeader("app_id", ApiMethod.HeadersValue.app_id);
        mClient.setMaxRetriesAndTimeout(AppConstt.LIMIT_API_RETRY, AppConstt.LIMIT_TIMOUT_MILLIS);
        mClient.post(myUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strResponse;
                        Log.e("status_code",statusCode+"");
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");

                            responseObject = gson.fromJson(strResponse, ResponseModel.class);

                            switch (statusCode) {

                                case AppConstt.ServerStatus.CREATED:
                                    AppConfig.getInstance().mUser.setmName(_name);
                                    AppConfig.getInstance().mUser.setmUserId(responseObject.getData().getId() + "");
                                    AppConfig.getInstance().mUser.setmEmail(_emailId);
                                    AppConfig.getInstance().mUser.setmGender(_gender);
                                    AppConfig.getInstance().mUser.setmNationality("");
                                    int pinCode = Integer.parseInt(_pin);
                                    int num4 = pinCode % 10;
                                    int num1 = pinCode / 1000 % 10;
                                    AppConfig.getInstance().mUser.setmPinCode(num1 + "**" + num4);
                                    AppConfig.getInstance().saveUserData();
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
                            Log.e("ex1","ex1",ex);
                            ex.printStackTrace();
                            iWebCallback.onWebException(ex);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                            error) {
                        Log.e("failure_status_code",statusCode+"");
                        switch (statusCode) {
                            case AppConstt.ServerStatus.NETWORK_ERROR:
                                iWebCallback.onWebResult(false, mContext.getResources().getString(R.string.MSG_ERROR_NETWORK));
                                break;

//                            case AppConstt.ServerStatus.CONFLICT:
////                                AppConfig.getInstance().navtoLogin();
//                                iWebCallback.onWebResult(false, mContext.getResources().getString(R.string.MSG_ERROR_PREFIX)
//                                        + error.getMessage());
//                                break;

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
                                    Log.d("hsasdfhdslkhf", "onFailure: "+strResponse);
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

            private int eligibility;

            public int getEligibility() {
                return eligibility;
            }

            public void setEligibility(int eligibility) {
                this.eligibility = eligibility;
            }

            private String premier_user;

            public String getPremierUser() {
                return this.premier_user;
            }

            public void setPremierUser(String premier_user) {
                this.premier_user = premier_user;
            }

            private String verificationCode;

            public String getVerificationCode() {
                return verificationCode;
            }

            public void setVerificationCode(String verificationCode) {
                this.verificationCode = verificationCode;
            }

            private String Authorization;

            public String getAuthorization() {
                return this.Authorization;
            }

            public void setAuthorization(String Authorization) {
                this.Authorization = Authorization;
            }
        }

    }


}
