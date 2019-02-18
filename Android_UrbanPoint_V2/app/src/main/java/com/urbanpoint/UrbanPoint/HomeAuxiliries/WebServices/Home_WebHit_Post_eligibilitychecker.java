package com.urbanpoint.UrbanPoint.HomeAuxiliries.WebServices;

import android.content.Context;

import com.google.android.gms.common.api.Api;
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
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * Created by lenovo on 09/08/2018.
 */

public class Home_WebHit_Post_eligibilitychecker {

    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseObject = null;
    Context mContext;


    public void checkEligibility(Context _mContext, final IWebCallbacks iWebCallback, String _phone) {

        this.mContext = _mContext;
        String myUrl = AppConstt.BASE_URL_SUBSCRIPTION + ApiMethod.POST.eligibilitychecker;
        RequestParams params = new RequestParams();
        params.put("phone", _phone);

        mClient.addHeader(ApiMethod.HEADER.Authorization, AppConfig.getInstance().mUser.getmAuthorizationToken());
        mClient.addHeader("app_id", ApiMethod.HeadersValue.app_id);
        mClient.setMaxRetriesAndTimeout(AppConstt.LIMIT_API_RETRY, AppConstt.LIMIT_TIMOUT_MILLIS);
        mClient.post(myUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strResponse;
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");
                            responseObject = gson.fromJson(strResponse, ResponseModel.class);

                            switch (responseObject.getStatus()) {
                                case AppConstt.ServerStatus.OK:
                                    iWebCallback.onWebResult(true, statusCode + "");
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

                            case AppConstt.ServerStatus.UNAUTHORIZED:
//                                AppConfig.getInstance().navtoLogin();
                                iWebCallback.onWebLogout();
                                break;

                            case AppConstt.ServerStatus.CONFLICT:
                                iWebCallback.onWebResult(false, statusCode + "");
                                break;

                            case AppConstt.ServerStatus.INTERNAL_SERVER_ERROR:
                                iWebCallback.onWebResult(false, mContext.getResources().getString(R.string.MSG_ERROR_PREFIX)
                                        + error.getMessage());
                                break;

                            case AppConstt.ServerStatus.DATABASE_NOT_FOUND:
                                iWebCallback.onWebResult(false, mContext.getResources().getString(R.string.MSG_ERROR_PREFIX)
                                        + error.getMessage());
                                break;

//                            case AppConstt.ServerStatus.BAD_REQUEST:
//                                iWebCallback.onWebResult(false, mContext.getResources().getString(R.string.MSG_ERROR_PREFIX)
//                                        + error.getMessage());
//                                break;

                            default:
                                try {
                                    Gson gson = new Gson();
                                    String strResponse = new String(responseBody, "UTF-8");
                                    responseObject = gson.fromJson(strResponse, ResponseModel.class);
                                    String msg = error.getMessage();
                                    if (responseObject.getMessage() != null &&
                                            responseObject.getMessage().length() > 0) {
                                        msg = responseObject.getMessage();
                                    }
                                    iWebCallback.onWebResult(false, msg);
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

        public class Data {
            private String eligibility;

            public String getEligibility() {
                return this.eligibility;
            }

            public void setEligibility(String eligibility) {
                this.eligibility = eligibility;
            }

            private String premier_user;

            public String getPremierUser() {
                return this.premier_user;
            }

            public void setPremierUser(String premier_user) {
                this.premier_user = premier_user;
            }

            private String premier_subscription;

            public String getPremier_subscription() {
                return premier_subscription;
            }

            public void setPremier_subscription(String premier_subscription) {
                this.premier_subscription = premier_subscription;
            }
        }


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
    }
}
