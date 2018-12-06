package com.urbanpoint.UrbanPoint.HomeAuxiliries.HomeFragments.WebServices;

import android.content.Context;

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
 * Created by Danish on 3/4/2018.
 */

public class FavoriteOffers_Webhit_POST_deleteMyFavouriteOffer {

    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseObject;
    private Context mContext;

    public void requestRemoveFromFav(Context _context, final IWebCallbacks iWebCallbacks, int id) {

        String myUrl = AppConstt.BASE_URL_MOBILE + ApiMethod.POST.deleteMyFavouriteOffer;
        this.mContext = _context;
        RequestParams params = new RequestParams();
        params.put("offer_id", id);

        mClient.addHeader(ApiMethod.HEADER.Authorization, AppConfig.getInstance().mUser.getmAuthorizationToken());
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
                            switch (statusCode) {
                                case AppConstt.ServerStatus.OK:
                                    iWebCallbacks.onWebResult(true, responseObject.getData());
                                    break;

                                case AppConstt.ServerStatus.NO_CONTENT:
                                    iWebCallbacks.onWebResult(true, responseObject.getMessage());
                                    break;

                                default:
                                    iWebCallbacks.onWebResult(false, responseObject.getMessage());
                                    break;
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            iWebCallbacks.onWebException(ex);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                            error) {
                        switch (statusCode) {
                            case AppConstt.ServerStatus.NETWORK_ERROR:
                                iWebCallbacks.onWebResult(false, mContext.getResources().getString(R.string.MSG_ERROR_NETWORK));
                                break;

                            case AppConstt.ServerStatus.CONFLICT:
//                                AppConfig.getInstance().navtoLogin();
                                iWebCallbacks.onWebLogout();
                                break;

                            case AppConstt.ServerStatus.INTERNAL_SERVER_ERROR:
                                iWebCallbacks.onWebResult(false, mContext.getResources().getString(R.string.MSG_ERROR_PREFIX)
                                        + error.getMessage());
                                break;

                            case AppConstt.ServerStatus.DATABASE_NOT_FOUND:
                                iWebCallbacks.onWebResult(false, mContext.getResources().getString(R.string.MSG_ERROR_PREFIX)
                                        + error.getMessage());
                                break;

                            case AppConstt.ServerStatus.BAD_REQUEST:
                                iWebCallbacks.onWebResult(false, mContext.getResources().getString(R.string.MSG_ERROR_PREFIX)
                                        + error.getMessage());
                                break;

                            default:
                                try {
                                    Gson gson = new Gson();
                                    String strResponse = new String(responseBody, "UTF-8");
                                    responseObject = gson.fromJson(strResponse, ResponseModel.class);
                                    iWebCallbacks.onWebResult(false, responseObject.getMessage());
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                    iWebCallbacks.onWebException(e);
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

        private String data;

        public String getData() {
            return this.data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

}
