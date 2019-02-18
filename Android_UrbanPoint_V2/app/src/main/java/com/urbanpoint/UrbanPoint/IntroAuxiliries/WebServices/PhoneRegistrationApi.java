package com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import cz.msebera.android.httpclient.entity.StringEntity;


public class PhoneRegistrationApi {

    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseObject = null;
    Context mContext;


    public void sendPhoneNumber(Context _mContext, final IWebCallbacks iWebCallback,
                                       final String _value) {

        this.mContext = _mContext;
        JsonObject jsonObject = new JsonObject();
        String myUrl = AppConstt.BASE_URL_OTP+ ApiMethod.POST.SEND_OTP;
        jsonObject.addProperty("user_id", AppConfig.getInstance().mUser.getmUserId());
        jsonObject.addProperty("mobilenumber", _value);
        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonObject.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mClient.setMaxRetriesAndTimeout(AppConstt.LIMIT_API_RETRY, AppConstt.LIMIT_TIMOUT_MILLIS);
        mClient.post(_mContext, myUrl, entity, ApiMethod.HeadersValue.CONTENT_TYPE, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strResponse;
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");
                            responseObject = gson.fromJson(strResponse, ResponseModel.class);
                            Log.e("step2_response",responseObject+"");
                            switch (responseObject.status) {
                                case AppConstt.ServerStatus.OK:
                                    iWebCallback.onWebResult(true, responseObject.getMessage());
                                    break;
                                case AppConstt.ServerStatus.INTERNAL_SERVER_ERROR:
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

    public void confirmCode(Context _mContext, final IWebCallbacks iWebCallback,
                                final String _value, String otp) {

        this.mContext = _mContext;
        JsonObject jsonObject = new JsonObject();
        String myUrl = AppConstt.BASE_URL_OTP+ ApiMethod.POST.VERIFY_OTP;
        jsonObject.addProperty("user_id", AppConfig.getInstance().mUser.getmUserId());
        jsonObject.addProperty("mobilenumber", _value);
        jsonObject.addProperty("otp", otp);
        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonObject.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mClient.setMaxRetriesAndTimeout(AppConstt.LIMIT_API_RETRY, AppConstt.LIMIT_TIMOUT_MILLIS);
        mClient.post(_mContext, myUrl, entity, ApiMethod.HeadersValue.CONTENT_TYPE, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strResponse;
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");
                            responseObject = gson.fromJson(strResponse, ResponseModel.class);
                            Log.e("step2_response",responseObject+"");
                            switch (responseObject.status) {
                                case AppConstt.ServerStatus.OK:
                                    iWebCallback.onWebResult(true, responseObject.getMessage());
                                    break;
                                case AppConstt.ServerStatus.ERROR:
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

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public class Data {
            private int OTP;

            public int getOTP() {
                return this.OTP;
            }

            public void setOTP(int id) {
                this.OTP = id;
            }
        }

    }

}
