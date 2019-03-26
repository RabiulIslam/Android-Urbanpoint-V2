package com.urbanpoint.UrbanPoint.HomeAuxiliries.WebServices;

import android.content.Context;
import android.os.StrictMode;
import android.util.JsonReader;
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

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * Created by lenovo on 09/08/2018.
 */

public class Home_WebHit_Post_homeApi {

    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseObject = null;
    Context mContext;

    public void requestHomeApi(Context _mContext, final IWebCallbacks iWebCallback) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        this.mContext = _mContext;
        String myUrl = AppConstt.BASE_URL_MOBILE + ApiMethod.POST.homeApi;
        Log.e("homeApi", myUrl);
        RequestParams requestParams = new RequestParams();
        requestParams.put("app_version", AppConfig.getInstance().mUser.getmAppVersion());
        Log.e("home_params", requestParams + "");
        Log.e("header,", AppConfig.getInstance().mUser.getmAuthorizationToken() + "");
        mClient.addHeader(ApiMethod.HEADER.Authorization, AppConfig.getInstance().mUser.getmAuthorizationToken());
        mClient.addHeader("app_id", ApiMethod.HeadersValue.app_id);
        Log.e("app_id", ApiMethod.HeadersValue.app_id);
        mClient.setMaxRetriesAndTimeout(AppConstt.LIMIT_API_RETRY, AppConstt.LIMIT_TIMOUT_MILLIS);
        mClient.get(myUrl, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        String strResponse;
                        try {
                            Gson gson = new Gson();

                            strResponse = new String(responseBody, "UTF-8");
//                            JsonReader reader = new JsonReader(new StringReader(strResponse.trim()));
//                            reader.setLenient(true);

                            Log.e("response----",strResponse);

                            responseObject = gson.fromJson(strResponse.trim(), ResponseModel.class);
                            Log.e("response", strResponse);
                            Log.e("res_object", responseObject + "");
                            switch (statusCode) {
                                case AppConstt.ServerStatus.OK:
                                    Log.e("emailverificationstatus", responseObject.getData().emailverified);
                                    //Log.e("phoneverificationstatus", responseObject.getData().phoneverified);
                                    AppConfig.getInstance().mUser.setmReferralCode(responseObject.getData().refferelcode);
                                    AppConfig.getInstance().mUser.setWallet(responseObject.getData().wallet);
                                    AppConfig.getInstance().mUser.setEmailVerified(responseObject.getData().emailverified);
                                    AppConfig.getInstance().mUser.setPhoneVerified(responseObject.getData().phoneverified);
//                                    if (responseObject.getData().getSubscription().getPremierUser().equalsIgnoreCase("1")) {
//                                        //Premier User is always subscribed and not allowed to unsub
//                                        AppConfig.getInstance().mUser.setmCanUnSubscribe(false);
//                                        AppConfig.getInstance().mUser.setSubscribed(true);
//                                        AppConfig.getInstance().mUser.setPremierUser(true);
//                                    } else {
                                    Log.e("home_subscription", responseObject.getData().getSubscription().getSubscription());
                                    if (responseObject.getData().getSubscription().getSubscription().equalsIgnoreCase("1")) {
                                        // subscription==1 means user has access to all offers (for some no. of remaining days)
                                        AppConfig.getInstance().mUser.setSubscribed(true);

                                        Log.e("premium_userrrr", responseObject.getData().getSubscription().getStatus());
                                        if (responseObject.getData().getSubscription().getStatus().equalsIgnoreCase("0")) {
                                            AppConfig.getInstance().mUser.setPremierUser(false);
                                        } else {
                                            AppConfig.getInstance().mUser.setPremierUser(true);
                                        }

                                        //AppConfig.getInstance().mUser.setPremierUser(false);
//                                            if (responseObject.getData().getSubscription().getStatus().equalsIgnoreCase("0")) {
//                                                // status==0 means user has been unsubscribed
//                                                AppConfig.getInstance().mUser.setmCanUnSubscribe(false);
//                                            }
//                                            else {
//                                                switch (responseObject.getData().getSubscription().getNetwork()) {
//                                                    case AppConstt.SubscriptionTypes.Ooredoo:
//                                                        AppConfig.getInstance().mUser.setmCanUnSubscribe(true);
//                                                        break;
//
//                                                    case AppConstt.SubscriptionTypes.VodaFone:
//                                                        AppConfig.getInstance().mUser.setmCanUnSubscribe(true);
//                                                        break;
//
//                                                    case AppConstt.SubscriptionTypes.Card:
//                                                        AppConfig.getInstance().mUser.setmCanUnSubscribe(true);
//                                                        break;
//
//                                                    case AppConstt.SubscriptionTypes.Code:
//                                                        AppConfig.getInstance().mUser.setmCanUnSubscribe(false);
//                                                        break;
//                                                }
                                        // }
                                    } else {
//                                            AppConfig.getInstance().mUser.setPremierUser(false);
//                                            AppConfig.getInstance().mUser.setmCanUnSubscribe(false);
                                        AppConfig.getInstance().mUser.setSubscribed(false);
                                    }
                                    if (Home_WebHit_Post_homeApi.responseObject.getData().getSubscription().getPhone() != null &&
                                            !responseObject.getData().getSubscription().getPhone().equalsIgnoreCase("")) {
                                        AppConfig.getInstance().mUser.setmPhoneNumber(responseObject.getData().getSubscription().getPhone());
                                    }
                                    if (Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getUber() != null &&
                                            Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getUber().equalsIgnoreCase("1")) {
                                        AppConfig.getInstance().mUser.setUberRequired(true);
                                    } else {
                                        AppConfig.getInstance().mUser.setUberRequired(false);
                                    }
                                    if (Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getVersion().getForcefullyUpdated() != null &&
                                            Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getVersion().getForcefullyUpdated().equalsIgnoreCase("1")) {
                                        AppConfig.getInstance().mUser.setForcefullyUpdateActive(true);
                                    } else {
                                        AppConfig.getInstance().mUser.setForcefullyUpdateActive(false);
                                    }
                                    AppConfig.getInstance().mUser.setMasterMerchant(Home_WebHit_Post_homeApi.responseObject.getData().getSuper_access_pin());

                                    /*Changes by Rashmi*/
                                    /*if (Home_WebHit_Post_homeApi.responseObject.getData().getSubscription().getPhone() != null) {
                                        AppConfig.getInstance().mUser.setmPhoneNumber(Home_WebHit_Post_homeApi.responseObject.getData().getSubscription().getPhone());
                                    }*/
                                    AppConfig.getInstance().mUserBadges.setNewOfferCount(Home_WebHit_Post_homeApi.responseObject.getData().getNewOffer());
                                    AppConfig.getInstance().mUserBadges.setNotificationCount(Home_WebHit_Post_homeApi.responseObject.getData().getUnReadNotification());
                                    AppConfig.getInstance().mUserBadges.setReviewCount(Home_WebHit_Post_homeApi.responseObject.getData().getReview());

                                    //Changes by Rashmi
                                    /*Add expiry date in User Data*/
                                    AppConfig.getInstance().mUser.setExpiryDate(Home_WebHit_Post_homeApi.responseObject.getData().getSubscription().getExpiry_datetime());

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
                            ex.printStackTrace();
                            Log.e("exception", "ex", ex);
                            iWebCallback.onWebException(ex);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                            error) {
                        Log.e("errr", error.getMessage() + "," + statusCode);
                        switch (statusCode) {
                            case AppConstt.ServerStatus.NETWORK_ERROR:
                                iWebCallback.onWebResult(false, mContext.getResources().getString(R.string.MSG_ERROR_NETWORK));
                                break;

                            case AppConstt.ServerStatus.UNAUTHORIZED:
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

        public class Subscription {
            private String subscription;

            public String getSubscription() {
                return this.subscription;
            }

            public void setSubscription(String subscription) {
                this.subscription = subscription;
            }

            private String phone;

            public String getPhone() {
                return this.phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            private String network;

            public String getNetwork() {
                return this.network;
            }

            public void setNetwork(String network) {
                this.network = network;
            }

            private String premier_user;

            public String getPremierUser() {
                return this.premier_user;
            }

            public void setPremierUser(String premier_user) {
                this.premier_user = premier_user;
            }

            private String status;

            public String getStatus() {
                return this.status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            //Changes by Rashmi
            private String expiry_datetime;

            public String getExpiry_datetime() {
                return expiry_datetime;
            }

            public void setExpiry_datetime(String expiry_datetime) {
                this.expiry_datetime = expiry_datetime;
            }
        }

        public class SubscriptionText1 {
            private String text;

            public String getText() {
                return this.text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }

        public class SubscriptionText2 {
            private String text;

            public String getText() {
                return this.text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }

        public class CreditcardPackage {
            private String id;

            public String getId() {
                return this.id;
            }

            public void setId(String id) {
                this.id = id;
            }

            private String name;

            public String getName() {
                return this.name;
            }

            public void setName(String name) {
                this.name = name;
            }

            private String type;

            public String getType() {
                return this.type;
            }

            public void setType(String type) {
                this.type = type;
            }

            private String qatar_value;

            public String getQatarValue() {
                return this.qatar_value;
            }

            public void setQatarValue(String qatar_value) {
                this.qatar_value = qatar_value;
            }

            private String doller_value;

            public String getDollerValue() {
                return this.doller_value;
            }

            public void setDollerValue(String doller_value) {
                this.doller_value = doller_value;
            }
        }

        public class Version {
            private String version;

            public String getVersion() {
                return this.version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            private String forcefully_updated;

            public String getForcefullyUpdated() {
                return this.forcefully_updated;
            }

            public void setForcefullyUpdated(String forcefully_updated) {
                this.forcefully_updated = forcefully_updated;
            }
        }

        public class Defaults {
            private String home_page;

            public String getHomePage() {
                return this.home_page;
            }

            public void setHomePage(String home_page) {
                this.home_page = home_page;
            }

            private String uber;

            public String getUber() {
                return this.uber;
            }

            public void setUber(String uber) {
                this.uber = uber;
            }

            private ArrayList<SubscriptionText1> subscription_text_1;

            public ArrayList<SubscriptionText1> getSubscriptionText1() {
                return this.subscription_text_1;
            }

            public void setSubscriptionText1(ArrayList<SubscriptionText1> subscription_text_1) {
                this.subscription_text_1 = subscription_text_1;
            }

            private ArrayList<SubscriptionText2> subscription_text_2;

            public ArrayList<SubscriptionText2> getSubscriptionText2() {
                return this.subscription_text_2;
            }

            public void setSubscriptionText2(ArrayList<SubscriptionText2> subscription_text_2) {
                this.subscription_text_2 = subscription_text_2;
            }

            private ArrayList<CreditcardPackage> creditcardPackages;

            public ArrayList<CreditcardPackage> getCreditcardPackages() {
                return this.creditcardPackages;
            }

            public void setCreditcardPackages(ArrayList<CreditcardPackage> creditcardPackages) {
                this.creditcardPackages = creditcardPackages;
            }

            private Version version;

            public Version getVersion() {
                return this.version;
            }

            public void setVersion(Version version) {
                this.version = version;
            }
        }

        public class SpecailOffer {
            private String id;

            public String getId() {
                return this.id;
            }

            public void setId(String id) {
                this.id = id;
            }

            private String outlet_id;

            public String getOutletId() {
                return this.outlet_id;
            }

            public void setOutletId(String outlet_id) {
                this.outlet_id = outlet_id;
            }

            private String title;

            public String getTitle() {
                return this.title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            private String image;

            public String getImage() {
                return this.image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            private String SKU;

            public String getSKU() {
                return this.SKU;
            }

            public void setSKU(String SKU) {
                this.SKU = SKU;
            }

            private String search_tags;

            public String getSearchTags() {
                return this.search_tags;
            }

            public void setSearchTags(String search_tags) {
                this.search_tags = search_tags;
            }

            private String price;

            public String getPrice() {
                return this.price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            private String special_price;

            public String getSpecialPrice() {
                return this.special_price;
            }

            public void setSpecialPrice(String special_price) {
                this.special_price = special_price;
            }

            private String approx_saving;

            public String getApproxSaving() {
                return this.approx_saving;
            }

            public void setApproxSaving(String approx_saving) {
                this.approx_saving = approx_saving;
            }

            private String start_datetime;

            public String getStartDatetime() {
                return this.start_datetime;
            }

            public void setStartDatetime(String start_datetime) {
                this.start_datetime = start_datetime;
            }

            private String end_datetime;

            public String getEndDatetime() {
                return this.end_datetime;
            }

            public void setEndDatetime(String end_datetime) {
                this.end_datetime = end_datetime;
            }

            private String valid_for;

            public String getValidFor() {
                return this.valid_for;
            }

            public void setValidFor(String valid_for) {
                this.valid_for = valid_for;
            }

            private String description;

            public String getDescription() {
                return this.description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            private String special;

            public String getSpecial() {
                return this.special;
            }

            public void setSpecial(String special) {
                this.special = special;
            }

            private String special_type;

            public String getSpecialType() {
                return this.special_type;
            }

            public void setSpecialType(String special_type) {
                this.special_type = special_type;
            }

            private String renew;

            public String getRenew() {
                return this.renew;
            }

            public void setRenew(String renew) {
                this.renew = renew;
            }

            private String redemptions;

            public String getRedemptions() {
                return this.redemptions;
            }

            public void setRedemptions(String redemptions) {
                this.redemptions = redemptions;
            }

            private String redeemed;

            public String getRedeemed() {
                return this.redeemed;
            }

            public void setRedeemed(String redeemed) {
                this.redeemed = redeemed;
            }

            private String per_user;

            public String getPerUser() {
                return this.per_user;
            }

            public void setPerUser(String per_user) {
                this.per_user = per_user;
            }

            private String active;

            public String getActive() {
                return this.active;
            }

            public void setActive(String active) {
                this.active = active;
            }

            private String created_at;

            public String getCreatedAt() {
                return this.created_at;
            }

            public void setCreatedAt(String created_at) {
                this.created_at = created_at;
            }

            private String updated_at;

            public String getUpdatedAt() {
                return this.updated_at;
            }

            public void setUpdatedAt(String updated_at) {
                this.updated_at = updated_at;
            }

            private String name;

            public String getName() {
                return this.name;
            }

            public void setName(String name) {
                this.name = name;
            }

            private String latitude;

            public String getLatitude() {
                return this.latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            private String longitude;

            public String getLongitude() {
                return this.longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            private String isfavourite;

            public String getIsfavourite() {
                return this.isfavourite;
            }

            public void setIsfavourite(String isfavourite) {
                this.isfavourite = isfavourite;
            }
        }

        public class MostLovedOffer {
            private String id;

            public String getId() {
                return this.id;
            }

            public void setId(String id) {
                this.id = id;
            }

            private String outlet_id;

            public String getOutletId() {
                return this.outlet_id;
            }

            public void setOutletId(String outlet_id) {
                this.outlet_id = outlet_id;
            }

            private String title;

            public String getTitle() {
                return this.title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            private String image;

            public String getImage() {
                return this.image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            private String SKU;

            public String getSKU() {
                return this.SKU;
            }

            public void setSKU(String SKU) {
                this.SKU = SKU;
            }

            private String search_tags;

            public String getSearchTags() {
                return this.search_tags;
            }

            public void setSearchTags(String search_tags) {
                this.search_tags = search_tags;
            }

            private String price;

            public String getPrice() {
                return this.price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            private String special_price;

            public String getSpecialPrice() {
                return this.special_price;
            }

            public void setSpecialPrice(String special_price) {
                this.special_price = special_price;
            }

            private String approx_saving;

            public String getApproxSaving() {
                return this.approx_saving;
            }

            public void setApproxSaving(String approx_saving) {
                this.approx_saving = approx_saving;
            }

            private String start_datetime;

            public String getStartDatetime() {
                return this.start_datetime;
            }

            public void setStartDatetime(String start_datetime) {
                this.start_datetime = start_datetime;
            }

            private String end_datetime;

            public String getEndDatetime() {
                return this.end_datetime;
            }

            public void setEndDatetime(String end_datetime) {
                this.end_datetime = end_datetime;
            }

            private String valid_for;

            public String getValidFor() {
                return this.valid_for;
            }

            public void setValidFor(String valid_for) {
                this.valid_for = valid_for;
            }

            private String description;

            public String getDescription() {
                return this.description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            private String special;

            public String getSpecial() {
                return this.special;
            }

            public void setSpecial(String special) {
                this.special = special;
            }

            private String special_type;

            public String getSpecialType() {
                return this.special_type;
            }

            public void setSpecialType(String special_type) {
                this.special_type = special_type;
            }

            private String renew;

            public String getRenew() {
                return this.renew;
            }

            public void setRenew(String renew) {
                this.renew = renew;
            }

            private String redemptions;

            public String getRedemptions() {
                return this.redemptions;
            }

            public void setRedemptions(String redemptions) {
                this.redemptions = redemptions;
            }

            private String redeemed;

            public String getRedeemed() {
                return this.redeemed;
            }

            public void setRedeemed(String redeemed) {
                this.redeemed = redeemed;
            }

            private String per_user;

            public String getPerUser() {
                return this.per_user;
            }

            public void setPerUser(String per_user) {
                this.per_user = per_user;
            }

            private String active;

            public String getActive() {
                return this.active;
            }

            public void setActive(String active) {
                this.active = active;
            }

            private String created_at;

            public String getCreatedAt() {
                return this.created_at;
            }

            public void setCreatedAt(String created_at) {
                this.created_at = created_at;
            }

            private String updated_at;

            public String getUpdatedAt() {
                return this.updated_at;
            }

            public void setUpdatedAt(String updated_at) {
                this.updated_at = updated_at;
            }

            private String name;

            public String getName() {
                return this.name;
            }

            public void setName(String name) {
                this.name = name;
            }

            private String latitude;

            public String getLatitude() {
                return this.latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            private String longitude;

            public String getLongitude() {
                return this.longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            private String orderCount;

            public String getOrderCount() {
                return this.orderCount;
            }

            public void setOrderCount(String orderCount) {
                this.orderCount = orderCount;
            }

            private String isfavourite;

            public String getIsfavourite() {
                return this.isfavourite;
            }

            public void setIsfavourite(String isfavourite) {
                this.isfavourite = isfavourite;
            }
        }

        public class Category {
            private String id;

            public String getId() {
                return this.id;
            }

            public void setId(String id) {
                this.id = id;
            }

            private String name;

            public String getName() {
                return this.name;
            }

            public void setName(String name) {
                this.name = name;
            }

            private String image;

            public String getImage() {
                return this.image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }

        public class Data {
            private String emailverified;
            private String phoneverified;
            private int wallet;
            private Subscription subscription;

            public int getWallet() {
                return wallet;
            }

            public void setWallet(int wallet) {
                this.wallet = wallet;
            }

            public Subscription getSubscription() {
                return this.subscription;
            }

            public void setSubscription(Subscription subscription) {
                this.subscription = subscription;
            }

            private String refferelcode;

            public String getRefferelcode() {
                return refferelcode;
            }

            public void setRefferelcode(String refferelcode) {
                this.refferelcode = refferelcode;
            }

            private int newOffer;

            public int getNewOffer() {
                return this.newOffer;
            }

            public void setNewOffer(int newOffer) {
                this.newOffer = newOffer;
            }

            private int review;

            public int getReview() {
                return this.review;
            }

            public void setReview(int review) {
                this.review = review;
            }

            public String super_access_pin;

            public String getSuper_access_pin() {
                return super_access_pin;
            }

            public void setSuper_access_pin(String super_access_pin) {
                this.super_access_pin = super_access_pin;
            }

            private int unReadNotification;

            public int getUnReadNotification() {
                return this.unReadNotification;
            }

            public String getEmailVerified() {
                return emailverified;
            }

            public void setEmailVerified(String emailVerified) {
                this.emailverified = emailVerified;
            }

            public String getPhoneVerified() {
                return phoneverified;
            }

            public void setPhoneVerified(String phoneverified) {
                this.phoneverified = phoneverified;
            }

            public void setUnReadNotification(int unReadNotification) {
                this.unReadNotification = unReadNotification;
            }

            private Defaults defaults;

            public Defaults getDefaults() {
                return this.defaults;
            }

            public void setDefaults(Defaults defaults) {
                this.defaults = defaults;
            }

            private ArrayList<SpecailOffer> specailOffers;

            public ArrayList<SpecailOffer> getSpecailOffers() {
                return this.specailOffers;
            }

            public void setSpecailOffers(ArrayList<SpecailOffer> specailOffers) {
                this.specailOffers = specailOffers;
            }

            private ArrayList<MostLovedOffer> mostLovedOffers;

            public ArrayList<MostLovedOffer> getMostLovedOffers() {
                return this.mostLovedOffers;
            }

            public void setMostLovedOffers(ArrayList<MostLovedOffer> mostLovedOffers) {
                this.mostLovedOffers = mostLovedOffers;
            }

            private ArrayList<Category> categories;

            public ArrayList<Category> getCategories() {
                return this.categories;
            }

            public void setCategories(ArrayList<Category> categories) {
                this.categories = categories;
            }
        }
    }
}
