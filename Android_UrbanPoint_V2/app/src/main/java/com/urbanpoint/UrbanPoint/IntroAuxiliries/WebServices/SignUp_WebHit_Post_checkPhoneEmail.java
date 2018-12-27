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



public class SignUp_WebHit_Post_checkPhoneEmail {

    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseObject = null;
    Context mContext;


    public void requestcheckPhoneEmail(Context _mContext, final IWebCallbacks iWebCallback,
                                      final String _value) {

        this.mContext = _mContext;
        RequestParams params = new RequestParams();
        String deviceInfo = "Android|" + android.os.Build.VERSION.RELEASE + "|" + android.os.Build.BRAND + "|" + android.os.Build.MODEL;


        String myUrl;
//        if (_isPhone) {
            myUrl = AppConstt.BASE_URL_MOBILE + ApiMethod.POST.checkPhone;
            params.put("phone", _value);
            params.put("device_info",deviceInfo);
            params.put("deviceType","android");
            params.put("id",AppConfig.getInstance().mUser.getmUserId());

            Log.e("step2_params",params+"");
        Log.e("step2_url",myUrl+"");
        Log.e("step2_header",AppConstt.HeadersValue.Authorization+"");

        mClient.addHeader(ApiMethod.HEADER.Authorization, AppConstt.HeadersValue.Authorization);
        mClient.addHeader("app_id", AppConstt.HeadersValue.app_id);
        mClient.setMaxRetriesAndTimeout(AppConstt.LIMIT_API_RETRY, AppConstt.LIMIT_TIMOUT_MILLIS);
        mClient.post(myUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strResponse;
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");

                            responseObject = gson.fromJson(strResponse, ResponseModel.class);

                         Log.e("step2_response",responseObject+"");
                            switch (statusCode) {

                                case AppConstt.ServerStatus.CREATED:

                                    AppConfig.getInstance().mUser.setmPhoneNumber( _value);


                                    AppConfig.getInstance().mUser.setmAuthorizationToken(responseObject.getData().getAuthorization());
                                    AppConfig.getInstance().isEligible = (SignUp_WebHit_Post_checkPhoneEmail.responseObject.getData().getEligibility() == 1 ? true : false);
                                    Log.e("isEligible", "onSuccess: " + AppConfig.getInstance().isEligible);
                                    AppConfig.getInstance().mUser.setLoggedIn(true);
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
                        switch (statusCode) {
                            case AppConstt.ServerStatus.NETWORK_ERROR:
                                iWebCallback.onWebResult(false, mContext.getResources().getString(R.string.MSG_ERROR_NETWORK));
                                break;

                            case AppConstt.ServerStatus.CONFLICT:
//                                AppConfig.getInstance().navtoLogin();
                                iWebCallback.onWebResult(false, mContext.getResources().getString(R.string.MSG_ERROR_PREFIX)
                                        + error.getMessage());
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

//        public String getData() {
//            return this.data;
//        }
//
//        public void setData(String data) {
//            this.data = data;
//        }


        public Data getData() {
            return data;
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
