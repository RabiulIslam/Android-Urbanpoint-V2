package com.urbanpoint.UrbanPoint.CommonFragments.WebServices;

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
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Danish on 2/27/2018.
 */

public class MerchantDetail_Webhit_Get_getMerchantDetail {
    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseObject;
    public Context mContext;

    public void requestOfferDetail(Context _context, final IWebCallbacks iWebCallbacks, int _outletId, int _page) {

        String myUrl = AppConstt.BASE_URL_MOBILE + ApiMethod.GET.getOffers;
        Log.e("get_merchant_detail",myUrl);
        this.mContext = _context;
        RequestParams params = new RequestParams();
        params.put("outlet_id", _outletId);
        params.put("index", _page);
        Log.e("mrchnt_detail_params",params+"");

        mClient.addHeader(ApiMethod.HEADER.Authorization, AppConfig.getInstance().mUser.getmAuthorizationToken());
        mClient.addHeader("app_id", AppConstt.HeadersValue.app_id);
        Log.e("header",AppConfig.getInstance().mUser.getmAuthorizationToken());
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
                                    iWebCallbacks.onWebResult(true, responseObject.getMessage());
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
                                iWebCallbacks.onWebResult(false, statusCode+"");
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

        public int getStatus() { return this.status; }

        public void setStatus(int status) { this.status = status; }

        private String message;

        public String getMessage() { return this.message; }

        public void setMessage(String message) { this.message = message; }

        private ArrayList<Datum> data;

        public ArrayList<Datum> getData() { return this.data; }

        public void setData(ArrayList<Datum> data) { this.data = data; }

        public class Datum
        {
            private String id;

            public String getId() { return this.id; }

            public void setId(String id) { this.id = id; }

            private String outlet_id;

            public String getOutletId() { return this.outlet_id; }

            public void setOutletId(String outlet_id) { this.outlet_id = outlet_id; }

            private String title;

            public String getTitle() { return this.title; }

            public void setTitle(String title) { this.title = title; }

            private String image;

            public String getImage() { return this.image; }

            public void setImage(String image) { this.image = image; }

            private String SKU;

            public String getSKU() { return this.SKU; }

            public void setSKU(String SKU) { this.SKU = SKU; }

            private String search_tags;

            public String getSearchTags() { return this.search_tags; }

            public void setSearchTags(String search_tags) { this.search_tags = search_tags; }

            private String price;

            public String getPrice() { return this.price; }

            public void setPrice(String price) { this.price = price; }

            private String special_price;

            public String getSpecialPrice() { return this.special_price; }

            public void setSpecialPrice(String special_price) { this.special_price = special_price; }

            private String approx_saving;

            public String getApproxSaving() { return this.approx_saving; }

            public void setApproxSaving(String approx_saving) { this.approx_saving = approx_saving; }

            private String start_datetime;

            public String getStartDatetime() { return this.start_datetime; }

            public void setStartDatetime(String start_datetime) { this.start_datetime = start_datetime; }

            private String end_datetime;

            public String getEndDatetime() { return this.end_datetime; }

            public void setEndDatetime(String end_datetime) { this.end_datetime = end_datetime; }

            private String valid_for;

            public String getValidFor() { return this.valid_for; }

            public void setValidFor(String valid_for) { this.valid_for = valid_for; }

            private String description;

            public String getDescription() { return this.description; }

            public void setDescription(String description) { this.description = description; }

            private String special;

            public String getSpecial() { return this.special; }

            public void setSpecial(String special) { this.special = special; }

            private String special_type;

            public String getSpecialType() { return this.special_type; }

            public void setSpecialType(String special_type) { this.special_type = special_type; }

            private String renew;

            public String getRenew() { return this.renew; }

            public void setRenew(String renew) { this.renew = renew; }

            private String redemptions;

            public String getRedemptions() { return this.redemptions; }

            public void setRedemptions(String redemptions) { this.redemptions = redemptions; }

            private String redeemed;

            public String getRedeemed() { return this.redeemed; }

            public void setRedeemed(String redeemed) { this.redeemed = redeemed; }

            private String per_user;

            public String getPerUser() { return this.per_user; }

            public void setPerUser(String per_user) { this.per_user = per_user; }

            private String active;

            public String getActive() { return this.active; }

            public void setActive(String active) { this.active = active; }

            private String created_at;

            public String getCreatedAt() { return this.created_at; }

            public void setCreatedAt(String created_at) { this.created_at = created_at; }

            private String updated_at;

            public String getUpdatedAt() { return this.updated_at; }

            public void setUpdatedAt(String updated_at) { this.updated_at = updated_at; }

            private String name;

            public String getName() { return this.name; }

            public void setName(String name) { this.name = name; }

            private String latitude;

            public String getLatitude() { return this.latitude; }

            public void setLatitude(String latitude) { this.latitude = latitude; }

            private String longitude;

            public String getLongitude() { return this.longitude; }

            public void setLongitude(String longitude) { this.longitude = longitude; }

            private String isfavourite;

            public String getIsfavourite() { return this.isfavourite; }

            public void setIsfavourite(String isfavourite) { this.isfavourite = isfavourite; }
        }


    }
}
