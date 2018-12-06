package com.urbanpoint.UrbanPoint.HomeAuxiliries;

/**
 * Created by Lenovo on 8/8/2017.
 */

public class DModelHomeGrdVw {
    String strImgUrl;
    String strOfferName;
    String strProductId;
    String strMerchantName;
    String strMerchantAddress;
    String strCategoryImage;
    String strCategoryId;
    String strFestival;
    String strSpecial;
    int distance;
    boolean isDistanceRequired;


    public DModelHomeGrdVw(String strImgUrl, String strOfferName,
                           String strProductId, String strMerchantName,String special,
                           String strFestival, int distance, boolean isDistanceRequired) {
        this.strImgUrl = strImgUrl;
        this.strOfferName = strOfferName;
        this.strProductId = strProductId;
        this.strMerchantName = strMerchantName;
        this.strMerchantAddress = "";
        this.strFestival = strFestival;
        this.distance = distance;
        this.strSpecial=special;
        this.isDistanceRequired=isDistanceRequired;

    }

    public DModelHomeGrdVw(String strImgUrl, String strOfferName, String strProductId, String strMerchantName, String strMerchantAddress, String strCategoryImage,String strFestival) {
        this.strImgUrl = strImgUrl;
        this.strOfferName = strOfferName;
        this.strProductId = strProductId;
        this.strMerchantName = strMerchantName;
        this.strMerchantAddress = strMerchantAddress;
        this.strCategoryImage = strCategoryImage;
        this.strFestival = strFestival;
        this.distance = 0;
        this.isDistanceRequired = false;
    }

    public String getStrImgUrl() {
        return strImgUrl;
    }

    public void setStrImgUrl(String strImgUrl) {
        this.strImgUrl = strImgUrl;
    }

    public String getStrOfferName() {
        return strOfferName;
    }

    public void setStrOfferName(String strOfferName) {
        this.strOfferName = strOfferName;
    }

    public String getStrProductId() {
        return strProductId;
    }

    public void setStrProductId(String strProductId) {
        this.strProductId = strProductId;
    }

    public String getStrMerchantName() {
        return strMerchantName;
    }

    public void setStrMerchantName(String strMerchantName) {
        this.strMerchantName = strMerchantName;
    }

    public String getStrMerchantAddress() {
        return strMerchantAddress;
    }

    public void setStrMerchantAddress(String strMerchantAddress) {
        this.strMerchantAddress = strMerchantAddress;
    }

    public String getStrFestival() {
        return strFestival;
    }

    public void setStrFestival(String strFestival) {
        this.strFestival = strFestival;
    }

    public String getStrCategoryImage() {
        return strCategoryImage;
    }

    public void setStrCategoryImage(String strCategoryImage) {
        this.strCategoryImage = strCategoryImage;
    }

    public void setStrSpecial(String strSpecial) {
        this.strSpecial = strSpecial;
    }

    public String getStrSpecial() {
        return strSpecial;
    }

    public void setStrCategoryId(String strCategoryId) {
        this.strCategoryId = strCategoryId;
    }

    public String getStrCategoryId() {
        return strCategoryId;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean isDistanceRequired() {
        return isDistanceRequired;
    }

    public void setDistanceRequired(boolean distanceRequired) {
        isDistanceRequired = distanceRequired;
    }
}
