package com.urbanpoint.UrbanPoint.CommonFragments.WebServices;

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
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Danish on 2/27/2018.
 */

public class MerchantDetail_Webhit_Get_Uber_Estimate_price {
    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseObject;
    public Context mContext;

    public void getUberPrice(Context _context, final IWebCallbacks iWebCallbacks,
                                   double start_lat, double start_lng, double end_lat, double end_lng) {

        this.mContext = _context;
        String myUrl = ApiMethod.GET.uber_price_estimate;
        RequestParams params = new RequestParams();
        params.put("start_latitude", start_lat);
        params.put("start_longitude", start_lng);
        params.put("end_latitude", end_lat);
        params.put("end_longitude", end_lng);

        mClient.addHeader("Authorization", "Token " + AppConstt.UberRideEstimate.SERVER_TOKEN);
        mClient.addHeader("Accept-Language", "en_US");
        mClient.setMaxRetriesAndTimeout(AppConstt.LIMIT_API_RETRY, AppConstt.LIMIT_TIMOUT_MILLIS);
        mClient.get(myUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strResponse;
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");
                            responseObject = gson.fromJson(strResponse, ResponseModel.class);
                            switch (statusCode) {
                                case AppConstt.ServerStatus.OK:
                                    iWebCallbacks.onWebResult(true, "");
                                    break;

                                case AppConstt.ServerStatus.NO_CONTENT:
                                    iWebCallbacks.onWebResult(true, "");
                                    break;

                                default:
                                    iWebCallbacks.onWebResult(false, "");
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
                                    iWebCallbacks.onWebResult(false, "");
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
        private ArrayList<Price> prices;

        public ArrayList<Price> getPrices() {
            return this.prices;
        }

        public void setPrices(ArrayList<Price> prices) {
            this.prices = prices;
        }

        public class Price {
            private String localized_display_name;

            public String getLocalizedDisplayName() {
                return this.localized_display_name;
            }

            public void setLocalizedDisplayName(String localized_display_name) {
                this.localized_display_name = localized_display_name;
            }

            private double distance;

            public double getDistance() {
                return this.distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            private String display_name;

            public String getDisplayName() {
                return this.display_name;
            }

            public void setDisplayName(String display_name) {
                this.display_name = display_name;
            }

            private String product_id;

            public String getProductId() {
                return this.product_id;
            }

            public void setProductId(String product_id) {
                this.product_id = product_id;
            }

            private int high_estimate;

            public int getHighEstimate() {
                return this.high_estimate;
            }

            public void setHighEstimate(int high_estimate) {
                this.high_estimate = high_estimate;
            }

            private int low_estimate;

            public int getLowEstimate() {
                return this.low_estimate;
            }

            public void setLowEstimate(int low_estimate) {
                this.low_estimate = low_estimate;
            }

            private int duration;

            public int getDuration() {
                return this.duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            private String estimate;

            public String getEstimate() {
                return this.estimate;
            }

            public void setEstimate(String estimate) {
                this.estimate = estimate;
            }

            private String currency_code;

            public String getCurrencyCode() {
                return this.currency_code;
            }

            public void setCurrencyCode(String currency_code) {
                this.currency_code = currency_code;
            }
        }


    }

}
