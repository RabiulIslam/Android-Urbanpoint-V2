package com.urbanpoint.UrbanPoint.DrawerAuxiliries.WebServices;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.ApiMethod;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;



public class PurchaseHistory_Webhit_Get_getMyPurchaseHistory {
    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseObject;
    public Context mContext;

    public void getPurchaseHistory(Context _context, final IWebCallbacks iWebCallbacks) {

        String myUrl = AppConstt.BASE_URL_MOBILE + ApiMethod.GET.getMyPurchaseHistory;
        Log.e("get_purchase_hostory",myUrl);
        this.mContext = _context;
        mClient.addHeader(ApiMethod.HEADER.Authorization, AppConfig.getInstance().mUser.getmAuthorizationToken());
        mClient.addHeader("app_id", ApiMethod.HeadersValue.app_id);
        mClient.setMaxRetriesAndTimeout(AppConstt.LIMIT_API_RETRY, AppConstt.LIMIT_TIMOUT_MILLIS);
        Log.e("header",AppConfig.getInstance().mUser.getmAuthorizationToken());
        mClient.get(myUrl, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strResponse;
                        Log.e("purchsehistory_reponse",responseBody+"");
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
                        Log.e("error",error.getMessage());
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

        public int getStatus() { return this.status; }

        public void setStatus(int status) { this.status = status; }

        private String message;

        public String getMessage() { return this.message; }

        public void setMessage(String message) { this.message = message; }

        private Data data;

        public Data getData() { return this.data; }

        public void setData(Data data) { this.data = data; }

        public class Order
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

            private String order_id;

            public String getOrderId() { return this.order_id; }

            public void setOrderId(String order_id) { this.order_id = order_id; }

            private String outlet_name;

            public String getOutletName() { return this.outlet_name; }

            public void setOutletName(String outlet_name) { this.outlet_name = outlet_name; }

            private String outlet_phone;

            public String getOutletPhone() { return this.outlet_phone; }

            public void setOutletPhone(String outlet_phone) { this.outlet_phone = outlet_phone; }

            private String outlet_address;

            public String getOutletAddress() { return this.outlet_address; }

            public void setOutletAddress(String outlet_address) { this.outlet_address = outlet_address; }

            private String outlet_latitude;

            public String getOutletLatitude() { return this.outlet_latitude; }

            public void setOutletLatitude(String outlet_latitude) { this.outlet_latitude = outlet_latitude; }

            private String outlet_longitude;

            public String getOutletLongitude() { return this.outlet_longitude; }

            public void setOutletLongitude(String outlet_longitude) { this.outlet_longitude = outlet_longitude; }

            private String outlet_logo;

            public String getOutletLogo() { return this.outlet_logo; }

            public void setOutletLogo(String outlet_logo) { this.outlet_logo = outlet_logo; }

            private String outlet_description;

            public String getOutletDescription() { return this.outlet_description; }

            public void setOutletDescription(String outlet_description) { this.outlet_description = outlet_description; }

            private String outlet_image;

            public String getOutletImage() { return this.outlet_image; }

            public void setOutletImage(String outlet_image) { this.outlet_image = outlet_image; }

            private String order_created_at;

            public String getOrderCreatedAt() { return this.order_created_at; }

            public void setOrderCreatedAt(String order_created_at) { this.order_created_at = order_created_at; }

            private String review;

            public String getReview() { return this.review; }

            public void setReview(String review) { this.review = review; }
        }

        public class Allorder
        {
            private String date;

            public String getDate() { return this.date; }

            public void setDate(String date) { this.date = date; }

            private ArrayList<Order> orders;

            public ArrayList<Order> getOrders() { return this.orders; }

            public void setOrders(ArrayList<Order> orders) { this.orders = orders; }

            private String monthly_saving;

            public String getMonthlySaving() { return this.monthly_saving; }

            public void setMonthlySaving(String monthly_saving) { this.monthly_saving = monthly_saving; }
        }

        public class Data {
            private ArrayList<Allorder> allorders;

            public ArrayList<Allorder> getAllorders() { return this.allorders; }

            public void setAllorders(ArrayList<Allorder> allorders) { this.allorders = allorders; }

            private String totalsaving;

            public String getTotalsaving() { return this.totalsaving; }

            public void setTotalsaving(String totalsaving) { this.totalsaving = totalsaving; }
        }
    }
}
